package com.cds.promotion.module.store;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cds.promotion.R;
import com.cds.promotion.base.BaseFragment;
import com.cds.promotion.data.Constant;
import com.cds.promotion.data.entity.StoreBean;
import com.cds.promotion.data.entity.StoreList;
import com.cds.promotion.module.adapter.StoreAdapter;
import com.cheng.refresh.library.PullToRefreshBase;
import com.cheng.refresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 10:25
 * @Version: 3.0.0
 */
public class StoreFragment extends BaseFragment implements StoreContract.View, PullToRefreshBase.OnRefreshListener<ListView>, AdapterView.OnItemClickListener {
    @Bind(R.id.empty_layout)
    RelativeLayout emptyLayout;
    @Bind(R.id.refresh_listView)
    PullToRefreshListView refreshListView;
    private ListView mListView;

    private StoreAdapter adapter;

    private StoreContract.Presenter mPresenter;

    public static final int REQUEST_NUM = 20;
    private int offset = 0;
    private boolean hasMoreData = false;//是否有更多数据
    private boolean isLoadMore = false;//是否加载更多
    List<StoreBean> mDataList = new ArrayList<>();

    private int type = -1;

    public static StoreFragment newInstance(int index) {
        StoreFragment mainFragment = new StoreFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", index);
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_store;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        refreshListView.setPullLoadEnabled(false);//上拉加载是否可用
        refreshListView.setScrollLoadEnabled(true);//判断滑动到底部加载是否可用
        refreshListView.setPullRefreshEnabled(true);//设置是否能下拉
        refreshListView.setOnRefreshListener(this);
        mListView = refreshListView.getRefreshableView();
        adapter = new StoreAdapter(getActivity());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        new StorePresenter(this);
        type = getArguments().getInt("type");
//        mPresenter.getDealerList(type, offset);

        if (type == 2) {
//            emptyImg.setImageResource(R.mipmap.icn_empty_picture);
//            emptyTv.setText("当前没有图片");
        } else {
//            emptyImg.setImageResource(R.mipmap.icn_empty_video);
//            emptyTv.setText("当前没有视频");
        }
//        errorLayout.setVisibility(View.VISIBLE);
        getData();
    }

    @Override
    protected void onLazyLoad() {
        super.onLazyLoad();
        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.unsubscribe();
        ButterKnife.unbind(this);
    }

    private void getData() {
        switch (type) {
            case 0:
                mPresenter.getDealerList(Constant.STORE_IN_AUDIT, offset);
                break;
            case 1:
                mPresenter.getDealerList(Constant.STORE_SIGNED, offset);
                break;
            case 2:
                mPresenter.getDealerList(Constant.STORE_TO_BE_SIGNED, offset);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        offset = 0;
        isLoadMore = false;
        getData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        offset++;
        isLoadMore = true;
        getData();
    }

    @Override
    public void setPresenter(StoreContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getDealerListSuccess(StoreList resp) {
        List<StoreBean> list = resp.getDealers();
        if (!isLoadMore) {
            mDataList.clear();
            if (list.isEmpty()) {
                emptyLayout.setVisibility(View.VISIBLE);
                refreshListView.setScrollLoadEnabled(false);
            } else {
                emptyLayout.setVisibility(View.GONE);
                refreshListView.setScrollLoadEnabled(true);
            }
        }
        if (list.size() == REQUEST_NUM) {
            hasMoreData = true;
        } else {
            hasMoreData = false;
        }
        mDataList.addAll(list);
        adapter.setDataList(mDataList);
        refreshListView.onPullDownRefreshComplete();
        refreshListView.onPullUpRefreshComplete();
        refreshListView.setHasMoreData(hasMoreData);
    }

    @Override
    public void getDealerListFail() {
        refreshListView.onPullDownRefreshComplete();
        refreshListView.onPullUpRefreshComplete();
        refreshListView.setHasMoreData(hasMoreData);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listener != null) {
            listener.onStoreClick(adapter.getDataList().get(position).getId(),adapter.getDataList().get(position).getName());
        }
    }

    OnStoreClickListener listener;

    public StoreFragment setListener(Boolean isChoose,OnStoreClickListener listener) {
        if(isChoose){
            this.listener = listener;
        }else {
            this.listener = null;
        }
        return this;
    }

    public interface OnStoreClickListener {
        void onStoreClick(String dealer_id,String name);
    }
}

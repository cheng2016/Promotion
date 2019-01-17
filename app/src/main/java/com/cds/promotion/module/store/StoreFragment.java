package com.cds.promotion.module.store;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cds.promotion.R;
import com.cds.promotion.base.BaseFragment;
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
public class StoreFragment extends BaseFragment implements StoreContract.View, PullToRefreshBase.OnRefreshListener<ListView> {
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
    List mDataList = new ArrayList<>();

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
    }

    @Override
    protected void initData() {
        new StorePresenter(this);
        type = getArguments().getInt("type");
        if (type == 2) {
//            emptyImg.setImageResource(R.mipmap.icn_empty_picture);
//            emptyTv.setText("当前没有图片");
        } else {
//            emptyImg.setImageResource(R.mipmap.icn_empty_video);
//            emptyTv.setText("当前没有视频");
        }
//        errorLayout.setVisibility(View.VISIBLE);
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
        if (type == 2) {
//            mPresenter.getImage(offset, REQUEST_NUM);
        } else {
//            mPresenter.getVideo(type, offset, REQUEST_NUM);
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

}

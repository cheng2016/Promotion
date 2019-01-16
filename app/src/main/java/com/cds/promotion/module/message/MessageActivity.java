package com.cds.promotion.module.message;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;
import com.cds.promotion.data.entity.SMessage;
import com.cds.promotion.module.adapter.MessageAdapter;
import com.cheng.refresh.library.PullToRefreshBase;
import com.cheng.refresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @Author: chengzj
 * @CreateDate: 2018/12/3 17:09
 * @Version: 3.0.0
 */
public class MessageActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener<ListView>, MessageContract.View, AdapterView.OnItemClickListener, View.OnClickListener {
    public static final int REQUEST_NUM = 10;
    @Bind(R.id.empty_layout)
    RelativeLayout emptyLayout;
    @Bind(R.id.refresh_listView)
    PullToRefreshListView refreshListView;
    private ListView mListView;

    List<SMessage> mDataList = new ArrayList<>();

    MessageAdapter adapter;

    private int offset = 0;
    private boolean hasMoreData = false;//是否有更多数据
    private boolean isLoadMore = false;//是否加载更多

    MessageContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_img).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("Messages");
        refreshListView.setPullLoadEnabled(false);//上拉加载是否可用
        refreshListView.setScrollLoadEnabled(true);//判断滑动到底部加载是否可用
        refreshListView.setPullRefreshEnabled(true);//设置是否能下拉
        refreshListView.setOnRefreshListener(this);
        mListView = refreshListView.getRefreshableView();
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        new MessagePresenter(this);
        adapter = new MessageAdapter(this);
        mListView.setAdapter(adapter);
        mPresenter.queryMessage(offset);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void queryMessageSuccess(List<SMessage> list) {
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
    public void queryMessage() {
        offset = 0;
        isLoadMore = false;
        mPresenter.queryMessage(offset);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                offset = 0;
                isLoadMore = false;
                mPresenter.queryMessage(offset);

            }
        }, 1200);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                offset++;
                isLoadMore = true;
                mPresenter.queryMessage(offset);
            }
        }, 1200);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), DiagnosticReportActivity.class);
//        intent.putExtra("orderId", adapter.getDataList().get(position).getOrderId());
//        startActivity(intent);
    }

    @Override
    public void setPresenter(MessageContract.Presenter presenter) {
        mPresenter = presenter;
    }
}

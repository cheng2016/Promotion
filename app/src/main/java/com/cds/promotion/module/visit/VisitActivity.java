package com.cds.promotion.module.visit;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;
import com.cds.promotion.module.adapter.VisitAdapter;
import com.cheng.refresh.library.PullToRefreshBase;
import com.cheng.refresh.library.PullToRefreshListView;

import butterknife.Bind;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 13:56
 * @Version: 3.0.0
 */
public class VisitActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener<ListView>, AdapterView.OnItemClickListener, VisitContract.View, View.OnClickListener {
    public static final int REQUEST_NUM = 10;
    @Bind(R.id.empty_layout)
    RelativeLayout emptyLayout;
    @Bind(R.id.refresh_listView)
    PullToRefreshListView refreshListView;
    private ListView mListView;

    VisitAdapter adapter;

    private int offset = 0;
    private boolean hasMoreData = false;//是否有更多数据
    private boolean isLoadMore = false;//是否加载更多

    VisitContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_visit;
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_img).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("Visit log");
        refreshListView.setPullLoadEnabled(false);//上拉加载是否可用
        refreshListView.setScrollLoadEnabled(true);//判断滑动到底部加载是否可用
        refreshListView.setPullRefreshEnabled(true);//设置是否能下拉
        refreshListView.setOnRefreshListener(this);
        mListView = refreshListView.getRefreshableView();
        mListView.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        new VisitPresenter(this);
        adapter = new VisitAdapter(this);
        mListView.setAdapter(adapter);

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
    public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                offset = 0;
                isLoadMore = false;
//                mPresenter.queryMessage(offset);

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
//                mPresenter.queryMessage(offset);
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
    public void setPresenter(VisitContract.Presenter presenter) {
        mPresenter = presenter;
    }
}

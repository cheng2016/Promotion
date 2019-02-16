package com.cds.promotion.module.visit.record;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;
import com.cds.promotion.data.entity.VisitBean;
import com.cds.promotion.data.entity.VisitingList;
import com.cds.promotion.module.adapter.VisitAdapter;
import com.cds.promotion.module.visit.detail.VisitDetailActivity;
import com.cheng.refresh.library.PullToRefreshBase;
import com.cheng.refresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 10:43
 * @Version: 3.0.0
 */
public class VisitRecordActivity extends BaseActivity implements PullToRefreshBase.OnRefreshListener<ListView>, AdapterView.OnItemClickListener, VisitRecordContract.View, View.OnClickListener {
    public static final int REQUEST_NUM = 10;
    @Bind(R.id.empty_layout)
    RelativeLayout emptyLayout;
    @Bind(R.id.refresh_listView)
    PullToRefreshListView refreshListView;
    private ListView mListView;

    VisitAdapter adapter;

    List<VisitBean> mDataList = new ArrayList<>();

    private int offset = 0;
    private boolean hasMoreData = false;//是否有更多数据
    private boolean isLoadMore = false;//是否加载更多

    VisitRecordContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_visit_record;
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
        new VisitRecordPresenter(this);
        adapter = new VisitAdapter(this);
        mListView.setAdapter(adapter);
        mPresenter.getVisitingList(0);
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
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        offset = 0;
        isLoadMore = false;
        mPresenter.getVisitingList(offset);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        offset++;
        isLoadMore = true;
        mPresenter.getVisitingList(offset);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.setClass(VisitRecordActivity.this, VisitDetailActivity.class);
        intent.putExtra("visiting_id", adapter.getDataList().get(position).getVisiting_id());
        startActivity(intent);
    }

    @Override
    public void setPresenter(VisitRecordContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void getVisitingListSuccess(VisitingList resp) {
        List<VisitBean> list = resp.getVisiting();
        if (!isLoadMore) {
            mDataList.clear();
            if (list == null || list.isEmpty()) {
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
    public void getVisitingListFail() {
        refreshListView.onPullDownRefreshComplete();
        refreshListView.onPullUpRefreshComplete();
        refreshListView.setHasMoreData(hasMoreData);
    }
}

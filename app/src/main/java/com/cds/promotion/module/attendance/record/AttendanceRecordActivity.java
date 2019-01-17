package com.cds.promotion.module.attendance.record;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;
import com.cds.promotion.data.entity.ClockOnInfo;
import com.cds.promotion.data.entity.ClockOnList;
import com.cds.promotion.module.adapter.AttendanceAdapter;
import com.cheng.refresh.library.PullToRefreshBase;
import com.cheng.refresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 11:27
 * @Version: 3.0.0
 */
public class AttendanceRecordActivity extends BaseActivity implements View.OnClickListener, AttendanceRecordContract.View, PullToRefreshBase.OnRefreshListener<ListView> {
    public static final int REQUEST_NUM = 10;

    @Bind(R.id.empty_layout)
    RelativeLayout emptyLayout;
    @Bind(R.id.refresh_listView)
    PullToRefreshListView refreshListView;
    private ListView mListView;

    List<ClockOnInfo> mDataList = new ArrayList<>();

    AttendanceAdapter adapter;

    private int offset = 0;
    private boolean hasMoreData = false;//是否有更多数据
    private boolean isLoadMore = false;//是否加载更多

    AttendanceRecordContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attendance_record;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_img).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("Attendance Record");

        refreshListView.setPullLoadEnabled(false);//上拉加载是否可用
        refreshListView.setScrollLoadEnabled(true);//判断滑动到底部加载是否可用
        refreshListView.setPullRefreshEnabled(true);//设置是否能下拉
        refreshListView.setOnRefreshListener(this);
        mListView = refreshListView.getRefreshableView();
    }

    @Override
    protected void initData() {
        new AttendanceRecordPresenter(this);
        adapter = new AttendanceAdapter(this);
        mListView.setAdapter(adapter);
        mPresenter.getClockOnList(0);
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
    public void setPresenter(AttendanceRecordContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        offset = 0;
        isLoadMore = false;
        mPresenter.getClockOnList(offset);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        offset++;
        isLoadMore = true;
        mPresenter.getClockOnList(offset);
    }

    @Override
    public void getClockOnSuccess(ClockOnList resp) {
        List<ClockOnInfo> list = resp.getClock_on_list();
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
}

package com.cds.promotion.module.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;
import com.cds.promotion.data.Constant;
import com.cds.promotion.data.entity.ClockOnInfo;
import com.cds.promotion.module.attendance.record.AttendanceRecordActivity;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 14:15
 * @Version: 3.0.0
 * 打卡界面
 */
public class AttendanceActivity extends BaseActivity implements View.OnClickListener, AttendanceContract.View {
    @Bind(R.id.check_in_time)
    TextView checkInTime;
    @Bind(R.id.check_in_status)
    TextView checkInStatus;
    @Bind(R.id.check_in_btn)
    LinearLayout checkInBtn;
    @Bind(R.id.check_out_time)
    TextView checkOutTime;
    @Bind(R.id.check_out_status)
    TextView checkOutStatus;
    @Bind(R.id.check_out_btn)
    LinearLayout checkOutBtn;
    @Bind(R.id.record_btn)
    TextView recordBtn;
    @Bind(R.id.go_work_icon)
    AppCompatImageView goWorkIcon;
    @Bind(R.id.off_work_icon)
    AppCompatImageView offWorkIcon;
    @Bind(R.id.go_work_title)
    TextView goWorkTitle;
    @Bind(R.id.go_work_layout)
    LinearLayout goWorkLayout;
    @Bind(R.id.off_work_title)
    TextView offWorkTitle;
    @Bind(R.id.off_work_layout)
    LinearLayout offWorkLayout;

    AttendanceContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attendance;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_img).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.record_btn).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("Attendance");
    }

    @Override
    protected void initData() {
        new AttendancePresenter(this);
        mPresenter.getClockOn();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.record_btn:
                Intent intent = new Intent();
                intent.setClass(AttendanceActivity.this, AttendanceRecordActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void setPresenter(AttendanceContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @OnClick({R.id.check_in_btn, R.id.check_out_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.check_in_btn:
                showProgressDilog();
                mPresenter.clockOn(Constant.ON_WORK);
                break;
            case R.id.check_out_btn:
                showProgressDilog();
                mPresenter.clockOn(Constant.OFF_WORK);
                break;
        }
    }

    @Override
    public void getClockOnSuccess(ClockOnInfo resp) {
        goWorkTitle.setText("Working hours " + resp.getGo_work_time().substring(10));
        offWorkTitle.setText("Working hours " + resp.getOff_work_time().substring(10));
        if (TextUtils.isEmpty(resp.getGo_work_active_time())) {
            goWorkIcon.setImageResource(R.mipmap.attendancedot_current);
            offWorkIcon.setImageResource(R.mipmap.attendancedot);

            checkInBtn.setVisibility(View.VISIBLE);
            goWorkLayout.setVisibility(View.GONE);
        } else {
            goWorkIcon.setImageResource(R.mipmap.attendancedot);
            offWorkIcon.setImageResource(R.mipmap.attendancedot_current);

            checkInBtn.setVisibility(View.GONE);
            goWorkLayout.setVisibility(View.VISIBLE);
            checkInTime.setText(resp.getGo_work_active_time());

            DateTime start = DateTime.parse(resp.getGo_work_active_time(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));
            DateTime end = DateTime.parse(resp.getGo_work_time(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));
            if(start.isAfter(end)){
                checkInStatus.setVisibility(View.VISIBLE);
            }else {
                checkInStatus.setVisibility(View.GONE);
            }
        }

        if (TextUtils.isEmpty(resp.getOff_work_active_time())) {
            checkOutBtn.setVisibility(View.VISIBLE);
            offWorkLayout.setVisibility(View.GONE);
        } else {
            checkOutBtn.setVisibility(View.GONE);
            offWorkLayout.setVisibility(View.VISIBLE);
            checkOutTime.setText(resp.getOff_work_active_time());

            DateTime start = DateTime.parse(resp.getOff_work_active_time(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));
            DateTime end = DateTime.parse(resp.getOff_work_time(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"));
            if(start.isAfter(end)){
                checkOutStatus.setVisibility(View.GONE);
            }else {
                checkOutStatus.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void clockOnSuccess() {
        hideProgressDilog();
        mPresenter.getClockOn();
    }

    @Override
    public void clockOnFailed() {
        hideProgressDilog();
    }
}

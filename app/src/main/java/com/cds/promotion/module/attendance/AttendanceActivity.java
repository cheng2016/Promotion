package com.cds.promotion.module.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;
import com.cds.promotion.module.attendance.record.AttendanceRecordActivity;
import com.cds.promotion.util.ToastUtils;

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
    AttendanceContract.Presenter mPresenter;
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

                break;
            case R.id.check_out_btn:

                break;
        }
    }

    @Override
    public void clockOnSuccess() {

    }
}

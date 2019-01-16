package com.cds.promotion.module.attendance;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;
import com.cds.promotion.module.message.MessageContract;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 14:15
 * @Version: 3.0.0
 */
public class AttendanceActivity extends BaseActivity implements View.OnClickListener, AttendanceContract.View {
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
        }
    }

    @Override
    public void setPresenter(AttendanceContract.Presenter presenter) {
        mPresenter = presenter;
    }
}

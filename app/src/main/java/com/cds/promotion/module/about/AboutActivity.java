package com.cds.promotion.module.about;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 14:25
 * @Version: 3.0.0
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_img).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("About APP");
    }

    @Override
    protected void initData() {
//        new AttendancePresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
        }
    }
}

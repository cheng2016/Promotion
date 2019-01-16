package com.cds.promotion.module.achievement;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 14:21
 * @Version: 3.0.0
 * 业绩
 *
 */
public class AchievementActivity  extends BaseActivity implements View.OnClickListener, AchievementContract.View {

    AchievementContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_achievement;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        findViewById(R.id.back_img).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
        ((TextView) findViewById(R.id.title)).setText("Achievement");
    }

    @Override
    protected void initData() {
        new AchievementPresenter(this);
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
    public void setPresenter(AchievementContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
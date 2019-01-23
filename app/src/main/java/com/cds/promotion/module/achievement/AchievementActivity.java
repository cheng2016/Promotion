package com.cds.promotion.module.achievement;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;
import com.cds.promotion.data.entity.AchievemenBean;

import butterknife.Bind;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 14:21
 * @Version: 3.0.0
 * 业绩
 */
public class AchievementActivity extends BaseActivity implements View.OnClickListener, AchievementContract.View {
    @Bind(R.id.signing_rand_tv)
    TextView signingRandTv;
    @Bind(R.id.sales_rand_tv)
    TextView salesRandTv;
    @Bind(R.id.amount_rand_tv)
    TextView amountRandTv;

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

        mPresenter.getAchievement();
        mLoadingView.showLoading();
        mLoadingView.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getAchievement();
            }
        });
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
    public void getAchievementSuccess(AchievemenBean resp) {
        mLoadingView.showContent();
        salesRandTv.setText("No." + resp.getSales_rank());
        signingRandTv.setText("No." + resp.getDealer_rank());
        amountRandTv.setText("No." + resp.getOrder_rank());
    }

    @Override
    public void getAchievementFailed() {
        mLoadingView.showError();
    }
}
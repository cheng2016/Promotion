package com.cds.promotion.module.visit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;
import com.cds.promotion.module.adapter.VisitAdapter;
import com.cds.promotion.module.main.MainActivity;
import com.cds.promotion.module.message.MessageActivity;
import com.cds.promotion.module.visit.record.VisitRecordActivity;
import com.cheng.refresh.library.PullToRefreshBase;
import com.cheng.refresh.library.PullToRefreshListView;

import butterknife.Bind;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 13:56
 * @Version: 3.0.0
 */
public class VisitActivity extends BaseActivity implements  VisitContract.View, View.OnClickListener {
    @Bind(R.id.right_img)
    ImageView rightImg;



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
        ((TextView) findViewById(R.id.title)).setText("Visiting");

        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.mipmap.btn_visitlog);
        findViewById(R.id.right_button).setOnClickListener(this);
    }

    @Override
    protected void initData() {
        new VisitPresenter(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.right_button:
                Intent intent = new Intent();
                intent.setClass(VisitActivity.this, VisitRecordActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(VisitContract.Presenter presenter) {
        mPresenter = presenter;
    }
}

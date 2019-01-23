package com.cds.promotion.module.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;

import butterknife.Bind;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 10:10
 * @Version: 3.0.0
 * 商铺
 */
public class StoreActivity extends BaseActivity implements View.OnClickListener, StoreFragment.OnStoreClickListener {
    @Bind(R.id.vp_horizontal_ntb)
    ViewPager viewPager;
    @Bind(R.id.tab)
    TabLayout tabLayout;

    private boolean isChooseActivity = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.title)).setText("Store");
        findViewById(R.id.back_img).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        findViewById(R.id.back_button).setOnClickListener(this);
        if (getIntent() != null && getIntent().getExtras() != null) {
            String title = getIntent().getStringExtra("title");
            ((TextView) findViewById(R.id.title)).setText(title);
            isChooseActivity = true;
        }
    }

    @Override
    protected void initData() {
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            Fragment[] mFragments = new Fragment[3];
            String[] titles = new String[]{"In Audit", "Signed", "To Be Signed"};

            @Override
            public int getCount() {
                return mFragments.length;
            }

            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0:
                        if (mFragments[0] == null) {
                            mFragments[0] = StoreFragment.newInstance(0).setListener(isChooseActivity,StoreActivity.this);
                        }
                        return mFragments[0];
                    case 1:
                        if (mFragments[1] == null) {
                            mFragments[1] = StoreFragment.newInstance(1).setListener(isChooseActivity,StoreActivity.this);
                        }
                        return mFragments[1];
                    case 2:
                        if (mFragments[2] == null) {
                            mFragments[2] = StoreFragment.newInstance(2).setListener(isChooseActivity,StoreActivity.this);
                        }
                        return mFragments[2];
                    default:
                        return null;
                }
            }

            /**
             * //此方法用来显示tab上的名字
             * @param position
             * @return
             */
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStoreClick(String dealer_id,String name) {
        Intent intent = new Intent();
        intent.putExtra("dealer_id", dealer_id);
        intent.putExtra("name", name);
        setResult(RESULT_OK,intent);
        finish();
    }
}

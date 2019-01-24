package com.cds.promotion.module.main;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.cds.promotion.App;
import com.cds.promotion.R;
import com.cds.promotion.base.BaseActivity;
import com.cds.promotion.data.entity.LoginOut;
import com.cds.promotion.data.entity.MenuBean;
import com.cds.promotion.data.entity.SalesInfo;
import com.cds.promotion.module.about.AboutActivity;
import com.cds.promotion.module.achievement.AchievementActivity;
import com.cds.promotion.module.adapter.MenuAdapter;
import com.cds.promotion.module.attendance.AttendanceActivity;
import com.cds.promotion.module.feedback.FeedBackActivity;
import com.cds.promotion.module.login.LoginActivity;
import com.cds.promotion.module.message.MessageActivity;
import com.cds.promotion.module.store.StoreActivity;
import com.cds.promotion.module.visit.VisitActivity;
import com.cds.promotion.service.SocketMsg;
import com.cds.promotion.service.SocketService;
import com.cds.promotion.service.message.ContentMsg;
import com.cds.promotion.service.message.HeadMsg;
import com.cds.promotion.service.message.LocationMsg;
import com.cds.promotion.service.message.TailMsg;
import com.cds.promotion.util.AppManager;
import com.cds.promotion.util.Logger;
import com.cds.promotion.util.PreferenceConstants;
import com.cds.promotion.util.PreferenceUtils;
import com.cds.promotion.util.ToastUtils;
import com.cds.promotion.util.picasso.PicassoCircleTransform;
import com.cds.promotion.view.CustomDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity implements MainContract.View, View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String MESSAGE_BROADCAST_ACTION = "com.cds.promotion.action.message";
    @Bind(R.id.right_img)
    ImageView rightImg;
    @Bind(R.id.head_img)
    ImageView headImg;
    @Bind(R.id.name)
    AppCompatTextView nameTv;
    @Bind(R.id.company)
    AppCompatTextView companyTv;
    @Bind(R.id.job_number)
    AppCompatTextView jobNumberTv;
    @Bind(R.id.menu_grid_list)
    GridView menuGridList;

    MainContract.Presenter mPresenter;

    MessageReceiver messageReceiver;

    int[] icons = {R.mipmap.btn_attendance, R.mipmap.btn_visiting, R.mipmap.btn_store,
            R.mipmap.btn_feedback, R.mipmap.btn_achievement, R.mipmap.btn_aboutapp
            , R.mipmap.btn_logout};

    String[] descripes = {"Attendance", "Visiting", "Shops", "Feedback", "Achievement", "About APP", "Logout"};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        rightImg.setVisibility(View.VISIBLE);
        rightImg.setImageResource(R.mipmap.group);
        findViewById(R.id.right_button).setOnClickListener(this);

        MenuAdapter menuAdapter = new MenuAdapter(this);
        menuGridList.setAdapter(menuAdapter);

        List<MenuBean> data = new ArrayList<>();
        for (int i = 0; i < icons.length; i++) {
            MenuBean bean = new MenuBean(icons[i], descripes[i]);
            data.add(bean);
        }
        menuAdapter.setDataList(data);
        menuGridList.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        new MainPresenter(this);
        //请求连接tcp服务器
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSocketService != null) {
                    mSocketService.startPushService();
                }
            }
        }, 300);

        mPresenter.getSalesInfo();
        registerReceiver();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //请求定位
                mPresenter.requestLocation();
            }
        }, 3000);
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onEventMainThread(Object event) {
        super.onEventMainThread(event);
        if(event instanceof LoginOut){
            //清空登录信息
            PreferenceUtils.setPrefString(App.getInstance(), PreferenceConstants.USER_PASSWORD, "");
            PreferenceUtils.setPrefString(App.getInstance(), PreferenceConstants.USER_ID, "");
            closeNetty();
            Intent i = new Intent().setClass(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(messageReceiver);
        closeNetty();
        //程序退出前清除所有推送
        clearNotificaction();
        mPresenter.unsubscribe();
    }

    private void clearNotificaction() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancelAll();
    }

    private void registerReceiver() {
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(MESSAGE_BROADCAST_ACTION);
        messageReceiver = new MessageReceiver();
        registerReceiver(messageReceiver, iFilter);
    }

    /**
     * 构造消息广播监听类，收到消息后刷新该页面
     */
    public class MessageReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MESSAGE_BROADCAST_ACTION)) {
//                messageTipImg.setVisibility(View.VISIBLE);
                Logger.i(TAG, "有一条新消息通知");
                findViewById(R.id.new_tip).setVisibility(View.VISIBLE);
            }
        }
    }

    //双击返回键 退出
    //----------------------------------------------------------------------------------------------
    private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            ToastUtils.showShort("再次点击返回键退出");
        }
        mBackPressed = System.currentTimeMillis();
    }

    void closeNetty() {
        if (null != mSocketService) {
            mSocketService.shutdownNetty();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_button:
                findViewById(R.id.new_tip).setVisibility(View.GONE);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MessageActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        switch (position) {
            case 0:
                intent.setClass(MainActivity.this, AttendanceActivity.class);
                startActivity(intent);
                break;
            case 1:
                intent.setClass(MainActivity.this, VisitActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent.setClass(MainActivity.this, StoreActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent.setClass(MainActivity.this, FeedBackActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent.setClass(MainActivity.this, AchievementActivity.class);
                startActivity(intent);
                break;
            case 5:
                intent.setClass(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case 6:
                new CustomDialog(this)
                        .setTitle(getResources().getString(R.string.exit_account))
                        .setPositiveButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (null != mSocketService) {
                                    mSocketService.shutdownNetty();
                                }
                                //清空登录信息
                                Intent i = new Intent().setClass(MainActivity.this, LoginActivity.class);
                                startActivity(i);
                                AppManager.getInstance().finishAllActivity();
                            }
                        }).showDialog();
                break;
        }
    }

    @Override
    public void getSalesInfoSuccess(SalesInfo resp) {
        Picasso.with(MainActivity.this)
                .load(resp.getHead_img())
                .error(R.mipmap.userportrait)
                .transform(new PicassoCircleTransform())
                .into(headImg);
        nameTv.setText(Html.fromHtml(resp.getName() + "&nbsp&nbsp<font color='#999999'>Welcome!</font>"));
        companyTv.setText(resp.getCooperative_name());
        jobNumberTv.setText(Html.fromHtml("<font color='#999999'>Job number :&nbsp&nbsp</font>" + resp.getJob_no()));
    }

    @Override
    public void onLocationChanged(Location location) {
        String userId = PreferenceUtils.getPrefString(App.getInstance(), PreferenceConstants.USER_ID, "");
        String token = PreferenceUtils.getPrefString(App.getInstance(), PreferenceConstants.ACCESS_TOKEN, "");
        SocketMsg socketMsg = new SocketMsg();
        socketMsg.setHead(new HeadMsg("", "13", userId, token));
        ContentMsg contentMsg = new ContentMsg();
        List<LocationMsg> list = new ArrayList<>();
        LocationMsg locationMsg = new LocationMsg(String.valueOf(location.getLongitude()),
                String.valueOf(location.getLatitude()), String.valueOf(location.getTime()));
        list.add(locationMsg);
        contentMsg.setLocations(list);
        socketMsg.setContent(contentMsg);
        socketMsg.setTail(new TailMsg(System.currentTimeMillis() + ""));
        if (null != mSocketService) {
            mSocketService.sendMessage(new Gson().toJson(socketMsg) + SocketService.END_FLAG);
        }
    }
}

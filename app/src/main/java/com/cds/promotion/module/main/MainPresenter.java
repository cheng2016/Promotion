package com.cds.promotion.module.main;

import android.location.Location;

import com.cds.promotion.App;
import com.cds.promotion.data.BaseResp;
import com.cds.promotion.data.entity.Info;
import com.cds.promotion.data.entity.SMessage;
import com.cds.promotion.data.entity.SalesInfo;
import com.cds.promotion.data.source.local.SMessageDaoUtils;
import com.cds.promotion.data.source.local.greendao.SMessageDao;
import com.cds.promotion.data.source.remote.BaseObserver;
import com.cds.promotion.data.source.remote.HttpApi;
import com.cds.promotion.data.source.remote.HttpFactory;
import com.cds.promotion.module.location.GpsLocationStrategy;
import com.cds.promotion.module.location.LocationController;
import com.cds.promotion.module.location.UpdateLocationListener;
import com.cds.promotion.module.message.MessageActivity;
import com.cds.promotion.util.Logger;
import com.cds.promotion.util.PreferenceConstants;
import com.cds.promotion.util.PreferenceUtils;
import com.cds.promotion.util.ToastUtils;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: chengzj
 * @CreateDate: 2018/12/5 16:51
 * @Version: 3.0.0
 */
public class MainPresenter implements MainContract.Presenter, UpdateLocationListener {
    public final static String TAG = "MainPresenter";
    private MainContract.View view;
    private HttpApi mHttpApi;
    private CompositeDisposable mCompositeDisposable;

    private SMessageDaoUtils daoUtils;
    private String userId;

    public MainPresenter(MainContract.View view) {
        this.view = view;
        view.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
        mHttpApi = HttpFactory.createRetrofit2(HttpApi.class);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
        stopLocation();
    }

    public void queryMessage(int offset) {
        Logger.i(TAG, "queryMessage  offset：" + offset);
        List<SMessage> messageList = daoUtils.querySMessage("USER_ID = ?", new String[]{userId}, SMessageDao.Properties.Id, offset, MessageActivity.REQUEST_NUM);//条件查询
        Logger.i(TAG, "queryMessage messageList：" + new Gson().toJson(messageList));
    }

    @Override
    public void getSalesInfo() {
        String userId = PreferenceUtils.getPrefString(App.getInstance(), PreferenceConstants.USER_ID, "");
        Info req = new Info(userId);
        mHttpApi.getSalesInfo(new Gson().toJson(req))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResp<SalesInfo>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResp<SalesInfo> resp) {
                        if ("200".equals(resp.getInfo().getCode())) {
                            view.getSalesInfoSuccess(resp.getData());
                        } else {
                            ToastUtils.showShort(resp.getInfo().getInfo());
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }


    private LocationController mLocationController;

    private GpsLocationStrategy gpsLocationStrategy;

    public void requestLocation() {
        if (gpsLocationStrategy == null) {
            gpsLocationStrategy = new GpsLocationStrategy(App.getInstance());
        }
        if (mLocationController == null) {
            mLocationController = new LocationController();
        }
        mLocationController.setLocationStrategy(gpsLocationStrategy);
        mLocationController.setListener(this);
    }

    public void stopLocation() {
        if (gpsLocationStrategy != null) {
            gpsLocationStrategy.stopLocation();
        }
    }

    private Location lastLocation = null;

    private long uploadGpsTime = 0L;

    @Override
    public void updateLocationChanged(Location location, int gpsCount) {
        if (lastLocation == null) {
            lastLocation = location;
            uploadGpsTime = System.currentTimeMillis();
            view.onLocationChanged(location);
        } else {
            //每次间隔15秒上传一次位置
            if (System.currentTimeMillis() - uploadGpsTime >= App.getInstance().LOCATION_BEAT_RATE) {
                lastLocation = location;
                uploadGpsTime = System.currentTimeMillis();
                view.onLocationChanged(location);
            }
        }
    }
}

package com.cds.promotion.module.attendance;

import com.cds.promotion.App;
import com.cds.promotion.data.BaseResp;
import com.cds.promotion.data.entity.ClockOnInfo;
import com.cds.promotion.data.entity.ClockOnReq;
import com.cds.promotion.data.entity.Info;
import com.cds.promotion.data.source.remote.BaseObserver;
import com.cds.promotion.data.source.remote.HttpApi;
import com.cds.promotion.data.source.remote.HttpFactory;
import com.cds.promotion.util.PreferenceConstants;
import com.cds.promotion.util.PreferenceUtils;
import com.cds.promotion.util.ToastUtils;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 14:16
 * @Version: 3.0.0
 */
public class AttendancePresenter implements AttendanceContract.Presenter {
    public final static String TAG = "AttendancePresenter";
    private AttendanceContract.View view;
    private HttpApi mHttpApi;
    private CompositeDisposable mCompositeDisposable;

    public AttendancePresenter(AttendanceContract.View view) {
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
    }

    @Override
    public void getClockOn() {
        String userId = PreferenceUtils.getPrefString(App.getInstance(), PreferenceConstants.USER_ID, "");
        Info req = new Info(userId);
        mHttpApi.getClockOn(new Gson().toJson(req))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResp<ClockOnInfo>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResp<ClockOnInfo> resp) {
                        if ("200".equals(resp.getInfo().getCode())) {
                            view.getClockOnSuccess(resp.getData());
                        } else {
                            view.getClockOnFail();
                            ToastUtils.showShort(resp.getInfo().getInfo());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.getClockOnFail();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void clockOn(String type,String location) {
        String userId = PreferenceUtils.getPrefString(App.getInstance(), PreferenceConstants.USER_ID, "");
        ClockOnReq req = new ClockOnReq(userId, type,location);
        mHttpApi.clockOn(new Gson().toJson(req))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResp>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResp resp) {
                        if ("200".equals(resp.getInfo().getCode())) {
                            view.clockOnSuccess();
                        } else {
                            view.clockOnFailed();
                            ToastUtils.showShort(resp.getInfo().getInfo());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.clockOnFailed();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public static void main(String[] args) {
        ClockOnReq req = new ClockOnReq("1", "0", "2018-02-02 09:06");
        System.out.printf(new Gson().toJson(req));
    }
}
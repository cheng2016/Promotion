package com.cds.promotion.module.attendance.record;

import com.cds.promotion.App;
import com.cds.promotion.data.BaseResp;
import com.cds.promotion.data.entity.ClockOnList;
import com.cds.promotion.data.entity.RecordListReq;
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
 * @CreateDate: 2019/1/17 11:27
 * @Version: 3.0.0
 */
public class AttendanceRecordPresenter implements AttendanceRecordContract.Presenter {
    public final static String TAG = "AttendanceRecordPresenter";
    private AttendanceRecordContract.View view;
    private HttpApi mHttpApi;
    private CompositeDisposable mCompositeDisposable;

    public AttendanceRecordPresenter(AttendanceRecordContract.View view) {
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
    public void getClockOnList(int offset) {
        String userId = PreferenceUtils.getPrefString(App.getInstance(), PreferenceConstants.USER_ID, "");
        RecordListReq req = new RecordListReq(userId,offset);
        mHttpApi.getClockOnList(new Gson().toJson(req))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResp<ClockOnList>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResp<ClockOnList> resp) {
                        if ("200".equals(resp.getInfo().getCode())) {
                            view.getClockOnListSuccess(resp.getData());
                        } else {
                            view.getClockOnListFail();
                            ToastUtils.showShort(resp.getInfo().getInfo());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.getClockOnListFail();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}

package com.cds.promotion.module.attendance;

import com.cds.promotion.data.source.remote.HttpApi;
import com.cds.promotion.data.source.remote.HttpFactory;

import io.reactivex.disposables.CompositeDisposable;

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
}
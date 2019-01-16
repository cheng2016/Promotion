package com.cds.promotion.module.visit;

import com.cds.promotion.data.source.remote.HttpApi;
import com.cds.promotion.data.source.remote.HttpFactory;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 13:56
 * @Version: 3.0.0
 */
public class VisitPresenter implements VisitContract.Presenter{
    public final static String TAG = "VisitPresenter";
    private VisitContract.View view;
    private HttpApi mHttpApi;
    private CompositeDisposable mCompositeDisposable;

    public VisitPresenter(VisitContract.View view) {
        this.view = view;
        view.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
        mHttpApi =  HttpFactory.createRetrofit2(HttpApi.class);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}

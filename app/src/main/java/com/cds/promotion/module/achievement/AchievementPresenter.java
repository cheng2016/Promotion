package com.cds.promotion.module.achievement;

import com.cds.promotion.data.source.remote.HttpApi;
import com.cds.promotion.data.source.remote.HttpFactory;

import io.reactivex.disposables.CompositeDisposable;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 14:21
 * @Version: 3.0.0
 */
public class AchievementPresenter implements AchievementContract.Presenter {
    public final static String TAG = "AchievementPresenter";
    private AchievementContract.View view;
    private HttpApi mHttpApi;
    private CompositeDisposable mCompositeDisposable;

    public AchievementPresenter(AchievementContract.View view) {
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
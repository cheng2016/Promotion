package com.cds.promotion.module.login;

import com.blankj.utilcode.util.ToastUtils;
import com.cds.promotion.App;
import com.cds.promotion.data.BaseResp;
import com.cds.promotion.data.entity.Info;
import com.cds.promotion.data.entity.LoginReq;
import com.cds.promotion.data.source.remote.BaseObserver;
import com.cds.promotion.data.source.remote.HttpApi;
import com.cds.promotion.data.source.remote.HttpFactory;
import com.cds.promotion.util.MD5Utils;
import com.cds.promotion.util.PreferenceConstants;
import com.cds.promotion.util.PreferenceUtils;
import com.google.gson.Gson;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginContract.Presenter {
    public final static String TAG = "LoginPresenter";
    private LoginContract.View view;
    private HttpApi mHttpApi;
    private CompositeDisposable mCompositeDisposable;


    public LoginPresenter(LoginContract.View view) {
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
    public void login(final String name, final String pwd) {
        LoginReq req = new LoginReq(name, MD5Utils.MD5(pwd));
        mHttpApi.login(new Gson().toJson(req))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResp<Info>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResp<Info> resp) {
                        if ("200".equals(resp.getInfo().getCode())) {
                            view.loginSuccess();
                            PreferenceUtils.setPrefString(App.getInstance(), PreferenceConstants.USER_ID, resp.getData().getUser_id());
                            PreferenceUtils.setPrefString(App.getInstance(), PreferenceConstants.USER_NAME, name);
                            PreferenceUtils.setPrefString(App.getInstance(), PreferenceConstants.USER_PASSWORD, pwd);
                        } else {
                            view.loginFailed();
                            ToastUtils.showShort(resp.getInfo().getInfo());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.loginFailed();
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }
}

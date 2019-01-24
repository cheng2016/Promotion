package com.cds.promotion.module.store;

import com.cds.promotion.App;
import com.cds.promotion.data.BaseResp;
import com.cds.promotion.data.entity.DealerListReq;
import com.cds.promotion.data.entity.StoreList;
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
 * @CreateDate: 2019/1/17 10:11
 * @Version: 3.0.0
 */
public class StorePresenter implements StoreContract.Presenter{
    public final static String TAG = "StorePresenter";
    private StoreContract.View view;
    private HttpApi mHttpApi;
    private CompositeDisposable mCompositeDisposable;

    public StorePresenter(StoreContract.View view) {
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

    @Override
    public void getDealerList(int type, int offset) {
        String userId = PreferenceUtils.getPrefString(App.getInstance(), PreferenceConstants.USER_ID, "");
        DealerListReq req = new DealerListReq(userId,type,offset);
        mHttpApi.getDealerList(new Gson().toJson(req))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<BaseResp<StoreList>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResp<StoreList> resp) {
                        if ("200".equals(resp.getInfo().getCode())) {
                            view.getDealerListSuccess(resp.getData());
                        } else {
                            view.getDealerListFail();
                            ToastUtils.showShort(resp.getInfo().getInfo());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.getDealerListFail();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}

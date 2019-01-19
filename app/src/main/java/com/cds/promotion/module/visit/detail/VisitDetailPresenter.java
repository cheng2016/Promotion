package com.cds.promotion.module.visit.detail;

import com.blankj.utilcode.util.ToastUtils;
import com.cds.promotion.data.BaseResp;
import com.cds.promotion.data.entity.VisitingList;
import com.cds.promotion.data.source.remote.BaseObserver;
import com.cds.promotion.data.source.remote.HttpApi;
import com.cds.promotion.data.source.remote.HttpFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/19 13:59
 * @Version: 3.0.0
 */
public class VisitDetailPresenter implements VisitDetailContract.Presenter {
    public final static String TAG = "VisitDetailPresenter";
    private VisitDetailContract.View view;
    private HttpApi mHttpApi;
    private CompositeDisposable mCompositeDisposable;

    public VisitDetailPresenter(VisitDetailContract.View view) {
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
    public void getVisitingInfo(String visiting_id) {
        JsonObject object = new JsonObject();
        object.addProperty("visiting_id", visiting_id);
        mHttpApi.getVisitingInfo(object.toString())
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
                            view.getVisitingInfoSuccess();
                        } else {
                            ToastUtils.showShort(resp.getInfo().getInfo());
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public static void main(String[] args) {
        String str = "{'dealer_id':'用户id'}";
        JsonParser parse = new JsonParser();
        JsonObject object = new JsonObject();
        object.addProperty("visiting_id", "11");
        System.out.println(object.toString());
    }
}

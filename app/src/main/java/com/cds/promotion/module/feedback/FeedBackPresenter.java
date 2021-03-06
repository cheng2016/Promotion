package com.cds.promotion.module.feedback;

import android.text.TextUtils;

import com.cds.promotion.App;
import com.cds.promotion.data.BaseResp;
import com.cds.promotion.data.entity.FeedBackReq;
import com.cds.promotion.data.source.remote.BaseObserver;
import com.cds.promotion.data.source.remote.HttpApi;
import com.cds.promotion.data.source.remote.HttpFactory;
import com.cds.promotion.util.PreferenceConstants;
import com.cds.promotion.util.PreferenceUtils;
import com.cds.promotion.util.ToastUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FeedBackPresenter implements FeedBackContract.Presenter {
    public final static String TAG = "FeedBackPresenter";
    private FeedBackContract.View view;
    private HttpApi mHttpApi;
    private CompositeDisposable mCompositeDisposable;

    public FeedBackPresenter(FeedBackContract.View view) {
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
    public void feedback(String content, final List<String> imageUrls) {
        int userId = Integer.parseInt(PreferenceUtils.getPrefString(App.getInstance(), PreferenceConstants.USER_ID, ""));
        final FeedBackReq req = new FeedBackReq(userId, content);
        Observable.just("")
                .subscribeOn(Schedulers.io())                // 切换至IO线程
                .flatMap(new Function<String, ObservableSource<BaseResp>>() {
                    @Override
                    public ObservableSource<BaseResp> apply(String s) throws Exception {
                        if (imageUrls == null || imageUrls.size() == 0) {
                            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("content", new Gson().toJson(req))
                                    .addPart(MultipartBody.Part.createFormData("imgs", ""))
                                    .build();
                            return mHttpApi.feedback(requestBody);
                        } else {
                            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("content", new Gson().toJson(req));
                            for (String imagePath : imageUrls) {
                                File file = new File(imagePath);
                                if(!TextUtils.isEmpty(imagePath) && file.exists()){
                                    // 封装请求体
                                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);
                                    MultipartBody.Part filePart =
                                            MultipartBody.Part.createFormData("imgs", file.getName(), requestFile);
                                    builder.addPart(filePart);
                                }
                            }
                            return mHttpApi.feedback(builder.build());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())           // 切换至Android主线程
                .subscribe(new BaseObserver<BaseResp>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResp baseResp) {
                        if ("200".equals(baseResp.getInfo().getCode())) {
                            view.feedbackSuccess();
                        } else {
                            view.feedbackFailed();
                            ToastUtils.showShort(App.getInstance(), baseResp.getInfo().getInfo());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.feedbackFailed();
                    }
                });
    }
}

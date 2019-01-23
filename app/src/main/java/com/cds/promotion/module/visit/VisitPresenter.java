package com.cds.promotion.module.visit;

import android.text.TextUtils;

import com.cds.promotion.App;
import com.cds.promotion.data.BaseResp;
import com.cds.promotion.data.entity.SaveVisitingReq;
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

    @Override
    public void saveVisiting(String dealer_id, String visiting_notes, String visiting_location,
                             final List<String> imageUrls) {
        String userId = PreferenceUtils.getPrefString(App.getInstance(), PreferenceConstants.USER_ID, "");
        final SaveVisitingReq req = new SaveVisitingReq(userId,dealer_id,visiting_notes,visiting_location);
        Observable.just("")
                .subscribeOn(Schedulers.io())                // 切换至IO线程
                .flatMap(new Function<String, ObservableSource<BaseResp>>() {
                    @Override
                    public ObservableSource<BaseResp> apply(String s) throws Exception {
                        if (imageUrls == null || imageUrls.size() == 0) {
                            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("content", new Gson().toJson(req))
                                    .addPart(MultipartBody.Part.createFormData("visiting_img", ""))
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
                                            MultipartBody.Part.createFormData("visiting_img", file.getName(), requestFile);
                                    builder.addPart(filePart);
                                }
                            }
                            return mHttpApi.saveVisiting(builder.build());
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
                            view.saveVisitingSuccess();
                        } else {
                            view.saveVisitingFail();
                            ToastUtils.showShort(App.getInstance(), baseResp.getInfo().getInfo());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.saveVisitingFail();
                    }
                });
    }
}

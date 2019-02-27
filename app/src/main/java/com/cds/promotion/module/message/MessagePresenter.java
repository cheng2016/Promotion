package com.cds.promotion.module.message;

import com.cds.promotion.App;
import com.cds.promotion.data.entity.SMessage;
import com.cds.promotion.data.source.local.SMessageDaoUtils;
import com.cds.promotion.data.source.local.greendao.SMessageDao;
import com.cds.promotion.data.source.remote.BaseObserver;
import com.cds.promotion.data.source.remote.HttpApi;
import com.cds.promotion.data.source.remote.HttpFactory;
import com.cds.promotion.util.Logger;
import com.cds.promotion.util.PreferenceConstants;
import com.cds.promotion.util.PreferenceUtils;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: chengzj
 * @CreateDate: 2018/12/4 14:12
 * @Version: 3.0.0
 */
public class MessagePresenter implements MessageContract.Presenter {
    public final static String TAG = "MessagePresenter";
    private MessageContract.View view;
    private HttpApi mHttpApi;
    private CompositeDisposable mCompositeDisposable;
    private SMessageDaoUtils daoUtils;
    private String userId;

    public MessagePresenter(MessageContract.View view) {
        this.view = view;
        view.setPresenter(this);
        mCompositeDisposable = new CompositeDisposable();
        mHttpApi = HttpFactory.createRetrofit2(HttpApi.class);
        daoUtils = new SMessageDaoUtils(App.getInstance());
        userId = PreferenceUtils.getPrefString(App.getInstance(), PreferenceConstants.USER_ID, "");
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void queryMessage(final int offset) {
        Logger.i(TAG, "queryMessage  offset：" + offset);
        Observable.create(new ObservableOnSubscribe<List<SMessage>>() {
            @Override
            public void subscribe(ObservableEmitter<List<SMessage>> emitter) throws Exception {
                List<SMessage> messageList = daoUtils.querySMessage("USER_ID = ?", new String[]{userId}, SMessageDao.Properties.Id, offset, MessageActivity.REQUEST_NUM);//条件查询
                Logger.i(TAG, "queryMessage messageList：" + new Gson().toJson(messageList));
                emitter.onNext(messageList);
                emitter.onComplete();
            }
        }).observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io())//执行在io线程
                .subscribe(new BaseObserver<List<SMessage>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Logger.e(TAG, "onSubscribe");
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<SMessage> value) {
                        Logger.e(TAG, "onNext:" + value);
                        view.queryMessageSuccess(value);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void updateMessage(List<SMessage> list) {
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setIsNew(1);
            }
            daoUtils.updateSMessage(list);
        }
    }

    @Override
    public void clearMessage() {
        daoUtils.deleteAll();
    }
}

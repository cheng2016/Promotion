package com.cds.promotion.module.main;

import android.location.Location;

import com.cds.promotion.data.entity.SalesInfo;
import com.cds.promotion.module.BasePresenter;
import com.cds.promotion.module.BaseView;

/**
 * @Author: chengzj
 * @CreateDate: 2018/12/5 16:50
 * @Version: 3.0.0
 */
public interface MainContract {
    interface View extends BaseView<Presenter> {
        void getSalesInfoSuccess(SalesInfo resp);

        void onLocationChanged(Location location);


    }

    interface Presenter extends BasePresenter {
        void getSalesInfo();

        void requestLocation();
    }
}

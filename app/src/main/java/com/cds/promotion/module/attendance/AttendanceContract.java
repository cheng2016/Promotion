package com.cds.promotion.module.attendance;

import com.cds.promotion.module.BasePresenter;
import com.cds.promotion.module.BaseView;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 14:16
 * @Version: 3.0.0
 */
public interface AttendanceContract {
    interface View extends BaseView<Presenter> {
        void clockOnSuccess();
    }

    interface Presenter extends BasePresenter {
        void clockOn( String time,
                     String type);

        void clockOn(String location,
                     String address,
                     String time,
                     String description,
                     String type);
    }
}

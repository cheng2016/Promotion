package com.cds.promotion.module.attendance;

import com.cds.promotion.data.entity.ClockOnInfo;
import com.cds.promotion.module.BasePresenter;
import com.cds.promotion.module.BaseView;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 14:16
 * @Version: 3.0.0
 */
public interface AttendanceContract {
    interface View extends BaseView<Presenter> {
        void getClockOnSuccess(ClockOnInfo resp);

        void clockOnSuccess();

        void clockOnFailed();
    }

    interface Presenter extends BasePresenter {
        void getClockOn();

        void clockOn(String type);

        void clockOn(String location,
                     String address,
                     String description,
                     String type);
    }
}

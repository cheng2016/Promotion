package com.cds.promotion.module.attendance.record;

import com.cds.promotion.data.entity.ClockOnList;
import com.cds.promotion.module.BasePresenter;
import com.cds.promotion.module.BaseView;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 11:27
 * @Version: 3.0.0
 */
public interface AttendanceRecordContract {
    interface View extends BaseView<Presenter> {
        void getClockOnListSuccess(ClockOnList resp);

        void getClockOnListFail();
    }

    interface Presenter extends BasePresenter {
        void getClockOnList(int offset);
    }
}

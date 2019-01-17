package com.cds.promotion.module.attendance.record;

import com.cds.promotion.data.entity.ClockOnInfo;
import com.cds.promotion.data.entity.ClockOnList;
import com.cds.promotion.module.BasePresenter;
import com.cds.promotion.module.BaseView;

import java.util.List;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 11:27
 * @Version: 3.0.0
 */
public interface AttendanceRecordContract {
    interface View extends BaseView<Presenter> {
        void getClockOnSuccess(ClockOnList resp);
    }

    interface Presenter extends BasePresenter {
        void getClockOnList(int offset);
    }
}

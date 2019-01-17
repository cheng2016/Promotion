package com.cds.promotion.module.visit.record;

import com.cds.promotion.data.entity.VisitingList;
import com.cds.promotion.module.BasePresenter;
import com.cds.promotion.module.BaseView;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 10:43
 * @Version: 3.0.0
 */
public interface VisitRecordContract {
    interface View extends BaseView<Presenter> {
        void getVisitingListSuccess(VisitingList resp);
    }

    interface Presenter extends BasePresenter {
        void getVisitingList(int offset);
    }
}

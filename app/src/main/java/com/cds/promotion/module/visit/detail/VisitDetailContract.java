package com.cds.promotion.module.visit.detail;

import com.cds.promotion.data.entity.VisitBean;
import com.cds.promotion.module.BasePresenter;
import com.cds.promotion.module.BaseView;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/19 13:58
 * @Version: 3.0.0
 */
public interface VisitDetailContract {
    interface View extends BaseView<Presenter> {
        void getVisitingInfoSuccess();

        void getVisitingInfoFail();
    }

    interface Presenter extends BasePresenter {
        void getVisitingInfo(String visiting_id);
    }

}

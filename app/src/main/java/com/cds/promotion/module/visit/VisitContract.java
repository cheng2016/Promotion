package com.cds.promotion.module.visit;

import com.cds.promotion.module.BasePresenter;
import com.cds.promotion.module.BaseView;

import java.util.List;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 13:56
 * @Version: 3.0.0
 */
public interface VisitContract {
    interface View extends BaseView<Presenter> {
        void saveVisitingSuccess();

        void saveVisitingFail();
    }

    interface Presenter extends BasePresenter {
        void saveVisiting(String dealer_id, String visiting_notes,
                              String visiting_location, List<String> imgUrls);
    }
}

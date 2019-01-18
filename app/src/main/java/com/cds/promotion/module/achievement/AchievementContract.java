package com.cds.promotion.module.achievement;

import com.cds.promotion.data.entity.AchievemenBean;
import com.cds.promotion.module.BasePresenter;
import com.cds.promotion.module.BaseView;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 14:21
 * @Version: 3.0.0
 */
public interface AchievementContract {
    interface View extends BaseView<Presenter> {
        void getAchievementSuccess(AchievemenBean resp);
    }

    interface Presenter extends BasePresenter {
        void getAchievement();
    }
}

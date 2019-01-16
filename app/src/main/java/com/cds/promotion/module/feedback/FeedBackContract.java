package com.cds.promotion.module.feedback;

import com.cds.promotion.module.BasePresenter;
import com.cds.promotion.module.BaseView;

public interface FeedBackContract {
    interface View extends BaseView<Presenter> {
        void feedbackSuccess();

        void feedbackFailed();
    }

    interface Presenter extends BasePresenter {
        void feedback(String content, String imageUrl);
    }
}

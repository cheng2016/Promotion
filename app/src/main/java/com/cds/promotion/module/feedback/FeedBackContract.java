package com.cds.promotion.module.feedback;

import com.cds.promotion.module.BasePresenter;
import com.cds.promotion.module.BaseView;

import java.util.List;

public interface FeedBackContract {
    interface View extends BaseView<Presenter> {
        void feedbackSuccess();

        void feedbackFailed();
    }

    interface Presenter extends BasePresenter {
        void feedback(String content, List<String> imageUrls);
    }
}

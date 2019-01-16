package com.cds.promotion.module.login;

import com.cds.promotion.module.BasePresenter;
import com.cds.promotion.module.BaseView;

public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void loginSuccess();

        void loginFailed();
    }

    interface Presenter extends BasePresenter {
        void login(String name, String pwd);
    }
}

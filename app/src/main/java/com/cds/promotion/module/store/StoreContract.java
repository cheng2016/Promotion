package com.cds.promotion.module.store;

import com.cds.promotion.data.entity.StoreList;
import com.cds.promotion.module.BasePresenter;
import com.cds.promotion.module.BaseView;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 10:11
 * @Version: 3.0.0
 */
public interface StoreContract {
    interface View extends BaseView<Presenter> {
        void getDealerListSuccess(StoreList resp);

        void getDealerListFail();
    }

    interface Presenter extends BasePresenter {
        void getDealerList(int type, int offset);
    }
}

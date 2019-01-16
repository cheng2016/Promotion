package com.cds.promotion.module.message;

import com.cds.promotion.data.entity.SMessage;
import com.cds.promotion.module.BasePresenter;
import com.cds.promotion.module.BaseView;

import java.util.List;

/**
 * @Author: chengzj
 * @CreateDate: 2018/12/4 14:11
 * @Version: 3.0.0
 */
public interface MessageContract {
    interface View extends BaseView<Presenter> {
        void queryMessageSuccess(List<SMessage> dataList);

        void queryMessage();
    }

    interface Presenter extends BasePresenter {
        void queryMessage(int offset);
    }
}

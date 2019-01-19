package com.cds.promotion.data.entity;

import java.util.List;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/19 10:46
 * @Version: 3.0.0
 */
public class StoreList {
    List<StoreBean> dealers;

    public List<StoreBean> getDealers() {
        return dealers;
    }

    public void setDealers(List<StoreBean> dealers) {
        this.dealers = dealers;
    }
}

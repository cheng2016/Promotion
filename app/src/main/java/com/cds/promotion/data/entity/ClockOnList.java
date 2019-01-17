package com.cds.promotion.data.entity;

import java.util.List;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 14:47
 * @Version: 3.0.0
 */
public class ClockOnList {
    List<ClockOnInfo> clock_on_list;

    public List<ClockOnInfo> getClock_on_list() {
        return clock_on_list;
    }

    public void setClock_on_list(List<ClockOnInfo> clock_on_list) {
        this.clock_on_list = clock_on_list;
    }
}

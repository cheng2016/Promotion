package com.cds.promotion.data.entity;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 14:42
 * @Version: 3.0.0
 */
public class ClockOnInfo {

    /**
     * go_work_date : 2019-01-10
     * off_work_active_time : 18:46:08
     * go_work_active_time : 09:29:49
     */

    private String go_work_date;
    private String off_work_active_time;
    private String go_work_active_time;

    public String getGo_work_date() {
        return go_work_date;
    }

    public void setGo_work_date(String go_work_date) {
        this.go_work_date = go_work_date;
    }

    public String getOff_work_active_time() {
        return off_work_active_time;
    }

    public void setOff_work_active_time(String off_work_active_time) {
        this.off_work_active_time = off_work_active_time;
    }

    public String getGo_work_active_time() {
        return go_work_active_time;
    }

    public void setGo_work_active_time(String go_work_active_time) {
        this.go_work_active_time = go_work_active_time;
    }
}

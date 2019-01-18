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

    private String go_work_date;//打卡日期

    private String go_work_active_time;//上班打卡时间
    private String go_work_address;//上班打卡地址
    private String go_work_time;//上班打卡时间

    private String off_work_active_time;//下班打卡时间
    private String off_work_address;//下班打卡地址
    private String off_work_time;//下班打卡时间

    private String late;//否 【有值时显示】
    private String leave_early;//否【有值时显示】

    public String getGo_work_date() {
        return go_work_date;
    }

    public void setGo_work_date(String go_work_date) {
        this.go_work_date = go_work_date;
    }

    public String getGo_work_active_time() {
        return go_work_active_time;
    }

    public void setGo_work_active_time(String go_work_active_time) {
        this.go_work_active_time = go_work_active_time;
    }

    public String getGo_work_address() {
        return go_work_address;
    }

    public void setGo_work_address(String go_work_address) {
        this.go_work_address = go_work_address;
    }

    public String getGo_work_time() {
        return go_work_time;
    }

    public void setGo_work_time(String go_work_time) {
        this.go_work_time = go_work_time;
    }

    public String getOff_work_active_time() {
        return off_work_active_time;
    }

    public void setOff_work_active_time(String off_work_active_time) {
        this.off_work_active_time = off_work_active_time;
    }

    public String getOff_work_address() {
        return off_work_address;
    }

    public void setOff_work_address(String off_work_address) {
        this.off_work_address = off_work_address;
    }

    public String getOff_work_time() {
        return off_work_time;
    }

    public void setOff_work_time(String off_work_time) {
        this.off_work_time = off_work_time;
    }

    public String getLate() {
        return late;
    }

    public void setLate(String late) {
        this.late = late;
    }

    public String getLeave_early() {
        return leave_early;
    }

    public void setLeave_early(String leave_early) {
        this.leave_early = leave_early;
    }
}

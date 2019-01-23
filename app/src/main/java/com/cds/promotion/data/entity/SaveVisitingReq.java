package com.cds.promotion.data.entity;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/22 18:54
 * @Version: 3.0.0
 */
public class SaveVisitingReq {
    private String user_id;
    private String dealer_id;
    private String visiting_notes;
    private String visiting_location;

    public SaveVisitingReq(String user_id, String dealer_id, String visiting_notes, String visiting_location) {
        this.user_id = user_id;
        this.dealer_id = dealer_id;
        this.visiting_notes = visiting_notes;
        this.visiting_location = visiting_location;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDealer_id() {
        return dealer_id;
    }

    public void setDealer_id(String dealer_id) {
        this.dealer_id = dealer_id;
    }

    public String getVisiting_notes() {
        return visiting_notes;
    }

    public void setVisiting_notes(String visiting_notes) {
        this.visiting_notes = visiting_notes;
    }

    public String getVisiting_location() {
        return visiting_location;
    }

    public void setVisiting_location(String visiting_location) {
        this.visiting_location = visiting_location;
    }
}

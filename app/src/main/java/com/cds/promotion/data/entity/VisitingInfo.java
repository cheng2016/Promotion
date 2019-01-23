package com.cds.promotion.data.entity;

import java.util.List;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/22 19:18
 * @Version: 3.0.0
 */
public class VisitingInfo {

    /**
     * visiting_date : 1546955871000
     * visiting_notes : sadfsdf111111111111111111
     * dealer_name : SASF phone shop 1
     * visiting_img : ["https://images.chuanboyi.com/201706/5809fcb452.jpg","https://images.chuanboyi.com/201706/5809fcb452.jpg","https://images.chuanboyi.com/201706/5809fcb452.jpg"]
     * dealer_location : 19.885291,78.424878
     * dealer_id : 1
     */

    private long visiting_date;
    private String visiting_notes;
    private String dealer_name;
    private String dealer_location;
    private int dealer_id;
    private List<String> visiting_img;

    public long getVisiting_date() {
        return visiting_date;
    }

    public void setVisiting_date(long visiting_date) {
        this.visiting_date = visiting_date;
    }

    public String getVisiting_notes() {
        return visiting_notes;
    }

    public void setVisiting_notes(String visiting_notes) {
        this.visiting_notes = visiting_notes;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public String getDealer_location() {
        return dealer_location;
    }

    public void setDealer_location(String dealer_location) {
        this.dealer_location = dealer_location;
    }

    public int getDealer_id() {
        return dealer_id;
    }

    public void setDealer_id(int dealer_id) {
        this.dealer_id = dealer_id;
    }

    public List<String> getVisiting_img() {
        return visiting_img;
    }

    public void setVisiting_img(List<String> visiting_img) {
        this.visiting_img = visiting_img;
    }
}

package com.cds.promotion.data.entity;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/16 14:05
 * @Version: 3.0.0
 */
public class VisitBean {

    /**
     * dealer_img : https://images.chuanboyi.com/201706/5809fcb452.jpg
     * visiting_date : 1546946871000
     * user_id : 1
     * dealer_address : asdfdsfad
     * dealer_name : SASF phone shop 1
     */

    private String dealer_img;
    private long visiting_date;
    private int user_id;
    private String dealer_address;
    private String dealer_name;

    public String getDealer_img() {
        return dealer_img;
    }

    public void setDealer_img(String dealer_img) {
        this.dealer_img = dealer_img;
    }

    public long getVisiting_date() {
        return visiting_date;
    }

    public void setVisiting_date(long visiting_date) {
        this.visiting_date = visiting_date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDealer_address() {
        return dealer_address;
    }

    public void setDealer_address(String dealer_address) {
        this.dealer_address = dealer_address;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }
}

package com.cds.promotion.data.entity;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 14:09
 * @Version: 3.0.0
 */
public class ClockOnReq {
    /**
     * user_id : 用户id
     * location : 经纬度
     * address : 打卡地址
     * time : 打卡时间
     * description : 打卡说明
     * type : 类型 0 下班打卡，1上班打卡
     */

    private String user_id;
    private String type;
    private String location;
    private String address;
    //    private String time;
    private String description;


    public ClockOnReq() {
    }

    public ClockOnReq(String user_id, String type, String location) {
        this.user_id = user_id;
        this.type = type;
        this.location = location;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

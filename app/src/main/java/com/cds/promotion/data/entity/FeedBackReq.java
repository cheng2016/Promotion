package com.cds.promotion.data.entity;

public class FeedBackReq {

    /**
     * user_id : id
     * content : 反馈内容
     * device_id : 设备id
     */

    private int user_id;
    private String content;

    public FeedBackReq(int user_id, String content) {
        this.user_id = user_id;
        this.content = content;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

package com.cds.promotion.data.entity;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 14:38
 * @Version: 3.0.0
 */
public class RecordListReq {
    /**
     * user_id : 医生id
     * page_no : 0
     */

    private String user_id;

    private int page_no;

    public RecordListReq(String user_id, int page_no) {
        this.user_id = user_id;
        this.page_no = page_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getPage_no() {
        return page_no;
    }

    public void setPage_no(int page_no) {
        this.page_no = page_no;
    }
}

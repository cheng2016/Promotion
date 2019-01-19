package com.cds.promotion.data.entity;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/19 9:42
 * @Version: 3.0.0
 */
public class DealerListReq {
    /**
     * user_id : 用户id
     * type : 查询类型  0：签约中，1：已签约，2.待签约
     * page_no : 页码
     */

    private String user_id;
    private int type;
    private int page_no;

    public DealerListReq(String user_id, int type, int page_no) {
        this.user_id = user_id;
        this.type = type;
        this.page_no = page_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPage_no() {
        return page_no;
    }

    public void setPage_no(int page_no) {
        this.page_no = page_no;
    }
}

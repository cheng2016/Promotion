package com.cds.promotion.data.entity;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/17 17:51
 * @Version: 3.0.0
 */
public class SalesInfo {
    /**
     * cooperative_name : 测试公司一
     * user_id : 1
     * head_img : https://sit.wecarelove.com/opt/eshop/headImg/c0292752e721ea9805eaf6d1e8830136.JPG
     * job_no : Z201-1
     * name : 张三4
     */

    private String cooperative_name;
    private int user_id;
    private String head_img;
    private String job_no;
    private String name;

    public String getCooperative_name() {
        return cooperative_name;
    }

    public void setCooperative_name(String cooperative_name) {
        this.cooperative_name = cooperative_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getJob_no() {
        return job_no;
    }

    public void setJob_no(String job_no) {
        this.job_no = job_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

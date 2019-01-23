package com.cds.promotion.data.entity;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/19 10:46
 * @Version: 3.0.0
 */
public class StoreBean {

    /**
     * img : https://images.chuanboyi.com/201706/5809fcb452.jpg
     * address : asdfdsfad
     * tel_phone_number : 13691912020
     * name : 店铺一
     */
    private String id;
    private String img;
    private String address;
    private String tel_phone_number;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel_phone_number() {
        return tel_phone_number;
    }

    public void setTel_phone_number(String tel_phone_number) {
        this.tel_phone_number = tel_phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

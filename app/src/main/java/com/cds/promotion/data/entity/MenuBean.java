package com.cds.promotion.data.entity;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/12 13:46
 * @Version: 3.0.0
 */
public class MenuBean {
    private int icon;
    private String name;

    public MenuBean(int icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

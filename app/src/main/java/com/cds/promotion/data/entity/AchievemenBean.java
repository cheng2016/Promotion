package com.cds.promotion.data.entity;

/**
 * @Author: chengzj
 * @CreateDate: 2019/1/18 14:44
 * @Version: 3.0.0
 */
public class AchievemenBean {
    /**
     * sales_rank : 1
     * dealer_rank : 1
     * order_rank : 1
     * sales_count : 2
     */

    private String sales_rank;
    private String dealer_rank;
    private String order_rank;
    private int sales_count;

    public String getSales_rank() {
        return sales_rank;
    }

    public void setSales_rank(String sales_rank) {
        this.sales_rank = sales_rank;
    }

    public String getDealer_rank() {
        return dealer_rank;
    }

    public void setDealer_rank(String dealer_rank) {
        this.dealer_rank = dealer_rank;
    }

    public String getOrder_rank() {
        return order_rank;
    }

    public void setOrder_rank(String order_rank) {
        this.order_rank = order_rank;
    }

    public int getSales_count() {
        return sales_count;
    }

    public void setSales_count(int sales_count) {
        this.sales_count = sales_count;
    }
}

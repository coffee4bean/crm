package com.shsxt.crm.query;

import com.shsxt.crm.base.BaseQuery;

import java.util.Date;


public class SaleChanceQuery extends BaseQuery {
    private String customerName;

    private Integer state;

    private Integer devResult;

    private Date createDate;


    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getDevResult() {
        return devResult;
    }

    public void setDevResult(Integer devResult) {
        this.devResult = devResult;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

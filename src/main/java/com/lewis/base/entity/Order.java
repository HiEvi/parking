package com.lewis.base.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private Integer id;

    private String account;

    private String name;

    private Date preTime;

    private Date startStay;

    private Integer stayTime;

    private Date endStay;

    private String status;

    private BigDecimal money;

    private String parkUid;

    private String phone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getPreTime() {
        return preTime;
    }

    public void setPreTime(Date preTime) {
        this.preTime = preTime;
    }

    public Date getStartStay() {
        return startStay;
    }

    public void setStartStay(Date startStay) {
        this.startStay = startStay;
    }

    public Integer getStayTime() {
        return stayTime;
    }

    public void setStayTime(Integer stayTime) {
        this.stayTime = stayTime;
    }

    public Date getEndStay() {
        return endStay;
    }

    public void setEndStay(Date endStay) {
        this.endStay = endStay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getParkUid() {
        return parkUid;
    }

    public void setParkUid(String parkUid) {
        this.parkUid = parkUid == null ? null : parkUid.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }
}
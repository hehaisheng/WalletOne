package com.zhiyu.wallet.bean;

import android.text.TextUtils;

/**
 * Created by Administrator on 2018/11/7.
 */

public class Record {

    private String time;
    private String money;
    private String repaymenttime;
    private String status;
    private String repaidamount;
    private String statuscopy;
    private int codestatus;


    public String getTime() {
        return time.split(" ")[0];
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRepaymenttime() {
        return repaymenttime;
    }

    public void setRepaymenttime(String repaymenttime) {
        this.repaymenttime = repaymenttime;
    }

    public String getRepaidamount() {
        return repaidamount;
    }

    public void setRepaidamount(String repaidamount) {
        this.repaidamount = repaidamount;
    }

    public String getStatuscopy() {
        return statuscopy;
    }

    public void setStatuscopy(String statuscopy) {
        this.statuscopy = statuscopy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCodestatus() {
        return codestatus;
    }

    public void setCodestatus(int codestatus) {
        this.codestatus = codestatus;
    }
}

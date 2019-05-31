package com.zhiyu.wallet.bean;

import com.zhiyu.wallet.util.Println;

/**
 * Created by Administrator on 2019/2/15.
 */

public class YDResult {

    private String ret_msg;

    private String ret_code;

    private String result_auth;

    private String result_status;

    private String classify;

    private String id_name;

    private String id_no;

    private String be_idcard;

    private String date_birthday;

    private String addr_card;

    private String start_card;

    private String branch_issued;

    private String flag_sex;

    private String state_id;

    private String url_photoget;

    private String url_frontcard;

    private String url_backcard;

    private String url_photoliving;

    private Risk_tag risk_tag;

    public class Risk_tag {

        public String living_attack;

    }

    public String getRet_msg() {
        return ret_msg;
    }

    public String getRet_code() {
        return ret_code;
    }

    public String getResult_auth() {
        return result_auth;
    }

    public String getResult_status() {
        return result_status;
    }

    public String getClassify() {
        return classify;
    }

    public String getId_name() {
        return id_name;
    }

    public String getId_no() {
        return id_no;
    }

    public String getBe_idcard() {
        return be_idcard;
    }

    public String getDate_birthday() {
        return date_birthday;
    }

    public String getAddr_card() {
        return addr_card;
    }

    public String getStart_card() {
        return start_card;
    }

    public String getBranch_issued() {
        return branch_issued;
    }

    public String getFlag_sex() {
        return flag_sex;
    }

    public String getState_id() {
        return state_id;
    }

    public String getUrl_photoget() {
        return url_photoget;
    }

    public String getUrl_frontcard() {
        return url_frontcard;
    }

    public String getUrl_backcard() {
        return url_backcard;
    }

    public String getUrl_photoliving() {
        return url_photoliving;
    }

    public Risk_tag getRisk_tag() {
        return risk_tag;
    }
}

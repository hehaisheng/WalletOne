package com.zhiyu.wallet.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @
 * Created by Administrator on 2018/12/28.
 */

public class AuthenInformation {

    private String operatorauth;

    private String bankcardauth;

    private String jingdongauth;

    private String taobaoauth;

    private String alipyauth;

    private String socialauth;

    private String accunulationauth;

    private String riceauth;

    private String loanauth;

    private String todayauth;

    public void setAutheninfo(List<Credit> creditList){
        if(0 ==  creditList.size()){
            throw new IllegalArgumentException();
        }

        for(int i =0; i<creditList.size();i++){

            switch (creditList.get(i).getCredit_name()){

                case "运营商认证" : creditList.get(i).setIsauthen(operatorauth);break;

                case "银行卡认证" : creditList.get(i).setIsauthen(bankcardauth);break;

                case "京东认证" : creditList.get(i).setIsauthen(jingdongauth);break;

                case "淘宝认证" : creditList.get(i).setIsauthen(taobaoauth);break;

                case "支付宝认证" :creditList.get(i).setIsauthen(alipyauth);break;

                case "社保认证" : creditList.get(i).setIsauthen(socialauth);break;

                case "公积金认证" :creditList.get(i).setIsauthen(accunulationauth);break;

                case "米房认证" : creditList.get(i).setIsauthen(riceauth);break;

                case "借贷宝认证" : creditList.get(i).setIsauthen(loanauth);break;

                case "今借到认证" : creditList.get(i).setIsauthen(todayauth);break;

            }

        }
    }

}

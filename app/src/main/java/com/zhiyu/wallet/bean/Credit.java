package com.zhiyu.wallet.bean;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.util.Println;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 2018/8/2
 *
 * @author zhiyu
 */
public class Credit {
    private String credit_name;

    private String credentials;

    private String icon;

    private String isauthen ;

    public static List<Credit> list;
    public static List<Credit> showlist = new ArrayList<Credit>();

    private boolean isVisiable = true;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCredit_name() {
        return credit_name;
    }

    public void setCredit_name(String credit_name) {
        this.credit_name = credit_name;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public boolean isVisiable() {
        return isVisiable;
    }

    public void setVisiable(boolean visiable) {
        isVisiable = visiable;
    }

    public String getIsauthen() {
        return isauthen;
    }

    public void setIsauthen(String isauthen) {
        this.isauthen = isauthen;
    }

    public static List<Credit> getInstance(JSONArray jsonarray) throws JSONException {
        JSONObject json = jsonarray.getJSONObject(0);
        Credit credit2 = new Credit();
        credit2.setCredit_name("运营商认证");
        credit2.setCredentials(json.getString("operatorcertification"));

        credit2.setIcon(String.valueOf(R.mipmap.credit_communications));
        credit2.setVisiable(true);

        Credit credit5 = new Credit();
        credit5.setCredit_name("淘宝认证");
        credit5.setCredentials(json.getString("taobaocertification"));
        credit5.setIcon(String.valueOf(R.mipmap.credit_tb));
        credit5.setVisiable(true);

        Credit credit7 = new Credit();
        credit7.setCredit_name("支付宝认证");
        credit7.setCredentials(json.getString("alipaycertification"));
        credit7.setIcon(String.valueOf(R.mipmap.credit_zfb));
        credit7.setVisiable(true);

        Credit credit6 = new Credit();
        credit6.setCredit_name("京东认证");
        credit6.setCredentials(json.getString("jingdongcertification"));
        credit6.setIcon(String.valueOf(R.mipmap.credit_jd));
//        credit6.setCredentials("已完成");
        credit6.setVisiable(true);

        Credit credit13 = new Credit();
        credit13.setCredit_name("社保认证");
        credit13.setCredentials(json.getString("socialsecuritycertification"));
        credit13.setIcon(String.valueOf(R.mipmap.credit_sb));
        credit13.setVisiable(true);

        Credit credit14 = new Credit();
        credit14.setCredit_name("公积金认证");
        credit14.setCredentials(json.getString("providentfundcertification"));
        credit14.setIcon(String.valueOf(R.mipmap.credit_gjj));
        credit14.setVisiable(true);

        Credit credit8 = new Credit();
        credit8.setCredit_name("银行卡认证");
        credit8.setCredentials(json.getString("bankcardauthentication"));
        credit8.setIcon(String.valueOf(R.mipmap.credit_card));
        credit8.setVisiable(true);

        if (list == null) {

            list = new ArrayList<>();
//            Credit credit = new Credit();
//            credit.setCredit_name("实名认证");
//            credit.setCredentials(json.getString("autonymstatus"));
//            credit.setIcon(String.valueOf(R.mipmap.credit_name));
//            if (json.getInt("autonyvisible") == 1) {
//                credit.setVisiable(true);
//            } else {
//                credit.setVisiable(false);
//            }
//            list.add(credit);
//
//            Credit credit1 = new Credit();
//            credit1.setCredit_name("身份证认证");
////            if(!TextUtils.isEmpty(User.getInstance().getIdcar())){
////                credit1.setCredentials("已完成");
////            }else {
////
////            }
//        credit1.setCredentials(json.getString("idcarstatus"));
//            credit1.setIcon(String.valueOf(R.mipmap.credit_idcard));
//            if (json.getInt("idcarvisible") == 1) {
//                credit1.setVisiable(true);
//            } else {
//                credit1.setVisiable(false);
//            }
//            list.add(credit1);
//
//            Credit credit2 = new Credit();
//            credit2.setCredit_name("运营商认证");
//            credit2.setCredentials(json.getString("operatorcertification"));
////        credit2.setCredentials("未完成");
//            credit2.setIcon(String.valueOf(R.mipmap.credit_communications));
////            if (json.getInt("operatorvisible") == 1) {
//            credit2.setVisiable(true);
//            } else {
//                credit2.setVisiable(false);
//            }

//                Credit credit3 = new Credit();
//                credit3.setCredit_name("芝麻分认证");
//                credit3.setCredentials(json.getString("zmstatus"));
//                credit3.setIcon(String.valueOf(R.mipmap.credit_sesame));
//                if(json.getInt("zmvisible")==1) {
//                credit3.setVisiable(true);
//                } else {
//                credit3.setVisiable(false);
//                   }
//                list.add(credit3);
//
//
//                Credit credit4 = new Credit();
//                credit4.setCredit_name("芝麻信用认证");
//                credit4.setCredentials(json.getString("zmcreditstatus"));
//                credit4.setIcon(String.valueOf(R.mipmap.credit_sesame));
//                 if(json.getInt("zmcreditvisible")==1) {
            //            credit4.setVisiable(true);
            //        }else {
            //            credit4.setVisiable(false);
            //        }
//                list.add(credit4);
//            Credit credit8 = new Credit();
//            credit8.setCredit_name("银行卡认证");
//            credit8.setCredentials(json.getString("bankcardauthentication"));
////        credit8.setCredentials("未完成");
//            credit8.setIcon(String.valueOf(R.mipmap.credit_card));
////      if(json.getInt("xinyongvisible")==1) {
//            credit8.setVisiable(true);
//            }else {
//                credit8.setVisiable(false);
//            }

//            Credit credit5 = new Credit();
//            credit5.setCredit_name("淘宝认证");
//            credit5.setCredentials(json.getString("taobaocertification"));
////        credit5.setCredentials("未完成");
//            credit5.setIcon(String.valueOf(R.mipmap.credit_tb));
////            if(json.getInt("taobaovisible")==1) {
//            credit5.setVisiable(true);
//            }else {
//                credit5.setVisiable(false);
//            }

//            Credit credit7 = new Credit();
//            credit7.setCredit_name("支付宝认证");
//            credit7.setCredentials(json.getString("alipaycertification"));
////        credit7.setCredentials("未完成");
//            credit7.setIcon(String.valueOf(R.mipmap.credit_zfb));
////            if(json.getInt("zfbvisible")==1) {
//            credit7.setVisiable(true);
//            }else {
//                credit7.setVisiable(false);
//            }


//            Credit credit6 = new Credit();
//            credit6.setCredit_name("京东认证");
//            credit6.setCredentials(json.getString("jingdongcertification"));
////        credit6.setCredentials("未完成");
//            credit6.setIcon(String.valueOf(R.mipmap.credit_jd));
////            if(json.getInt("jdvisible")==1) {
//            credit6.setVisiable(true);
//            }else {
//                credit6.setVisiable(false);
//            }

//            Credit credit9 = new Credit();
//            credit9.setCredit_name("今借到认证");
//            credit9.setCredentials(json.getString("jinjiestatus"));
//            credit9.setIcon(String.valueOf(R.mipmap.credit_jjd));
//            if(json.getInt("jinjievisible")==1) {
//                credit9.setVisiable(true);
//            }else {
//                credit9.setVisiable(false);
//            }
//            list.add(credit9);

//            Credit credit10 = new Credit();
//            credit10.setCredit_name("借贷宝认证");
//            credit10.setCredentials(json.getString("jiedaistatus"));
//            credit10.setIcon(String.valueOf(R.mipmap.credit_jdb));
//            if(json.getInt("jiedaivisible")==1) {
//                credit10.setVisiable(true);
//            }else {
//                credit10.setVisiable(false);
//            }
//            list.add(credit10);

//            Credit credit11 = new Credit();
//            credit11.setCredit_name("米房认证");
//            credit11.setCredentials(json.getString("mifangstatus"));
//            credit11.setIcon(String.valueOf(R.mipmap.credit_mf));
//            if(json.getInt("mifangvisible")==1) {
//                credit11.setVisiable(true);
//            }else {
//                credit11.setVisiable(false);
//            }
//            list.add(credit11);


//                Credit credit12 = new Credit();
//                credit12.setCredit_name("央行认证");
//                credit12.setCredentials(json.getString("centralbankstatus"));
//                credit12.setIcon(String.valueOf(R.mipmap.credit_yh));
//                 if(json.getInt("centralbankvisible")==1) {
//                    credit12.setVisiable(true);
//                    }else {
//                     credit12.setVisiable(false);
//                    }
//                list.add(credit12);
//
//            Credit credit13 = new Credit();
//            credit13.setCredit_name("社保认证");
//            credit13.setCredentials(json.getString("socialsecuritycertification"));
////        credit13.setCredentials("未完成");
//            credit13.setIcon(String.valueOf(R.mipmap.credit_sb));
////                 if(json.getInt("socialvisible")==1) {
//            credit13.setVisiable(true);
//                    }else {
//                     credit13.setVisiable(false);
//                    }

//            Credit credit14 = new Credit();
//            credit14.setCredit_name("公积金认证");
//            credit14.setCredentials(json.getString("providentfundcertification"));
////        credit14.setCredentials("未完成");
//            credit14.setIcon(String.valueOf(R.mipmap.credit_gjj));
////                 if(json.getInt("accumulationvisible")==1) {
//            credit14.setVisiable(true);
//                    }else {
//                     credit14.setVisiable(false);
//                    }
//
//                Credit credit15 = new Credit();
//                credit15.setCredit_name("学历认证");
//                credit15.setCredentials(json.getString("educationstatus"));
//                credit15.setIcon(String.valueOf(R.mipmap.credit_xl));
//                 if(json.getInt("educationvisible")==1) {
//                    credit15.setVisiable(true);
//                    }else {
//                     credit15.setVisiable(false);
//                    }
//                list.add(credit15);
            list.add(credit2);
            list.add(credit5);
            list.add(credit7);
            list.add(credit6);
            list.add(credit8);
            list.add(credit13);
            list.add(credit14);


            credit2=null;credit5=null;credit6=null;credit7=null;credit8=null;credit13=null;credit14=null;
        }else {
            list.set(0,credit2);
            list.set(1,credit5);
            list.set(2,credit7);
            list.set(3,credit6);
            list.set(4,credit8);
            list.set(5,credit14);
            list.set(6,credit13);
        }
        return list;


    }
}

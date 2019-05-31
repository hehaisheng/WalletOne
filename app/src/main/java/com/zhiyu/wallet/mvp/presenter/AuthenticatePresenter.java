package com.zhiyu.wallet.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zhiyu.wallet.bean.AuthenInformation;
import com.zhiyu.wallet.bean.Credit;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.common.Constant;
import com.zhiyu.wallet.http.AuthenticateService;
import com.zhiyu.wallet.http.BasehttpModel;
import com.zhiyu.wallet.mvp.contract.AuthenticateContract;
import com.zhiyu.wallet.mvp.contract.UserInfoContract;
import com.zhiyu.wallet.mvp.ui.activity.AuthenCenterActivity;
import com.zhiyu.wallet.util.AppUtil;
import com.zhiyu.wallet.util.Println;
import com.zhiyu.wallet.util.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @ Created by Administrator on 2018/11/9.
 */

public class AuthenticatePresenter extends BasePresenter<AuthenticateContract.AuthenticateIView> implements AuthenticateContract.AuthenticateIModel {

    private AuthenticateService authenticateService;
    private BasehttpModel basehttpModel;
    private UserInfoContract.UserInfoIView userInfoIView;

    public AuthenticatePresenter() {
        basehttpModel = BasehttpModel.getInstance(Constant.Local_ipAddress3);
        authenticateService = basehttpModel.create(AuthenticateService.class);
    }


    @Override
    public void requestAuthenItemdata() {
        JSONObject JSON = new JSONObject();
        try {
            JSON.put("phone", User.getInstance().getPhone());
            Println.out("号码",User.getInstance().getPhone());
            JSON.put("copy3","");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        basehttpModel.observa(authenticateService.authencenter(basehttpModel.getRequestBody(JSON.toString())), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.out("authen", json);
                    List<Credit> list;
                    List<Credit> showlist = new ArrayList<>();
                    if (!TextUtils.isEmpty(json)) {
                        try {
                            JSONArray jsonArray = new JSONArray(json);
                            list = Credit.getInstance(jsonArray);

                            showlist = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).isVisiable()) {
                                    showlist.add(list.get(i));

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    checkAuthenInfo(showlist,"itemdata");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                Iview.showdialog("连接失败，请检查网络连接");
                Println.err("authen", e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });


    }


    @Override
    public void requestAuthenCenterItem() {

        basehttpModel.observa(authenticateService.authencenter(new BasehttpModel.Builder()
                .addParam("phone", User.getInstance().getPhone()).addParam("copy3","").build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.out("authen", json);
                    List<Credit> list = new ArrayList<>();
                    if (!TextUtils.isEmpty(json)) {
                        try {
                            JSONArray jsonArray = new JSONArray(json);
                            list = Credit.getInstance(jsonArray);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    checkAuthenInfo(list,"centeritem");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                List<Credit> list = new ArrayList<>();
                checkAuthenInfo(list,"centeritem");
            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Override
    public void checkAuthenInfo(final List<Credit> credits, final String type) {

        basehttpModel.observa(authenticateService.authencenterinfomation(), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.out("check",json);
                    Gson gson = new Gson();
                    AuthenInformation authenInformation = gson.fromJson(json, AuthenInformation.class);
                    authenInformation.setAutheninfo(credits);

                    if(type.equals("itemdata")){
                        Iview.showAuthenItemlist(credits);

                    }else if(type.equals("centeritem")){
                        Bundle bundle = new Bundle();
                        for (int i = 0; i < credits.size(); i++) {
                            if (credits.get(i).getIsauthen().equals("1")) {
                                if (credits.get(i).getCredentials().equals("未完成")) {
                                    UIUtils.toast("请先完成" + credits.get(i).getCredit_name(), false);
                                    bundle.putString("authen_name", credits.get(i).getCredit_name());
                                    bundle.putString("applyloan", "applyloancheck");
                                    Iview.initBundle(bundle);
                                    Iview.showAuthenItemlist(credits);
                                    return;
                                }
                            }
                        }

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                userInfoIView.connectFailure("网络请求失败，请检查你的网络连接");
            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Override
    public void saveBankInformation(UserInfoContract.UserInfoIView userInfoIview, String zfbAccount, final List<Map<String, Object>> banklist, final int type) {

        this.userInfoIView = userInfoIview;
        basehttpModel.observa(authenticateService.savebankinfo(new BasehttpModel.Builder()
                .addParam("phone", User.getInstance().getPhone())
                .addParam("alipy","")
                .addParams("card", banklist).build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.out("savebank",json);
                    if (TextUtils.isEmpty(json)) {
                        userInfoIView.showFailure("保存失败");
                        return;
                    }

                    JSONObject jsonObject1 = new JSONObject(json);

                    if (jsonObject1.getInt("code")==200) {
                        if (type == 0) {
                            userInfoIView.showSuccess("认证成功");
                        } else if (type == 1) {
//                            saveBankInfoAddress(banklist.get(0).get("cardno").toString(),
//                                    banklist.get(0).get("province").toString(),
//                                    banklist.get(0).get("city").toString(),
//                                    banklist.get(0).get("subbranch").toString(),
//                                    banklist.get(0).get("cardname").toString());
                        }
                    } else {
                        userInfoIView.showFailure(jsonObject1.getString("msg"));
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    userInfoIView.showFailure("保存失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                userInfoIView.connectFailure("网络请求失败，请检查你的网络连接");
            }

            @Override
            public void onComplete() {

            }
        });

    }

    //no use
    @Override
    public void requestBankCardComfirm(String bankAccount) {


        basehttpModel.observa(authenticateService.bankcardBindSendSms(new BasehttpModel.Builder()
                .addParam("merchantno", Constant.merchantno)
                .addParam("identitytype", "PHONE")
                .addParam("cardno", bankAccount)
                .addParam("idcardno", User.getInstance().getIdcar())
                .addParam("callbackurl", Constant.bindcallBackUrl)
                .addParam("idcardtype", "ID")
                .addParam("username", User.getInstance().getUsername())
                .addParam("phone", User.getInstance().getPhone())
                .addParam("issms", true).build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                Map<String, String> map = new HashMap<>();
                try {
                    String json = responseBody.string();
                    if (TextUtils.isEmpty(json)) {
                        map.put("msg", "认证失败");
                        Iview.showAuthenResult(false, map);
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(json);
                    if(jsonObject.has("code")){
                        if("400".equals(jsonObject.getString("code"))){
                            map.put("msg", jsonObject.getString("msg"));
                            Iview.showAuthenResult(false, map);
                            return;
                        }
                    }

                    JSONArray jsonArray = jsonObject.getJSONArray("memberInfo");
                    String status = "";
                    String errorcode = "";

                    if (jsonArray.getJSONObject(0).has("status")) {
                        status = jsonArray.getJSONObject(0).getString("status");
                        if (status.equals("TO_VALIDATE")) {
                            //requestjudgeCradcode(jsonArray.getJSONObject(0).getString("bankcode"), jsonArray.getJSONObject(0).getString("requestno"));

                        } else {
                            errorcode = jsonArray.getJSONObject(0).getString("errorcode");
                            String msg = "";
                            switch (errorcode) {
                                case "DK0011111":
                                    msg = "身份证号码不符";
                                    map.put("shake", "true");
                                    break;
                                case "DK0400002":
                                    msg = "请输入正确的银行卡号";
                                    map.put("shake", "true");
                                    break;
                                case "DKAU00008":
                                    msg = "手机号不符";
                                    map.put("shake", "true");
                                    break;
                                case "DKAU00010":
                                    msg = "姓名不符";
                                    map.put("shake", "true");
                                    break;
                                default:
                                    msg = jsonArray.getJSONObject(0).getString("errormsg");
                                    map.put("shake", "false");
                                    break;
                            }
                            map.put("msg", msg);
                            Iview.showAuthenResult(false, map);
                        }

                    } else {
                        map.put("msg", "认证失败");
                        Iview.showAuthenResult(false, map);
                    }

                    Println.printJson("banksms", json, "");
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    map.put("msg", "认证失败");
                    Iview.showAuthenResult(false, map);

                }
            }

            @Override
            public void onError(Throwable e) {
                Println.err("banksms", e.getMessage());
                if (Iview != null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("msg", "连接失败，请检查网络连接");
                    Iview.showAuthenResult(false, map);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestBankCardComfirmfuyou(String bankAccount) {

        String bankAccount2 = bankAccount.replace(" ","");
        final String userid = UIUtils.getUUID32();
        basehttpModel.observa(authenticateService.bankcardBindSendSmsfuyou(new BasehttpModel.Builder()
                .addParam("mchntcd", Constant.merchantNo)
                .addParam("cardno", bankAccount2)
                .addParam("idcard", User.getInstance().getIdcar())
                .addParam("version","1.0")
                .addParam("account", User.getInstance().getUsername())
                .addParam("userid",userid)
                .addParam("appid","1")
                .addParam("mobileno", User.getInstance().getPhone()).build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                Map<String, String> map = new HashMap<>();
                try {
                    String json = responseBody.string();
                    Println.printJson("banksms", json, "");
                    if (TextUtils.isEmpty(json)) {
                        map.put("msg", "认证失败");
                        Iview.showAuthenResult(false, map);
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(json);
                    String msg = "";
                    String MCHNTSSN = "";

                    if (jsonObject.has("RESPONSECODE")) {
                            if(jsonObject.getString("RESPONSECODE").equals("0000")){
                                MCHNTSSN = jsonObject.getString("MCHNTSSN");
                                Iview.sendsmsSuccess(true,MCHNTSSN,userid);
                            }else {
                                msg = jsonObject.getString("RESPONSEMSG");
                                map.put("msg", msg);
                                Iview.showAuthenResult(false, map);
                            }

                    } else {
                        msg = jsonObject.getString("RESPONSEMSG");
                        map.put("msg", msg);
                        Iview.showAuthenResult(false, map);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    map.put("msg", "认证失败");
                    Iview.showAuthenResult(false, map);

                }
            }

            @Override
            public void onError(Throwable e) {
                Println.err("banksms", e.getMessage());
                if (Iview != null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("msg", "连接失败，请检查网络连接");
                    Iview.showAuthenResult(false, map);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestjudgeCradcode(String banktype, final String requestno) {

        basehttpModel.observa(authenticateService.judgebanktype(new BasehttpModel.Builder()
                .addParam("cardname", banktype).build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    JSONObject jsonObject = new JSONObject(json);

                    if (400 == jsonObject.getInt("code")) {
                        Iview.sendsmsSuccess(true, requestno,"");
                    } else {
                        Iview.sendsmsSuccess(false, requestno,"");
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Iview.sendsmsSuccess(false, requestno,"");
                }


            }

            @Override
            public void onError(Throwable e) {
                if (Iview != null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("msg", "连接失败，请检查网络连接");
                    Iview.showAuthenResult(false, map);
                }
            }

            @Override
            public void onComplete() {

            }
        });

    }

    //null
    @Override
    public void saveBankInfoAddress(String cradno, String provice, String city, String subbranch, String cardname) {

        User user = User.getInstance();
        basehttpModel.observa(authenticateService.savebankaddress(new BasehttpModel.Builder()
                .addParam("phone", user.getPhone())
                .addParam("username", user.getUsername())
                .addParam("pid", user.getIdcar())
                .addParam("cardno", cradno)
                .addParam("province", provice)
                .addParam("city", city)
                .addParam("subbranch", subbranch)
                .addParam("cardname", cardname).build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    if (TextUtils.isEmpty(json)) {
                        userInfoIView.showFailure("保存失败");
                        return;
                    }
                    Println.out("saveaddress", json);
                    JSONObject jsonObject = new JSONObject(json);
                    if (200 == jsonObject.getInt("code")) {
                        userInfoIView.showSuccess("认证成功");
                    } else {
                        userInfoIView.showFailure("认证失败");
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    userInfoIView.showFailure("认证失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                userInfoIView.connectFailure("网络请求失败，请检查你的网络连接");
            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void requestBankCardComfirmSMS(String requestno, String verifyCode) {


        basehttpModel.observa(authenticateService.bankcardBindRequest(new BasehttpModel.Builder()
                .addParam("phone", User.getInstance().getPhone())
                .addParam("merchantno", Constant.merchantno)
                .addParam("requestno", requestno)
                .addParam("validatecode", verifyCode).build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                Map<String, String> map = new HashMap<>();
                try {
                    String json = responseBody.string();
                    Println.printJson("bankcomfirm", json, "");

                    if (TextUtils.isEmpty(json)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray jsonArray = jsonObject.getJSONArray("memberInfo");
                    String status = "";
                    String errorcode = "";

                    if (jsonArray.getJSONObject(0).has("errorcode")) {

                        switch (errorcode) {
                            case "DK0000002":
                                map.put("msg", "该银行卡已绑定");
                                break;
                        }
                        Iview.showAuthenResult(false, map);
                        return;
                    }


                    if (jsonArray.getJSONObject(0).has("status")) {
                        status = jsonArray.getJSONObject(0).getString("status");
                        if (status.equals("BIND_SUCCESS")) {
                            map.put("data", jsonArray.getJSONObject(0).getString("bankcode"));
                            Iview.showAuthenResult(true, map);
                        } else {
                            map.put("msg", jsonArray.getJSONObject(0).getString("errormsg"));
                            Iview.showAuthenResult(false, map);
                        }

                    } else {
                        map.put("msg", "认证失败");
                        Iview.showAuthenResult(false, map);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    map.put("msg", "认证失败");
                    Iview.showAuthenResult(false, map);
                }
            }

            @Override
            public void onError(Throwable e) {
                Println.err("bankcomfirm", e.getMessage());
                if (Iview != null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("msg", "连接失败，请检查网络连接");
                    Iview.showAuthenResult(false, map);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestBankCardComfirmSMSfuyou(String mchntssn,String userid, String cardno,String verifyCode) {


        basehttpModel.observa(authenticateService.bankcardBindRequestfuyou(new BasehttpModel.Builder()
                .addParam("mobileno", User.getInstance().getPhone())
                .addParam("account",User.getInstance().getUsername())
                .addParam("idcard",User.getInstance().getIdcar())
                .addParam("cardno",cardno)
                .addParam("mchntssn",mchntssn)
                .addParam("userid",userid)
                .addParam("copy1", AppUtil.getIPAddress())
                .addParam("mchntcd", Constant.merchantNo)
                .addParam("version","1.0")
                .addParam("appid","1")
                .addParam("msgcode", verifyCode).build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                Map<String, String> map = new HashMap<>();
                try {
                    String json = responseBody.string();
                    Println.printJson("bankcomfirm", json, "");

                    if (TextUtils.isEmpty(json)) {
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(json);
                    if("0000" .equals(jsonObject.getString("RESPONSECODE")) ){
                        map.put("data","");
                        Iview.showAuthenResult(true, map);
                    }else {
                        map.put("msg", jsonObject.getString("RESPONSEMSG"));
                        Iview.showAuthenResult(false, map);
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    map.put("msg", "认证失败");
                    Iview.showAuthenResult(false, map);
                }
            }

            @Override
            public void onError(Throwable e) {
                Println.err("bankcomfirm", e.getMessage());
                if (Iview != null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("msg", "连接失败，请检查网络连接");
                    Iview.showAuthenResult(false, map);
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestAuthentication(String AuthenType, final String username, final String password) {

        User user = User.getInstance();

        if (AuthenType.equals("运营商认证")) {
//            basehttpModel.observa(authenticateService.carrierAuthenid(basehttpModel.getRequestBody(jsonObject.toString())), new DisposableObserver<ResponseBody>() {
            basehttpModel.observa(authenticateService.carrierAuthenid(new BasehttpModel.Builder()
                    .addParam("phone", user.getPhone())
                    .addParam("username", user.getUsername())
                    .addParam("idcar", user.getIdcar()).build()), new DisposableObserver<ResponseBody>() {
                @Override
                public void onNext(ResponseBody responseBody) {

                    try {
                        String json = responseBody.string();
                        try {
                            JSONObject jsonObject1 = new JSONObject(json);
                            int code = jsonObject1.getInt("code");
                            if (code == 0) {
                                String task_id = jsonObject1.getString("task_id");
                                requestCarrierAuthen(task_id, username, password);
                            } else {
                                Map<String, String> map = new HashMap<>();
                                map.put("message", jsonObject1.getString("message"));
                                map.put("code", code + "");
                                Iview.showAuthenResult(false, map);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Map<String, String> map = new HashMap<>();
                            map.put("message", "请稍后再尝试");
                            map.put("code", "-200");
                            Iview.showAuthenResult(false, map);
                        }
                        Println.suc("carrier", json);

                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (Iview == null) {
                        return;
                    }
                    Iview.showdialog("连接失败，请检查网络连接");
                    Println.err("err", e.getMessage());
                }

                @Override
                public void onComplete() {

                }
            });

        } else if (AuthenType.equals("京东认证")) {

//            basehttpModel.observa(authenticateService.jdauthenTaskcreate(basehttpModel.getRequestBody(jsonObject.toString())), new DisposableObserver<ResponseBody>() {
            basehttpModel.observa(authenticateService.jdauthenTaskcreate(new BasehttpModel.Builder()
                    .addParam("phone", user.getPhone())
                    .addParam("username", user.getUsername())
                    .addParam("idcar", user.getIdcar()).build()), new DisposableObserver<ResponseBody>() {
                @Override
                public void onNext(ResponseBody responseBody) {

                    try {
                        String json = responseBody.string();
                        try {
                            JSONObject jsonObject1 = new JSONObject(json);
                            int code = jsonObject1.getInt("code");
                            if (code == 0) {
                                String task_id = jsonObject1.getString("task_id");
                                requestJDAuthen(task_id, username, password);
                            } else {
                                Map<String, String> map = new HashMap<>();
                                map.put("message", jsonObject1.getString("message"));
                                map.put("code", code + "");
                                Iview.showAuthenResult(false, map);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Println.suc("jdauthen", json);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    if (Iview == null) {
                        return;
                    }
                    Iview.showdialog("连接失败，请检查网络连接");
                    Println.err("err", e.getMessage());
                }

                @Override
                public void onComplete() {

                }
            });
        }


    }

    @Override
    public void requestCarrierAuthen(String task_id, String username, String password) {
        //String md5pwd = MD5Utils.MD5(password);
        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("user_name","18028651268");
//            jsonObject.put("user_pass","880419");
            jsonObject.put("user_name", username);
            jsonObject.put("user_pass", password);
            jsonObject.put("task_id", task_id);
            jsonObject.put("sogo","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        basehttpModel.observa(authenticateService.carrierAuthenpwd(basehttpModel.getRequestBody(jsonObject.toString())), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {

                try {
                    String json = responseBody.string();
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    JSONObject jsonObject1 = new JSONObject(json);
                    if (137 == jsonObject1.getInt("code") || 2007 == jsonObject1.getInt("code")) {
                        Iview.showAuthenResult(true, null);
                    } else if (105 == jsonObject1.getInt("code")) {
                        Map<String, String> map = new HashMap<>();
                        map.put("message", jsonObject1.getString("message"));
                        map.put("code", jsonObject1.getInt("code") + "");
                        map.put("next_stage", jsonObject1.getJSONObject("data").getString("next_stage"));
                        map.put("task_id", jsonObject1.getString("task_id"));
                        map.put("type", "运营商认证2");
                        Iview.showAuthenResult(false, map);

                    } else {
                        Map<String, String> map = new HashMap<>();
                        map.put("message", jsonObject1.getString("message"));
                        map.put("code", jsonObject1.getInt("code") + "");
                        Iview.showAuthenResult(false, map);
                    }

                    Println.suc("carrierpwd", json);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                Iview.showdialog("连接失败，请检查网络连接");
                Println.err("err", e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestJDAuthen(String task_id, String username, String password) {

        //String md5pwd = MD5Utils.MD5(password);
        JSONObject jsonObject = new JSONObject();
        try {
//            jsonObject.put("user_name","18028651268");
//            jsonObject.put("user_pass","880419");
            jsonObject.put("phone", User.getInstance().getPhone());
            jsonObject.put("user_name", username);
            jsonObject.put("user_pass", password);
            jsonObject.put("task_id", task_id);
            jsonObject.put("sogo","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        basehttpModel.observa(authenticateService.jdauthen(basehttpModel.getRequestBody(jsonObject.toString())), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {

                try {
                    String json = responseBody.string();
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    JSONObject jsonObject1 = new JSONObject(json);
                    if (137 == jsonObject1.getInt("code") || 2007 == jsonObject1.getInt("code")) {
                        Iview.showAuthenResult(true, null);
                    } else if (105 == jsonObject1.getInt("code")) {
                        Map<String, String> map = new HashMap<>();
                        map.put("message", jsonObject1.getString("message"));
                        map.put("code", jsonObject1.getInt("code") + "");
                        map.put("next_stage", jsonObject1.getJSONObject("data").getString("next_stage"));
                        map.put("task_id", jsonObject1.getString("task_id"));
                        map.put("type", "京东认证2");
                        Iview.showAuthenResult(false, map);

                    } else {
                        Map<String, String> map = new HashMap<>();
                        map.put("message", jsonObject1.getString("message"));
                        map.put("code", jsonObject1.getInt("code") + "");
                        Iview.showAuthenResult(false, map);
                    }

                    Println.suc("jdauthen", json);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                Iview.showdialog("连接失败，请检查网络连接");
                Println.err("err", e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void requestCarrierAuthen2(String task_id, String next_stage, String username, String sms_code) {

        basehttpModel.observa(authenticateService.carrierAuthenverify(new BasehttpModel.Builder()
                .addParam("user_name", username)
                .addParam("task_id", task_id)
                .addParam("sms_code", sms_code)
                .addParam("next_stage", next_stage)
                .addParam("sogo","").build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {

                try {
                    String json = responseBody.string();
                    Println.suc("carrier2", json);
                    JSONObject jsonObject = new JSONObject(json);
                    if (137 == jsonObject.getInt("code") || 2007 == jsonObject.getInt("code")) {
                        Iview.showAuthenResult(true, null);
                    } else {
                        Map<String, String> map = new HashMap<>();
                        map.put("message", jsonObject.getString("message"));
                        map.put("code", jsonObject.getInt("code") + "");
                        Iview.showAuthenResult(false, map);
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                Iview.showdialog("连接失败，请检查网络连接");
                Println.err("err", e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestJDAuthen2(String task_id, String next_stage, String username, String sms_code) {

        basehttpModel.observa(authenticateService.jdauthenverify(new BasehttpModel.Builder()
                .addParam("phone", User.getInstance().getPhone())
                .addParam("task_id", task_id)
                .addParam("sms_code", sms_code)
                .addParam("next_stage", next_stage)
                .addParam("sogo","").build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {

                try {
                    String json = responseBody.string();
                    Println.suc("jdauthen2", json);

                    JSONObject jsonObject = new JSONObject(json);
                    if (137 == jsonObject.getInt("code") || 2007 == jsonObject.getInt("code")) {
                        Iview.showAuthenResult(true, null);
                    } else {
                        Map<String, String> map = new HashMap<>();
                        map.put("message", jsonObject.getString("message"));
                        map.put("code", jsonObject.getInt("code") + "");
                        Iview.showAuthenResult(false, map);
                    }


                    Iview.showdialog("");
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                Iview.showdialog("连接失败，请检查网络连接");
                Println.err("err", e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestTaobaoAuthencomfirm() {
        basehttpModel.observa(authenticateService.updateAuthenstatus(new BasehttpModel.Builder()
                .addParam("phone", User.getInstance().getPhone())
                .addParam("copy3","")
                .addParam("taobaocertificationstatus", 1)
                .build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                Map<String, String> map = new HashMap<>();
                String json = null;
                try {
                    json = responseBody.string();
                    Println.out("taobao ", json);
                    if (TextUtils.isEmpty(json)) {
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt("code") == 200) {
                        map.put("data", "淘宝认证成功");
                        Iview.showAuthenResult(true, map);

                    } else {
                        map.put("data", "认证失败");
                        Iview.showAuthenResult(false, map);
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    map.put("data", "认证失败");
                    Iview.showAuthenResult(false, map);
                }


            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                Iview.showdialog("连接失败，请检查网络连接");
                Println.err("err", e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestZfbAuthencomfirm() {

        basehttpModel.observa(authenticateService.updateAuthenstatus(new BasehttpModel.Builder()
                .addParam("phone", User.getInstance().getPhone())
                .addParam("copy3","")
                .addParam("alipaycertificationstatus", 1).build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {

                Map<String, String> map = new HashMap<>();
                String json = null;
                try {
                    json = responseBody.string();
                    Println.out("zfb ", json);
                    if (TextUtils.isEmpty(json)) {

                        return;
                    }
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getInt("code") == 200) {
                        map.put("data", "支付宝认证成功");
                        Iview.showAuthenResult(true, map);

                    } else {
                        map.put("data", "认证失败");
                        Iview.showAuthenResult(false, map);
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    map.put("data", "认证失败");
                    Iview.showAuthenResult(false, map);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                Iview.showdialog("连接失败，请检查网络连接");
                Println.err("err", e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestsavetaskid(final String tbtaskid, final String zfbtaskid) {


        basehttpModel.observa(authenticateService.savetaskid(new BasehttpModel.Builder()
                .addParam("phone", User.getInstance().getPhone())
                .addParam("sogo", "")
                .addParam("tbtaskid", tbtaskid)
                .addParam("zfbtaskid", zfbtaskid)
                .build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                Map<String, String> map = new HashMap<>();
                try {
                    String json = responseBody.string() ;
                    Println.out("savetaskid",json);
                    JSONObject jsonObject = new JSONObject(json);
                    if(jsonObject.getInt("status") == 200){
                        if(!TextUtils.isEmpty(tbtaskid)){
                            requestTaobaoAuthencomfirm();
                        }else if(!TextUtils.isEmpty(zfbtaskid)){
                            requestZfbAuthencomfirm();
                        }
                    }else {
                        map.put("data", "认证失败");
                        Iview.showAuthenResult(false, map);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    map.put("data", "认证失败");
                    Iview.showAuthenResult(false, map);
                }
            }

            @Override
            public void onError(Throwable e) {
                Map<String, String> map = new HashMap<>();
                map.put("data", "认证失败"+e.getMessage());
                Iview.showAuthenResult(false, map);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}

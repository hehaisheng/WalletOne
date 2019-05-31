package com.zhiyu.wallet.mvp.presenter;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.zhiyu.wallet.adapter.RecordAdapter;
import com.zhiyu.wallet.bean.AuthenInformation;
import com.zhiyu.wallet.bean.Credit;
import com.zhiyu.wallet.bean.LoanLimit;
import com.zhiyu.wallet.bean.Record;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.common.Constant;
import com.zhiyu.wallet.http.BaseCommonModel;
import com.zhiyu.wallet.http.BasehttpModel;
import com.zhiyu.wallet.http.HomeService;
import com.zhiyu.wallet.mvp.contract.HomeContract;
import com.zhiyu.wallet.mvp.ui.activity.AuthenCenterActivity;
import com.zhiyu.wallet.util.AppUtil;
import com.zhiyu.wallet.util.Println;
import com.zhiyu.wallet.util.UIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by Administrator on 2018/11/5.
 */

public class HomePresenter extends BasePresenter<HomeContract.HomeIView> implements HomeContract.HomeModel {

    private HomeService homeService;
    private BasehttpModel basehttpModel;
    private List<Record> recordList = new ArrayList<>();


    public HomePresenter() {
        basehttpModel = BasehttpModel.getInstance(Constant.Local_ipAddress3);
        homeService = basehttpModel.create(HomeService.class);

    }


    @Override
    public void queryRecord() {

        basehttpModel.observa(homeService.record(new BasehttpModel.Builder()
                .addParam("phone", User.getInstance().getPhone())
//                .addParam("sogo","")
                .build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String responbody = responseBody.string();
                    Println.out("repay",responbody);
                    if(TextUtils.isEmpty(responbody)){
                        Iview.requestfailure("查询记录失败，请稍后再试");
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responbody);
                    JSONArray jsonArray = jsonObject.getJSONArray("historys");
                    for(int i= 0;i<jsonArray.length();i++){
                        Gson gson = new Gson();
                        Record record = gson.fromJson(jsonArray.getJSONObject(i).toString(),Record.class);
                        switch (record.getStatus()){
                            case "0": record.setStatuscopy("已还");break;
                            case "1": record.setStatuscopy("审核中");break;
                            case "2": record.setStatuscopy("待放款");break;
                            case "3": record.setStatuscopy("未还");break;
                            case "4": record.setStatuscopy("支付处理中");break;
                            case "5": record.setStatuscopy("审核失败");break;
                            case "7": record.setStatuscopy("支付失败");break;
                            case "8": record.setStatuscopy("拒绝申请");break;
                        }
                        recordList.clear();
                        recordList.add(record);
                    }
                    Iview.repayRecordListCallback(recordList);


                } catch (IOException |JSONException e) {
                    e.printStackTrace();
                    Iview.requestfailure("查询记录失败，请稍后再试");
                }
            }

            @Override
            public void onError(Throwable e) {
                if(Iview==null){
                    return;
                }
                Iview.requestfailure("网络连接失败，请检查网络连接");
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void homeBannerPictureRequest() {
        basehttpModel.observa(homeService.bannerpic(), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    jsontoString(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            @Override
            public void onError(Throwable e) {
                System.out.println("banner " + e);
                requestFailure();

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void jsontoString(String json) {
        List<String> images = new ArrayList<>();
           Println.printJson("images",json,"");
        if (TextUtils.isEmpty(json) || "null".equals(json)) {
            homeBannerPictureRequestCallBack(images);
            return;
        }

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                images.add(jsonArray.getJSONObject(i).getString("image"));

            }
            homeBannerPictureRequestCallBack(images);
        } catch (JSONException e) {
            e.printStackTrace();
            homeBannerPictureRequestCallBack(new ArrayList<String>());
        }
    }

    @Override
    public void homeBannerPictureRequestCallBack(List<String> images) {
        Iview.initbanner(images);
    }

    @Override
    public void homeNoticeRequest() {

        basehttpModel.observa(homeService.notice(), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    getGenerateTips(responseBody.string());
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Println.err("notice", e.getMessage());
                requestFailure();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void getGenerateTips(String json) throws JSONException {
        List<String> tips = new ArrayList<>();
        if (TextUtils.isEmpty(json) || "null".equals(json)) {
            homeNoticeRequestCallBack(tips);
            return;
        }
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            tips.add(jsonObject.getString("succtime") + " " + jsonObject.getString("phone")+ jsonObject.getString("businessname") + " 成功贷款" + jsonObject.getString("money") + "元");
          // System.out.println(tips.get(i));
        }
        homeNoticeRequestCallBack(tips);
    }

    @Override
    public void homeNoticeRequestCallBack(List<String> looptexts) {
        Iview.inittextloopview(looptexts);
    }

    @Override
    public void requestLoanLimit() {
        //BasehttpModel basehttpModel1 = new BasehttpModel("1");
        basehttpModel.observa(homeService.loanlimit(), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    Gson gson = new Gson();
                    String json = responseBody.string();
                    LoanLimit limit = new LoanLimit();
                    if (TextUtils.isEmpty(json) || "null".equals(json)) {
                        requestLoanLimitCallBack(limit);
                        return;
                    }

                    JSONArray jsonArray = new JSONArray(json);
                    //Println.out("jsonarray",jsonArray.toString());
                    Println.printJson("jsonarray", jsonArray.toString(), "");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        LoanLimit.LoanLimitItem loanLimitItem = gson.fromJson(jsonArray.getJSONObject(i).toString(), LoanLimit.LoanLimitItem.class);
                        limit.getList().add(loanLimitItem);
                    }
                    requestLoanLimitCallBack(limit);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println(loanLimit.getList().size() );
            }

            @Override
            public void onError(Throwable e) {
                requestFailure();
                System.out.println(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestLoanLimitCallBack(LoanLimit loanLimit) {
        Iview.initloanMoney(loanLimit);
    }

    @Override
    public void saveAppinfo(String imei, String channelID, String gpsAddress, String simphone,String phoneType) {

        basehttpModel.observa(homeService.saveAppinfo(new BasehttpModel.Builder()
                .addParam("channel", channelID)
                .addParam("address", gpsAddress)
                .addParam("imei", imei)
                .addParam("sim", simphone)
                .addParam("copy2",phoneType)
                .build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    Println.out("saveinfo", responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Println.err("saveinfo", e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestNoticeDialogContent() {

        basehttpModel.observa(homeService.getnotice(), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String content = "";
                try {
                    String json = responseBody.string();
                    if (TextUtils.isEmpty(json)) {
                        content = "";
                        Iview.showNoticeDialog(content);
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(json);
                    if (200 == jsonObject.getInt("status")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (i == jsonArray.length() - 1) {
                                content = content + jsonArray.getJSONObject(i).getString("content");
                            } else {

                                content = content + jsonArray.getJSONObject(i).getString("content") + "\n";
                            }
                        }
                    }
                    Iview.showNoticeDialog(content);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Iview.showNoticeDialog(content);
                }
            }

            @Override
            public void onError(Throwable e) {
                Println.err("getnotice", e.getMessage());
                if (Iview == null) {
                    return;
                }
                Iview.showNoticeDialog("");
            }

            @Override
            public void onComplete() {

            }
        });

    }



    @Override
    public void requestAuthenCenterItem(final Activity activity) {

        basehttpModel.observa(homeService.authencenter(new BasehttpModel.Builder().addParam("phone", User.getInstance().getPhone()).addParam("copy3","").build()), new DisposableObserver<ResponseBody>() {
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
                    checkAuthenInfo(activity, list);

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
                checkAuthenInfo(activity, list);
            }

            @Override
            public void onComplete() {

            }
        });
    }


    @Override
    public void checkAuthenInfo(final Activity activity, final List<Credit> credits) {


        basehttpModel.observa(homeService.authencenterinfomation(), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json  =responseBody.string();
                    Println.out("check",json);
                    Gson gson = new Gson();
                    AuthenInformation authenInformation = gson.fromJson(json,AuthenInformation.class);
                    authenInformation.setAutheninfo(credits);

                    if (credits.size() != 0) {
                        Intent intent;
                        Bundle bundle = new Bundle();
                        for (int i = 0; i < credits.size(); i++) {
                            if(credits.get(i).getIsauthen().equals("1")) {
                                if (credits.get(i).getCredentials().equals("未完成")) {
                                    UIUtils.toast("请先完成" + credits.get(i).getCredit_name(), true);
                                    intent = new Intent(activity, AuthenCenterActivity.class);
                                    bundle.putString("authen_name", credits.get(i).getCredit_name());
                                    bundle.putString("applyloan", "applyloan");
                                    intent.putExtra("data", bundle);
                                    activity.startActivity(intent);
                                    Iview.checkAuthenInfo(false);
                                    return;
                                }
                            }
                        }
                        Iview.checkAuthenInfo(true);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(Throwable e) {
                requestFailure();
            }

            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    public void requestFailure() {
        String msg = "网络请求失败，请检查你的网络连接";
        if (Iview == null) {
            return;
        }
        Iview.showToast(msg);
    }



    @Override
    public void requestTaobaoAuthencomfirm() {
        basehttpModel.observa(homeService.updateAuthenstatus(new BasehttpModel.Builder()
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
//                        Iview.showAuthenResult(true, map);

                    } else {
                        map.put("data", "认证失败");
//                        Iview.showAuthenResult(false, map);
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    map.put("data", "认证失败");
//                    Iview.showAuthenResult(false, map);
                }


            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
//                Iview.showdialog("连接失败，请检查网络连接");
                Println.err("err", e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

}

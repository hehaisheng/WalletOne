package com.zhiyu.wallet.mvp.presenter;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zhiyu.wallet.bean.Record;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.common.Constant;
import com.zhiyu.wallet.http.BasehttpModel;
import com.zhiyu.wallet.http.RepayService;
import com.zhiyu.wallet.mvp.contract.OrderContract;
import com.zhiyu.wallet.mvp.contract.UserInfoContract;
import com.zhiyu.wallet.util.Println;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;

/**
 * Created by Administrator on 2018/11/29.
 */

public class OrderPresenter extends BasePresenter<OrderContract.OrderIView> implements OrderContract.OrderIModel {
    private BasehttpModel basehttpModel;
    private RepayService repayService;


    public OrderPresenter() {
        basehttpModel = new BasehttpModel(Constant.Local_ipAddress3);
        repayService = basehttpModel.create(RepayService.class);
    }




    @Override
    public void queryBankinfor() {

        basehttpModel.observa(repayService.querybankinfo(new BasehttpModel.Builder().addParam("phone", User.getInstance().getPhone())
                            .addParam("alipy","").build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody response) {
                List<Map<String, String>> list = new ArrayList<>();
                try {
                    String json = response.string();
                    Println.out("querybank", json);
                    if (TextUtils.isEmpty(json)) {
                        Iview.requestBankinfoCallBack(list);
                        return;
                    }
                    JSONArray jsonArray = new JSONArray(json);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Map<String, String> map = new HashMap<>();
                        map.put("bankcardname", jsonArray.getJSONObject(i).getString("bankcardname"));
                        map.put("bankcardaccount", jsonArray.getJSONObject(i).getString("bankcardaccount"));
                        list.add(map);
                    }
                    Iview.requestBankinfoCallBack(list);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Iview.requestBankinfoCallBack(list);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (Iview == null) {
                    return;
                }
                List<Map<String, String>> list = new ArrayList<>();
                Iview.requestBankinfoCallBack(list);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestapplyOrder(String borrowemoney, String comprehensivemoney, String arrivalmoney, String lifeofloan, String borroweofdate, String repaymentdate, String purpose, String creditcard,String channelId,String gps) {
        if (TextUtils.isEmpty(borrowemoney) || TextUtils.isEmpty(comprehensivemoney) || TextUtils.isEmpty(arrivalmoney) || TextUtils.isEmpty(lifeofloan) || TextUtils.isEmpty(borroweofdate) ||
                TextUtils.isEmpty(repaymentdate) || TextUtils.isEmpty(purpose) || TextUtils.isEmpty(creditcard)) {
            Iview.applyOrderfailure();
            return;
        }
        User user = User.getInstance();
        String loan_type = "1";
        switch (purpose){
            case "消费":loan_type = "2";break;
            case "旅游":loan_type = "15";break;
            case "教育":loan_type = "9" ;break;
            case "装修":loan_type = "14"; break;
        }

        basehttpModel.observa(repayService.applyorder(new BasehttpModel.Builder()
//                .addParam("phone", user.getPhone())
                .addParam("name",user.getUsername())
                .addParam("pid",user.getIdcar())
                .addParam("mobile",user.getPhone())
                .addParam("loanType",loan_type)
//                .addParam("borrower", user.getUsername())
//                .addParam("borrowemoney", borrowemoney)
//                .addParam("purpose", purpose)
                .addParam("loanAmount",borrowemoney)
                .addParam("comprehensivemoney", comprehensivemoney)
                .addParam("arrivalmoney", arrivalmoney)
                .addParam("lifeofloan", lifeofloan)
                .addParam("borroweofdate", borroweofdate)
                .addParam("repaymentdate", repaymentdate)
                .addParam("creditcard", creditcard)
                .addParam("idfv","")
                .addParam("hd_serial_number",channelId)
                .addParam("serial_number",gps)
                .build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.out("applyorder",json);

                    JSONObject jsonObject = new JSONObject(json);
                    if(200 == jsonObject.getInt("status")){
                        Iview.applyOrderSuccess();
                    }else {
                        Iview.applyOrderfailure();
                    }

                } catch (IOException |JSONException e) {
                    e.printStackTrace();
                    Iview.applyOrderfailure();
                }
            }

            @Override
            public void onError(Throwable e) {
                if(Iview==null){
                    return;
                }
                Iview.applyOrderfailure();
                Println.out("applyorder",e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void saveOrderBitmap(String orderbitmappath) {
        File file = new File(orderbitmappath);

        BasehttpModel basehttpModelform = new BasehttpModel(Constant.Local_ipAddress3,1);
        RepayService repayService = basehttpModelform.create(RepayService.class);

        MultipartBody.Part filepart = MultipartBody.Part.createFormData("file",file.getName(), RequestBody.create(MediaType.parse("image/*"),file));

    }
}

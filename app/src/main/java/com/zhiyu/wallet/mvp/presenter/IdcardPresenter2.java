package com.zhiyu.wallet.mvp.presenter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.zhiyu.wallet.bean.ImageViewID;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.common.Constant;
import com.zhiyu.wallet.http.BasehttpModel;
import com.zhiyu.wallet.http.IdcardService;
import com.zhiyu.wallet.mvp.contract.IdcardContract;
import com.zhiyu.wallet.mvp.contract.IdcardContract2;
import com.zhiyu.wallet.util.BitmapUtils;
import com.zhiyu.wallet.util.Println;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import cn.xsshome.taip.ocr.TAipOcr;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/11/16.
 */

public class IdcardPresenter2 extends BasePresenter<IdcardContract2.IdcardIView2> implements IdcardContract2.IdcardIModel2 {

    private IdcardService idcardService;
    private BasehttpModel basehttpModel;
    private Handler handler;

    public IdcardPresenter2() {
        basehttpModel = new BasehttpModel(Constant.Service_ipAddress);
        idcardService = basehttpModel.create(IdcardService.class);

    }


    @SuppressLint("HandlerLeak")
    @Override
    public void requestIdCardAIORC(String pathResult,final int card_type) {
        if (TextUtils.isEmpty(pathResult)) {
            requestIdCardAIORCCallBack(false, "上传失败", "", "", "", card_type);
            return;
        }
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = msg.obj.toString();
                try {
                    ocrRejson(result, msg.arg1);
                } catch (JSONException e) {
                    e.printStackTrace();
                    requestIdCardAIORCCallBack(false, "", "", "", "", card_type);
                }
            }
        };
        Println.out("orcstart","");
        MyTaipOcr myTaipOcr = new MyTaipOcr(pathResult, card_type);
        myTaipOcr.start();

    }

    @Override
    public void requestIdCardAIORCCallBack(boolean success, String name, String id, String authority, String valid_date, int cartype) {
        if (success) {
            if (cartype == 0) {
                if (id.length() == 18) {
                    Iview.idTaiOCRsuccess(name, id);
                } else {
                    Iview.idTaiOCRFailure("正面照片认证失败,请重新拍摄照片");
                }
            } else if (cartype == 1) {
                if(TextUtils.isEmpty(authority) || TextUtils.isEmpty(valid_date)){
                    Iview.idTaiOCRBackFailure("反面照片认证失败，请重新拍摄照片");
                    return;
                }
                Iview.idTaiOCRBacksuccess();
            }

        } else {
            if (cartype == 0) {
                Iview.idTaiOCRFailure("正面照片认证失败,请重新选择照片");
            } else if (cartype == 1) {

                Iview.idTaiOCRFailure("反面照片认证失败,请重新选择照片");
            }
            // idCardView.idTaiORCFailure(name);
        }
    }

    @Override
    public void requestZMCerification(String name, String idNumber) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", name);
            jsonObject.put("idcar", idNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        basehttpModel.observa(idcardService.zminit(basehttpModel.getRequestBody(jsonObject.toString())), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String json = null;
                try {
                    json = responseBody.string();
                    Println.out("zminit",json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject1 = new JSONObject(json);
                    JSONObject initialize_response = jsonObject1.getJSONObject("zhima_customer_certification_initialize_response");
                    String sub_msg = initialize_response.getString("sub_msg");
                    if (!TextUtils.isEmpty(sub_msg)) {
                        Iview.zMatteInitFailure("芝麻初始化失败:" + sub_msg);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    String bizNO = json.replace("\"", "");
                    Iview.zMatteInitsuccess("芝麻初始化成功", bizNO);
                }

            }

            @Override
            public void onError(Throwable e) {
                if(Iview==null){
                    return;
                }
                Iview.zMatteInitFailure("芝麻初始化失败");
            }

            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    public void saveIdcardpicture(String idfacePath, String idbackPath, String idcardPath) {
        List<File> fileList =new ArrayList<>();
        File filefornt = new File(idfacePath);
        File fileback = new File(idbackPath);
        File fileliveness = new File(idcardPath);
        fileList.add(filefornt);
        fileList.add(fileback);
        fileList.add(fileliveness);

        BasehttpModel basehttpModelform = new BasehttpModel(Constant.Local_ipAddress3,1);
        IdcardService idcardService1 = basehttpModelform.create(IdcardService.class);
        MultipartBody.Builder fileBody = new MultipartBody.Builder();

        for(File file : fileList){
            fileBody.addFormDataPart("file",file.getName(),RequestBody.create(MediaType.parse("image/*"),file));

        }
        fileBody.addFormDataPart("phone", User.getInstance().getPhone());
        fileBody.addFormDataPart("sogo", "");

        basehttpModel.observa(idcardService1.saveimg(fileBody.build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.out("saveimg",json);
                    if(TextUtils.isEmpty(json)){
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(json);
                    if(200 == jsonObject.getInt("status")){
                        Iview.saveIdBitmapsuccess("认证成功");
                    }else {
                        Iview.saveIdBitmapFailure("认证失败");
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Iview.saveIdBitmapFailure("认证失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                if(Iview==null){
                    return;
                }
                Iview.saveIdBitmapFailure("认证失败");
                Println.err("saveimg",e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

    }


    @Override
    public void requestIdCardDetectmultiface(String pathResult) {
        File file = new File(pathResult);

        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part bodyfile = MultipartBody.Part.createFormData("","2018-12-03.jpg",requestFile);


        final RequestBody requestFile1 =
                RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part bodyfile1 = MultipartBody.Part.createFormData("","2018-12-03.jpg",requestFile);


        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        multipartBody.addFormDataPart("file", file.getName(),RequestBody.create(MediaType.parse("image/*"),file));
        RequestBody requestBody = multipartBody.build();

        //basehttpModel.observa(idcardService.saveimg());

    }

    @Override
    public void requestIdCardDetectmultifaceCallBack(boolean success, String msg) {

    }


    public void ocrRejson(String json, int card_type) throws JSONException {
        Println.out("idcard",json);
        JSONObject jsonObject = new JSONObject(json);
        String result = jsonObject.getString("msg");
        if (result.equals("ok")) {
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            String name = jsonObject1.getString("name");
            String id = jsonObject1.getString("id");
            String authority = jsonObject1.getString("authority");
            String valid_date = jsonObject1.getString("valid_date");
            requestIdCardAIORCCallBack(true, name, id, authority, valid_date, card_type);
        } else if (result.equals("system busy, please try again later")) {
            requestIdCardAIORCCallBack(false, "", "", "", "", card_type);
        } else {
            requestIdCardAIORCCallBack(false, "", "", "", "", card_type);
        }

    }

    public class MyTaipOcr extends Thread {
        private String pathResult;
        private int card_type = 0;

        public MyTaipOcr(String pathResult, int card_type) {
            this.card_type = card_type;
            this.pathResult = pathResult;
        }

        @Override
        public void run() {
            super.run();

            // 初始化一个TAipOcr
//            TAipOcr client = new TAipOcr(Constant.AppID, Constant.AppKey);
//            client.setConnectionTimeoutInMillis(5000);
//            client.setSocketTimeoutInMillis(10000);
            try {
//                String result = client.idcardOcr(pathResult, card_type);
                Message msg = handler.obtainMessage();
//                msg.obj = result;
                msg.arg1 = card_type;
                handler.sendMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

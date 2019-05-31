package com.zhiyu.wallet.mvp.presenter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.zhiyu.wallet.bean.IDFrontInfo;
import com.zhiyu.wallet.bean.LivenessResult;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.common.Constant;
import com.zhiyu.wallet.http.BasehttpModel;
import com.zhiyu.wallet.http.IdcardService;
import com.zhiyu.wallet.mvp.contract.IdcardContract;
import com.zhiyu.wallet.util.Println;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.observers.DisposableObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/11/16.
 */

public class IdcardPresenter extends BasePresenter<IdcardContract.IdcardIView> implements IdcardContract.IdcardIModel {

    private IdcardService idcardService;
    private BasehttpModel basehttpModel;
    private Handler handler;
    private String app_id = "ebf834c58a9d4fc1a5e8a4af24758e6f";


    public IdcardPresenter() {
        basehttpModel = new BasehttpModel(Constant.Local_ipAddress3);
        idcardService = basehttpModel.create(IdcardService.class);

    }


    @Override
    public void requestSaveLiveness(byte[] images) {
        if (images==null && images.length>0){

            return;
        }
        String imageBase64 = Base64.encodeToString(images,Base64.NO_WRAP);
        BasehttpModel basehttpModelform = new BasehttpModel("https://res.51datakey.com/resource/api/v1/",1);
        IdcardService idcardService1 = basehttpModelform.create(IdcardService.class);

        Map<String,Object>map = new HashMap<>();
        map.put("app_id",app_id);
        map.put("file",imageBase64);
        map.put("type",3);
        basehttpModelform.observa(idcardService1.idcardocrform(map), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.out("saveliveness",json);

                    Gson gson = new Gson();
                    IDFrontInfo idFrontInfo =  gson.fromJson(json,IDFrontInfo.class);

                    if (idFrontInfo.getData() != null&&idFrontInfo.isSuccess()){

                        requestLivenessAndOcrCompare(idFrontInfo.getData().getFid());
                    }else {
                       Iview.livenessFailure("认证失败");
                    }
                } catch (IOException  e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                if(Iview==null){
                    return;
                }
                Println.err("idfront",e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
//        LivenessResult livenessResult= JSON.parseObject(result,LivenessResult.class);
//        if (livenessResult.getData()!=null && livenessResult.getData().getStatus().equals("1")){
//            Iview.livenessSuccess();
//        }else {
//            Iview.showToast("认证比对失败:"+livenessResult.getData().getReason());
//        }
    }

    @Override
    public void requestGetIdOcrFrontInfo(byte[] idcardFrontImageByte) {
        BasehttpModel basehttpModelform = new BasehttpModel("https://res.51datakey.com/resource/api/v1/",1);
        IdcardService idcardService1 = basehttpModelform.create(IdcardService.class);

        String imageBase64 = Base64.encodeToString(idcardFrontImageByte,Base64.NO_WRAP);

//        basehttpModel.observa(idcardService.idcardocr(new BasehttpModel.Builder().addParam("phone", User.getInstance().getPhone())
//                .addParam("file",imageBase64)
//                .addParam("type",1).build()), new DisposableObserver<ResponseBody>() {
//            @Override
//            public void onNext(ResponseBody responseBody) {
//                try {
//                    String json = responseBody.string();
//                    Gson gson = new Gson();
//                    IDFrontInfo idFrontInfo =  gson.fromJson(json,IDFrontInfo.class);
//
//                    if (idFrontInfo.getData() != null&& idFrontInfo.getData().getStatus().equals("1")){
//                         Iview.idcardFrontOcrResult(true);
//                    }else {
//                         Iview.idcardFrontOcrResult(false);
//                    }
//                Println.out("front",json);
//                } catch (IOException  e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                if(Iview==null){
//                    return;
//                }
//                Iview.idcardFrontOcrResult(false);
//                Println.err("idfront",e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });

        Map<String,Object>map = new HashMap<>();
        map.put("app_id",app_id);
        map.put("file",imageBase64);
        map.put("type",1);
        basehttpModelform.observa(idcardService1.idcardocrform(map), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.out("front",json);

                    Gson gson = new Gson();
                    IDFrontInfo idFrontInfo =  gson.fromJson(json,IDFrontInfo.class);

                    if (idFrontInfo.getData() != null&&idFrontInfo.isSuccess()){
                        requestidForntOCR(idFrontInfo.getData().getFid());
                       // Iview.idcardFrontOcrResult(true);
                    }else {
                        Iview.idcardFrontOcrResult(false);
                    }
                } catch (IOException  e) {
                    e.printStackTrace();
                    Iview.idcardFrontOcrResult(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                if(Iview==null){
                    return;
                }
                Iview.idcardFrontOcrResult(false);
                Println.err("idfront",e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    public void requestGetIdOcrBackInfo(byte[] idcardBackImageByte) {

        String imageBase64 = Base64.encodeToString(idcardBackImageByte,Base64.NO_WRAP);
//        basehttpModel.observa(idcardService.idcardocr(new BasehttpModel.Builder().addParam("phone", User.getInstance().getPhone())
//                .addParam("file",imageBase64)
//                .addParam("type",2).build()), new DisposableObserver<ResponseBody>() {
//            @Override
//            public void onNext(ResponseBody responseBody) {
//                try {
//                    String json = responseBody.string();
//                    Gson gson = new Gson();
//                    IDFrontInfo idFrontInfo =  gson.fromJson(json,IDFrontInfo.class);
//
//                    if (idFrontInfo.getData() != null&& idFrontInfo.getData().getStatus().equals("1")){
//                        Iview.idcardBackOcrResult(true);
//                    }else {
//                        Iview.idcardBackOcrResult(false);
//                    }
//                    Println.out("back",json);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                if(Iview==null){
//                    return;
//                }
//                Iview.idcardBackOcrResult(false);
//                Println.err("idback",e.getMessage());
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//        });
        BasehttpModel basehttpModelform = new BasehttpModel("https://res.51datakey.com/resource/api/v1/",1);
        IdcardService idcardService1 = basehttpModelform.create(IdcardService.class);

        Map<String,Object>map = new HashMap<>();
        map.put("app_id",app_id);
        map.put("file",imageBase64);
        map.put("type",2);
        basehttpModelform.observa(idcardService1.idcardocrform(map), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.out("front",json);

                    Gson gson = new Gson();
                    IDFrontInfo idFrontInfo =  gson.fromJson(json,IDFrontInfo.class);

                    if (idFrontInfo.getData() != null&& idFrontInfo.isSuccess()){
                        requestidbackOCR(idFrontInfo.getData().getFid());
                        //Iview.idcardFrontOcrResult(true);
                    }else {
                        Iview.idcardBackOcrResult(false);
                    }
                } catch (IOException  e) {
                    e.printStackTrace();
                    Iview.idcardBackOcrResult(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                if(Iview==null){
                    return;
                }
                Iview.idcardBackOcrResult(false);
                Println.err("idfront",e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestidForntOCR(String fid) {

        basehttpModel.observa(idcardService.idcardocr(new BasehttpModel.Builder()
                .addParam("phone", User.getInstance().getPhone())
                .addParam("fid", fid).build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.out("idFornt",json);

                    if(TextUtils.isEmpty(json)){
                        Iview.idcardFrontOcrResult(false);
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(json);
                    int status = jsonObject.getInt("status");
                    if( 200==status ){

                        User.getInstance().setIdcarbackup(jsonObject.getString("data"));
                        Iview.idcardFrontOcrResult(true);
                    }else {

                        Iview.idcardFrontOcrResult(false);
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Iview.idcardFrontOcrResult(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                if(Iview==null){
                    return;
                }
                Iview.idcardFrontOcrResult(false);
                Println.err("idFront",e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void requestidbackOCR(String fid) {
        basehttpModel.observa(idcardService.idcardocr(new BasehttpModel.Builder().addParam("phone", User.getInstance().getPhone())
                .addParam("fidre", fid).build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.out("idBack",json);

                    if(TextUtils.isEmpty(json)){
                        Iview.idcardBackOcrResult(false);
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(json);
                    int status = jsonObject.getInt("status");
                    if(300 == status ){

                        Iview.idcardBackOcrResult(true);
                    }else {

                        Iview.idcardBackOcrResult(false);
                    }


                } catch (IOException |JSONException e) {
                    e.printStackTrace();
                    Iview.idcardBackOcrResult(false);
                }
            }

            @Override
            public void onError(Throwable e) {
                if(Iview==null){
                    return;
                }
                Iview.idcardBackOcrResult(false);
                Println.err("idBack",e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestLivenessAndOcrCompare(String fid) {

        basehttpModel.observa(idcardService.idauthen(new BasehttpModel.Builder()
                .addParam("phone", User.getInstance().getPhone())
                .addParam("fidhuoti", fid).build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.out("compare",json);

                    Gson gson = new Gson();
                    IDFrontInfo idFrontInfo =  gson.fromJson(json,IDFrontInfo.class);

                    if (idFrontInfo.getData() != null&& idFrontInfo.isSuccess()){

                        if(idFrontInfo.getData().getStatus().equals("1")){
                            Iview.livenessSuccess();

                        }else {
                            Iview.livenessFailure(idFrontInfo.getData().getReason());
                        }
                    }else {
                        Iview.livenessFailure("认证失败");
                    }
                } catch (IOException  e) {
                    e.printStackTrace();
                    Iview.livenessFailure("认证失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                if(Iview==null){
                    return;
                }
                Iview.showToast("认证失败");
                Println.err("compare",e.getMessage());

            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void requestSaveImgae(List<File> fileList) {
        // 创建 RequestBody，用于封装构建RequestBody
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part bodyfile = MultipartBody.Part.createFormData("","2018-12-03.jpg",requestFile);
//
//
//        final RequestBody requestFile1 =
//                RequestBody.create(MediaType.parse("image/jpg"), file);
//        MultipartBody.Part bodyfile1 = MultipartBody.Part.createFormData("","2018-12-03.jpg",requestFile);
        BasehttpModel basehttpModelform = new BasehttpModel(Constant.Local_ipAddress3,1);
        IdcardService idcardService1 = basehttpModelform.create(IdcardService.class);

        MultipartBody.Builder multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for(File file :fileList){

            multipartBody.addFormDataPart("file", file.getName(),RequestBody.create(MediaType.parse("image/*"),file));
        }
        multipartBody.addFormDataPart("phone",User.getInstance().getPhone());

//        Map<String ,RequestBody> requestBodyMap=new HashMap<>();
//        requestBodyMap.put("")

        basehttpModelform.observa(idcardService1.saveimg(multipartBody.build()), new DisposableObserver<ResponseBody>() {
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
                        Iview.saveImageSuccess();
                    }else {
                        Iview.showToast("认证失败");
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Iview.showToast("认证失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                if(Iview==null){
                    return;
                }
                Iview.showToast("认证失败");
                Println.err("saveimg",e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }


}

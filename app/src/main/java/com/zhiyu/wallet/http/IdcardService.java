package com.zhiyu.wallet.http;


import com.zhiyu.wallet.common.Constant;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

/**
 * Created by Administrator on 2018/11/16.
 */

public interface IdcardService {

    @POST(Constant.Service_ipAddress+"userinfo/ZMatte.do")
    Observable<ResponseBody> zminit(@Body RequestBody requestBody);

//    @Multipart
//    @POST("savaIdPic/save")
//    Observable<ResponseBody> saveimg(@Part("description") RequestBody description,@Part MultipartBody.Part file);

//    @Multipart
//    @POST("savaIdPic/save")
//    Observable<ResponseBody> saveimg(@PartMap Map<String,RequestBody> file);

    @POST(Constant.Service_ipAddress+"savaIdPic/save")
    Observable<ResponseBody> saveimg(@Body MultipartBody file);

    @POST(Constant.Service_ipAddress+"ocr/senIdPic_forntOrReverse.do")
    Observable<ResponseBody> idcardocr(@Body RequestBody requestBody);

    @FormUrlEncoded
    @POST(Constant.Service_ipAddress+"upload/file/encode")
    Observable<ResponseBody> idcardocrform(@FieldMap Map<String ,Object >map);

    @POST(Constant.Service_ipAddress+"ocr/renzhen.do")
    Observable<ResponseBody> idauthen(@Body RequestBody requestBody);

}

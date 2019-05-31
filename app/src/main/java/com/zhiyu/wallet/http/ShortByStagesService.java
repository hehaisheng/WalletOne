package com.zhiyu.wallet.http;

import com.zhiyu.wallet.common.Constant;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2019/5/20.
 */

public interface ShortByStagesService {
    //正式的ip域名
    //@POST(Constant.Service_ipAddress+"order/apply_4day")
    //测试
    @POST("http://192.168.1.102:8080/HMwallet-web-ME/order/apply_4day")
    Observable<ResponseBody> apply_4day(@Body RequestBody requestBody);

}

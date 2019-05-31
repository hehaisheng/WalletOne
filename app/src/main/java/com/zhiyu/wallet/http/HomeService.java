package com.zhiyu.wallet.http;

import com.zhiyu.wallet.bean.LoanLimit;
import com.zhiyu.wallet.common.Constant;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/11/5.
 */

public interface HomeService {

    @POST(Constant.Service_ipAddress+"newapp/jugde")
    Observable<ResponseBody> update(@Body RequestBody requestBody);  //更新

    @POST(Constant.Service_ipAddress+"advchart/getall")
    Observable<ResponseBody> bannerpic();

    @POST(Constant.Service_ipAddress+"notice/allnotice")
    Observable<ResponseBody> notice();

    @POST(Constant.Service_ipAddress+"businessAmount/moneyselect")
    Observable<ResponseBody> loanlimit();

    @POST(Constant.Service_ipAddress+"channel/saveChannel")
    Observable<ResponseBody> saveAppinfo(@Body RequestBody requestBody);

    @POST(Constant.Service_ipAddress+"bulletin/get")
    Observable<ResponseBody> getnotice();

    @POST(Constant.Service_ipAddress+"authenticationCenter/getallAuthenticationCenter")
    Observable<ResponseBody> authencenter(@Body RequestBody requestBody);

    @POST(Constant.Service_ipAddress+"information/findInformation")
    Observable<ResponseBody> authencenterinfomation();    // 认证信息

    @POST(Constant.Service_ipAddress+"repayment/repaymentlist")
    Observable<ResponseBody> record(@Body RequestBody requestBody);   // 历史记录

    //淘宝认证
    @POST(Constant.Service_ipAddress+"userinfo/updateStatus")
    Observable<ResponseBody> updateAuthenstatus(@Body RequestBody requestBody);  //更新认证状态
}

package com.zhiyu.wallet.http;

import android.provider.ContactsContract;

import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.common.Constant;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/11/7.
 */

public interface UserInfoService {

    @POST(Constant.Service_ipAddress+"login")
    Observable<ResponseBody> login(@Body RequestBody requestBody);  //登录

    @POST(Constant.Service_ipAddress+"register")
    Observable<ResponseBody> register(@Body RequestBody requestBody);   //注册

    @POST(Constant.Service_ipAddress+"login/sendSms")
    Observable<ResponseBody> sendSMSgetcode(@Body RequestBody requestBody);  //发送短信

    @POST(Constant.Service_ipAddress+"login/forgetPassword")
    Observable<ResponseBody> forgetpwd(@Body RequestBody requestBody);//忘记密码

    @POST(Constant.Service_ipAddress+"personalData/PersonalData")
    Observable<ResponseBody> userinfo(@Body RequestBody requestBody);//保存个人资料

    @POST(Constant.Service_ipAddress+"maillist/addmaillist")
    Observable<ResponseBody> allContact(@Body RequestBody requestBody);//保存全部联系人

    @POST(Constant.Service_ipAddress+"msg/savemsg")
    Observable<ResponseBody> saveMesg(@Body RequestBody requestBody);  //保存短信

    @POST(Constant.Service_ipAddress+"outlogin")
    Observable<ResponseBody> logout();  //退出

    @POST(Constant.Service_ipAddress+"bankInformation/selectBankCard")
    Observable<ResponseBody> querybankinfo(@Body RequestBody requestBody); //查询银行信息

    @POST(Constant.Service_ipAddress+"message/unreadMessage")
    Observable<ResponseBody> querynews(@Body RequestBody requestBody); //查询消息  json phone

    @POST(Constant.Service_ipAddress+"message/updateStatus")
    Observable<ResponseBody> updatenewsstatus(@Body RequestBody requestBody); //修改消息状态

    @POST(Constant.Service_ipAddress+"message/notMessage")
    Observable<ResponseBody> judgenewsstatus(@Body RequestBody requestBody); //判断是否有消息

    @POST(Constant.Service_ipAddress+"channel/saveChannel")
    Observable<ResponseBody> saveAppinfo(@Body RequestBody requestBody);   //保存APP信息

    @POST(Constant.Service_ipAddress+"authenticationCenter/getallAuthenticationCenter")
    Observable<ResponseBody> authencenter(@Body RequestBody requestBody);  //认证信息


}

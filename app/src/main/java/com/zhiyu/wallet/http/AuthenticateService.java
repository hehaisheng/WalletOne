package com.zhiyu.wallet.http;



import com.zhiyu.wallet.common.Constant;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @
 * Created by Administrator on 2018/11/9.
 */

public interface AuthenticateService {

    @POST(Constant.Service_ipAddress+"userinfo/getTask_id.do")
    Observable<ResponseBody> carrierAuthenid(@Body RequestBody requestBody);   //运营商认证1

    @POST(Constant.Service_ipAddress+"userinfo/beginMofangCaiJi.do")
    Observable<ResponseBody> carrierAuthenpwd(@Body RequestBody requestBody);  //运营商认证2

    @POST(Constant.Service_ipAddress+"authenticationCenter/getallAuthenticationCenter")
    Observable<ResponseBody> authencenter(@Body RequestBody requestBody);    // 认证中心

    @POST(Constant.Service_ipAddress+"information/findInformation")
    Observable<ResponseBody> authencenterinfomation();    // 认证信息

    @POST(Constant.Service_ipAddress+"JD/getTask_id.do")
    Observable<ResponseBody> jdauthenTaskcreate(@Body RequestBody requestBody);  //京东认证1

    @POST(Constant.Service_ipAddress+"userinfo/JDCaiJi.do")
    Observable<ResponseBody> jdauthen(@Body RequestBody requestBody);  //京东认证2

    @POST(Constant.Service_ipAddress+"userinfo/messageAuth.do")
    Observable<ResponseBody> carrierAuthenverify(@Body RequestBody requestBody); //运营商认证2.1

    @POST(Constant.Service_ipAddress+"userinfo/JDCaiJiDuanXin.do")
    Observable<ResponseBody> jdauthenverify(@Body RequestBody requestBody);   //京东认证2.1

    @POST(Constant.Service_ipAddress+"authbindcardrequest/sendAuthbindcardrequest")
    Observable<ResponseBody> bankcardBindSendSms(@Body RequestBody requestBody); // 发送绑卡短信  //no use

    @POST(Constant.Service_ipAddress+"fuyou/msgauth")
    Observable<ResponseBody> bankcardBindSendSmsfuyou(@Body RequestBody requestBody); // 发送绑卡短信  fuyou

    @POST(Constant.Service_ipAddress+"authbindcardrequest/Authbindcardsmsconfirm")
    Observable<ResponseBody> bankcardBindRequest(@Body RequestBody requestBody); //绑定银行卡请求

    @POST(Constant.Service_ipAddress+"fuyou/fuyouauthbind")
    Observable<ResponseBody> bankcardBindRequestfuyou(@Body RequestBody requestBody); //绑定银行卡请求 fuyou

    @POST(Constant.Service_ipAddress+"bankInformation/addBankInformation")
    Observable<ResponseBody> savebankinfo(@Body RequestBody requestBody); //保存银行信息

    @POST(Constant.Service_ipAddress+"bankcard/findbankcard")
    Observable<ResponseBody> judgebanktype(@Body RequestBody requestBody); //判断卡号  //null

    @POST(Constant.Service_ipAddress+"bankcard/savebankcard")
    Observable<ResponseBody> savebankaddress(@Body RequestBody requestBody); //保存银行信息   //null

    @POST(Constant.Service_ipAddress+"userinfo/updateStatus")
    Observable<ResponseBody> updateAuthenstatus(@Body RequestBody requestBody);  //更新认证状态

    @POST(Constant.Service_ipAddress+"userinfo/savataskid_tbAndzfb")
    Observable<ResponseBody> savetaskid(@Body RequestBody requestBody);   //保存taskid

}

package com.zhiyu.wallet.http;

import com.zhiyu.wallet.common.Constant;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2018/11/7.
 */

public interface RepayService {

    @POST(Constant.Service_ipAddress+"repayment/repaymentlist")
    Observable<ResponseBody> record(@Body RequestBody requestBody);   // 历史记录

    @POST(Constant.Service_ipAddress+"bankInformation/selectBankCard")
    Observable<ResponseBody> querybankinfo(@Body RequestBody requestBody); //查询银行信息

    @POST(Constant.Service_ipAddress+"order/apply")
    Observable<ResponseBody> applyorder(@Body RequestBody requestBody);  // 申请订单

    @POST(Constant.Service_ipAddress+"cardpayment/cardpaymentrequest")
    Observable<ResponseBody> repay(@Body RequestBody requestBody);    //还款  no use

    @POST(Constant.Service_ipAddress+"authbindcardrequest/selectrequestAndindentityid")
    Observable<ResponseBody> querybankcard(@Body RequestBody requestBody);  //查询银行卡信息  no use

    @POST(Constant.Service_ipAddress+"cardpayment/smspaymentconfirmation")
    Observable<ResponseBody> smspaymentconfirmation(@Body RequestBody requestBody);  //还款确认  no use

    @POST(Constant.Service_ipAddress+"fy/findphoneandcard")
    Observable<ResponseBody> queryrepaymentssn(@Body RequestBody requestBody); // 查找userid  fuyou

    @POST(Constant.Service_ipAddress+"fy/payfuyou")
    Observable<ResponseBody> requestrepay(@Body RequestBody requestBody); // 还款  fuyou

    @Multipart
    @POST()
    Observable<ResponseBody> saveimgae(@Part("phone") RequestBody requestBody,@Part MultipartBody.Part file);


}

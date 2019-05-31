package com.zhiyu.wallet.mvp.presenter;

import android.support.v4.app.NavUtils;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.zhiyu.wallet.adapter.RecordAdapter;
import com.zhiyu.wallet.bean.Record;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.common.Constant;
import com.zhiyu.wallet.http.BasehttpModel;
import com.zhiyu.wallet.http.RepayService;
import com.zhiyu.wallet.mvp.contract.RepayContract;
import com.zhiyu.wallet.util.AppUtil;
import com.zhiyu.wallet.util.Println;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * @
 * Created by Administrator on 2018/11/7.
 */

public class RepayPresenter extends BasePresenter<RepayContract.RepayIView> implements RepayContract.RepayIModel{

    private RepayService repayService;
    private BasehttpModel basehttpModel;
    private String repaidmount = "0.00";  //待还款金额
    private int repaidretio = 0;        //待还款总笔数
    private String latespaymenttime = "- -";  //最近还款日期
    private String latesbill = "0.00";  //最近账单
    private String latetime = "0"; //逾期天数


    public RepayPresenter(){
        basehttpModel=BasehttpModel.getInstance(Constant.Local_ipAddress3);
        repayService=basehttpModel.create(RepayService.class);

    }

    @Override
    public void recordSortwithType(int type, RecordAdapter recordAdapter, List<Record> recordList) {
        if(type==0){
            recordAdapter.lists = recordList;
            recordAdapter.notifyDataSetChanged();

        }else {
            List<Record> records = new ArrayList<>();
            records.addAll(recordList);

            for(int i=0;i< records.size();i++){
                switch (records.get(i).getStatuscopy()){
                    case "审核中": records.get(i).setCodestatus(1);break;
                    case "待放款" :records.get(i).setCodestatus(2);break;
                    case "未还":  records.get(i).setCodestatus(3);break;
                    case "支付处理中": records.get(i).setCodestatus(4);break;
                    case "支付失败":   records.get(i).setCodestatus(5);break;
                    case "审核失败":  records.get(i).setCodestatus(6);break;
                    case "已还": records.get(i).setCodestatus(8);break;
                    case "拒绝申请":records.get(i).setCodestatus(7);break;
                }
            }

            Collections.sort(records, new Comparator<Record>() {
                @Override
                public int compare(Record r1, Record r2) {
                    if(r1.getCodestatus()<r2.getCodestatus()){
                        return 1;
                    }
                    if(r1.getCodestatus() == r2.getCodestatus()){
                        return 0;
                    }
                    return -1;
                }
            });
            recordAdapter.lists = records;
            recordAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void requestRepayhistoryRecord() {
          repaidmount = "0";  //待还款金额
          repaidretio = 0;        //待还款总笔数
          latespaymenttime = "- -";  //最近还款日期
          latesbill = "0";  //最近账单
          latetime = "0"; //逾期天数

        basehttpModel.observa(repayService.record(new BasehttpModel.Builder()
                .addParam("phone", User.getInstance().getPhone())
//                .addParam("sogo","")
                .build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                 List<Record> recordList = new ArrayList<>();
                 List<Map<String ,Object>> mapList = new ArrayList<>();
                try {
                    String responbody = responseBody.string();
                    Println.out("repay",responbody);
                    Iview.connectResult(true,"");
                    if(TextUtils.isEmpty(responbody)){
                        Iview.repayRecordListCallback(recordList);
                        Iview.repayRecordCallback(repaidmount,repaidretio,latespaymenttime,latesbill,latetime);
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(responbody);
                    JSONArray jsonArray = new JSONArray();
                    if(jsonObject.has("repaidmount")){
                        repaidmount = jsonObject.getString("repaidmount");
                    }
                    if(jsonObject.has("repaidmount3")){
                        repaidmount = jsonObject.getString("repaidmount3");
                    }
                    if(jsonObject.has("repaidretio")){
                        repaidretio = jsonObject.getInt("repaidretio");
                    }
                    if(jsonObject.has("latestpaymenttime")){
                        latespaymenttime = jsonObject.getString("latestpaymenttime");
                    }
                    if(jsonObject.has("latesbill")){
                        latesbill = jsonObject.getString("latesbill");
                    }
                    if(jsonObject.has("content")){
                        String content = jsonObject.getString("content");
                        int fir = content.indexOf(":");
                        int last = content.indexOf("天");
                        int secondf = content.lastIndexOf(":");
                        int therer = content.indexOf("是");
                        latetime = content.substring(fir+1,last);
                        Map<String ,Object > map = new HashMap<>();
                        Map<String ,Object > map1 = new HashMap<>();
                        Map<String ,Object > map2 = new HashMap<>();

                        map.put("costname","利息规则");
                        map.put("costmoney",content.substring(therer+1,secondf));

                        map1.put("costname","逾期天数");
                        map1.put("costmoney",content.substring(fir+1,last+1));

                        map2.put("costname","总计");
                        map2.put("costmoney",content.substring(secondf+1,content.length()));
                        mapList.add(map);
                        mapList.add(map1);
                        mapList.add(map2);
                    }else {
                        latetime = "0";
                    }
//                    String content = "您已逾期:16天本金:10000还款金额是[利息是本金的5%*天数+还款金额]:180";
//                    int fir = content.indexOf(":");
//                    int last = content.indexOf("天");
//                    int secondf = content.lastIndexOf(":");
//                    int therer = content.indexOf("还");
//                    Println.out("content",fir+"   "+last +"   "+content.substring(fir,last));
//                    Println.out("content2",therer+"   "+secondf +"   "+content.substring(therer,secondf));
//                    Println.out("content2",content.substring(secondf,content.length()));
                    jsonArray=jsonObject.getJSONArray("historys");
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
                        recordList.add(record);
                    }
                    Iview.repayfinedetail(mapList);
                    Iview.repayRecordListCallback(recordList);
                    Iview.repayRecordCallback(repaidmount,repaidretio,latespaymenttime,latesbill,latetime);

                } catch (IOException |JSONException e) {
                    e.printStackTrace();
                    Iview.repayfinedetail(mapList);
                    Iview.repayRecordListCallback(recordList);
                    Iview.repayRecordCallback(repaidmount,repaidretio,latespaymenttime,latesbill,latetime);
                }
            }

            @Override
            public void onError(Throwable e) {
                if(Iview==null){
                    return;
                }
                Iview.connectResult(false,"网络连接失败"+e.getMessage());
                Println.out("repaye",e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void requestQueryBankinfo() {

        basehttpModel.observa(repayService.querybankinfo(new BasehttpModel.Builder().addParam("phone",User.getInstance().getPhone())
                                .addParam("alipy","").build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody response) {
                List<Map<String ,String>> list = new ArrayList<>();
                try {
                    String json = response.string();
                    Println.out("querybank", json);
                    if(TextUtils.isEmpty(json)){
                        Iview.requestBankinfoCallBack(list);
                        return;
                    }
                    JSONArray jsonArray=new JSONArray(json);
                    for(int i =0 ;i<jsonArray.length();i++){
                        Map<String,String> map = new HashMap<>();
                        map.put("bankcardname",jsonArray.getJSONObject(i).getString("bankcardname"));
                        map.put("bankcardaccount",jsonArray.getJSONObject(i).getString("bankcardaccount"));
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
                List<Map<String ,String>> list = new ArrayList<>();
                Iview.requestBankinfoCallBack(list);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 获取绑卡流水号
     * @param bankAccount 银行卡号
     */
    @Override
    public void requestQueryBankCardNO(String bankAccount) {
        String mbankAccount = bankAccount.replace(" ","");

        basehttpModel.observa(repayService.querybankcard(new BasehttpModel.Builder()
                .addParam("cardno",mbankAccount).build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.out("querycardno",json);
                    if(TextUtils.isEmpty(json)){
                        Iview.repayfailure("支付失败");
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(json);
                    if(jsonObject.has("code")){
                        if(jsonObject.getInt("code")==400){
                            Iview.repayfailure(jsonObject.getString("msg"));
                        }
                    }else {
                        JSONArray jsonArray = jsonObject.getJSONArray("memberInfo");
                        //Iview.repaysendSMS(jsonArray.getJSONObject(0).getString("identityid"));;
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Iview.repayfailure("支付失败");
                }
            }

            @Override
            public void onError(Throwable e) {
                Println.err("querycardno",e.getMessage());
                if(Iview!= null){
                    Iview.repayfailure("网络请求失败，请检查网络连接");
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 请求还款短信
     * @param bankAccount 银行卡号
     *  @param repayAmount 还款金额
     * @param bankType 银行类型
     */
    @Override
    public void requestrepay(String repayAmount, String bankAccount,String bankType,String identityid) {
        String mbankAccount = bankAccount.replace(" ","");
        String cardtop = mbankAccount.substring(0,6);
        String cardlast = mbankAccount.substring(mbankAccount.length()-4,mbankAccount.length());
        Double amount = Double.parseDouble(repayAmount);
        basehttpModel.observa(repayService.repay(new BasehttpModel.Builder()
                .addParam("phone",User.getInstance().getPhone())
                .addParam("merchantno",Constant.merchantno)
                .addParam("issms",true)
                .addParam("identitytype","PHONE")
                .addParam("cardtop",cardtop)
                .addParam("cardlast",cardlast)
                .addParam("productname",bankType)
                .addParam("identityid",identityid)
                .addParam("callbackurl",Constant.callBackUrl)
                .addParam("amount",String.format(Locale.CHINA,"%.2f",amount))
                .addParam("terminalno",Constant.QUICK_REPAY)
                .build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.printJson("repaysendSms",json,"");
                    if(TextUtils.isEmpty(json)){
                        Iview.repayfailure("支付失败");
                        return;
                    }
                    JSONObject jsonObject =new JSONObject(json);
                    JSONArray jsonArray  = jsonObject.getJSONArray("memberInfo");
                    String status = "";
                    String errorcode = "";

                    if(jsonArray.getJSONObject(0).has("status")){
                        status = jsonArray.getJSONObject(0).getString("status");
                        if(status.equals("TO_VALIDATE")){
                            Iview.repaySuccessSendSMS(jsonArray.getJSONObject(0).getString("requestno"));

                        }else {
                            errorcode = jsonArray.getJSONObject(0).getString("errorcode");
                            String msg = "";
                            switch (errorcode){
                               // case  "DK0200001": msg="支付中，请稍后再试";break;
                                default: msg = jsonArray.getJSONObject(0).getString("errormsg");break;
                            }
                            Iview.repayfailure(msg);
                        }

                    }else {

                        Iview.repayfailure("支付失败");
                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Iview.repayfailure("支付失败");
                }
            }

            @Override
            public void onError(Throwable e) {

                Println.err("repay",e.getMessage());
                if(Iview!=null){
                    Iview.repayfailure("支付失败");

                }

            }

            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    public void requestrepaySMS( String validatecode,String requestno) {

        basehttpModel.observa(repayService.smspaymentconfirmation(new BasehttpModel.Builder()
                .addParam("phone",User.getInstance().getPhone())
                .addParam("requestno",requestno)
                .addParam("merchantno",Constant.merchantno)
                .addParam("validatecode",validatecode)
                .build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.printJson("repaySMS",json,"");

                    if(TextUtils.isEmpty(json)){
                        Iview.repayfailure("");
                        return;
                    }

                    JSONObject jsonObject =new JSONObject(json);
                    JSONArray jsonArray  = jsonObject.getJSONArray("memberInfo");
                    String status = "";
                    String errorcode = "";

                    if(jsonArray.getJSONObject(0).has("status")){
                        status = jsonArray.getJSONObject(0).getString("status");
                        if(status.equals("PROCESSING")){
                            Iview.repaysuccess();

                        }else {
                            errorcode = jsonArray.getJSONObject(0).getString("errorcode");
                            String msg = "";
                            switch (errorcode){
                                default: msg = jsonArray.getJSONObject(0).getString("errormsg");break;
                            }
                            Iview.repayfailure(msg);
                        }

                    }else {

                        Iview.repayfailure("支付失败");
                    }



                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Iview.repayfailure("支付失败");
                }
            }

            @Override
            public void onError(Throwable e) {

                Println.err("repaySMS",e.getMessage());
                if(Iview!=null){
                    Iview.repayfailure("支付失败");

                }

            }

            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    public void requestqueryRepayssn(final String bankAccount, final String money) {
        basehttpModel.observa(repayService.queryrepaymentssn(new BasehttpModel.Builder()
                .addParam("phone",User.getInstance().getPhone())
                .addParam("cardno",bankAccount)
                .addParam("appid","1")
                .build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String json = responseBody.string();
                    Println.printJson("repayssn",json,"");

                    if(TextUtils.isEmpty(json)){
                        Iview.repayfailure("");
                        return;
                    }

                    JSONObject jsonObject =new JSONObject(json);
                    if(jsonObject.getString("responsecode").equals("0000")){

                        requestrepayfuyou(jsonObject.getString("protocolno"),jsonObject.getString("userid"),money,bankAccount);

                    }else {

                        Iview.repayfailure(jsonObject.getString("responsemsg"));
                    }



                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Iview.repayfailure("支付失败");
                }
            }

            @Override
            public void onError(Throwable e) {

                Println.err("repaySMS",e.getMessage());
                if(Iview!=null){
                    Iview.repayfailure("支付失败");

                }

            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void requestrepayfuyou(String protocolno,String userid,String money,String bankaccount) {

        basehttpModel.observa(repayService.requestrepay(new BasehttpModel.Builder()
                .addParam("version", "1.0")
                .addParam("userip", AppUtil.getIPAddress())
                .addParam("phone", User.getInstance().getPhone())
                .addParam("mchntcd", Constant.merchantNo)
                .addParam("protocolno", protocolno)
                .addParam("userid", userid)
                .addParam("bankurl","120.77.168.71/HMwallet-web-ZY/open")
                .addParam("needsendmsg","")
                .addParam("cardno",bankaccount)
                .addParam("appid","1")
                .addParam("amt",Integer.parseInt(money)*100).build()), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                String json = null;
                try {
                    json = responseBody.string();
                    Println.printJson("repay",json,"");

                    if(TextUtils.isEmpty(json)){
                        Iview.repayfailure("");
                        return;
                    }

                    JSONObject jsonObject = new JSONObject(json);

                    if(jsonObject.has("code")){
                        Iview.repayfailure(jsonObject.getString("msg"));
                    }else if(jsonObject.has("RESPONSECODE")){
                        if(jsonObject.getString("RESPONSECODE").equals("0000")){
                            Iview.repaysuccess();
                        }else {
                            Iview.repayfailure(jsonObject.getString("RESPONSEMSG"));
                        }
                    }else {
                        Iview.repayfailure(jsonObject.getString("RESPONSEMSG"));
                    }
//                    03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║ {
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "VERSION": "1.0",
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "RESPONSECODE": "0000",
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "RESPONSEMSG": "成功",
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "MCHNTORDERID": "1bb56e062fac4bb99e8ecb11c636aff0",
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "USERID": "6399f4c233594233a65fd635fb47fa92",
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "ORDERID": "000038230425",
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "AMT": "1",
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "BANKCARD": "6222023602092471143",
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "REM1": [],
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "REM2": [],
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "REM3": [],
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "SIGNTP": "MD5",
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "MCHNTCD": "0002900F0096235",
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "SIGN": "0bebd312d2b0edbec051fe9fc2130556",
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "TYPE": "03",
//                                03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║     "PROTOCOLNO": "PDO1A6100000042012GO7X"
//                        03-07 08:45:10.133 21577-21577/com.zhiyu.wallet D/repay: ║ }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Iview.repayfailure("还款失败，数据异常");

                }


            }

            @Override
            public void onError(Throwable e) {
                Iview.repayfailure("还款失败，请检查网络连接");
            }

            @Override
            public void onComplete() {

            }
        });
    }


}

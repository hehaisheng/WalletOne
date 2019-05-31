package com.zhiyu.wallet.mvp.contract;

import com.zhiyu.wallet.adapter.RecordAdapter;
import com.zhiyu.wallet.bean.Record;
import com.zhiyu.wallet.mvp.ui.BaseIView;

import java.util.List;
import java.util.Map;

/**
 * @
 * Created by Administrator on 2018/11/7.
 */

public interface RepayContract {

    interface RepayIView extends BaseIView{

        void repayRecordListCallback(List<Record> recordList);

        void repayRecordCallback(String repaidmount,int repaidretio,String latestpaymenttime,String latesbill,String latetime);

        void repayfinedetail(List<Map<String ,Object>> mapList);

        void connectResult(boolean success,String msg);

        void requestBankinfoCallBack(List<Map<String ,String>> banks);

        /**
         * 发送支付短信验证
         * @param identityid 绑卡流水号
         */
        void repaysendSMS(String identityid);


        /**
         * 发送支付短信成功
         */
        void repaySuccessSendSMS(String requestno);


        /**
         * 还款成功
         */
        void repaysuccess();

        void repayfailure(String msg);

        void repayOverdue(String latetime);

    }

    interface RepayIModel extends BaseIModel{

        void recordSortwithType(int type, RecordAdapter recordAdapter, List<Record> recordList);

        void requestRepayhistoryRecord();

        void requestQueryBankinfo();


        /**
         * 获取绑卡流水号
         * @param bankAccount 银行卡号
         */
        void requestQueryBankCardNO(String bankAccount);

        /**
         * 请求还款短信 no use
         * @param bankAccount 银行卡号
         *  @param repayAmount 还款金额
         */
        void requestrepay(String repayAmount,String bankAccount,String bankType,String identityid);

        void requestrepaySMS(String validatecode,String requestno);


        void requestqueryRepayssn(String bankAccount,String money);

        void requestrepayfuyou(String protocolno,String userid,String money,String bankAccount);

    }
}

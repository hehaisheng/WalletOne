package com.zhiyu.wallet.mvp.contract;

import android.app.Activity;
import android.os.Bundle;

import com.zhiyu.wallet.bean.Credit;
import com.zhiyu.wallet.mvp.ui.BaseIView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/9.
 */

public interface AuthenticateContract {

    interface AuthenticateIView extends BaseIView {

        void initBundle(Bundle bundle);

        void showAuthenItemlist(List<Credit> credits);

        void showAuthenResult(boolean isSuccess, Map<String,String> data);

        void sendsmsSuccess(boolean isinputAddress,String msg,String uuid);

        void showdialog(String msg);

        void closedialog();
    }

    interface AuthenticateIModel extends BaseIModel{


        void requestAuthenItemdata();

        /**
         * 获取认证信息
         */
        void requestAuthenCenterItem();

        /**
         * 检查认证信息
         * @param list
         */
        void checkAuthenInfo( List<Credit> list,String type);

        /**
         * 保存银行信息
         * @param zfbAccount zfb账号
         * @param banklist  银行卡list
         * @param type  ..
         */
        void saveBankInformation(UserInfoContract.UserInfoIView userInfoIView,String zfbAccount, List<Map<String,Object>> banklist, int type);

        /**
         * 发送绑卡短信
         * @param bankAccount 银行卡号
         */
        void requestBankCardComfirm(String bankAccount );

        /**
         * 发送绑卡短信
         * @param bankAccount 银行卡号  fuyou
         */
        void requestBankCardComfirmfuyou(String bankAccount );

        /**
         * 判断银行类型
         * @param bankcode
         * @param requestno
         */
        void requestjudgeCradcode(String bankcode,String requestno);

        /**
         * 保存银行卡地址信息
         * @param cradno 卡号
         * @param provice 地址省
         * @param city 地址市
         * @param subbranch 支行
         * @param cardname 卡名
         */
        void saveBankInfoAddress(String cradno,String provice,String city,String subbranch,String cardname);

        /**
         * 绑卡短信验证
         * @param requestno 绑卡请求流水号
         * @param verifyCode 短信验证码
         */
        void requestBankCardComfirmSMS(String requestno,String verifyCode );

        /**
         * 绑卡短信验证
         * @param mchntssn 绑卡请求流水号
         * @param verifyCode 短信验证码
         */
        void requestBankCardComfirmSMSfuyou(String mchntssn,String userid, String cardno,String verifyCode );

        /**
         * 请求认证
         * @param AuthenType 认证的类型
         * @param username 账号
         * @param password 密码
         */
        void requestAuthentication(String AuthenType ,String username,String password);

        /**
         * 运营商认证
         * @param task_id 流水号
         * @param username 账号
         * @param password 密码
         */
        void requestCarrierAuthen(String task_id ,String username,String password);

        /**
         * 京东认证
         * @param task_id 流水号
         * @param username 账号
         * @param password 密码
         */
        void requestJDAuthen(String task_id,String username,String password);

        /**
         * 运营商认证第二步
         * @param task_id 流水号
         * @param next_stage
         * @param username 账号
         * @param sms_code 验证码
         */
        void requestCarrierAuthen2(String task_id,String next_stage ,String username,String sms_code);

        /**
         * 京东认证第二步
         * @param task_id 流水号
         * @param next_stage
         * @param username 账号
         * @param sms_code 验证码
         */
        void requestJDAuthen2(String task_id,String next_stage,String username,String sms_code);

        /**
         * 淘宝认证第二步
         */
        void requestTaobaoAuthencomfirm();

        /**
         * 支付宝认证第二步
         */
        void requestZfbAuthencomfirm();

        void requestsavetaskid(String tbtaskid,String zfbtaskid);
    }
}

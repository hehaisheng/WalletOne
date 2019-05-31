package com.zhiyu.wallet.mvp.contract;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.zhiyu.wallet.bean.Credit;
import com.zhiyu.wallet.mvp.ui.BaseIView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhiyu
 * Created by Administrator on 2018/11/7.
 */

public interface UserInfoContract {

    interface UserInfoIView extends BaseIView{

        //打印成功信息
        void showSuccess(String msg);

        //打印失败信息
        void showFailure(String msg);

        //连接失败信息
        void connectFailure(String msg);

    }
    interface BankinfoIView extends BaseIView{

        /**
         * 查询银行信息回调
         * @param banks 银行卡信息
         */
        void requestBankinfoCallBack(List<Map<String ,String>> banks);
    }


    interface UserInfoIModel extends BaseIModel{

        /**
         * 请求发送验证码
         * @param telnum 手机号
         */
        void requestCheckCode(String telnum);

        /**
         * 登录请求
         * @param username 手机号
         * @param autocode 验证码
         * @param activity null
         */
        void requestLogin(String username, String autocode, Activity activity,String channelid);

        /**
         * 登录回调* @param msg* @param code
         */
        void returnLogin(String msg, int code,Context activity);

        /**
         * 注册请求
         * @param username 手机号
         * @param requestCode 验证码
         * @param password 密码
         * @param passwordagin 确认密码
         * @param channelId 渠道号
         */
        void requestRegister(String username,String requestCode,String password,String passwordagin,String channelId);

        /**
         * 找回密码请求
         * @param username 手机号
         * @param requestCode 验证码
         * @param password  密码
         * @param passwordagin 确认密码
         */
        void requestForgetpwd(String username,String requestCode,String password,String passwordagin);

        /**
         * 保存联系人信息
         * @param ftn 第一联系人名字
         * @param ftt ..手机
         * @param fts ..关系
         * @param urn 紧急联系人名字
         * @param urt ..手机
         * @param urs ..关系
         */
        void saveUserRelatives(String ftn,String ftt,String fts,String urn,String urt,String urs);

        /**
         * 保存全部联系人信息
         * @param result 全部联系人list
         */
        void saveUserAllContacts(List<HashMap<String ,Object>> result);

        /**
         * 获取联系人信息
         */
        HashMap<String ,Object> requestUesrRelatives();

        /**
         * 保存个人资料
         * @param nm 姓名
         * @param tel 手机号
         * @param id 身份证
         * @param wcid 微信号
         * @param reinf 联系人
         * @param addr 居住地址
         * @param occp 职业类型
         */
        void requestUerinfo(String nm,String tel,String id,String wcid,String reinf,String addr,String occp,Context context);

        void saveUserinfo(Context context);

        //退出登录请求
        void requestQuitUesr();

        /**
         * 银行信息查询
         * @param bankinfoIView view
         */
        void queryBankinfor(BankinfoIView bankinfoIView);

        void checkNewsCenter(ImageView imageView);

        void setReadAllNews();

        void saveAppinfo(String msg,String imei,String channelID,String gpsAddress,String simphone,String phoneType);



    }
}

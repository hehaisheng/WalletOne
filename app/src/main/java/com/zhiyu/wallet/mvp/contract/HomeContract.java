package com.zhiyu.wallet.mvp.contract;

import android.app.Activity;

import com.zhiyu.wallet.adapter.RecordAdapter;
import com.zhiyu.wallet.bean.Credit;
import com.zhiyu.wallet.bean.LoanLimit;
import com.zhiyu.wallet.bean.Record;
import com.zhiyu.wallet.mvp.ui.BaseIView;

import java.util.List;

/**
 * Created by Administrator on 2018/11/5.
 */

public interface HomeContract {

    interface HomeIView extends BaseIView{
        void initbanner(List<String> images);

        void inittextloopview(List<String> looptexts);

        void initloanMoney(LoanLimit loanLimit);

        void initDashboard( double []moneyText);

        void initaccrualcost(int position);

        void saveAppInfo();

        void showToast(String msg);

        void showNoticeDialog(String msg);

        void checkAuthenInfo(boolean complete);

        void repayRecordListCallback(List<Record> recordList);

        void requestfailure(String msg);

    }

    interface HomeModel extends BaseIModel{

        void queryRecord();
        /**
         * 广告图
         */
        void homeBannerPictureRequest();

        /**
         * 广告图返回
         * @param images
         */
        void homeBannerPictureRequestCallBack(List<String> images);

        /**
         * 轮播公告
         */
        void homeNoticeRequest();

        /**
         * 轮播公告返回
         * @param looptexts
         */
        void homeNoticeRequestCallBack(List<String> looptexts);

        /**
         * 贷款信息
         */
        void requestLoanLimit();

        /**
         * 贷款信息返回
         * @param loanLimit
         */
        void requestLoanLimitCallBack(LoanLimit loanLimit);

        /**
         * 保存APP信息
         * @param imei
         * @param channelID
         * @param gpsAddress
         * @param simphone
         */
        void saveAppinfo(String imei,String channelID,String gpsAddress,String simphone,String phoneType);

        /**
         * 弹出公告
         */
        void requestNoticeDialogContent();

        /**
         * 获取认证信息
         * @param activity
         */
        void requestAuthenCenterItem(Activity activity);

        /**
         * 检查认证信息
         * @param activity
         * @param list
         */
        void checkAuthenInfo(Activity activity, List<Credit> list);

        void requestFailure();

        void requestTaobaoAuthencomfirm();

    }
}

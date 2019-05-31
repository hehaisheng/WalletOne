package com.zhiyu.wallet.mvp.contract;

import android.content.Intent;

import com.zhiyu.wallet.mvp.ui.BaseIView;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2018/11/16.
 */

public interface IdcardContract2 {

    interface IdcardIView2 extends BaseIView {

        void idTaiOCRsuccess(String name, String id);

        void idTaiOCRBacksuccess();

        void idTaiOCRBackFailure(String msg);

        void idTaiOCRFailure(String msg);

        void zMatteInitsuccess(String msg,String bizNo);

        void zMatteInitFailure(String msg);

        void idCard_detectfaceSuccess();

        void idCard_detectfaceFailure(String msg);

        void saveIdBitmapsuccess(String msg);

        void saveIdBitmapFailure(String msg);

        void showdialog();

        void closedialog();


    }

    interface IdcardIModel2 extends  BaseIModel{

        /**
         * 身份证ORC认证
         * @param pathResult  图片路径
         * @param card_type  正反面
         */
        void requestIdCardAIORC(String pathResult,int card_type);

        /**
         * 身份证ORC认证结果返回
         * @param success 是否成功
         * @param name  名字
         * @param id  身份证号码
         * @param authority 签证机关
         * @param valid_date 日期
         * @param cartype 正反面
         */
        void requestIdCardAIORCCallBack(boolean success,String name,String id,String authority,String valid_date,int cartype);

        /**
         * 芝麻认证初始化
         * @param name 名字
         * @param idNumber 身份证号码
         */
        void requestZMCerification(String name,String idNumber);

        /**
         * 保存图片
         * @param idfacePath  正面图片路径
         * @param idbackPath  反面图片路径
         * @param idcardPath  手持图片路径
         */
        void saveIdcardpicture(String idfacePath,String idbackPath,String idcardPath);

        void requestIdCardDetectmultiface(String pathResult);

        void requestIdCardDetectmultifaceCallBack(boolean success , String msg);




    }

}

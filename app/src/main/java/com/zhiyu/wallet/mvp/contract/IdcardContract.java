package com.zhiyu.wallet.mvp.contract;

import android.content.Intent;

import com.zhiyu.wallet.mvp.ui.BaseIView;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2018/11/16.
 */

public interface IdcardContract {

    interface IdcardIView extends BaseIView {

//        void idTaiOCRsuccess(String name, String id);
//
//        void idTaiOCRBacksuccess();
//
//        void idTaiOCRBackFailure(String msg);
//
//        void idTaiOCRFailure(String msg);
//
//        void zMatteInitsuccess(String msg,String bizNo);
//
//        void zMatteInitFailure(String msg);
//
//        void idCard_detectfaceSuccess();
//
//        void idCard_detectfaceFailure(String msg);
//
//        void saveIdBitmapsuccess(String msg);
//
//        void saveIdBitmapFailure(String msg);
//
        void toScanIdCardBoth();

        void startMXLivenessActivity();

        void dealDetectResult(Intent data, int resultCode);

        void detectSuccess(Intent data);

        void livenessSuccess();

        void livenessFailure(String msg);

        void idcardFrontOcrResult(boolean success);

        void idcardBackOcrResult(boolean success);

        void saveImageSuccess();

        void showdialog();

        void showToast(String msg);

    }

    interface IdcardIModel extends  BaseIModel{


//        void requestIdCardAIORC(String pathResult,int card_type);
//
//        void requestIdCardAIORCCallBack(boolean success,String name,String id,String authority,String valid_date,int cartype);
//
//        void requestZMCerification(String name,String idNumber);
//
//        void saveIdcardpicture(String idfacePath,String idbackPath,String idcardPath);
//
//        void requestIdCardDetectmultiface(String pathResult);
//
//        void requestIdCardDetectmultifaceCallBack(boolean success , String msg);

        /**
         * 活体认证
         * @param image 活体认证照片
         */
          void requestSaveLiveness(byte []image);

        /**
         * 身份证OCR正面 上传文件
         * @param idcardFrontImageByte
         */
          void requestGetIdOcrFrontInfo(byte []idcardFrontImageByte);

        /**
         * 身份证OCR反面(国徽面)  上传文件
         * @param idcardBackImageByte
         */
          void requestGetIdOcrBackInfo(byte []idcardBackImageByte);

        /**
         * 获取身份证正面ocr结果
         * @param fid
         */
          void requestidForntOCR(String fid);

        /**
         * 获取身份证反面ocr结果
         * @param fid
         */
          void requestidbackOCR(String fid);

          void requestLivenessAndOcrCompare(String fid);

         void requestSaveImgae(List<File> fileList);

    }

}

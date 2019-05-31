package com.zhiyu.wallet.mvp.contract;

import android.graphics.Bitmap;

import com.zhiyu.wallet.bean.Record;
import com.zhiyu.wallet.mvp.ui.BaseIView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/11/29.
 */

public interface OrderContract  {

     interface OrderIView extends BaseIView{

         /**
          * 查询银行信息回调
          * @param banks 银行卡信息
          */
         void requestBankinfoCallBack(List<Map<String ,String>> banks);

         void applyOrderSuccess();

         void applyOrderfailure();

         void saveOrderBitmapSuccess();




    }

     interface OrderIModel extends BaseIModel{

         /**
          * 银行信息查询
          */
         void queryBankinfor();

         void requestapplyOrder(String borrowemoney,String comprehensivemoney,String arrivalmoney,String lifeofloan,String borroweofdate,String repaymentdate,String purpose,String creditcard,String channelId,String gps);

         void saveOrderBitmap(String orderbitmappath);
    }
}

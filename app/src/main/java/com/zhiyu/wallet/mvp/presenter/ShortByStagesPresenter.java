package com.zhiyu.wallet.mvp.presenter;

import android.os.Handler;

import com.zhiyu.wallet.common.Constant;
import com.zhiyu.wallet.http.BasehttpModel;
import com.zhiyu.wallet.http.ShortByStagesService;
import com.zhiyu.wallet.http.UserInfoService;
import com.zhiyu.wallet.mvp.contract.ShortByStagesContract;
import com.zhiyu.wallet.mvp.contract.UserInfoContract;
import com.zhiyu.wallet.util.Println;

import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2019/5/20.
 */

public class ShortByStagesPresenter extends BasePresenter<ShortByStagesContract.ShortByStagesIView>{
    private ShortByStagesService userInfoService;
    private BasehttpModel basehttpModel;
    private ShortByStagesContract.ShortByStagesIView shortByStagesIView;
    private static Handler handler ;

    public ShortByStagesPresenter() {
        basehttpModel = BasehttpModel.getInstance(Constant.Local_ipAddress3);
        userInfoService = basehttpModel.create(ShortByStagesService.class);

    }
//    @Override
    public void apply4day(JSONObject jsonObject) {
        basehttpModel.observa(userInfoService.apply_4day(basehttpModel.getRequestBody(jsonObject.toString())), new DisposableObserver<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    Println.out("apply4day", responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Println.err("apply4day", e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}

package com.zhiyu.wallet.mvp.contract;

import com.zhiyu.wallet.mvp.ui.BaseIView;

/**
 * Created by Administrator on 2019/5/20.
 */

public interface ShortByStagesContract {
        interface ShortByStagesIView extends BaseIView {

            //提交成功信息
            void showSuccess(String msg);

            //提交失败信息
            void showFailure(String msg);

            //提交失败信息
            void connectFailure(String msg);

        }
    void apply4day();
}

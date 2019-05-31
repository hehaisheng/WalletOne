package com.zhiyu.wallet.mvp.contract;

import android.content.res.AssetManager;

import com.zhiyu.wallet.bean.News;
import com.zhiyu.wallet.mvp.ui.BaseIView;

import java.util.List;

/**
 * Created by Administrator on 2018/11/7.
 */

public interface NewsCenterContract {

    interface NewsCenterIView extends BaseIView{
        void showNewsData(List<News> newsList);

        void showHelpcenterData(List<News> helplist);

        void showErrorMsg(String msg);

    }

    interface NewsCenterIModel extends BaseIModel{
        void requestNewsData();

        void requestHelpcenterData(AssetManager assetManager);
    }
}

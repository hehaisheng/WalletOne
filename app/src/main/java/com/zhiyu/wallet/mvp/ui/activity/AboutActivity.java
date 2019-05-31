package com.zhiyu.wallet.mvp.ui.activity;

import android.content.pm.PackageInfo;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.common.ActivityManager;
import com.zhiyu.wallet.mvp.presenter.HomePresenter;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UpdateUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @
 * Created by Administrator on 2018/9/16.
 */

public class AboutActivity extends BaseActivity<HomePresenter> {

    @BindView(R.id.iv_title_back)
    LinearLayout ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ly_about)
    LinearLayout lyabout;
    @BindView(R.id.tv_version)
    TextView version;
    @BindView(R.id.tv_textmulti)
    TextView textmulti;

    private String type ="";

    @Override
    protected void initData() {
        StatusbarUtil.setdropBarSystemWindows(this,getResources().getColor(android.R.color.white));

        String versionname = new UpdateUtil(this).getVersion();

        version.setText("当前版本："+versionname);
//        TextJustification.justify(textmulti,textmulti.getMaxWidth());
    }

    @Override
    protected void initTitle() {

        ivTitleBack.setVisibility(View.VISIBLE);
        tvTitle.setText("关于我们");


    }

    @OnClick(R.id.iv_title_back)
    public void back(View view) {
        //销毁当前的页面
        this.removeCurrentActivity();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_business;
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }

}


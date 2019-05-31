package com.zhiyu.wallet.mvp.ui.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.mvp.presenter.HomePresenter;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UpdateUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @
 * Created by Administrator on 2018/9/16.
 */

public class ApplySuccessActivity extends BaseActivity<HomePresenter> {

    @BindView(R.id.iv_title_back)
    LinearLayout ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;


    @Override
    protected void initData() {
        StatusbarUtil.setdropBarSystemWindows(this,getResources().getColor(android.R.color.white));



    }

    @Override
    protected void initTitle() {

        ivTitleBack.setVisibility(View.VISIBLE);
        tvTitle.setText("申请借款");

    }

    @OnClick(R.id.iv_title_back)
    public void back(View view) {
        //销毁当前的页面
       goToActivity(MainActivity.class,null);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_applysuccess;
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @OnClick(R.id.btn_close)
    public void onClose(){

        goToActivity(MainActivity.class,null);
    }

}


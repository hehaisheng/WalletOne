package com.zhiyu.wallet.mvp.ui.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.adapter.HelpContentAdapter;
import com.zhiyu.wallet.adapter.NewsCentetAdapter;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.bean.News;
import com.zhiyu.wallet.mvp.contract.NewsCenterContract;
import com.zhiyu.wallet.mvp.contract.UserInfoContract;
import com.zhiyu.wallet.mvp.presenter.NewsCenterPresenter;
import com.zhiyu.wallet.mvp.presenter.UserInfoPresenter;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 2018/8/12
 *
 * @author zhiyu
 */
public class HelpCenterActivity extends BaseActivity<NewsCenterPresenter> implements NewsCenterContract.NewsCenterIView {
    @BindView(R.id.iv_title_back)
    LinearLayout ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.list_help)
    RecyclerView listhelp;


    @Override
    protected void initData() {

        presenter.attachview(this);
        StatusbarUtil.setdropBarSystemWindows(this,Color.WHITE);
        presenter.requestHelpcenterData(getResources().getAssets());



    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help_center;
    }

    @Override
    protected NewsCenterPresenter createPresenter() {
        return new NewsCenterPresenter();
    }

    @Override
    public void showNewsData(List<News> newsList) {

    }

    @Override
    public void showHelpcenterData(List<News> helplist) {
        HelpContentAdapter helpContentAdapter = new HelpContentAdapter(helplist,getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        listhelp.setLayoutManager(layoutManager);
        listhelp.setAdapter(helpContentAdapter);
        listhelp.getItemAnimator().setChangeDuration(300);
        listhelp.getItemAnimator().setMoveDuration(300);

    }

    @Override
    public void showErrorMsg(String msg) {

    }

    @OnClick(R.id.iv_title_back)
    public void back(View view) {
        this.removeCurrentActivity();
    }

}

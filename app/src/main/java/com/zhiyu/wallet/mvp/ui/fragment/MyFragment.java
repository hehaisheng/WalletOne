package com.zhiyu.wallet.mvp.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.base.BaseFragment;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.common.Constant;
import com.zhiyu.wallet.mvp.contract.UserInfoContract;
import com.zhiyu.wallet.mvp.presenter.BasePresenter;
import com.zhiyu.wallet.mvp.presenter.HomePresenter;
import com.zhiyu.wallet.mvp.presenter.UserInfoPresenter;
import com.zhiyu.wallet.mvp.ui.activity.AboutActivity;
import com.zhiyu.wallet.mvp.ui.activity.AuthenCenterActivity;
import com.zhiyu.wallet.mvp.ui.activity.BankInformationActivity;
import com.zhiyu.wallet.mvp.ui.activity.CustomerServiceActivity;
import com.zhiyu.wallet.mvp.ui.activity.HelpCenterActivity;
import com.zhiyu.wallet.mvp.ui.activity.IdCardActivity2;
import com.zhiyu.wallet.mvp.ui.activity.LoginActivity;
import com.zhiyu.wallet.mvp.ui.activity.NewsCenterActivity;
import com.zhiyu.wallet.mvp.ui.activity.UserInfoActivity;
import com.zhiyu.wallet.util.AppUtil;
import com.zhiyu.wallet.util.Println;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;
import com.zhiyu.wallet.widget.LD_ActionSheet;
import com.zhiyu.wallet.widget.gesture.GestureEditActivity;
import com.zhiyu.wallet.widget.gesture.GestureVerifyActivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.Unbinder;
//import cn.fraudmetrix.octopus.aspirit.bean.OctopusParam;
//import cn.fraudmetrix.octopus.aspirit.main.OctopusManager;
//import cn.fraudmetrix.octopus.aspirit.main.OctopusTaskCallBack;
import cn.fraudmetrix.octopus.aspirit.bean.OctopusParam;
import cn.fraudmetrix.octopus.aspirit.main.OctopusManager;
import cn.fraudmetrix.octopus.aspirit.main.OctopusTaskCallBack;
import io.reactivex.functions.Consumer;

/**
 * @ Created by Administrator on 2018/11/5.
 */

public class MyFragment extends BaseFragment<UserInfoPresenter> implements View.OnClickListener, UserInfoContract.UserInfoIView {

    LinearLayout ivTitleBack;
    TextView tvTitle;
    ImageView ivMeIcon, MeIcon, ivHavenews;
    Button logout;
    TextView tvMeName, MeName, llZichan;
    RelativeLayout rlMe, rlMefore;
    LinearLayout protocol, customerService, repayment, person_data, message_center, bank_news, set_apppassword, helpcenter, aboutme;
    LinearLayout set_password, news;
    private User user;
    private boolean islogin = false;


    @Override
    protected UserInfoPresenter createPresenter() {
        return new UserInfoPresenter();
    }

    @Override
    protected void initData() {
        presenter.attachview(this);
        rlMe = getView().findViewById(R.id.rl_me);
        rlMefore = getView().findViewById(R.id.rl_mefore);
        MeName = getView().findViewById(R.id.me_name);
        MeIcon = getView().findViewById(R.id.login_icon);
        tvMeName = getView().findViewById(R.id.tv_me_name);
        tvMeName.setOnClickListener(this);
        protocol = getView().findViewById(R.id.protocol);
        protocol.setOnClickListener(this);

        customerService = getView().findViewById(R.id.ly_customerService);
        customerService.setOnClickListener(this);

        person_data = getView().findViewById(R.id.person_data);
        person_data.setOnClickListener(this);
        message_center = getView().findViewById(R.id.message_center);
        message_center.setOnClickListener(this);
        bank_news = getView().findViewById(R.id.banek_news);
        bank_news.setOnClickListener(this);
        set_apppassword = getView().findViewById(R.id.set_Apppassword);
        set_apppassword.setOnClickListener(this);

        helpcenter = getView().findViewById(R.id.help_center);
        helpcenter.setOnClickListener(this);

        aboutme = getView().findViewById(R.id.ly_aboutMe);
        aboutme.setOnClickListener(this);

        ivHavenews = getView().findViewById(R.id.iv_haveNews);

        logout = getView().findViewById(R.id.btn_logout);
        logout.setOnClickListener(this);

    }

    @Override
    protected void initTitle() {
        tvTitle = getView().findViewById(R.id.tv_title);
        tvTitle.setText("我的中心");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    public void iniRefresh() {

    }

    @Override
    public void finishRefresh(View view) {

    }


    @Override
    public void onResume() {
        super.onResume();
        //Constants.sharedPreferences = getActivity().getSharedPreferences("SESSION",getContext().MODE_PRIVATE);
        //presenter.checkLoginNet();
        checkLogin();
    }


    public void showToast(String msg) {
        if ("0".equals(msg)) {
            ivHavenews.setVisibility(View.VISIBLE);
        } else {
            ivHavenews.setVisibility(View.GONE);
        }
    }

    public void checkLogin() {
        user = User.getInstance();
        if (user.isLogin()) {
            MeName.setText(user.getPhone());
            rlMefore.setVisibility(View.VISIBLE);
            presenter.checkNewsCenter(ivHavenews);
            // myinfopresenter.requestApplyrecord(Constants.ApplyRecode_Person);
            logout.setVisibility(View.VISIBLE);
            islogin = true;
        } else {
            tvMeName.setText("请点击登录");
            rlMe.setVisibility(View.VISIBLE);
            rlMefore.setVisibility(View.INVISIBLE);
            ivHavenews.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
            islogin = false;
        }


    }

    public void toAuthenCenter() {
        if (islogin) {
            ((BaseActivity) this.getActivity()).goToActivity(AuthenCenterActivity.class, null);
        } else {
//            ((BaseActivity)this.getActivity()).goToActivity(AuthenCenterActivity.class,null);
            ((BaseActivity) this.getActivity()).goToActivity(LoginActivity.class, null);
        }

    }

    public void toPerson_data() {
        if (islogin) {
            ((BaseActivity) this.getActivity()).goToActivity(UserInfoActivity.class, null);
        } else {
//            ((BaseActivity)this.getActivity()).goToActivity(IdCardActivity2.class,null);
            ((BaseActivity)this.getActivity()).goToActivity(LoginActivity.class,null);
        }
    }

    private void toBankNews() {
        if (islogin) {
            Bundle bundle = new Bundle();

            ((BaseActivity) this.getActivity()).goToActivity(BankInformationActivity.class, bundle);
        } else {
            ((BaseActivity) this.getActivity()).goToActivity(LoginActivity.class, null);
//            ((BaseActivity)this.getActivity()).goToActivity(BankInformationActivity.class,null);
        }
    }

    private void toLogin() {
        if (islogin) {

        } else {
            ((BaseActivity) this.getActivity()).goToActivity(LoginActivity.class, null);
        }
    }

    private void toHelpCenter() {
        if (islogin) {
            ((BaseActivity) this.getActivity()).goToActivity(HelpCenterActivity.class, null);
        } else {
            ((BaseActivity) this.getActivity()).goToActivity(HelpCenterActivity.class, null);
        }
    }


    private void toGesture() {
        SharedPreferences msharedPreferences = getActivity().getSharedPreferences("secret_lock", Context.MODE_PRIVATE);
        final boolean isOpen = msharedPreferences.getBoolean("isOpen", false);
        final Bundle bundle = new Bundle();
        if (islogin) {
            final String[] hascaritem = new String[]{"设置密码锁", "修改密码锁", "取消密码锁"};
            LD_ActionSheet.showActionSheet(getContext(), hascaritem, "取消", new LD_ActionSheet.Builder.OnActionSheetselectListener() {
                @Override
                public void itemSelect(Dialog dialog, int index) {
                    dialog.dismiss();
                    switch (index) {
                        case 1:
                            if (isOpen) {
                                // bundle.putInt("type", Constants.Gesture_Set);
                                UIUtils.toast("已经设置密码锁", false);
                            } else {
                                ((BaseActivity) MyFragment.this.getActivity()).goToActivity(GestureEditActivity.class, null);
                            }
                            break;
                        case 2:
                            if (isOpen) {
                                bundle.putInt("type", Constant.Gesture_Change);
                                ((BaseActivity) MyFragment.this.getActivity()).goToActivity(GestureVerifyActivity.class, bundle);
                            } else {
                                UIUtils.toast("未设置密码锁", false);
                            }
                            break;
                        case 3:
                            if (isOpen) {
                                bundle.putInt("type", Constant.Gesture_Cancle);
                                ((BaseActivity) MyFragment.this.getActivity()).goToActivity(GestureVerifyActivity.class, bundle);
                            } else {
                                UIUtils.toast("未设置密码锁", false);
                            }
                            break;
                        default:
                            break;
                    }
                }
            });

        } else {
            ((BaseActivity) this.getActivity()).goToActivity(LoginActivity.class, null);
        }
    }

    private void toMessageCenter() {
        if (islogin) {
            presenter.setReadAllNews();
            ((BaseActivity) this.getActivity()).goToActivity(NewsCenterActivity.class, null);
        } else {
//            ((BaseActivity) this.getActivity()).goToActivity(NewsCenterActivity.class, null);
            ((BaseActivity) this.getActivity()).goToActivity(LoginActivity.class, null);
        }

    }

    private void toLogout() {
        presenter.requestQuitUesr();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.protocol:
                toAuthenCenter();break;
            case R.id.person_data:
                toPerson_data();
                break;
            case R.id.tv_me_name:
                toLogin();
                break;
            case R.id.help_center:
                toHelpCenter();
                break;
            case R.id.banek_news:
                toBankNews();
                break;
            case R.id.set_Apppassword:
                toGesture();
                break;
            case R.id.message_center:
                toMessageCenter();
                break;
            case R.id.btn_logout:
                toLogout();
                break;
            case R.id.ly_customerService:
                ((BaseActivity) this.getActivity()).goToActivity(CustomerServiceActivity.class, null);
                break;
            case R.id.ly_aboutMe:
                ((BaseActivity) this.getActivity()).goToActivity(AboutActivity.class, null);
                break;
            default:
                break;

        }
    }


    @Override
    public void showSuccess(String msg) {
        UIUtils.toast(msg, false);
        checkLogin();
    }

    @Override
    public void showFailure(String msg) {
        UIUtils.toast(msg, false);
    }

    @Override
    public void connectFailure(String msg) {

    }
}

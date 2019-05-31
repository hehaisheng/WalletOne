package com.zhiyu.wallet.mvp.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.mvp.contract.UserInfoContract;
import com.zhiyu.wallet.mvp.presenter.UserInfoPresenter;
import com.zhiyu.wallet.mvp.ui.fragment.RepayFragment;
import com.zhiyu.wallet.util.AppUtil;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;
import com.zhiyu.wallet.widget.CheckBoxSample;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 2018/7/12
 *
 * @author zhiyu
 */
public class LoginActivity extends BaseActivity<UserInfoPresenter> implements UserInfoContract.UserInfoIView {


    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.et_password)
    EditText password;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.tv_logintips)
    TextView tv_tips;
    @BindView(R.id.iv_showpwd)
    ImageView showpwd;
    @BindView(R.id.tv_forgetPwd)
    TextView forgetPwd;
    @BindView(R.id.tv_register)
    TextView register;

    private boolean hasusername = false;
    private boolean haspwd = false, isShowpwd = false;

    @Override
    protected void initData() {
        presenter.attachview(this);
        StatusbarUtil.setfullScreenSystemWindows(this, Color.WHITE);


        login.setClickable(false);
        login.setSelected(false);
        SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
        username.setText(sharedPreferences.getString("user_name",""));
        username.setSelection(sharedPreferences.getString("user_name","").length());
        hasusername=true;
        //设置手机格式 xxx xxxx xxxx
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence == null || charSequence.length() == 0) {
                    hasusername = false;
                    return;
                }
                hasusername = true;
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < charSequence.length(); i++) {
                    if (i != 3 && i != 8 && charSequence.charAt(i) == ' ') {
                        continue;
                    } else {
                        stringBuilder.append(charSequence.charAt(i));
                        if ((stringBuilder.length() == 4 || stringBuilder.length() == 9)
                                && stringBuilder.charAt(stringBuilder.length() - 1) != ' ') {
                            stringBuilder.insert(stringBuilder.length() - 1, ' ');
                        }
                    }
                }
                if (!stringBuilder.toString().equals(charSequence.toString())) {
                    int index = start + 1;
                    if (stringBuilder.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }

                    username.setText(stringBuilder.toString());
                    username.setSelection(index);
                }
                login.setSelected(hasusername && haspwd);
                login.setClickable(hasusername && haspwd);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //给EditText 设置监听，有输入内容改变登录Button的背景颜色（灰色->蓝色）
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    haspwd = true;
                } else {
                    haspwd = false;
                }
                login.setSelected(hasusername && haspwd);
                login.setClickable(hasusername && haspwd);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    protected void initTitle() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected UserInfoPresenter createPresenter() {
        return new UserInfoPresenter();
    }

    @OnClick({R.id.login})
    public void onClick() {
        if (!TextUtils.isEmpty(username.getText().toString()) || !TextUtils.isEmpty(password.getText().toString())) {

            presenter.requestLogin(username.getText().toString(), password.getText().toString(),this, AppUtil.getChannelData(this,"UMENG_CHANNEL"));

        }
    }

    @OnClick({ R.id.tv_register, R.id.tv_forgetPwd})
    public void protocol(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                goToActivity(RegisterActivity.class, null);
                break;
            case R.id.tv_forgetPwd:
                goToActivity(ForgetPasswordActivity.class, null);
                break;
        }
    }


    @OnClick(R.id.iv_showpwd)
    public void showpwd() {
        if (isShowpwd) {
            showpwd.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_hide));
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            password.setSelection(password.getText().toString().length());
            isShowpwd = !isShowpwd;

        } else {
            showpwd.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_show));
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            password.setSelection(password.getText().toString().length());
            isShowpwd = !isShowpwd;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showSuccess(String msg) {
        SharedPreferences sharedPreferences = getSharedPreferences("username",MODE_PRIVATE);
        sharedPreferences.edit().putString("user_name",username.getText().toString()).apply();
        UIUtils.toast(msg,false);
        finish();
    }

    @Override
    public void showFailure(String msg) {
        tv_tips.setText(msg);
        tv_tips.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.shake);
        tv_tips.startAnimation(animation);
    }

    @Override
    public void connectFailure(String msg) {
        UIUtils.toast(msg,false);
    }
}

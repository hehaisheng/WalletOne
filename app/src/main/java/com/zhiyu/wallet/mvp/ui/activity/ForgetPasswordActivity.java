package com.zhiyu.wallet.mvp.ui.activity;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.mvp.contract.UserInfoContract;
import com.zhiyu.wallet.mvp.presenter.UserInfoPresenter;
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
public class ForgetPasswordActivity extends BaseActivity<UserInfoPresenter> implements UserInfoContract.UserInfoIView {


    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.et_checkcode)
    EditText checkcode;
    @BindView(R.id.et_password)
    EditText password;
    @BindView(R.id.et_againPassword)
    EditText againPassword;
    @BindView(R.id.btn_forget)
    Button forget;
    @BindView(R.id.requestCode)
    Button requestCode;
    @BindView(R.id.tv_logintips)
    TextView tv_tips;
    @BindView(R.id.iv_pwdshow)
    ImageView showpwd;
    @BindView(R.id.iv_pwdagainshow)
    ImageView showAgainpwd;

    private String returnmsg;
    private int currentTime = 10;
    private CountDownTime mTimer;
    private boolean hasusername=false,isShowAgainpwd=false;
    private boolean haspwd=false,isShowpwd=false;

    @Override
    protected void initData() {
        presenter.attachview(this);
        StatusbarUtil.setdropBarSystemWindows(this, Color.WHITE);
        mTimer = new CountDownTime(60000, 1000);//初始化对象

        //设置手机格式 xxx xxxx xxxx
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence == null || charSequence.length() == 0) {
                    hasusername=false;
                    return;
                }
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
                    hasusername=true;
                    username.setText(stringBuilder.toString());
                    username.setSelection(index);
                }
                forget.setSelected(hasusername&&haspwd);
                forget.setClickable(hasusername&&haspwd);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        forget.setClickable(false);
        forget.setSelected(false);
        //给EditText 设置监听，有输入内容改变登录Button的背景颜色（灰色->蓝色）
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                   haspwd=true;
                } else {
                    haspwd=false;
                }
                forget.setSelected(hasusername&&haspwd);
                forget.setClickable(hasusername&&haspwd);

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
        return R.layout.activity_forgetpwd;
    }

    @Override
    protected UserInfoPresenter createPresenter() {
        return new UserInfoPresenter();
    }

    @OnClick({R.id.btn_forget})
    public void onClick() {
        presenter.requestForgetpwd(username.getText().toString(),checkcode.getText().toString(), password.getText().toString(),againPassword.getText().toString());
    }

    @OnClick(R.id.requestCode)
    public void onRequestCode() {
        presenter.requestCheckCode(username.getText().toString());
    }


    @OnClick({R.id.iv_pwdshow,R.id.iv_pwdagainshow})
    public void showpwd(View view){
        switch (view.getId()){
            case R.id.iv_pwdshow:
                if(isShowpwd){
                    showpwd.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_hide));
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.getText().toString().length());
                    isShowpwd = !isShowpwd;

                }else {
                    showpwd.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_show));
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.getText().toString().length());
                    isShowpwd = !isShowpwd;
                }  break;
            case R.id.iv_pwdagainshow:
                if(isShowAgainpwd){
                    showAgainpwd.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_hide));
                    againPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    againPassword.setSelection(againPassword.getText().toString().length());
                    isShowAgainpwd = !isShowAgainpwd;

                }else {
                    showAgainpwd.setImageDrawable(getResources().getDrawable(R.mipmap.pwd_show));
                    againPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    againPassword.setSelection(againPassword.getText().toString().length());
                    isShowAgainpwd = !isShowAgainpwd;
                }  break;
        }

    }

    @Override
    public void showSuccess(String msg) {
        if(msg.contains("requestCode")){
            UIUtils.toast(msg.replace("requestCode:",""),false);
            mTimer.start();
        }else if(msg.contains("forgetpwd")){
            tv_tips.setVisibility(View.GONE);
            UIUtils.toast(msg.replace("forgetpwd:",""),false);
            finish();
        }
    }

    @Override
    public void showFailure(String msg) {
        if(msg.contains("tips")){
            tv_tips.setVisibility(View.VISIBLE);
            tv_tips.setText(msg.replace("tips:",""));
            Animation animation = AnimationUtils.loadAnimation(this,R.anim.shake);
            tv_tips.startAnimation(animation);
        }else {
            tv_tips.setVisibility(View.GONE);
            UIUtils.toast(msg.replace("forgetpwd:",""),false);
        }
    }

    @Override
    public void connectFailure(String msg) {

    }


    class CountDownTime extends CountDownTimer {

        //构造函数  第一个参数代表总的计时时长  第二个参数代表计时间隔  单位都是毫秒
        public CountDownTime(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) { //每计时一次回调一次该方法
            if(requestCode!=null) {
                requestCode.setClickable(false);
                requestCode.setTextColor(getResources().getColor(R.color.yellow));
                requestCode.setText(l / 1000 + "秒");
            }
        }

        @Override
        public void onFinish() { //计时结束回调该方法
            if(requestCode!=null) {
                requestCode.setClickable(true);
                requestCode.setText("重新获取");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }
}

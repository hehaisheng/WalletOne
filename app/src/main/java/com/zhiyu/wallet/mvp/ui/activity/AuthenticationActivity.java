package com.zhiyu.wallet.mvp.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.bean.Credit;
import com.zhiyu.wallet.mvp.contract.AuthenticateContract;
import com.zhiyu.wallet.mvp.presenter.AuthenticatePresenter;
import com.zhiyu.wallet.util.Println;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;
import com.zhiyu.wallet.util.UtilsDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 2018/7/24
 *
 * @author ZhiYu
 *         认证界面
 */
public class AuthenticationActivity extends BaseActivity<AuthenticatePresenter> implements AuthenticateContract.AuthenticateIView {


    @BindView(R.id.iv_title_back)
    LinearLayout ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @Nullable
    @BindView(R.id.image)
    ImageView image;
    @Nullable
    @BindView(R.id.btn_login)
    Button btnLogin;
    @Nullable
    @BindView(R.id.et_number)
    EditText etNumber;
    @Nullable
    @BindView(R.id.et_password)
    EditText etPassword;

    @Nullable
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    @Nullable
    @BindView(R.id.et_zfb_number)
    EditText etZfbNumber;
    @Nullable
    @BindView(R.id.et_zfb_password)
    EditText etZfbPassword;
    @Nullable
    @BindView(R.id.toggle_pwd)
    ToggleButton togglePwd;
    private String mType;
    private String startMetheod;

    private int mLayoutID;
    private int bgId;
    private String buttonColor;
    private int bgSelectId;
    private String buttonSelectColor;
    private Unbinder unbinder;
    private Dialog loadingdialog;
    private String next_stage, task_id;


    //京东认证跟运营商认证
    @Override
    protected void initData() {
        presenter.attachview(this);
        unbinder = ButterKnife.bind(this);
        StatusbarUtil.setdropBarSystemWindows(this, Color.WHITE);
//        if(!mType.equals("借贷宝认证")) {
//            btnLogin.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onConfirm();
//                }
//            });
//        }
        setTextChangedListener();
    }

    private void setTextChangedListener() {
        switch (mType) {
//            case "借贷宝认证":
//                etNumber.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                        if (TextUtils.isEmpty(charSequence)) {
//                            tvNumber.setVisibility(View.VISIBLE);
//                        } else {
//                            tvNumber.setVisibility(View.GONE);
//                        }
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable editable) {
//
//                    }
//                });
//
//                break;
            case "支付宝认证":
                etZfbPassword.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (TextUtils.isEmpty(charSequence)) {
                            btnLogin.setBackground(getResources().getDrawable(getBgId()));
                            btnLogin.setTextColor(Color.parseColor(getButtonColor()));
                        } else {
                            btnLogin.setBackground(getResources().getDrawable(getBgSelectId()));
                            btnLogin.setTextColor(Color.parseColor(getButtonSelectColor()));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                etZfbNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });


                togglePwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                        if (isChecked) {
                            //如果选中，显示密码
                            togglePwd.setBackgroundDrawable(getResources().getDrawable(R.mipmap.pwd_show));
                            etZfbPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        } else {
                            //否则隐藏密码
                            togglePwd.setBackgroundDrawable(getResources().getDrawable(R.mipmap.pwd_hide));
                            etZfbPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                    }
                });
                break;

            default:
                btnLogin.setClickable(false);
                etNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                etPassword.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (TextUtils.isEmpty(charSequence)) {
                            btnLogin.setBackground(getResources().getDrawable(getBgId()));
                            btnLogin.setTextColor(Color.parseColor(getButtonColor()));
                            btnLogin.setClickable(false);
                        } else {
                            btnLogin.setBackground(getResources().getDrawable(getBgSelectId()));
                            btnLogin.setTextColor(Color.parseColor(getButtonSelectColor()));
                            btnLogin.setClickable(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                if (mType.equals("淘宝认证") || mType.equals("京东认证")) {

                    togglePwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                            if (isChecked) {
                                //如果选中，显示密码
                                togglePwd.setBackgroundDrawable(getResources().getDrawable(R.mipmap.pwd_show));
                                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            } else {
                                //否则隐藏密码
                                togglePwd.setBackgroundDrawable(getResources().getDrawable(R.mipmap.pwd_hide));
                                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            }
                        }
                    });
                }

                break;


        }


    }

    private void setLogin(Bundle bundle) {
        switch (mType) {
            case "淘宝认证":
                mLayoutID = R.layout.activity_authen_taobao;
                bgId = R.mipmap.tb_login_none;
                bgSelectId = R.mipmap.tb_login_select;
                buttonColor = "#4dfcfcfc";
                buttonSelectColor = "#fcfcfc";
                break;

            case "支付宝认证":
                mLayoutID = R.layout.activity_authen_zfb;
                bgId = R.mipmap.zfb_btn_none;
                bgSelectId = R.mipmap.zfb_btn_select;
                buttonColor = "#fcfcfc";
                buttonSelectColor = "#fcfcfc";
                break;

            case "京东认证":
                mLayoutID = R.layout.activity_authen_jd;
                bgId = R.mipmap.jd_login_none;
                bgSelectId = R.mipmap.jd_login_select;
                buttonColor = "#fcfcfc";
                buttonSelectColor = "#fcfcfc";
                break;
            case "京东认证2":
                mLayoutID = R.layout.activity_authen_jdverify;
                bgId = R.mipmap.jd_login_none;
                bgSelectId = R.mipmap.jd_login_select;
                buttonColor = "#fcfcfc";
                buttonSelectColor = "#fcfcfc";
                next_stage = bundle.getString("next_stage");
                task_id = bundle.getString("task_id");
                break;
            case "运营商认证":
                mLayoutID = R.layout.activity_authen_carrier;
                bgId = R.mipmap.carrier_none;
                bgSelectId = R.mipmap.carrier_select;
                buttonColor = "#fcfcfc";
                buttonSelectColor = "#fcfcfc";
                break;

            case "运营商认证2":
                mLayoutID = R.layout.activity_authen_carrierverify;
                bgId = R.mipmap.carrier_none;
                bgSelectId = R.mipmap.carrier_select;
                buttonColor = "#fcfcfc";
                buttonSelectColor = "#fcfcfc";
                next_stage = bundle.getString("next_stage");
                task_id = bundle.getString("task_id");
                break;
            default:
                break;
        }

    }

    @Override
    protected void initTitle() {

        ivTitleBack.setVisibility(View.VISIBLE);
        //ivTitleSetting.setVisibility(View.GONE);
    }

    @OnClick(R.id.iv_title_back)
    public void back(View view) {
        removeCurrentActivity();
    }


    @Override
    protected int getLayoutId() {
        Bundle bundle = getIntent().getBundleExtra("data");
        initBundle(bundle);
        setLogin(bundle);
        return mLayoutID;
    }

    @Override
    protected AuthenticatePresenter createPresenter() {
        return new AuthenticatePresenter();
    }


    public int getBgId() {

        return bgId;
    }

    public String getButtonColor() {
        return buttonColor;
    }

    public int getBgSelectId() {
        return bgSelectId;
    }

    public String getButtonSelectColor() {
        return buttonSelectColor;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_login)
    public void onConfirm() {
        if (TextUtils.isEmpty(etNumber.getText().toString().trim()) || TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            return;
        }
        switch (mType) {
            case "京东认证2":
                loadingdialog = UtilsDialog.createLoadingDialog(AuthenticationActivity.this, "认证中");
                presenter.requestJDAuthen2(task_id, next_stage, etNumber.getText().toString().trim(), etPassword.getText().toString().trim());
                break;
            case "京东认证":
                loadingdialog = UtilsDialog.createLoadingDialog(AuthenticationActivity.this, "认证中");
                presenter.requestAuthentication(mType, etNumber.getText().toString().trim(), etPassword.getText().toString().trim());
                break;

            case "运营商认证2":
                if (!UIUtils.isMobile(etNumber.getText().toString().trim())) {
                    return;
                }
                loadingdialog = UtilsDialog.createLoadingDialog(AuthenticationActivity.this, "认证中");
//                showAuthenResult(true, null);
                presenter.requestCarrierAuthen2(task_id,next_stage, etNumber.getText().toString().trim(), etPassword.getText().toString().trim());
                break;
            default:
                if (!UIUtils.isMobile(etNumber.getText().toString().trim())) {
                    return;
                }
                loadingdialog = UtilsDialog.createLoadingDialog(AuthenticationActivity.this, "认证中");
                    presenter.requestAuthentication(mType, etNumber.getText().toString().trim(), etPassword.getText().toString().trim());
//                Map<String, String> map = new HashMap<>();
//                map.put("message", "测试1");
//                map.put("code", "105");
//                map.put("type", "运营商认证2");
//                showAuthenResult(false, map);
                break;
        }

    }

    @Override
    public void initBundle(Bundle bundle) {
        //接收name值
        if (bundle != null) {
            mType = bundle.getString("type");
            if (!TextUtils.isEmpty(bundle.getString("startMetheod"))) {
                startMetheod = bundle.getString("startMetheod");
            }

        }
    }

    @Override
    public void showAuthenItemlist(List<Credit> credits) {

    }

    @Override
    public void showAuthenResult(boolean isSuccess, final Map<String, String> data) {
        loadingdialog.dismiss();
        if (isSuccess) {
            UtilsDialog.showDialogCommit("", "认证成功", this, new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if ("startForresult".equals(startMetheod)) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("resultCode",200);
                        goToActivity(AuthenCenterActivity.class,bundle);

                    } else if ("normal".equals(startMetheod)) {
                        AuthenticationActivity.this.finish();
                    }
                }
            });
        } else {
            if (data == null) {
                return;
            }
            if (data.get("code").equals("105")) {
                UtilsDialog.showDialogCommit("", data.get("message"), this, new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        AuthenticationActivity.this.finish();
                        Bundle bundle = new Bundle();
                        bundle.putString("type", data.get("type"));
                        bundle.putString("next_stage", data.get("next_stage"));
                        bundle.putString("task_id", data.get("task_id"));
                        if (!TextUtils.isEmpty(startMetheod)) {
                            bundle.putString("startMetheod", startMetheod);
                        }
                        goToActivity(AuthenticationActivity.class, bundle);
                    }
                });
                return;
            }

            UtilsDialog.showDialogCommit("", data.get("message"), this, new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
        }

    }

    @Override
    public void sendsmsSuccess(boolean isinputAddress, String msg,String uuid) {

    }

    @Override
    public void showdialog(String msg) {
        if (loadingdialog != null) {
            loadingdialog.dismiss();
        }
        UIUtils.toast(msg, false);
    }

    @Override
    public void closedialog() {

    }
}

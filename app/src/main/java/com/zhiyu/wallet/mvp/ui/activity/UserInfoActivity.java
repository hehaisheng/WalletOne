package com.zhiyu.wallet.mvp.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.authreal.api.AuthBuilder;
import com.authreal.api.OnResultListener;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.bean.YDResult;
import com.zhiyu.wallet.common.Constant;
import com.zhiyu.wallet.mvp.contract.UserInfoContract;
import com.zhiyu.wallet.mvp.presenter.UserInfoPresenter;
import com.zhiyu.wallet.mvp.ui.fragment.HomeFragment;
import com.zhiyu.wallet.util.Println;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;
import com.zhiyu.wallet.util.UtilsDialog;
import com.zhiyu.wallet.widget.LD_ActionSheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * 2018/7/12
 *
 * @author zhiyu
 */

public class UserInfoActivity extends BaseActivity<UserInfoPresenter> implements UserInfoContract.UserInfoIView {

    private static final int PICTURE = 100;
    private static final int CAMERA = 200;
    @BindView(R.id.iv_title_back)
    LinearLayout ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R.id.et_name)
    EditText etname;
    @BindView(R.id.et_id)
    TextView etid;
    @BindView(R.id.tv_telnum)
    TextView tvtelnum;
    @BindView(R.id.tv_family)
    TextView family;
    @BindView(R.id.et_weinxin)
    EditText weixin;
    @BindView(R.id.et_address)
    EditText et_address;
    @BindView(R.id.et_occupation)
    EditText et_occupation;
    @BindView(R.id.btn_myinfocommit)
    Button btn_myinfocommit;
    @BindView(R.id.ly_id)
    LinearLayout ly_id;
    @BindView(R.id.ly_relaInfo)
    LinearLayout ly_relaInfo;

    private User user = User.getInstance();


    @Override
    protected void initData() {
        presenter.attachview(this);
        StatusbarUtil.setdropBarSystemWindows(this, Color.WHITE);
        getUserdata();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (user.isRelainfo()) {
            family.setText("已填写");
        }
        String idcarnumber = user.getIdcar();
        if (!TextUtils.isEmpty(idcarnumber) && idcarnumber.length() == 18) {
            etid.setText(idcarnumber.replace(idcarnumber.substring(5, idcarnumber.length() - 2), "****"));
        }

        if((!TextUtils.isEmpty(user.getUsername()))
                && (!TextUtils.isEmpty(user.getIdcar()))
                && (user.isRelainfo())
                && (!TextUtils.isEmpty(user.getWechatid()))
                && (!TextUtils.isEmpty(user.getCopy1()))
                && (!TextUtils.isEmpty(user.getCopy2()))){
            btn_myinfocommit.setVisibility(View.INVISIBLE);
            etname.setEnabled(false);
            ly_id.setEnabled(false);
            ly_relaInfo.setEnabled(false);
            weixin.setEnabled(false);
            et_address.setEnabled(false);
            et_occupation.setEnabled(false);
        }
    }

    @Override
    protected void initTitle() {
        ivTitleBack.setVisibility(View.VISIBLE);
        tvTitle.setText("个人资料");
        ivTitleSetting.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.iv_title_back)
    public void back(View view) {
        //销毁当前的页面
        this.removeCurrentActivity();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_info;
    }

    @Override
    protected UserInfoPresenter createPresenter() {
        return new UserInfoPresenter();
    }




    @OnClick(R.id.btn_myinfocommit)
    public void oncommit() {

        UtilsDialog.showDialogForListener("", "本人声明以上填写的资料真实，如发现虚假资料与欺诈行为，愿承担相应责任。请珍爱个人信用记录！", this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nm = etname.getText().toString().trim();
                String tel = tvtelnum.getText().toString().trim();
                String id=user.getIdcar();
                String wcid = weixin.getText().toString().trim();
                String reinf = family.getText().toString().trim();
                String address = et_address.getText().toString().trim();
                String occupation = et_occupation.getText().toString().trim();
//                String address = "广州市天河区";
//                String id = "440106198804191511";
//                String nm = "钟锦开";
//                String wcid ="xx1";
//                String reinf = "已填写";
                presenter.requestUerinfo(nm, tel, id, wcid, reinf,address,occupation,getApplication());

            }
        });

    }


    @OnClick(R.id.ly_relaInfo)
    public void onRelaInfo() {

        requestPermissions();

    }
    @OnClick(R.id.ly_id)
    public void onCheckId(){

//        UserInfoActivity.this.goToActivity(IdCardActivity2.class,null);
        requestPermissionsCamera();
    }

    public void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(UserInfoActivity.this);
        rxPermission.request(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_SMS
        ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) {
                if (!granted) {
                    UIUtils.toast("应用未能获取全部需要的相关权限，该功能可能不能正常使用.", false);
                } else {
                    //startActivity(new Intent(UserInfoActivity.this, RelativesinfoActivity.class));
                     UserInfoActivity.this.goToActivity(RelativesinfoActivity.class,null);
                }

            }
        });

    }

    public void requestPermissionsCamera() {
        RxPermissions rxPermission = new RxPermissions(UserInfoActivity.this);
        rxPermission.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) {
                if (!granted) {
                    UIUtils.toast("应用未能获取全部需要的相关权限，该功能可能不能正常使用.", false);
                }else {

                    UserInfoActivity.this.goToActivity(IdCardActivity2.class,null);
                }

            }
        });

    }

    public void getUserdata() {
        tvtelnum.setText(user.getPhone());
        String name = user.getUsername();
        if(!TextUtils.isEmpty(name) && name.length()>=2 ){
            etname.setText(name.replace(name.charAt(1), '*'));
        }
        weixin.setText(user.getWechatid());
        et_address.setText(user.getCopy1());
        et_occupation.setText(user.getCopy2());
        etname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s ==null || s.length() == 0){
                    return;
                }
                StringBuilder stringBuffer = new StringBuilder();
                for(int i = 0;i<s.length();i++){

                    char a = s.charAt(i);
                    stringBuffer.append(a);

                }

                String name  = clearLimitStr("",stringBuffer.toString());

                Println.out("name",name+start);
                if (!name.equals(s.toString())) {
                    int index ;
                    if(start!=name.length()){
                        index= start;
                    }else {
                        index = name.length();
                    }
                    etname.setText(name);
                    etname.setSelection(index);
                }

//                etname.removeTextChangedListener(this);
//                etname.setText(name);
//                etname.setSelection(start);
//                etname.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private String clearLimitStr(String regex, String str) {
        return str.replaceAll("[^\u4E00-\u9FA5]", "");
    }

    @Override
    public void showSuccess(String msg) {
        UIUtils.toast(msg,false);
        Bundle bundle =getIntent().getBundleExtra("data");
        if(bundle!=null){
            HomeFragment fragment = HomeFragment.getInstance();
            fragment.OnApply();
        }
        finish();
    }

    @Override
    public void showFailure(String msg) {

    }

    @Override
    public void connectFailure(String msg) {
        UIUtils.toast(msg,false);
    }


}
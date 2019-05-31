package com.zhiyu.wallet.mvp.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.bean.Credit;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.mvp.contract.AuthenticateContract;
import com.zhiyu.wallet.mvp.contract.UserInfoContract;
import com.zhiyu.wallet.mvp.presenter.AuthenticatePresenter;
import com.zhiyu.wallet.mvp.presenter.UserInfoPresenter;
import com.zhiyu.wallet.util.AppUtil;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;
import com.zhiyu.wallet.util.UtilsDialog;
import com.zhiyu.wallet.widget.CheckBoxSample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 2018/8/8
 *
 * @author zhiyu
 */
public class BankaddActivity extends BaseActivity<AuthenticatePresenter> implements AuthenticateContract.AuthenticateIView,UserInfoContract.UserInfoIView {

    @BindView(R.id.et_bankAccount)
    EditText bankAccount;
    @BindView(R.id.tv_name)
    TextView tvname;
    @BindView(R.id.tv_telnum)
    TextView tvtelnum;
    @BindView(R.id.tv_idnum)
    TextView tvidnum;
    @BindView(R.id.btn_confirm)
    Button confirm;
    @BindView(R.id.tv_tips)
    TextView tips;
    @BindView(R.id.ly_verifyCode)
    LinearLayout ly_verifyCode;
    @BindView(R.id.et_verifycode)
    EditText et_verifycode;
    @BindView(R.id.ly_selectAddress)
    LinearLayout ly_selectaddress;
    @BindView(R.id.tv_bankprovice)
    TextView tv_bankprovice;
    @BindView(R.id.tv_bankcity)
    TextView tv_bankcity;
    @BindView(R.id.tv_bankdistrict)
    TextView tv_bankdistrict;
    @BindView(R.id.et_bankAddressdetail)
    EditText et_bankAddressdetail;
    @BindView(R.id.ly_bankAddressdetail)
    LinearLayout ly_bankAddressdetail;
    @BindView(R.id.ly_bankAddress)
    LinearLayout ly_bankAddress;

    private String type = "";
    private Map<String, String> bankinfo = new HashMap<>();
    private User user = User.getInstance();
    private Dialog loadingdialog;
    private Dialog finishdialog;
    private int TYPE_DIALOG = 0 ;
    private boolean CONFIRM = false; // 是否输入短信验证码
    private boolean ISINPUTADDRESS= false;  //是否需要输入地址信息，，
    private String requestno = "";
    private String userid ="";
    private Bundle bundle = new Bundle();
    private CityPickerView mPicker = new CityPickerView();  //地址选择器

    @Override
    protected void initData() {
        mPicker.init(this);
        presenter.attachview(this);
        StatusbarUtil.setdropBarSystemWindows(this, Color.WHITE);
        showUserinfo();
        onEvent();
        confirm.setSelected(false);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_addbank;
    }

    @Override
    protected AuthenticatePresenter createPresenter() {
        return new AuthenticatePresenter();
    }

    @OnClick(R.id.ly_back)
    public void back(View view) {
        //销毁当前的页面
        this.removeCurrentActivity();

    }

    @OnClick({R.id.btn_confirm,R.id.ly_selectAddress})
    public void onConfirm(View view) {
        switch (view.getId()){
            case R.id.btn_confirm:
                if(TextUtils.isEmpty(bankAccount.getText().toString())){
                return;
                }
                showdialog("确认银行卡号无误后提交");break;
            case R.id.ly_selectAddress:
                mPicker.showCityPicker();
        }


    }

    public void showUserinfo(){
        Bundle bundle = getIntent().getBundleExtra("data");
        if(bundle!=null){
            type=bundle.getString("type");
        }
        tvname.setText(user.getUsername().replace(user.getUsername().charAt(1),'*'));
        String phone = user.getPhone();
        String phonehide = "";
        if(!TextUtils.isEmpty(phone) && phone.length()==11){
            phonehide = phone.replace(phone.substring(3,7),"****");
        }
        String idcar = user.getIdcar();
        String idcarhide = "";
        if(!TextUtils.isEmpty(idcar) && idcar.length()==18){
            idcarhide= idcar.replace(idcar.substring(7,15),"********");
        }

        tvtelnum.setText(phonehide);
        tvidnum.setText(idcarhide);

    }

    public void onEvent(){
        bankAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null || s.length() == 0) {
                    confirm.setSelected(false);
                    return;
                }
                confirm.setSelected(true);
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 4 && i != 9 && i != 14 && i != 19 && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        stringBuilder.append(s.charAt(i));
                        if ((stringBuilder.length() == 5 || stringBuilder.length() == 10 || stringBuilder.length() == 15 || stringBuilder.length() == 20)
                                && stringBuilder.charAt(stringBuilder.length() - 1) != ' ') {
                            stringBuilder.insert(stringBuilder.length() - 1, ' ');
                        }
                    }
                }
                if (!stringBuilder.toString().equals(s.toString())) {
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
                    bankAccount.setText(stringBuilder.toString());
                    bankAccount.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        CityConfig cityConfig = new CityConfig.Builder()
                .title("选择城市")//标题
                .confirmText("确定")//确认按钮文字
                .confirmTextSize(16)//确认按钮文字大小
                .cancelText("取消")//取消按钮文字
                .cancelTextSize(16)//取消按钮文字大小
                .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS)//显示类，只显示省份一级，显示省市两级还是显示省市区三级
                .showBackground(true)//是否显示半透明背景
                .province("北京市")//默认显示的省份
                .city("北京市")//默认显示省份下面的城市
                .district("海淀区")
                .provinceCyclic(true)//省份滚轮是否可以循环滚动
                .cityCyclic(true)//城市滚轮是否可以循环滚动
                .districtCyclic(true)//区县滚轮是否循环滚动
                .build();

//设置自定义的属性配置
        mPicker.setConfig(cityConfig);

        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                super.onSelected(province, city, district);
                tv_bankprovice.setText(province.getName());
                tv_bankcity.setText(city.getName());
                tv_bankdistrict.setText(district.getName());
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }
        });

    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void showAuthenItemlist(List<Credit> credits) {

    }

    @Override
    public void showAuthenResult(boolean isSuccess,final Map<String,String> data) {
        if(isSuccess) {
            if("bankinfo".equals(type)){
                bundle.putString("bankcardaccount",bankAccount.getText().toString());
                bundle.putString("bankcardname", data.get("data"));
            }
            List<Map<String,Object>> mapList = new ArrayList<>();
            Map<String,Object> map = new HashMap<>();
            map.put("bankcardaccount",bankAccount.getText().toString());
            map.put("bankcardname",data.get("data"));
            if(ISINPUTADDRESS){
                map.put("cardno",bankAccount.getText().toString());
                map.put("province",tv_bankprovice.getText().toString());
                map.put("city",tv_bankcity.getText().toString());
                map.put("subbranch",et_bankAddressdetail.getText().toString());
                map.put("cardname",data.get("data"));
            }
            mapList.add(map);
            if(ISINPUTADDRESS){
                presenter.saveBankInformation(this,"",mapList,1);
            }else {
                presenter.saveBankInformation(this,"",mapList,0);
            }

     }else {
            closedialog();

            if(data.get("shake")!=null){
                if("true".equals(data.get("shake"))){
                    tips.setText(data.get("msg"));
                    // 左右移动动画
                    Animation shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
                    tips.startAnimation(shakeAnimation);
                    return;
                }
            }
            UIUtils.toast(data.get("msg"),false);

        }
    }

    @Override
    public void sendsmsSuccess(boolean isinputAddress,String data,String uuid) {
        closedialog();
        UIUtils.toast("短信发送成功",false);
        ly_verifyCode.setVisibility(View.VISIBLE);
        CONFIRM=true;
        bankAccount.setEnabled(false);
        requestno = data;
        userid = uuid;
        et_verifycode.setFocusable(true);

        //ISINPUTADDRESS=isinputAddress;
        if(ISINPUTADDRESS){
            ly_bankAddress.setVisibility(View.VISIBLE);
            ly_bankAddressdetail.setVisibility(View.VISIBLE);
        }else {
            ly_bankAddress.setVisibility(View.GONE);
            ly_bankAddressdetail.setVisibility(View.GONE);
        }
    }

    @Override
    public void showdialog(String msg) {
        closedialog();

        if(TYPE_DIALOG == 0){
            if(CONFIRM){
                UtilsDialog.showDialogForListener("添加银行卡", msg, this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(TextUtils.isEmpty(et_verifycode.getText().toString())){
                             return;
                        }
                        if(ISINPUTADDRESS){
                            if(TextUtils.isEmpty(tv_bankprovice.getText().toString())){
                                return;
                            }
                            if(TextUtils.isEmpty(tv_bankcity.getText().toString())){
                                return;
                            }
                            if(TextUtils.isEmpty(et_bankAddressdetail.getText().toString())){
                                return;
                            }
                        }
                        loadingdialog = UtilsDialog.createLoadingDialog(BankaddActivity.this,"认证中");
//                        presenter.requestBankCardComfirmSMS(requestno,et_verifycode.getText().toString());
                        presenter.requestBankCardComfirmSMSfuyou(requestno,userid,bankAccount.getText().toString(),et_verifycode.getText().toString());
                    }
                });

            }else {
                UtilsDialog.showDialogForListener("添加银行卡", msg, this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(bankAccount.getText().toString().length()<18){
                            UIUtils.toast("请输入正确的银行卡号",false);
                            return;
                        }
                        if(TextUtils.isEmpty(User.getInstance().getIdcar())){
                            UIUtils.toast("请先完善个人资料",false);
                            return;
                        }

                        loadingdialog = UtilsDialog.createLoadingDialog(BankaddActivity.this,"认证中");
//                        Map<String ,String> map =new HashMap();
//                        map.put("data", "ICBC");
//                        showAuthenResult(true, map);
                        AppUtil.getIPAddress(BankaddActivity.this);
                        presenter.requestBankCardComfirmfuyou(bankAccount.getText().toString());
                    }
                });
            }


        }else {
            finishdialog=UtilsDialog.createFinishDialog(BankaddActivity.this, msg);

        }


    }

    @Override
    public void closedialog() {
        if(loadingdialog!=null) {
            loadingdialog.cancel();
        }
        if(finishdialog!=null){

            finishdialog.cancel();
        }
    }

    @Override
    public void showSuccess(String msg) {
        TYPE_DIALOG=1;
        showdialog(msg);
        UIUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                closedialog();
                if("bankinfo".equals(type)){

                    goToActivity(BankInformationActivity.class,bundle);
                }else if("authencenterForresult".equals(type)){
                    Bundle bundle = new Bundle();
                    bundle.putInt("resultCode",200);
                    goToActivity(AuthenCenterActivity.class,bundle);
                }else {
                    goToActivity(AuthenCenterActivity.class,null);
                }
            }
        },500);

    }

    @Override
    public void showFailure(String msg) {
        closedialog();
        UIUtils.toast(msg,false);
    }

    @Override
    public void connectFailure(String msg) {
        closedialog();
        UIUtils.toast(msg,false);
    }


}

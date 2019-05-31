package com.zhiyu.wallet.mvp.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.mvp.contract.UserInfoContract;
import com.zhiyu.wallet.mvp.presenter.UserInfoPresenter;
import com.zhiyu.wallet.util.AddViewUtil;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;
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
public class BankInformationActivity extends BaseActivity<UserInfoPresenter> implements UserInfoContract.UserInfoIView , UserInfoContract.BankinfoIView {

//    @BindView(R.id.et_zfb)
//    EditText zfbAccount;
   // @BindView(R.id.et_bankAccount)
//    TextView bankAccount;
//    @BindView(R.id.zfb_checkbox)
//    CheckBoxSample zfbCheckbox;
//    @BindView(R.id.bank_checkbox)
//    CheckBoxSample bankCheckbox;
    @BindView(R.id.ly_addbank)
    LinearLayout addbank;
    @BindView(R.id.ly_addTextview)
    LinearLayout addText;

    private List<Map<String,String>> banklist = new ArrayList<>();
    private CheckBoxSample[] checkBoxes = new CheckBoxSample[2];
    private Map<String, String> bankinfo = new HashMap<>();

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle =intent.getBundleExtra("data");
        if(bundle==null){
            return;
        }

        if(banklist.size()>=3){
            UIUtils.toast("银行卡上限为3张，如需删除或者修改请联系客服",false);
            return;
        }

        //判断是否存在
        for(int i =0 ; i<banklist.size();i++){
            if(bundle.getString("bankcardaccount").equals(banklist.get(i).get("bankcardaccount"))){
                UIUtils.toast("卡号已存在，请重新添加银行卡",false);
                return;
            }
        }

        Map<String ,String >bank = new HashMap<>();
        bank.put("bankcardaccount",bundle.getString("bankcardaccount"));
        bank.put("bankcardname",bundle.getString("bankcardname"));
        addbankText(bank);
        banklist.add(bank);
    }

    @Override
    protected void initData() {

        presenter.attachview(this);
        StatusbarUtil.setdropBarSystemWindows(this,Color.WHITE);
        presenter.queryBankinfor(this);

//        Map<String ,String >bank = new HashMap<>();
//        bank.put("bankcardaccount","6210 1111 1111 1111 111");
//        bank.put("bankcardname","银行");
//        addbankText(bank);
//        banklist.add(bank);
//        checkBoxes[0] = zfbCheckbox;
//        checkBoxes[1] = bankCheckbox;
//        checkBoxes[0].setOnClickListener(this);
//        checkBoxes[1].setOnClickListener(this);


    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bankinformation;
    }

    @Override
    protected UserInfoPresenter createPresenter() {
        return new UserInfoPresenter();
    }

    @OnClick(R.id.ly_back)
    public void back(View view) {
        //销毁当前的页面
        this.removeCurrentActivity();

    }

//    @OnClick(R.id.btn_confirm)
//    public void onConfirm() {
//
////        for (int i = 0; i < checkBoxes.length; i++) {
////            if (checkBoxes[i].isChecked()) {
////              break;
////            }
////        }
//
//    }
    @OnClick(R.id.ly_addbank)
    public void onaddbank(){

        if(TextUtils.isEmpty(User.getInstance().getIdcar())){
            UIUtils.toast("请先完善个人资料",false);
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("type","bankinfo");
        goToActivity(BankaddActivity.class,bundle);
    }

    public void addbankText(Map<String ,String > bank ){
        TextView tv_bankType = AddViewUtil.getTextViewBank(getApplicationContext(),20,20,0,0);
        TextView tv_bankAccount = AddViewUtil.getTextViewBank(getApplicationContext(),20,15,0,20);
        View view = new View(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,UIUtils.dp2px(1));
        layoutParams.setMargins(UIUtils.dp2px(20),0,20,0);
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(getResources().getColor(R.color.line_bg));
        tv_bankType.setText(bank.get("bankcardname"));

        String account = bank.get("bankcardaccount");

        String account1 = account.substring(0,4)+" **** **** **"+account.substring(17,account.length());
        tv_bankAccount.setText(account1);
        addText.addView(tv_bankType);
        addText.addView(tv_bankAccount);
        addText.addView(view);

    }


//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if (isChecked) {
//            for (int i = 0; i < checkBoxes.length; i++) {
//                if (checkBoxes[i] == buttonView) {
//                    checkBoxes[i].setChecked(true);
//                } else {
//                    checkBoxes[i].setChecked(false);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.zfb_checkbox:
//                if (!zfbCheckbox.isChecked()) {
//                    zfbCheckbox.setChecked(true);
//                    bankCheckbox.setChecked(false);
//                    zfbAccount.requestFocus();
//                }
//                break;
//            case R.id.bank_checkbox:
//                if (!bankCheckbox.isChecked()) {
//                    bankCheckbox.setChecked(true);
//                    zfbCheckbox.setChecked(false);
//                }
//                break;
//            default:
//                break;
//
//        }
//    }

    @Override
    public void showSuccess(String msg) {
        UIUtils.toast(msg,false);
        finish();
    }

    @Override
    public void showFailure(String msg) {
        UIUtils.toast(msg,false);
    }

    @Override
    public void connectFailure(String msg) {
        UIUtils.toast(msg,false);
    }

    @Override
    public void requestBankinfoCallBack(List<Map<String, String>> banks) {
        if (banks!=null && banks.size()!=0){

            banklist.addAll(banks);
            for(int i =0;i<banks.size();i++){
                addbankText(banks.get(i));
            }
        }

    }
}

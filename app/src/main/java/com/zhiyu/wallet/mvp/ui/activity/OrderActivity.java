package com.zhiyu.wallet.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Binder;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.ArraySet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.adapter.AccrualDetailAdapter;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.bean.LoanLimit;
import com.zhiyu.wallet.bean.Record;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.mvp.contract.OrderContract;
import com.zhiyu.wallet.mvp.presenter.OrderPresenter;
import com.zhiyu.wallet.util.AppUtil;
import com.zhiyu.wallet.util.BitmapUtils;
import com.zhiyu.wallet.util.Println;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;
import com.zhiyu.wallet.util.UtilsDialog;
import com.zhiyu.wallet.widget.CheckBoxSample;
import com.zhiyu.wallet.widget.LD_ActionSheet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @
 * Created by Administrator on 2018/11/29.
 */

public class OrderActivity extends BaseActivity<OrderPresenter> implements OrderContract.OrderIView{

    @BindView(R.id.tv_name)
    TextView tv_name ;
    @BindView(R.id.tv_loanAmount)
    TextView tv_loanAmount ;
    @BindView(R.id.tv_allcost)
    TextView tv_allcost ;
    @BindView(R.id.tv_showaccrual)
    TextView tv_accrual;
    @BindView(R.id.tv_transferAcc)
    TextView tv_transferAcc ;
    @BindView(R.id.tv_loanDate)
    TextView tv_loanDate ;
    @BindView(R.id.tv_loanTime)
    TextView tv_loanTime ;
    @BindView(R.id.tv_repayTime)
    TextView tv_repayTime ;
    @BindView(R.id.ly_purpose)
    LinearLayout ly_purpose;
    @BindView(R.id.tv_purpose)
    TextView tv_purpose ;
    @BindView(R.id.tv_transferCard)
    TextView tv_transferCard ;

    @BindView(R.id.cb_order_agree_userinfo)
    CheckBoxSample cb_order_agree_userinfo;
    @BindView(R.id.cb_order_agree_Credit)
    CheckBoxSample cb_order_agree_Credit;
    @BindView(R.id.cb_order_agree_acceeptrepay)
    CheckBoxSample cb_order_agree_acceeptrepay;
    @BindView(R.id.cb_order_agree_repaycontract)
    CheckBoxSample cb_order_agree_repaycontract;
    @BindView(R.id.tv_protocolacceeptrepay)
    TextView tv_protocolacceeptrepay;
    @BindView(R.id.tv_protocoluserinfo)
    TextView tv_protocoluserinfo;
    @BindView(R.id.tv_protocolCredit)
    TextView tv_protocolCredit;
    @BindView(R.id.tv_protocolrepaycontract)
    TextView tv_protocolrepaycontract;

    @BindView(R.id.btn_loancommit)
    Button btn_loancommit;
    @BindView(R.id.ly_transferCard)
    LinearLayout ly_transferCard;
    @BindView(R.id.sv_parent)
    ScrollView scrollView;
//    @BindView(R.id.iv_showscreen)
//    ImageView imageView;
    @BindView(R.id.ly_title)
    LinearLayout ly_title;

    Unbinder binder;
    TextView tvtitle;
    LinearLayout lyback;
    Dialog accdialog;
    Dialog carddialog;
    private String account="";
    private  String[] consumitem;
    private Dialog loadingdialog;
    private String gps;

    private List<Map<String,Object>> costlist = new ArrayList<>();
    private List<Map<String, String>> banks = new ArrayList<>();
    private List<LoanLimit.LoanLimitItem> loanLimitItems = new ArrayList<>();

    @Override
    protected void initData() {
        binder= ButterKnife.bind(this);
        presenter.attachview(this);
        StatusbarUtil.setdropBarSystemWindows(this,getResources().getColor(android.R.color.white));
        setData(getIntent().getBundleExtra("data"));
    }

    @Override
    protected void initTitle() {
        lyback=findViewById(R.id.iv_title_back);
        tvtitle=findViewById(R.id.tv_title);
        lyback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lyback.setVisibility(View.VISIBLE);
        tvtitle.setText("账单");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    protected OrderPresenter createPresenter() {
        return new OrderPresenter();
    }

    @OnClick({R.id.btn_loancommit})
    public void Onclick(){
        if(!cb_order_agree_userinfo.isChecked()){
            UIUtils.toast("请阅读"+getResources().getString(R.string.protocoluserinfo)+"并同意",false);
            return;
        }
        if(!cb_order_agree_Credit.isChecked()){
            UIUtils.toast("请阅读"+getResources().getString(R.string.protocolCreditInvestigation)+"并同意",false);
            return;
        }
        if(!cb_order_agree_repaycontract.isChecked()){
            UIUtils.toast("请阅读"+getResources().getString(R.string.protocolrepaycontract)+"并同意",false);
            return;
        }
        if(!cb_order_agree_acceeptrepay.isChecked()){
            UIUtils.toast("请阅读"+getResources().getString(R.string.protocolacceeptrepay)+"并同意",false);
            return;
        }
        loadingdialog= UtilsDialog.createLoadingDialog(this,"申请中...");


        String b1 =tv_loanAmount.getText().toString().replace("元","");
        String c2 = tv_allcost.getText().toString().replace("元","");
        String a3 = tv_transferAcc.getText().toString().replace("元","");
        String l4 = tv_loanDate.getText().toString().replace("元","");
        String b5 = tv_loanTime.getText().toString();
        String r6 = tv_repayTime.getText().toString();
        String p7 = tv_purpose.getText().toString();

        presenter.requestapplyOrder(b1,c2,a3,l4,b5,r6,p7,account, AppUtil.getChannelData(this,"UMENG_CHANNEL"),gps);

    }

    @SuppressLint("SetTextI18n")
    public void setData(Bundle bundle){
        if(bundle==null){
            return;
        }
        loanLimitItems = bundle.getParcelableArrayList("loanLimitItems");
        int position = bundle.getInt("position");
        gps = bundle.getString("gps");
        tv_protocolacceeptrepay.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_protocolCredit.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_protocolrepaycontract.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_protocoluserinfo.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        cb_order_agree_acceeptrepay.setChecked(true);
        cb_order_agree_repaycontract.setChecked(true);
        cb_order_agree_Credit.setChecked(true);
        cb_order_agree_userinfo.setChecked(true);


        Map<String,Object> map = new HashMap<>();
        map.put("costname", "利息费:");
        map.put("costmoney", loanLimitItems.get(position).getInterestfee());

        Map<String, Object> map1 = new HashMap<>();
        map1.put("costname", "风险管理费:");
        map1.put("costmoney", loanLimitItems.get(position).getRiskmanagementfees());

        Map<String, Object> map2 = new HashMap<>();
        map2.put("costname", "身份证认证费:");
        map2.put("costmoney", loanLimitItems.get(position).getIndentityauthfee());

        Map<String, Object> map3 = new HashMap<>();
        map3.put("costname", "手机验证费:");
        map3.put("costmoney", loanLimitItems.get(position).getPhoneverfree());

        Map<String, Object> map4 = new HashMap<>();
        map4.put("costname", "银行卡认证费:");
        map4.put("costmoney", loanLimitItems.get(position).getBankcardverfee());

        Map<String, Object> map5 = new HashMap<>();
        map5.put("costname", "樶合服务费:");
        map5.put("costmoney", loanLimitItems.get(position).getMathchupfee());

        Map<String, Object> map6 = new HashMap<>();
        map6.put("costname", "合计:");
        map6.put("costmoney", loanLimitItems.get(position).getServicecharge());
        costlist.add(map);
        costlist.add(map1);
        costlist.add(map2);
        costlist.add(map3);
        costlist.add(map4);
        costlist.add(map5);
        costlist.add(map6);

        User user = User.getInstance();
        String idcarnumber = user.getIdcar();
        tv_name.setText(user.getUsername()+"("+idcarnumber.replace(idcarnumber.substring(5, idcarnumber.length() - 2), "****")+")");

        tv_loanAmount.setText((int) loanLimitItems.get(position).getMoney()+"元");
        tv_allcost.setText((int)loanLimitItems.get(position).getLixifeiyong()+"元");
        tv_transferAcc.setText((int)loanLimitItems.get(position).getAccountamount()+"元");

        tv_loanDate.setText(loanLimitItems.get(position).getReserve2()+"天");
//        tv_loanDate.setText(-1+"天");
        tv_loanTime.setText(getToday());
        tv_repayTime.setText(getFeture(loanLimitItems.get(position).getReserve2()));
//        tv_repayTime.setText(getFeture(-1));
        consumitem= new String[]{"消费","旅游","教育","装修"};
        tv_purpose.setText(consumitem[0]);

        presenter.queryBankinfor();

    }

    @OnClick(R.id.tv_showaccrual)
    public void onaccrual(){

        accdialog= UtilsDialog.showCustomDialog(this, R.layout.dialog_accrualdetail, Gravity.CENTER_VERTICAL, true,false,new UtilsDialog.BindView() {
            @Override
            public void onBind(View rootView) {
                RecyclerView recyclerView = rootView.findViewById(R.id.rv_accrualdetail);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OrderActivity.this);
                AccrualDetailAdapter accrualDetailAdapter = new AccrualDetailAdapter(costlist);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(accrualDetailAdapter);
                TextView textView = rootView.findViewById(R.id.tv_accrualtitle);
                textView.setText("利息与费用");
                LinearLayout lyback = rootView.findViewById(R.id.ly_back);
                lyback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        accdialog.dismiss();
                    }
                });
            }
        });
    }

    @OnClick(R.id.ly_transferCard)
    public void showCardDialog(){
        carddialog = UtilsDialog.showCustomDialog(this, R.layout.dialog_bankcard, Gravity.CENTER_VERTICAL,true,false, new UtilsDialog.BindView() {
            @Override
            public void onBind(View rootView) {
                initRepayCardDialog(rootView);
            }
        });

    }

    public void initRepayCardDialog(View rootView){
        LinearLayout ly_back = rootView.findViewById(R.id.ly_back);
        LinearLayout ly_bankselect1 = rootView.findViewById(R.id.ly_bankselect1);
        final TextView tv_bankname1 = rootView.findViewById(R.id.tv_bankname1);
        TextView tv_bankcardNum1 = rootView.findViewById(R.id.tv_bankcardnum1);

        LinearLayout ly_bankselect2 = rootView.findViewById(R.id.ly_bankselect2);
        TextView tv_bankname2 = rootView.findViewById(R.id.tv_bankname2);
        TextView tv_bankcardNum2 = rootView.findViewById(R.id.tv_bankcardnum2);

        LinearLayout ly_bankselect3 = rootView.findViewById(R.id.ly_bankselect3);
        TextView tv_bankname3 = rootView.findViewById(R.id.tv_bankname3);
        TextView tv_bankcardNum3 = rootView.findViewById(R.id.tv_bankcardnum3);
        ly_bankselect1.setVisibility(View.GONE);
        ly_bankselect2.setVisibility(View.GONE);
        ly_bankselect3.setVisibility(View.GONE);

        String account1, account2,account3;

        if(banks.size()==1){
            String account = banks.get(0).get("bankcardaccount");
            account1 = account.substring(0,4)+" **** **** **"+account.substring(17,account.length());

            ly_bankselect1.setVisibility(View.VISIBLE);
            tv_bankname1.setText(banks.get(0).get("bankcardname"));
            tv_bankcardNum1.setText(account1);
        }
        if(banks.size()==2){
            String account11 = banks.get(0).get("bankcardaccount");
            account1 = account11.substring(0,4)+" **** **** **"+account11.substring(17,account11.length());

            ly_bankselect1.setVisibility(View.VISIBLE);
            tv_bankname1.setText(banks.get(0).get("bankcardname"));
            tv_bankcardNum1.setText(account1);


            String account22 = banks.get(1).get("bankcardaccount");
            account2 = account22.substring(0,4)+" **** **** **"+account22.substring(17,account22.length());

            ly_bankselect2.setVisibility(View.VISIBLE);
            tv_bankname2.setText(banks.get(1).get("bankcardname"));
            tv_bankcardNum2.setText(account2);
        }
        if(banks.size()==3){
            String account11 = banks.get(0).get("bankcardaccount");
            account1 = account11.substring(0,4)+" **** **** **"+account11.substring(17,account11.length());

            ly_bankselect1.setVisibility(View.VISIBLE);
            tv_bankname1.setText(banks.get(0).get("bankcardname"));
            tv_bankcardNum1.setText(account1);

            String account22 = banks.get(1).get("bankcardaccount");
            account2 = account22.substring(0,4)+" **** **** **"+account22.substring(17,account22.length());

            ly_bankselect2.setVisibility(View.VISIBLE);
            tv_bankname2.setText(banks.get(1).get("bankcardname"));
            tv_bankcardNum2.setText(account2);

            String account33 = banks.get(2).get("bankcardaccount");
            account3 = account33.substring(0,4)+" **** **** **"+account33.substring(17,account33.length());

            ly_bankselect3.setVisibility(View.VISIBLE);
            tv_bankname3.setText(banks.get(2).get("bankcardname"));
            tv_bankcardNum3.setText(account3);
        }

        ly_bankselect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRepayCard(0);
                carddialog.dismiss();
            }
        });

        ly_bankselect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRepayCard(1);
                carddialog.dismiss();
            }
        });

        ly_bankselect3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRepayCard(2);
                carddialog.dismiss();
            }
        });
        ly_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carddialog.dismiss();
            }
        });


    }

    @SuppressLint("SetTextI18n")
    public void initRepayCard(int position){
        account = banks.get(position).get("bankcardaccount");
        String account1 = account.substring(0,4)+" **** **** **"+account.substring(17,account.length());

        if(TextUtils.isEmpty(banks.get(position).get("bankcardname"))){

            tv_transferCard.setText(account1);
        }else {
            tv_transferCard.setText("("+banks.get(position).get("bankcardname")+")"+account1);

        }
    }


    @OnClick(R.id.ly_purpose)
    public void onPurpose(){
        LD_ActionSheet.showActionSheet(this, consumitem, "取消", new LD_ActionSheet.Builder.OnActionSheetselectListener() {
            @Override
            public void itemSelect(Dialog dialog, int index) {
                dialog.dismiss();
                if(0 <= index-1){
                    tv_purpose.setText(consumitem[index-1]);
                }
            }
        });
    }

    @OnClick({R.id.cb_order_agree_Credit,R.id.cb_order_agree_acceeptrepay,R.id.cb_order_agree_repaycontract,R.id.cb_order_agree_userinfo})
    public void onAgree(View view){
        switch (view.getId()){
            case R.id.cb_order_agree_acceeptrepay:
                cb_order_agree_acceeptrepay.setChecked(!cb_order_agree_acceeptrepay.isChecked());
                break;
            case R.id.cb_order_agree_Credit:
                cb_order_agree_Credit.setChecked(!cb_order_agree_Credit.isChecked());
                break;
            case R.id.cb_order_agree_repaycontract:
                cb_order_agree_repaycontract.setChecked(!cb_order_agree_repaycontract.isChecked());
                break;
            case R.id.cb_order_agree_userinfo:
                cb_order_agree_userinfo.setChecked(!cb_order_agree_userinfo.isChecked());
                break;


        }

    }

    @OnClick({R.id.tv_protocolrepaycontract,R.id.tv_protocolCredit,R.id.tv_protocolacceeptrepay,R.id.tv_protocoluserinfo})
    public void protocol(View view){
        Bundle bundle = new Bundle();
        switch (view.getId()){
            case R.id.tv_protocolrepaycontract:
                bundle.putString("type","repaycontract");break;
            case R.id.tv_protocolCredit:
                bundle.putString("type","credit");break;
            case R.id.tv_protocolacceeptrepay:
                bundle.putString("type","acceeptrepay");break;
            case R.id.tv_protocoluserinfo:
                bundle.putString("type","userinfo");break;
        }
        goToActivity(ProtocolActivity.class,bundle);

    }

    public String getToday(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        String result = simpleDateFormat.format(new Date());
        return result;
    }

    public String getFeture(int feture){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR)+feture);
        Date date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日",Locale.CHINA);
        String result = simpleDateFormat.format(date);
        return result;
    }

    @Override
    public void requestBankinfoCallBack(List<Map<String, String>> banks) {

        if(banks.size()==0){
            UIUtils.toast("请先添加银行卡",false);
            return;
        }
        this.banks = banks;

        initRepayCard(0);
    }

    @Override
    public void applyOrderSuccess() {
        if(loadingdialog!=null){
            loadingdialog.dismiss();
        }
         goToActivity(ApplySuccessActivity.class,null);
        finish();

    }

    @Override
    public void applyOrderfailure() {
        if(loadingdialog!=null){
            loadingdialog.dismiss();
        }
        UIUtils.toast("提交申请失败",false);
    }

    @Override
    public void saveOrderBitmapSuccess() {

        Bitmap bitmap1 = BitmapUtils.getScrollViewBitmap(scrollView);
        Bitmap bitmap = BitmapUtils.getLinearlayoutBitmap(ly_title);
        Bitmap bitmapMer = BitmapUtils.getMergeBitmap(bitmap,bitmap1);
        String filepath = BitmapUtils.saveCompressBitmap(this,"");
        BitmapUtils.compressImage(bitmapMer,filepath);

        presenter.saveOrderBitmap(filepath);
    }




}

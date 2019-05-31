package com.zhiyu.wallet.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.mvp.contract.ShortByStagesContract;
import com.zhiyu.wallet.mvp.presenter.ShortByStagesPresenter;
import com.zhiyu.wallet.util.StatusbarUtil;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * 创建者：Administrator
 * 时  间：2019/5/20.
 * 功  能：短期借款
 */

public class ShortByStagesActivity extends BaseActivity<ShortByStagesPresenter> implements ShortByStagesContract.ShortByStagesIView{
    @BindView(R.id.iv_title_back)
    LinearLayout ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    //选择借款金额TextView
    @BindView(R.id.tv_choosemoney)
    TextView TvChoosemoney;
    //借款金额TextView
    @BindView(R.id.tv_stagesmoney)
    TextView TvStagesmoney;
    //借款天数TextView
    @BindView(R.id.tv_stagesdata)
    TextView TvStagesdata;
    //到账金额TextView
    @BindView(R.id.tv_transferAcc)
    TextView TvTransferAcc;
    //到期应还TextView
    @BindView(R.id.tv_repayAmount)
    TextView TvRepayAmount;
    //服务费用TextView
    @BindView(R.id.tv_accrual)
    TextView TvAccrual;
    //服务费用TextView
    @BindView(R.id.tv_submitstage)
    TextView TvSubmitstage;

    //选择借款金额(元)
    String StrmoneySpinner;
    //还款期限(天)
    String StrdateSpinner;
    //还款金额(元)
    String StrtvRepayAmount;
    //利息与费用(元)
    String StrtvAccrual;
    //到账金额(元)
    String StrtvTransfer;
    //日费率
    String datarate;

    @Override
    public void showSuccess(String msg) {

    }

    @Override
    public void showFailure(String msg) {

    }

    @Override
    public void connectFailure(String msg) {

    }

    @Override
    protected void initData() {
        presenter.attachview(this);
        StatusbarUtil.setdropBarSystemWindows(this, Color.WHITE);
        tvTitle.setText("短期借款");
        ivTitleBack.setVisibility(View.VISIBLE);
        //选择借款金额(元)
        StrmoneySpinner = getIntent().getStringExtra("moneySpinner");
        //还款期限(天)
        StrdateSpinner = getIntent().getStringExtra("dateSpinner");
        //还款金额(元)
        StrtvRepayAmount = getIntent().getStringExtra("tvRepayAmount");
        //利息与费用(元)
        StrtvAccrual = getIntent().getStringExtra("tvAccrual");
        //到账金额(元)
        StrtvTransfer = getIntent().getStringExtra("tvTransfer");
        //日费率
        double dTransfer = Double.parseDouble(StrmoneySpinner);
        String strdatarate = getIntent().getStringExtra("datarate");
        datarate = strdatarate.replace("%","");
        double ddatarate = Double.parseDouble(datarate);


        //设置TextView的值
        TvChoosemoney.setText(StrmoneySpinner);
        TvStagesdata.setText(StrdateSpinner+"天");
        TvStagesmoney.setText(StrmoneySpinner+"元");
        TvTransferAcc.setText(StrtvTransfer+"元");
        TvRepayAmount.setText(dTransfer*(1+ddatarate/100)+"元");
        TvAccrual.setText(StrtvAccrual+"元");
    }

    @Override
    protected void initTitle() {
    }
    @OnClick(R.id.iv_title_back)
    public void back(View view) {
        //销毁当前的页面
        this.removeCurrentActivity();

    }
    @OnClick(R.id.tv_submitstage)
    public void showpwd(View view){
        switch (view.getId()){
            case R.id.tv_submitstage:
                JSONObject jsonObject = new JSONObject();
                try {
                    //姓名
                    jsonObject.put("name", User.getInstance().getUsername());
                    //身份证
                    jsonObject.put("pid", User.getInstance().getIdcar());
                    //手机号码
                    jsonObject.put("mobile", User.getInstance().getPhone());
                    //贷款类型，默认为2
                    jsonObject.put("loanType", "2");
                    //借款金额
                    jsonObject.put("loanAmount",StrmoneySpinner);
                    //APP标识,默认为1
                    jsonObject.put("idfv", "1");
                    //综合费用
                    jsonObject.put( "comprehensivemoney", StrtvAccrual);
                    //到账金额
                    jsonObject.put("arrivalmoney", StrtvTransfer);
                    //贷款天数
                    jsonObject.put("lifeofloan", StrdateSpinner+"天");
                    //卡号
                    jsonObject.put("creditcard",User.getInstance().getContacts());
                    //还款日期
                    jsonObject.put("repaymentdate", "2019年05月08日");
                    //状态码
                    jsonObject.put("status","1");

                }catch (Exception e){
                    e.printStackTrace();
                }
                presenter.apply4day(jsonObject);
                break;
        }

    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_shortbystage;
    }

    @Override
    protected ShortByStagesPresenter createPresenter() {
        return new ShortByStagesPresenter();
    }
}

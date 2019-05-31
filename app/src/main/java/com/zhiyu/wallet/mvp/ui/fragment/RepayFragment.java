package com.zhiyu.wallet.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhiyu.wallet.R;
import com.zhiyu.wallet.adapter.AccrualDetailAdapter;
import com.zhiyu.wallet.adapter.RecordAdapter;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.base.BaseFragment;
import com.zhiyu.wallet.bean.Record;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.mvp.contract.RepayContract;
import com.zhiyu.wallet.mvp.presenter.HomePresenter;
import com.zhiyu.wallet.mvp.presenter.RepayPresenter;
import com.zhiyu.wallet.mvp.ui.activity.LoginActivity;
import com.zhiyu.wallet.mvp.ui.activity.RepaySuccessActivity;
import com.zhiyu.wallet.util.AppUtil;
import com.zhiyu.wallet.util.Println;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;
import com.zhiyu.wallet.util.UtilsDialog;
import com.zhiyu.wallet.widget.CheckBoxSample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @ Created by Administrator on 2018/11/5.
 */

public class RepayFragment extends BaseFragment<RepayPresenter> implements View.OnClickListener, RepayContract.RepayIView {

    Unbinder unbinder;
    // @BindView(R.id.refreshLayout)
    // SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.btn_time)
    Button btn_time;
    @BindView(R.id.btn_status)
    Button btn_status;
    @BindView(R.id.btn_repay)
    Button repay;
    @BindView(R.id.tv_repayAmount)
    TextView repayAmount;
    @BindView(R.id.tv_repaymount)
    TextView repaymount;
    @BindView(R.id.tv_latesrepaybill)
    TextView latesrepaybill;
    @BindView(R.id.tv_latesrepaytime)
    TextView latesrepaytime;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv_connectfailure)
    RelativeLayout connectfailurelayout;
    @BindView(R.id.tv_lateTime)
    TextView lateTime;
    @BindView(R.id.ly_title)
    LinearLayout ly_title;
    @BindView(R.id.iv_repaybg)
    ImageView iv_repaybg;
    @BindView(R.id.ly_finedetail)
    LinearLayout ly_finedetail;

    TextView tvtitile;
    private List<Record> list = new ArrayList<>();

    private LinearLayout ly_back, ly_edit, ly_select, ly_repayverify, ly_repayAmount, ly_repayCard, ly_return;
    private RelativeLayout rl_repaytitle, rl_verifytitle;
    private EditText et_repayAmount, et_verifycode;
    private TextView tv_repaycard;
    private Button btnRepay;
    public  Dialog repaydialog,findialog;
    private Dialog loadingdialog, paysuccessdialog;
    private Dialog selectCard;
    public  boolean isVerifySMS = false;
    private String account,accounttype;
    public static  boolean isOverdue = false;

    private List<Map<String ,Object>>costlist = new ArrayList<>();
    private RecordAdapter recordAdapter;
    private int type_Time = 0;
    private int type_Status = 1;
    private List<Map<String, String>> banks = new ArrayList<>();
    private User user = User.getInstance();
    private String requestno;


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();
            refreshLayout.finishRefresh();

        } else {  // 在最前端显示 相当于调用了onResume();
            user= User.getInstance();
            if (!user.isLogin()) {
                repayAmount.setText("0");
                repaymount.setText("0");
                latesrepaybill.setText("0");
                latesrepaytime.setText("- -");
                recordAdapter.lists = new ArrayList<>();
                list = new ArrayList<>();
                recordAdapter.notifyDataSetChanged();
                iv_repaybg.setImageDrawable(getResources().getDrawable(R.mipmap.repay_back));
                ly_title.setBackgroundColor(getResources().getColor(R.color.yellowtitle));
                lateTime.setVisibility(View.INVISIBLE);
            }
            if (RepayFragment.isOverdue) {
                StatusbarUtil.setdropBarSystemWindows(getActivity(), getResources().getColor(R.color.redtext));
            } else {
                StatusbarUtil.setdropBarSystemWindows(getActivity(), getResources().getColor(R.color.yellowtitle));
            }
//            else {
//                presenter.requestRepayhistoryRecord();
//            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        user = User.getInstance();
        Println.out("ishidden",isHidden()+"");
        if(user.isLogin()){
            presenter.requestRepayhistoryRecord();
        }else {
            list = new ArrayList<>();
            if(!isHidden()) {
                if (RepayFragment.isOverdue) {
                    StatusbarUtil.setdropBarSystemWindows(getActivity(), getResources().getColor(R.color.redtext));
                } else {
                    StatusbarUtil.setdropBarSystemWindows(getActivity(), getResources().getColor(R.color.yellowtitle));
                }
            }
        }


    }

    @Override
    protected RepayPresenter createPresenter() {
        return new RepayPresenter();
    }

    @Override
    protected void initData() {

        unbinder = ButterKnife.bind(this, getView());
        //list=initlist();
        btn_time.setOnClickListener(this);
        btn_status.setOnClickListener(this);
        btn_time.setSelected(true);
        btn_status.setSelected(false);
        repay.setOnClickListener(this);
        recordAdapter = new RecordAdapter(list);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                user = User.getInstance();

                if (user.isLogin()) {
                    presenter.requestRepayhistoryRecord();
                } else {
                    refreshLayout.finishRefresh();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initTitle() {
        tvtitile = getView().findViewById(R.id.tv_title);
        tvtitile.setText("还款");
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_repay;
    }

    @Override
    public void iniRefresh() {

    }

    @Override
    public void finishRefresh(View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_time:
                btn_time.setSelected(true);
                btn_status.setSelected(false);
                btn_status.setTextColor(Color.parseColor("#ffffff"));
                btn_time.setTextColor(getResources().getColor(R.color.tvhint));
                presenter.recordSortwithType(type_Time, recordAdapter, list);
                break;
            //game.setTextColor(0xff5f15);;

            case R.id.btn_status:
                btn_status.setSelected(true);
                btn_time.setSelected(false);
                btn_status.setTextColor(getResources().getColor(R.color.tvhint));
                btn_time.setTextColor(Color.parseColor("#ffffff"));
                presenter.recordSortwithType(type_Status, recordAdapter, list);
                break;
            case R.id.btn_repay:
//                repaydialog = UtilsDialog.showCustomDialog(getActivity(), R.layout.dialog_repay, Gravity.BOTTOM,false, new UtilsDialog.BindView() {
//                    @Override
//                    public void onBind(View rootView) {
//                        initRepayView(rootView);
//                    }
//                });
                User user = User.getInstance();
                if (user.isLogin()) {
                    if (0 >= Double.parseDouble(repayAmount.getText().toString())) {
                        UIUtils.toast("没有需要还款的记录", false);
                        return;
                    }
                    presenter.requestQueryBankinfo();
                } else {
                    ((BaseActivity) this.getActivity()).goToActivity(LoginActivity.class, null);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }




    @Override
    public void requestBankinfoCallBack(List<Map<String, String>> banks) {
        if (banks.size() == 0) {
            UIUtils.toast("请先添加银行卡", false);
            return;
        }
        this.banks = banks;
        repaydialog = UtilsDialog.showCustomDialog(getActivity(), R.layout.dialog_repay, Gravity.BOTTOM, false,false, new UtilsDialog.BindView() {
            @Override
            public void onBind(View rootView) {
                initRepayView(rootView);
            }
        });

    }

    //no use
    @Override
    public void repaysendSMS(String identityid) {

        presenter.requestrepay(et_repayAmount.getText().toString(), account,accounttype,identityid);
    }

    @Override
    public void repaySuccessSendSMS(String requestno) {
        if (loadingdialog != null) {
            loadingdialog.dismiss();
        }

        TranslateAnimation showAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        showAnim.setDuration(300);
        ly_repayverify.startAnimation(showAnim);
        ly_repayverify.setVisibility(View.VISIBLE);
        rl_verifytitle.setAnimation(showAnim);
        rl_verifytitle.setVisibility(View.VISIBLE);

        TranslateAnimation hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        hideAnim.setDuration(300);

        rl_repaytitle.startAnimation(hideAnim);
        ly_repayAmount.startAnimation(hideAnim);
        ly_repayCard.startAnimation(hideAnim);

        rl_repaytitle.setVisibility(View.GONE);
        ly_repayAmount.setVisibility(View.GONE);
        ly_repayCard.setVisibility(View.INVISIBLE);
        isVerifySMS = true;
        this.requestno=requestno;
        UIUtils.toast("短信发送成功,请输入短信验证码",true);

    }

    @Override
    public void repaysuccess() {
        if (loadingdialog != null) {
            loadingdialog.dismiss();
        }
        if(repaydialog != null){
            repaydialog.dismiss();
        }
        Bundle bundle =new Bundle();
        bundle.putString("repayAmount",et_repayAmount.getText().toString());
        ((BaseActivity)this.getActivity()).goToActivity(RepaySuccessActivity.class,bundle);
    }

    @Override
    public void repayfailure(String msg) {
        if (loadingdialog != null) {
            loadingdialog.dismiss();
        }
        UIUtils.toast(msg, false);
    }

    @Override
    public void repayOverdue(String latetime) {

        if("0".equals(latetime)){
            iv_repaybg.setImageDrawable(getResources().getDrawable(R.mipmap.repay_back));
            ly_title.setBackgroundColor(getResources().getColor(R.color.yellowtitle));
            lateTime.setVisibility(View.INVISIBLE);
            if(!isHidden()){
                StatusbarUtil.setdropBarSystemWindows(getActivity(),getResources().getColor(R.color.yellowtitle));
            }
            isOverdue=false;
            ly_finedetail.setClickable(false);
        }else {
            isOverdue=true;
            if(!isHidden()){
                StatusbarUtil.setdropBarSystemWindows(getActivity(), Color.RED);
            }
            iv_repaybg.setImageDrawable(getResources().getDrawable(R.color.redtext));
            ly_title.setBackgroundColor(getResources().getColor(R.color.redtext));
            lateTime.setVisibility(View.VISIBLE);
            lateTime.setText("已逾期"+latetime+"天");
            ly_finedetail.setClickable(true);
            ly_finedetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    findialog = UtilsDialog.showCustomDialog(getActivity(), R.layout.dialog_accrualdetail, Gravity.CENTER_VERTICAL, true,false,new UtilsDialog.BindView() {
                        @Override
                        public void onBind(View rootView) {
                            RecyclerView recyclerView = rootView.findViewById(R.id.rv_accrualdetail);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            AccrualDetailAdapter accrualDetailAdapter = new AccrualDetailAdapter(costlist);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(accrualDetailAdapter);
                            TextView textView = rootView.findViewById(R.id.tv_accrualtitle);
                            textView.setText("罚金详情");
                            LinearLayout lyback = rootView.findViewById(R.id.ly_back);
                            lyback.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    findialog.dismiss();
                                }
                            });
                        }
                    });

                }
            });
        }

    }

    public void initRepayView(final View rootView) {
        et_verifycode = rootView.findViewById(R.id.et_repayverifycode);
        ly_return = rootView.findViewById(R.id.ly_return);
        rl_repaytitle = rootView.findViewById(R.id.rl_repaytitle);
        rl_verifytitle = rootView.findViewById(R.id.rl_verifytitle);
        ly_repayAmount = rootView.findViewById(R.id.ly_repayAmount);
        ly_repayCard = rootView.findViewById(R.id.ly_repaycard);
        ly_repayverify = rootView.findViewById(R.id.ly_repayverify);
        ly_back = rootView.findViewById(R.id.ly_back);
        et_repayAmount = rootView.findViewById(R.id.et_repayamount);
        ly_edit = rootView.findViewById(R.id.ly_editrepayAmount);
        ly_select = rootView.findViewById(R.id.ly_selectCard);
        btnRepay = rootView.findViewById(R.id.btn_confirm);
        tv_repaycard = rootView.findViewById(R.id.tv_repayCard);
        btnRepay.setSelected(false);

        initRepayCard(0);

        et_repayAmount.setText(repayAmount.getText());
        ly_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(et_repayAmount.getWindowToken(), 0);
                repaydialog.dismiss();
                repaydialog=null;
                isVerifySMS=false;
            }
        });

        ly_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_repayAmount.setEnabled(true);
                InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                //inputManager.showSoftInputFromInputMethod(et_repayAmount.getWindowToken(), 0);
                inputManager.showSoftInput(et_repayAmount, InputMethodManager.SHOW_IMPLICIT);
                et_repayAmount.setSelection(et_repayAmount.getText().toString().length());
            }
        });

        ly_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCard = UtilsDialog.showCustomDialog(getContext(), R.layout.dialog_bankcard, Gravity.CENTER_VERTICAL, true,false, new UtilsDialog.BindView() {
                    @Override
                    public void onBind(View rootView) {
                        initRepayCardDialog(rootView);
                    }
                });
            }
        });

        btnRepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVerifySMS) {
                    if (!TextUtils.isEmpty(et_verifycode.getText().toString())) {
                        loadingdialog = UtilsDialog.createLoadingDialog(getActivity(), "还款中");
                       // presenter.requestrepaySMS(et_verifycode.getText().toString(),requestno);
                    }
                } else {
                    loadingdialog = UtilsDialog.createLoadingDialog(getActivity(), "还款中");
                   // presenter.requestQueryBankCardNO(account);
                    AppUtil.getIPAddress(getContext());
                    presenter.requestqueryRepayssn(account,et_repayAmount.getText().toString());
                }

            }
        });

        ly_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UtilsDialog.showDialogForListener("", "确定取消支付吗？", getContext(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        TranslateAnimation hideAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, 1.0f,
                                Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.0f);
                        hideAnim.setDuration(300);

                        ly_repayverify.setAnimation(hideAnim);
                        rl_verifytitle.setAnimation(hideAnim);
                        ly_repayverify.setVisibility(View.GONE);
                        rl_verifytitle.setVisibility(View.GONE);

                        TranslateAnimation showAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                                Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.0f,
                                Animation.RELATIVE_TO_SELF, 0.0f);
                        showAnim.setDuration(300);

                        rl_repaytitle.setVisibility(View.VISIBLE);
                        ly_repayAmount.setVisibility(View.VISIBLE);
                        ly_repayCard.setVisibility(View.VISIBLE);

                        rl_repaytitle.setAnimation(showAnim);
                        ly_repayAmount.setAnimation(showAnim);
                        ly_repayCard.setAnimation(showAnim);
                        isVerifySMS=false;
                    }
                });

            }
        });
    }

    public void initRepayCardDialog(View rootView) {
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

        String account1, account2, account3;
        if (banks.size() == 1) {
            String account = banks.get(0).get("bankcardaccount");
            account1 = account.substring(0, 4) + " **** **** **" + account.substring(17, account.length());

            ly_bankselect1.setVisibility(View.VISIBLE);
            tv_bankname1.setText(banks.get(0).get("bankcardname"));
            tv_bankcardNum1.setText(account1);
        }
        if (banks.size() == 2) {
            String account11 = banks.get(0).get("bankcardaccount");
            account1 = account11.substring(0, 4) + " **** **** **" + account11.substring(17, account11.length());

            ly_bankselect1.setVisibility(View.VISIBLE);
            tv_bankname1.setText(banks.get(0).get("bankcardname"));
            tv_bankcardNum1.setText(account1);


            String account22 = banks.get(1).get("bankcardaccount");
            account2 = account22.substring(0, 4) + " **** **** **" + account22.substring(17, account22.length());

            ly_bankselect2.setVisibility(View.VISIBLE);
            tv_bankname2.setText(banks.get(1).get("bankcardname"));
            tv_bankcardNum2.setText(account2);
        }
        if (banks.size() == 3) {
            String account11 = banks.get(0).get("bankcardaccount");
            account1 = account11.substring(0, 4) + " **** **** **" + account11.substring(17, account11.length());

            ly_bankselect1.setVisibility(View.VISIBLE);
            tv_bankname1.setText(banks.get(0).get("bankcardname"));
            tv_bankcardNum1.setText(account1);

            String account22 = banks.get(1).get("bankcardaccount");
            account2 = account22.substring(0, 4) + " **** **** **" + account22.substring(17, account22.length());

            ly_bankselect2.setVisibility(View.VISIBLE);
            tv_bankname2.setText(banks.get(1).get("bankcardname"));
            tv_bankcardNum2.setText(account2);

            String account33 = banks.get(2).get("bankcardaccount");
            account3 = account33.substring(0, 4) + " **** **** **" + account33.substring(17, account33.length());

            ly_bankselect3.setVisibility(View.VISIBLE);
            tv_bankname3.setText(banks.get(2).get("bankcardname"));
            tv_bankcardNum3.setText(account3);
        }

        ly_bankselect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRepayCard(0);
                selectCard.dismiss();
            }
        });

        ly_bankselect2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRepayCard(1);
                selectCard.dismiss();
            }
        });

        ly_bankselect3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initRepayCard(2);
                selectCard.dismiss();
            }
        });
        ly_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCard.dismiss();
            }
        });


    }

    @SuppressLint("SetTextI18n")
    public void initRepayCard(int position) {
        account = banks.get(position).get("bankcardaccount");
        accounttype = banks.get(position).get("bankcardname");
        String account1 = account.substring(0, 4) + " **** **** **" + account.substring(17, account.length());
        if(TextUtils.isEmpty(accounttype)){
            tv_repaycard.setText(  account1);
        }else {
            tv_repaycard.setText("(" + banks.get(position).get("bankcardname") + ")" + account1);
        }
    }

    @Override
    public void repayRecordListCallback(List<Record> recordList) {
        list = recordList;
        recordAdapter.lists = list;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recordAdapter);
        refreshLayout.finishRefresh();
        btn_time.setSelected(true);
        btn_status.setSelected(false);
        btn_status.setTextColor(Color.parseColor("#ffffff"));
        btn_time.setTextColor(getResources().getColor(R.color.tvhint));


    }

    @Override
    public void repayRecordCallback(String repaidmount, int repaidretio, String latestpaymenttime, String latesbill,String latetime) {
        int irepayAmount= (int)Double.parseDouble(repaidmount);
        repayAmount.setText(irepayAmount+"");
        repaymount.setText(repaidretio + "");
        latesrepaybill.setText(latesbill);
        latesrepaytime.setText(latestpaymenttime);
        refreshLayout.finishRefresh();
        repayOverdue(latetime);

    }

    @Override
    public void repayfinedetail(List<Map<String, Object>> mapList) {
        costlist = mapList;
    }

    @Override
    public void connectResult(boolean success, String msg) {
        if (success) {
            connectfailurelayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            refreshLayout.finishRefresh();
            connectfailurelayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            isOverdue=false;
        }
    }



}

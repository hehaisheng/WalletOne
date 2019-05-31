package com.zhiyu.wallet.mvp.ui.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.mvp.presenter.BasePresenter;
import com.zhiyu.wallet.mvp.ui.fragment.HomeFragment;
import com.zhiyu.wallet.mvp.ui.fragment.MyFragment;
import com.zhiyu.wallet.mvp.ui.fragment.RepayFragment;
import com.zhiyu.wallet.util.Println;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {

    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.iv_main_home)
    ImageView ivMainHome;
    @BindView(R.id.tv_main_home)
    TextView tvMainHome;
    @BindView(R.id.ll_main_home)
    LinearLayout llMainHome;
    @BindView(R.id.iv_main_invest)
    ImageView ivMainInvest;
    @BindView(R.id.tv_main_invest)
    TextView tvMainInvest;
    @BindView(R.id.ll_main_invest)
    LinearLayout llMainInvest;
    @BindView(R.id.iv_main_me)
    ImageView ivMainMe;
    @BindView(R.id.tv_main_me)
    TextView tvMainMe;
    @BindView(R.id.ll_main_me)
    LinearLayout llMainMe;


    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private HomeFragment homeFragment;
    private RepayFragment repayFragment;
    private MyFragment myFragment;
    private int showStatus =0;
    private boolean flag = true;
    private final int WHAT_RESET_BACK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState !=null){
            fragmentManager= getSupportFragmentManager();
            // System.out.println("savedInstanceState  "+savedInstanceState.getInt("showStatus"));
            homeFragment = (HomeFragment) fragmentManager.getFragment(savedInstanceState,"homeFragment");

            repayFragment = (RepayFragment) fragmentManager.getFragment(savedInstanceState,"repayFragment");

            myFragment = (MyFragment) fragmentManager.getFragment(savedInstanceState,"myFragment");
            setSelect(savedInstanceState.getInt("showStatus"));
//            System.out.println(homeFragment);
//            System.out.println(investFragment);
//            System.out.println(myPropertyfragmentFragment);
        }else {
            setSelect(0);
        }

    }

    @Override
    protected void initData() {
        requestPermissions();
    }
    @Override
    protected void initTitle() {

    }


    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putInt("showStatus",showStatus);
        if(fragmentManager==null){
            fragmentManager = getSupportFragmentManager();
        }
        if(homeFragment!=null){
            fragmentManager.putFragment(bundle,"homeFragment",homeFragment);
        }
        if(repayFragment!=null){
            fragmentManager.putFragment(bundle,"repayFragment",repayFragment);
        }
        if(myFragment !=null){
            fragmentManager.putFragment(bundle,"myFragment", myFragment);
        }

    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @OnClick({R.id.ll_main_home, R.id.ll_main_invest, R.id.ll_main_me})
    public void showTab(View view) {

        switch (view.getId()) {
            case R.id.ll_main_home://首页
                setSelect(0);
                break;
            case R.id.ll_main_invest://还款
                setSelect(1);
                break;
            case R.id.ll_main_me://我的
                setSelect(2);
                break;
        }

    }

    public void setSelect(int i) {
        fragmentManager = this.getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        //隐藏Fragment
        hideFragments();
        //重置ImageView和TextView的显示状态
        resetTab();
        showStatus=i;
        switch (i) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = HomeFragment.getInstance();//创建对象以后，并不会马上调用生命周期方法。而是在commit()之后，方才调用
                    transaction.add(R.id.fl_main, homeFragment);
                    // System.out.println("homeFragment");

                }
                //显示当前的fragment
                transaction.show(homeFragment);
                //改变选中项的图片和文本颜色的变化
                ivMainHome.setSelected(true);
                tvMainHome.setTextColor(getResources().getColor(R.color.yellow));
                StatusbarUtil.setdropBarSystemWindows(this, Color.WHITE);

                break;
            case 1:
//                if (investFragment == null) {
//                    investFragment = new InvestFragment();
//                    transaction.add(R.id.fl_main, investFragment);
//                    //System.out.println("investFragment");
//                }
                if(repayFragment==null){
                    repayFragment= new RepayFragment();
                    transaction.add(R.id.fl_main, repayFragment);
                }
                ivMainInvest.setSelected(true);
                transaction.show(repayFragment);
                tvMainInvest.setTextColor(UIUtils.getColor(R.color.yellow));

                break;
            case 2:
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    transaction.add(R.id.fl_main, myFragment);
                }
                ivMainMe.setSelected(true);
                transaction.show(myFragment);
                tvMainMe.setTextColor(UIUtils.getColor(R.color.yellow));
                StatusbarUtil.setdropBarSystemWindows(this,getResources().getColor(R.color.yellowtitle));

                break;

        }
        transaction.commit();
    }

    public void resetTab() {
        ivMainHome.setSelected(false);
        ivMainInvest.setSelected(false);
        ivMainMe.setSelected(false);

        tvMainHome.setTextColor(UIUtils.getColor(R.color.unselect));
        tvMainInvest.setTextColor(UIUtils.getColor(R.color.unselect));
        tvMainMe.setTextColor(UIUtils.getColor(R.color.unselect));
    }





    public void hideFragments() {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (repayFragment != null) {
            transaction.hide(repayFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }

    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_RESET_BACK:
                    flag = true;//复原
                    break;
            }
        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && flag) {

            Toast.makeText(MainActivity.this, "再点击一次，退出当前应用", Toast.LENGTH_SHORT).show();
            flag = false;
            handler.sendEmptyMessageDelayed(WHAT_RESET_BACK, 2000);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

package com.zhiyu.wallet.mvp.ui.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhiyu.wallet.R;
import com.zhiyu.wallet.adapter.AccrualDetailAdapter;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.base.BaseFragment;
import com.zhiyu.wallet.bean.LoanLimit;
import com.zhiyu.wallet.bean.Record;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.common.ActivityManager;
import com.zhiyu.wallet.common.App;
import com.zhiyu.wallet.mvp.contract.HomeContract;
import com.zhiyu.wallet.mvp.presenter.HomePresenter;
import com.zhiyu.wallet.mvp.ui.activity.BannerItemActivity;
import com.zhiyu.wallet.mvp.ui.activity.LoginActivity;
import com.zhiyu.wallet.mvp.ui.activity.OrderActivity;
import com.zhiyu.wallet.mvp.ui.activity.ShortByStagesActivity;
import com.zhiyu.wallet.mvp.ui.activity.UserInfoActivity;
import com.zhiyu.wallet.util.AppUtil;
import com.zhiyu.wallet.util.Println;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;
import com.zhiyu.wallet.util.UtilsDialog;

import com.zhiyu.wallet.widget.DashboardView2;
import com.zhiyu.wallet.widget.LooperTextView;
import com.zhiyu.wallet.widget.RoundIndicatorView;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

/**
 * @
 * Created by Administrator on 2018/11/5.
 */

public class HomeFragment extends BaseFragment<HomePresenter> implements HomeContract.HomeIView {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.looperview)
    LooperTextView looperTextView;
    @BindView(R.id.tv_repayAmount)
    TextView tvRepayAmount;
    @BindView(R.id.tv_accrual)
    TextView tvAccrual;
    @BindView(R.id.tv_transferAcc)
    TextView tvTransfer;
    @BindView(R.id.tv_showaccrual)
    TextView tvShowAccrual;
    @BindView(R.id.tv_monthRate)
    TextView tvMonthRate;
    @BindView(R.id.btn_apply)
    Button apply;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.dbv2)
    DashboardView2 dashboardView;
    @BindView(R.id.spinner)
    Spinner moneySpinner;
    @BindView(R.id.spinner_loanDate)
    Spinner dateSpinner;
    @BindView(R.id.tv_oneself)
    LinearLayout tv_oneself;


    String datarate;
    // @BindView(R.id.tv_title)
    TextView tvTitle;
    // @BindView(R.id.image_position)
    ImageView image_position;
    //@BindView(R.id.text_position)
    TextView text_position;

    Dialog cumdialog;
    Dialog notdialog;
    Dialog loadingdialog;

    private String ChannelId = "", imei = "", gps = "", simtelnum = "";
    private List<String> imagesUrl = new ArrayList<>();
    private boolean isIsinitlooptext = true;
    private Unbinder unbinder;
    private String[] mmoneyText = new String[]{"","",""};
    private int[] mmoney = new int[5];
    private int[] itemindex;
    private List<LoanLimit.LoanLimitItem> loanLimitItems;
    private List<Map<String, Object>> costlist = new ArrayList<>();
    private Bundle bundle = new Bundle();
    private static HomeFragment homeFragment ;


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @Override
    protected void initData() {
        unbinder = ButterKnife.bind(this, getView());
        presenter.attachview(this);
        presenter.homeBannerPictureRequest();
        presenter.homeNoticeRequest();
        presenter.requestLoanLimit();
        presenter.requestNoticeDialogContent();
        getGPS();

//      initloanMoney(null);  下拉刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.homeBannerPictureRequest();
                presenter.homeNoticeRequest();
                presenter.requestLoanLimit();
                isIsinitlooptext = false;
            }
        });

    }

    @Override
    protected void initTitle() {
        image_position = getView().findViewById(R.id.image_position);
        text_position = getView().findViewById(R.id.text_position);
//        tvTitle = getView().findViewById(R.id.tv_title);
//        text_position.setVisibility(View.VISIBLE);
//        image_position.setVisibility(View.VISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void iniRefresh() {

    }

    @Override
    public void finishRefresh(View view) {

    }

    /*
     初始化Banner
     */
    @Override
    public void initbanner(List<String> images) {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片地址构成的集合
        imagesUrl = new ArrayList<>();
        if (images.size() != 0) {
            imagesUrl.addAll(images);
        } else {
            imagesUrl.add("");
        }
        banner.setImages(imagesUrl);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        //  String[] titles = new String[]{"人脉总动员", "想不到你是这样的app", "购物节，爱不单行", "最帅的男人"};
        //  banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2300);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getActivity(), BannerItemActivity.class);
                if(position==1){
                    bundle.putString("type","易借服务");
                    intent.putExtra("data",bundle);
                    startActivity(intent);
                }else if(position==0){
                    bundle.putString("type","仲裁名单");
                    intent.putExtra("data",bundle);
                    startActivity(intent);
                }
            }
        });

        //banner设置方法全部调用完毕时最后调用
        banner.start();
        refreshLayout.finishRefresh();
        imagesUrl = null;
    }

    /*
     初始化loopview
     */
    @Override
    public void inittextloopview(List<String> looptexts) {
        if (isIsinitlooptext) {
            looperTextView.setTipList(looptexts);
            isIsinitlooptext = false;
        } else {
            looperTextView.setFirstinit(false);
            looperTextView.setTipList(looptexts);
        }
        refreshLayout.finishRefresh();
    }

    /*
     刻度盘数据排序
   */
    @Override
    public void initloanMoney(LoanLimit loanLimit) {
        loanLimitItems = loanLimit.getList();
        if (loanLimitItems.size() == 0) {
            initDashboard(new double[0]);
            return;
        }
        double[] moneyfloat = new double[loanLimitItems.size()];

        for (int i = 0; i < loanLimitItems.size(); i++) {
            moneyfloat[i] = loanLimitItems.get(i).getMoney();

        }
//        float []moneyfloat= new float[]{3000,1000,10000,5000,20000,50000,100000};
        initDashboard(selectSort(moneyfloat));

    }
     /*
        初始化刻度盘
      */
    @Override
    public void initDashboard(double[] moneySelect) {
//        dashboardView.setArcColor(getResources().getColor(R.color.yellowdraw));
//        dashboardView.setProgressColor(getResources().getColor(R.color.yellowtitle));
//        dashboardView.setArcAngle(175, 190);
//        if (moneySelect.length == 0) {
//            dashboardView.setValue(0, false, false);
//            return;
//        }
        String[] CALIBRATION_NUMBER = new String[moneySelect.length]; //moneyText
        for (int i = 0; i < moneySelect.length; i++) {
            CALIBRATION_NUMBER[i] = (int) moneySelect[i] + "";
        }

//        mmoneyText[0] = "";
//        if (moneySelect[moneySelect.length - 1] / 2 >= 10000) {
//            mmoneyText[1] = (int) moneySelect[moneySelect.length - 1] / 20000 + "万";
//        } else {
//            mmoneyText[1] = (int) moneySelect[moneySelect.length - 1] / 2 + "";
//        }
//
//        if (moneySelect[moneySelect.length - 1] >= 10000) {
//            mmoneyText[2] = (int) moneySelect[moneySelect.length - 1] / 10000 + "万";
//        } else {
//            mmoneyText[2] = (int) moneySelect[moneySelect.length - 1] + "";
//        }
        mmoney[0] = 0;
        mmoney[4] = (int) moneySelect[moneySelect.length - 1];
        mmoney[2] = (int) moneySelect[moneySelect.length - 1] / 2;
        mmoney[1] = (mmoney[0] + mmoney[2]) / 2;
        mmoney[3] = (mmoney[4] + mmoney[2]) / 2;

//        dashboardView.setCalibration(mmoney, mmoneyText, 7);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_simple_item, R.id.text1, CALIBRATION_NUMBER);
        adapter.setDropDownViewResource(R.layout.spinner_drop_style);
        moneySpinner.setAdapter(adapter);
        moneySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dashboardView.setValue(Integer.valueOf(parent.getSelectedItem().toString()), true, false);
                tvTransfer.setText(String.valueOf(loanLimitItems.get(itemindex[position]).getAccountamount()));
                tvAccrual.setText(String.valueOf(loanLimitItems.get(itemindex[position]).getLixifeiyong()));
                tvRepayAmount.setText(String.valueOf(loanLimitItems.get(itemindex[position]).getMoney()));
                initaccrualcost(itemindex[position]);
                tvMonthRate.setText("日费率 " + loanLimitItems.get(itemindex[position]).getReserve1());
                datarate = loanLimitItems.get(itemindex[position]).getReserve1();
                bundle.putInt("position", itemindex[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<String> data = new ArrayList<>();
        int temp =0;

        for(int i =0 ;i<loanLimitItems.size();i++){
            if(temp!=loanLimitItems.get(i).getReserve2()){
                temp = loanLimitItems.get(i).getReserve2();
                data.add("借款期限："+ loanLimitItems.get(i).getReserve2()+"天");
            }
        }
        String [] datelist = new String[data.size()];


        for(int i = 0 ;i<data.size();i++){

          datelist[i] =data.get(i);

        }


        ArrayAdapter<String> adapterDate = new ArrayAdapter<String>(getContext(), R.layout.spinner_simple_item1, R.id.text2, datelist);
        adapterDate.setDropDownViewResource(R.layout.spinner_drop_style1);
        dateSpinner.setAdapter(adapterDate);

    }

    /*
    初始化利息弹窗
  */
    @Override
    public void initaccrualcost(int position) {
        if (position >= loanLimitItems.size()) {
            return;
        }
        costlist = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
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

    }

    @Override
    public void saveAppInfo() {

        ChannelId = AppUtil.getChannelData(getContext(), "UMENG_CHANNEL");
        imei = AppUtil.getImei(getContext());
        simtelnum = AppUtil.getNativePhoneNumber(getActivity());
        presenter.saveAppinfo(imei, ChannelId, gps, simtelnum,AppUtil.getBrand());
    }

    @Override
    public void showToast(String msg) {
        refreshLayout.finishRefresh();
        if (!isHidden()) {
            UIUtils.toast(msg, false);
        }

//      Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoticeDialog(final String msg) {
        notdialog = UtilsDialog.showCustomDialog(getActivity(), R.layout.dialog_notice, Gravity.CENTER_VERTICAL, true,true, new UtilsDialog.BindView() {
            @Override
            public void onBind(View rootView) {
                Button close = rootView.findViewById(R.id.btn_close);
                TextView notice = rootView.findViewById(R.id.tv_notice);
                notice.setText(msg);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notdialog.dismiss();
                    }
                });
            }
        });

    }

    @Override
    public void checkAuthenInfo(boolean complete) {
        if(complete){
            loadingdialog= UtilsDialog.createLoadingDialog(getContext(),"申请中...");
            presenter.queryRecord();
        }
    }

    @Override
    public void repayRecordListCallback(List<Record> recordList) {
        if(loadingdialog!=null){
            loadingdialog.dismiss();
        }
        if( 0 !=recordList.size()){
            if(recordList.get(0).getStatus().equals("8")){
                UIUtils.toast("当前状态不可申请，请保持良好信用，有疑问请联系客服！",false);
                return;
            }
            if(!recordList.get(0).getStatus().equals("0")){
                if(!recordList.get(0).getStatus().equals("5")){
                    UIUtils.toast("您已提交申请，请还款后再尝试",false);

                    return;
                }
            }
        }

        bundle.putParcelableArrayList("loanLimitItems", (ArrayList<? extends Parcelable>) loanLimitItems);
        bundle.putString("gps",gps);
        if(loanLimitItems==null){
            return;
        }

        ((BaseActivity) this.getActivity()).goToActivity(OrderActivity.class, bundle);

    }

    @Override
    public void requestfailure(String msg) {
        if(loadingdialog!=null){
            loadingdialog.dismiss();
        }
        UIUtils.toast(msg,false);

    }

    @OnClick(R.id.tv_showaccrual)
    public void onaccrual() {

        cumdialog = UtilsDialog.showCustomDialog(getActivity(), R.layout.dialog_accrualdetail, Gravity.CENTER_VERTICAL, true,false,new UtilsDialog.BindView() {
            @Override
            public void onBind(View rootView) {
                RecyclerView recyclerView = rootView.findViewById(R.id.rv_accrualdetail);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                AccrualDetailAdapter accrualDetailAdapter = new AccrualDetailAdapter(costlist);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(accrualDetailAdapter);
                TextView textView = rootView.findViewById(R.id.tv_accrualtitle);
                textView.setText("利息与费用");
                LinearLayout lyback = rootView.findViewById(R.id.ly_back);
                lyback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cumdialog.dismiss();
                    }
                });
            }
        });
    }

    @OnClick(R.id.btn_apply)
    public void onApply() {
        //
        clickapply();
    }
    @OnClick({R.id.tv_oneself,R.id.tv_shouji,R.id.tv_dianshang,R.id.tv_bankcard})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_oneself:
                if (User.getInstance().isLogin()) {
                    ((BaseActivity) this.getActivity()).goToActivity(UserInfoActivity.class, null);
                } else {
//            ((BaseActivity)this.getActivity()).goToActivity(IdCardActivity2.class,null);
                    ((BaseActivity)this.getActivity()).goToActivity(LoginActivity.class,null);
                }
                break;
            case R.id.tv_shouji:
                if (!User.getInstance().isLogin()) {
                    ((BaseActivity)this.getActivity()).goToActivity(LoginActivity.class,null);
                }else if (TextUtils.isEmpty(User.getInstance().getIdcar()) || TextUtils.isEmpty(User.getInstance().getCopy1()) || TextUtils.isEmpty(User.getInstance().getCopy2())) {
                    UIUtils.toast("请先完善个人资料！", true);
                    ((BaseActivity) this.getActivity()).goToActivity(UserInfoActivity.class, null);
                    return;
                }else {

                }
                break;
            case R.id.tv_dianshang:
                if (!User.getInstance().isLogin()) {
                    ((BaseActivity)this.getActivity()).goToActivity(LoginActivity.class,null);
                }else if (TextUtils.isEmpty(User.getInstance().getIdcar()) || TextUtils.isEmpty(User.getInstance().getCopy1()) || TextUtils.isEmpty(User.getInstance().getCopy2())) {
                    UIUtils.toast("请先完善个人资料！", true);
                    ((BaseActivity) this.getActivity()).goToActivity(UserInfoActivity.class, null);
                    return;
                }else {

                }
                break;
            case R.id.tv_bankcard:
                if (!User.getInstance().isLogin()) {
                    ((BaseActivity)this.getActivity()).goToActivity(LoginActivity.class,null);
                }else if (TextUtils.isEmpty(User.getInstance().getIdcar()) || TextUtils.isEmpty(User.getInstance().getCopy1()) || TextUtils.isEmpty(User.getInstance().getCopy2())) {
                    UIUtils.toast("请先完善个人资料！", true);
                    ((BaseActivity) this.getActivity()).goToActivity(UserInfoActivity.class, null);
                    return;
                }else {

                }
                break;

        }
    }

    public static HomeFragment getInstance(){
        if(homeFragment == null){
            homeFragment  = new HomeFragment();
        }
        return homeFragment;
    }
    public void OnApply(){
        presenter.requestAuthenCenterItem(getActivity());

    }

    public double[] selectSort(double[] arr) {
        itemindex = new int[arr.length];
        double[] marr = new double[arr.length];
        marr = arr.clone();
        for (int i = 0; i < arr.length; i++) {
            double min = arr[i];
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < min) {
                    min = arr[j];
                    arr[j] = arr[i];
                    arr[i] = min;
                }
            }
        }
        for (int k = 0; k < arr.length; k++) {
            for (int j = 0; j < marr.length; j++) {
                if (arr[k] == marr[j]) {
                    itemindex[k] = j;
                }
            }
        }

        return arr;
    }

    /*
    点击申请，检查认证内容，跳转到对应认证页面，检查历史记录，
    */
    public void clickapply() {

        final RxPermissions rxPermission = new RxPermissions(getActivity());
        rxPermission.request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) {
                if (!granted) {
                    UIUtils.toast("应用未能获取全部需要的相关权限，部分功能可能不能正常使用.", true);

                } else {
                    //AppUtil.locationListener.onLocationChanged(AppUtil.getLocation(getContext(), (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE), text_position));
                    AppUtil.startLocationlistener(getActivity(), new AppUtil.Ongetlocation() {
                        @Override
                        public void returnLocation(String city, String SpecificLocation) {
                            gps = SpecificLocation;
                            if (text_position != null) {
                                text_position.setText(city);
                            }
                            User user = User.getInstance();
                            if (user.isLogin()) {
                                if(TextUtils.isEmpty(user.getIdcar()) || TextUtils.isEmpty(user.getCopy1()) || TextUtils.isEmpty(user.getCopy2())){
                                    UIUtils.toast("请先填写个人资料",true);
                                    Bundle bundle =new Bundle();
                                    bundle.putInt("start",100);
                                    ((BaseActivity) getActivity()).goToActivity(UserInfoActivity.class, bundle);
                                    return;
                                }else{
                                    //跳转到短期借款页面
                                    Intent intent = new Intent(getActivity(), ShortByStagesActivity.class);
                                    //还款金额(元)
                                    intent.putExtra("tvRepayAmount",tvRepayAmount.getText().toString());
                                    //利息与费用(元)
                                    intent.putExtra("tvAccrual",tvAccrual.getText().toString());
                                    //到账金额(元)
                                    intent.putExtra("tvTransfer",tvTransfer.getText().toString());
                                    //选择借款金额(元)
                                    intent.putExtra("moneySpinner",moneySpinner.getSelectedItem().toString());
                                    // 日费率
                                    intent.putExtra("datarate",datarate);

                                    //还款期限(天)

                                    String data1 = dateSpinner.getSelectedItem().toString();
                                    String data2 = data1.replace("借款期限：","");
                                    String data = data2.replace("天","");

                                    intent.putExtra("dateSpinner",data.trim());
                                    startActivity(intent);
                                }
                                presenter.requestAuthenCenterItem(getActivity());
                            }else {

                                ((BaseActivity) getActivity()).goToActivity(LoginActivity.class, null);
                            }

                        }
                    });
                }

            }
        });

    }

    public void getGPS() {

        final RxPermissions rxPermission = new RxPermissions(getActivity());
        rxPermission.request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) {
                if (!granted) {
                    UIUtils.toast("应用未能获取全部需要的相关权限，部分功能可能不能正常使用.", true);
                    saveAppInfo();
                } else {
                    //AppUtil.locationListener.onLocationChanged(AppUtil.getLocation(getContext(), (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE), text_position));
                    AppUtil.startLocationlistener(getActivity(), new AppUtil.Ongetlocation() {
                        @Override
                        public void returnLocation(String city, String SpecificLocation) {
                            if (text_position != null) {
                                Println.out("address",city);
                                text_position.setText(city);
                            }
                            gps = SpecificLocation;
                            saveAppInfo();
                        }
                    });
                }

            }
        });

    }


    public static class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {

            Glide.with(context).load((String) path).into(imageView);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachview();
        }
        unbinder.unbind();
    }
}

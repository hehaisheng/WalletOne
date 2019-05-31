package com.zhiyu.wallet.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.bean.Credit;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.mvp.contract.AuthenticateContract;
import com.zhiyu.wallet.mvp.presenter.AuthenticatePresenter;
import com.zhiyu.wallet.util.Println;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;
import com.zhiyu.wallet.util.UtilsDialog;
import com.zhiyu.wallet.widget.CheckBoxSample;
import com.zhiyu.wallet.widget.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.fraudmetrix.octopus.aspirit.bean.OctopusParam;
import cn.fraudmetrix.octopus.aspirit.main.OctopusManager;
import cn.fraudmetrix.octopus.aspirit.main.OctopusTaskCallBack;
import io.reactivex.functions.Consumer;


/**
 * 2018/7/17
 *
 * @author ZhiYu
 *         <p>
 *         认证中心
 */
public class AuthenCenterActivity extends BaseActivity<AuthenticatePresenter> implements AuthenticateContract.AuthenticateIView {

    @BindView(R.id.gridView)
    MyGridView gridView;
    @BindView(R.id.cb_register_agree)
    CheckBoxSample cbRegisterAgree;
    @BindView(R.id.tv_protocol)
    TextView tvMainProtocol;
    @BindView(R.id.iv_title_back)
    LinearLayout linearLayoutback;

    private List<Map<String, Object>> dataList;
    private SimpleAdapter adapter;
    private String type;   //跳转类型
    private List<Credit> credislist;

    @Override
    protected void initData() {
        presenter.attachview(this);
        StatusbarUtil.setfullScreenSystemWindows(this, getResources().getColor(R.color.orangetitle));
        dataList = new ArrayList<>();
        initBundle(getIntent().getBundleExtra("data"));
        initGridView();
        initProtocol();
        cbRegisterAgree.setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(type)) {

            Println.out("中心","类型为空");
            presenter.requestAuthenItemdata();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getBundleExtra("data") != null) {
            presenter.requestAuthenCenterItem();
        }
    }

    private void initGridView() {
        if (dataList != null) {
            adapter = new SimpleAdapter(this, dataList, R.layout.activity_authencenter_item, new String[]{"credit_name", "credentials", "icon"},
                    new int[]{R.id.credit_pro_name, R.id.credentials, R.id.image});
            gridView.setFocusable(false);
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (cbRegisterAgree.isChecked()) {
                        if (TextUtils.isEmpty(User.getInstance().getIdcar()) || TextUtils.isEmpty(User.getInstance().getCopy1()) || TextUtils.isEmpty(User.getInstance().getCopy2())) {
                            UIUtils.toast("请先完善个人资料！", true);
                            return;
                        }

                        if (dataList.get(position).get("credentials").equals("未完成")) {
                            if ((position == 4) || (position == 5)) {
                                UIUtils.toast("该认证功能还没上线，请期待", false);
                                return;
                            }

                            if ((position > 0) && dataList.get(position - 1).get("credentials").equals("未完成") && credislist.get(position - 1).getIsauthen().equals("1")) {
                                UIUtils.toast("请先完成" + dataList.get(position - 1).get("credit_name"), true);
                            } else {

                                if (dataList.get(position).get("credit_name").equals("淘宝认证")) {
                                    requestpermission(true);
                                     presenter.requestTaobaoAuthencomfirm();
                                    return;
                                }
                                if (dataList.get(position).get("credit_name").equals("支付宝认证")) {
                                    requestpermission(false);
//                                     presenter.requestZfbAuthencomfirm();
                                    return;
                                }

                                if ((String.valueOf(dataList.get(position).get("credit_name")).equals("银行卡认证"))) {
                                    Bundle bundle = new Bundle();
                                    if (!TextUtils.isEmpty(type)) {
                                        bundle.putString("type", "authencenterForresult");
                                    } else {
                                        bundle.putString("type", "authencenter");
                                    }
                                    goToActivity(BankaddActivity.class, bundle);
                                    return;
                                }

                                Bundle bundle = new Bundle();
                                bundle.putString("type", String.valueOf(dataList.get(position).get("credit_name")));
                                if (!TextUtils.isEmpty(type)) {
                                    bundle.putString("startMetheod", "startForresult");
                                } else {
                                    bundle.putString("startMetheod", "normal");
                                }
                                goToActivity(AuthenticationActivity.class, bundle);
                            }

                        } else {
                            UtilsDialog.showDialog("", "认证" + dataList.get(position).get("credentials"), AuthenCenterActivity.this);
                        }
                    } else {
                        UIUtils.toast("请先认真阅读并同意用户授权协议！", false);
                    }


                }
            });
        }
    }

    @OnClick(R.id.cb_register_agree)
    public void agree() {
        if (cbRegisterAgree.isChecked()) {
            cbRegisterAgree.setChecked(false);
        } else {
            cbRegisterAgree.setChecked(true);
        }

    }

    @OnClick(R.id.iv_title_back)
    public void back() {
        finish();
    }

    private void initProtocol() {
        tvMainProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("type", "userinfo2");
                goToActivity(ProtocolActivity.class, bundle);
            }
        });
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_authencenter;
    }

    @Override
    protected AuthenticatePresenter createPresenter() {
        return new AuthenticatePresenter();
    }

    public void zfbauthen(){
        OctopusManager.getInstance().setNavImgResId(R.mipmap.return_black1);//
        OctopusManager.getInstance().setPrimaryColorResId(R.color.color_white);//
        OctopusManager.getInstance().setTitleColorResId(R.color.tvtitle);//
        OctopusManager.getInstance().setTitleSize(19);//sp
        OctopusManager.getInstance().setShowWarnDialog(true);
        OctopusManager.getInstance().setStatusBarBg(R.color.color_white);
        OctopusParam param = new OctopusParam();
        //param.passbackarams = "********";//透传参数
        //param.realName="jack";
        //param.mobile="13588003567";
        //param.identityCode = "410182199006121122";
        OctopusManager.getInstance().getChannel(this, "005004","DS", param,new OctopusTaskCallBack() {
            @Override
            public void onCallBack(int code, String taskId) {
                String msg = "success:";
                if(code == 0){//code
                    //String taskId = data.getStringExtra(OctopusConstants.OCTOPUS_TASK_RESULT_TASKID);//
                    msg+=taskId;
                    presenter.requestsavetaskid("",taskId);
                    type="1";
                }else {
                    msg = "failure："+code;

                }
                //Log.e("tag",taskId);
                Println.out("zfb",msg);
//                UIUtils.toast(msg,false);
            }
        });

    }

    public void tbauthen(){
        OctopusManager.getInstance().setNavImgResId(R.mipmap.return_black1);//
        OctopusManager.getInstance().setPrimaryColorResId(R.color.color_white);//
        OctopusManager.getInstance().setTitleColorResId(R.color.tvtitle);//
        OctopusManager.getInstance().setTitleSize(19);//sp
        OctopusManager.getInstance().setShowWarnDialog(true);
        OctopusManager.getInstance().setStatusBarBg(R.color.color_white);
        OctopusParam param = new OctopusParam();
        //param.passbackarams = "********";//透传参数
        //param.realName="jack";
        //param.mobile="13588003567";
        //param.identityCode = "410182199006121122";
        OctopusManager.getInstance().getChannel(this, "005003","DS", param,new OctopusTaskCallBack() {
            @Override
            public void onCallBack(int code, String taskId) {
                String msg = "success:";
                if(code == 0){//code
                    //String taskId = data.getStringExtra(OctopusConstants.OCTOPUS_TASK_RESULT_TASKID);//
                    msg+=taskId;
                    presenter.requestsavetaskid(taskId,"");
                    type="1";
                }else {
                    msg = "failure："+code;
                }
                //Log.e("tag",taskId);
                Println.out("tb",msg);
//                UIUtils.toast(msg,false);
            }
        });

    }

//    public void taobaoauthen() {
//
//        // MoxieSDK.getInstance().clear();
//        //合作⽅系统中的客户ID
//        String mUserId = "客户" + User.getInstance().getPhone(); //合作⽅系统中的客户ID
//        //获取任务状态时使⽤(合作⽅申请接⼊后由魔蝎数据提供)
//        String mApiKey = "b675dafbaf2f4bc4b10fe29556c63023";
//        String mThemeColor = "#ff9500"; //SDK⾥⻚⾯主调
//        MxParam mxParam = new MxParam();
//        mxParam.setUserId(mUserId); //必传
//        mxParam.setApiKey(mApiKey); //必传
//        mxParam.setTaskType(MxParam.PARAM_TASK_TAOBAO); //必传
//        mxParam.setPhone(User.getInstance().getPhone());
//        mxParam.setName(User.getInstance().getUsername());
//        mxParam.setIdcard(User.getInstance().getIdcar());
//        //mxParam.setThemeColor(mThemeColor);
//        TitleParams titleParams = new TitleParams.Builder()
//                //设置返回键的icon，不设置此方法会默认使用魔蝎的icon
//                .leftNormalImgResId(R.mipmap.return_black1)
//                //用于设置selector，表示按下的效果，不设置默认使leftNormalImgResId()设置的图片
//                //.leftPressedImgResId(R.drawable.back)
//                //标题字体颜色
//                .titleColor(this.getResources().getColor(R.color.tvtitle))
//                //title背景色
//                .backgroundColor(this.getResources().getColor(android.R.color.white))
//                //设置右边icon
//                //.rightNormalImgResId(R.drawable.refresh)
//                //是否支持沉浸式
//                .immersedEnable(true)
//                //title返回按钮旁边的文字（关闭）的颜色
//                //.leftTextColor(getContext().getResources().getColor(R.color.colorWhite))
//                //title返回按钮旁边的文字，一般为关闭
//                //.leftText(“关闭”)
//                .build();
//        mxParam.setTitleParams(titleParams);
//        mxParam.setQuitDisable(true);
//        mxParam.setCacheDisable(MxParam.PARAM_COMMON_YES);
//        mxParam.setLoadingViewText("验证过程中不会浪费您任何流量\n请稍等刻");
//        MoxieSDK.getInstance().start(this, mxParam, new MoxieCallBack() {
//            @Override
//            public boolean callback(MoxieContext moxieContext, MoxieCallBackData moxieCallBackData) {
//
//                String TAG = "";
//                if (moxieCallBackData != null) {
//                    Println.out("moxietaobao", moxieCallBackData.getCode() + "");
//                    ;
//                    Println.out("moxietaobao", moxieCallBackData.toString());
//                    switch (moxieCallBackData.getCode()) {
//                        /**
//                         * 账单导入中
//                         * 如果用户正在导入魔蝎SDK会出现这个情况，如需获取最终状态请轮询贵方后台接口
//                         * 魔蝎后台会向贵方后台推送Task通知和Bill通知
//                         * Task通知：登录成功/登录失败
//                         * Bill通知：账单通知
//                         */
//                        case MxParam.ResultCode.IMPORTING:
//                            if (moxieCallBackData.isLoginDone()) {
//                                //状态为IMPORTING, 且loginDone为true，说明这个时候已经在采集中，已经登录成功
//                                Log.d(TAG, "任务已经登录成功，正在采集中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
//                            } else {
//                                //状态为IMPORTING, 且loginDone为false，说明这个时候正在登录中
//                                Log.d(TAG, "任务正在登录中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
//                            }
//                            break;
//                        /**
//                         * 任务还未开始
//                         *
//                         * 假如有弹框需求，可以参考 {@link BigdataFragment#showDialog(MoxieContext)}
//                         *
//                         * example:
//                         *  case MxParam.ResultCode.IMPORT_UNSTART:
//                         *      showDialog(moxieContext);
//                         *      return true;
//                         * */
//                        case MxParam.ResultCode.IMPORT_UNSTART:
//                            Log.d(TAG, "任务未开始");
//                            moxieContext.finish();
//                            break;
//                        case MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR:
//                            Toast.makeText(AuthenCenterActivity.this, "导入失败(平台方服务问题)", Toast.LENGTH_SHORT).show();
//                            break;
//                        case MxParam.ResultCode.MOXIE_SERVER_ERROR:
//                            Toast.makeText(AuthenCenterActivity.this, "导入失败(魔蝎数据服务异常)", Toast.LENGTH_SHORT).show();
//                            break;
//                        case MxParam.ResultCode.USER_INPUT_ERROR:
//                            Toast.makeText(AuthenCenterActivity.this, "导入失败(" + moxieCallBackData.getMessage() + ")", Toast.LENGTH_SHORT).show();
//                            break;
//                        case MxParam.ResultCode.IMPORT_FAIL:
//                            Toast.makeText(AuthenCenterActivity.this, "导入失败", Toast.LENGTH_SHORT).show();
//                            break;
//                        case MxParam.ResultCode.IMPORT_SUCCESS:
//                            Log.d(TAG, "任务采集成功，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
//                            //根据taskType进行对应的处理
//                            switch (moxieCallBackData.getTaskType()) {
//                                case MxParam.PARAM_TASK_TAOBAO:
//                                    presenter.requestTaobaoAuthencomfirm();
//                                    break;
//                            }
//
//                            moxieContext.finish();
//                            return true;
//                    }
//                }
//                return false;
//            }
//
//            @Override
//            public void onError(MoxieContext moxieContext, MoxieException moxieException) {
//                super.onError(moxieContext, moxieException);
//
//                Println.printJson("moxieerr", moxieException.getCode() + moxieException.getMessage(), "");
//                ;
//            }
//        });
//
//    }
//
//    public void zfbAuthen() {
//
//        // MoxieSDK.getInstance().clear();
//        //合作⽅系统中的客户ID
//        String mUserId = "客户" + User.getInstance().getPhone(); //合作⽅系统中的客户ID
//        //获取任务状态时使⽤(合作⽅申请接⼊后由魔蝎数据提供)
//        String mApiKey = "b675dafbaf2f4bc4b10fe29556c63023";
//        String mThemeColor = "#ff9500"; //SDK⾥⻚⾯主⾊调
//        MxParam mxParam = new MxParam();
//        mxParam.setUserId(mUserId); //必传
//        mxParam.setApiKey(mApiKey); //必传
//        mxParam.setTaskType(MxParam.PARAM_TASK_ALIPAY); //必传
//        mxParam.setPhone(User.getInstance().getPhone());
//        mxParam.setName(User.getInstance().getUsername());
//        mxParam.setIdcard(User.getInstance().getIdcar());
////       mxParam.setThemeColor(mThemeColor);
//        TitleParams titleParams = new TitleParams.Builder()
////设置返回键的icon，不设置此方法会默认使用魔蝎的icon
//                .leftNormalImgResId(R.mipmap.return_black1)
////用于设置selector，表示按下的效果，不设置默认使leftNormalImgResId()设置的图片
////                .leftPressedImgResId(R.drawable.back)
////标题字体颜色
//                .titleColor(this.getResources().getColor(R.color.tvtitle))
////title背景色
//                .backgroundColor(this.getResources().getColor(android.R.color.white))
////设置右边icon
////                .rightNormalImgResId(R.drawable.refresh)
////是否支持沉浸式
//                .immersedEnable(true)
////title返回按钮旁边的文字（关闭）的颜色
////                .leftTextColor(getContext().getResources().getColor(R.color.colorWhite))
////title返回按钮旁边的文字，一般为关闭
////                .leftText(“关闭”)
//                .build();
//        mxParam.setTitleParams(titleParams);
//        mxParam.setQuitDisable(true);
//        mxParam.setCacheDisable(MxParam.PARAM_COMMON_YES);
//        mxParam.setLoadingViewText("验证过程中不会浪费您任何流量\n请稍等刻");
//        MoxieSDK.getInstance().start(this, mxParam, new MoxieCallBack() {
//            @Override
//            public boolean callback(MoxieContext moxieContext, MoxieCallBackData moxieCallBackData) {
//
//                String TAG = "";
//                if (moxieCallBackData != null) {
//                    Println.out("moxiezfb", moxieCallBackData.getCode() + "");
//                    ;
//                    Println.out("moxiezfb", moxieCallBackData.toString());
//                    switch (moxieCallBackData.getCode()) {
//                        /**
//                         * 账单导入中
//                         * 如果用户正在导入魔蝎SDK会出现这个情况，如需获取最终状态请轮询贵方后台接口
//                         * 魔蝎后台会向贵方后台推送Task通知和Bill通知
//                         * Task通知：登录成功/登录失败
//                         * Bill通知：账单通知
//                         */
//                        case MxParam.ResultCode.IMPORTING:
//                            if (moxieCallBackData.isLoginDone()) {
//                                //状态为IMPORTING, 且loginDone为true，说明这个时候已经在采集中，已经登录成功
//                                Log.d(TAG, "任务已经登录成功，正在采集中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
//                            } else {
//                                //状态为IMPORTING, 且loginDone为false，说明这个时候正在登录中
//                                Log.d(TAG, "任务正在登录中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
//                            }
//                            break;
//                        /**
//                         * 任务还未开始
//                         *
//                         * 假如有弹框需求，可以参考 {@link BigdataFragment#showDialog(MoxieContext)}
//                         *
//                         * example:
//                         *  case MxParam.ResultCode.IMPORT_UNSTART:
//                         *      showDialog(moxieContext);
//                         *      return true;
//                         * */
//                        case MxParam.ResultCode.IMPORT_UNSTART:
//                            Log.d(TAG, "任务未开始");
//                            moxieContext.finish();
//                            break;
//                        case MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR:
//                            Toast.makeText(AuthenCenterActivity.this, "导入失败(平台方服务问题)", Toast.LENGTH_SHORT).show();
//                            break;
//                        case MxParam.ResultCode.MOXIE_SERVER_ERROR:
//                            Toast.makeText(AuthenCenterActivity.this, "导入失败(魔蝎数据服务异常)", Toast.LENGTH_SHORT).show();
//                            break;
//                        case MxParam.ResultCode.USER_INPUT_ERROR:
//                            Toast.makeText(AuthenCenterActivity.this, "导入失败(" + moxieCallBackData.getMessage() + ")", Toast.LENGTH_SHORT).show();
//                            break;
//                        case MxParam.ResultCode.IMPORT_FAIL:
//                            Toast.makeText(AuthenCenterActivity.this, "导入失败", Toast.LENGTH_SHORT).show();
//                            break;
//                        case MxParam.ResultCode.IMPORT_SUCCESS:
//                            Log.d(TAG, "任务采集成功，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
//                            //根据taskType进行对应的处理
//                            switch (moxieCallBackData.getTaskType()) {
//                                case MxParam.PARAM_TASK_ALIPAY:
//                                    presenter.requestZfbAuthencomfirm();
//                                    break;
//                            }
//
//                            moxieContext.finish();
//                            return true;
//                    }
//                }
//                return false;
//            }
//
//            @Override
//            public void onError(MoxieContext moxieContext, MoxieException moxieException) {
//                super.onError(moxieContext, moxieException);
//
//                Println.printJson("moxieerr", moxieException.getCode() + moxieException.getMessage(), "");
//                UIUtils.toast(moxieException.getMessage(), false);
//                moxieContext.finish();
//                ;
//            }
//        });
//
//    }

    public void requestpermission(final boolean mtype) {
        final RxPermissions rxPermission = new RxPermissions(this);
        rxPermission.request(
                Manifest.permission.CAMERA
        ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) {
                if (!granted) {
                    UIUtils.toast("应用未能获取全部需要的相关权限，部分功能可能不能正常使用.", true);
                } else {
                    if (mtype) {
                        tbauthen();
                    } else {
                        zfbauthen();
                    }
                }

            }
        });
    }

    /**
     * 点击申请，检查认证内容，跳转到对应认证页面，检查历史记录，根据bundle判断是首页跳转还是认证中心跳转
     * 首页跳转：认证完跳转到对应认证页面，，
     * 认证中心跳转，认证完结束返回
     * @param bundle
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void initBundle(Bundle bundle) {
        if (bundle != null) {
            String name = bundle.getString("authen_name");
            type = bundle.getString("applyloan");
            if (type.equals("applyloan")) {
                presenter.requestAuthenItemdata();
            }

            if (name.equals("淘宝认证")) {
                requestpermission(true);

            } else if (name.equals("支付宝认证")) {

                requestpermission(false);
            } else if (name.equals("银行卡认证")) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("type", "authencenterForresult");
                goToActivity(BankaddActivity.class, bundle1);

            } else {
                Bundle bundle2 = new Bundle();
                bundle2.putString("type", name);
                bundle2.putString("startMetheod", "startForresult");
                goToActivity(AuthenticationActivity.class, bundle2);
            }
        }
    }


    @Override
    public void showAuthenItemlist(List<Credit> credits) {

        this.credislist = credits;
        if (dataList.size() == 0) {
            for (int i = 0; i < credits.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("credit_name", credits.get(i).getCredit_name());
                map.put("credentials", credits.get(i).getCredentials());
                map.put("icon", credits.get(i).getIcon());
                dataList.add(map);
            }
        } else {
            for (int i = 0; i < credits.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("credit_name", credits.get(i).getCredit_name());
                map.put("credentials", credits.get(i).getCredentials());
                map.put("icon", credits.get(i).getIcon());
                dataList.set(i, map);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showAuthenResult(boolean isSuccess, Map<String, String> data) {

        if (isSuccess) {
            UIUtils.toast(data.get("data"), false);
            if(type.equals("1")){
                presenter.requestAuthenItemdata();
                type=null;
            }
            if (!TextUtils.isEmpty(type)) {
                presenter.requestAuthenCenterItem();
            }
        } else {
            if(type.equals("1")){
                type=null;
            }
            UIUtils.toast(data.get("data"), false);
        }


    }

    @Override
    public void sendsmsSuccess(boolean isinputAddress, String msg,String uuid) {

    }

    @Override
    public void showdialog(String msg) {
        UIUtils.toast(msg, false);
    }

    @Override
    public void closedialog() {

    }


}

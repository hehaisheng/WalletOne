//package com.zhiyu.wallet.mvp.ui.activity;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.database.Cursor;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.text.TextUtils;
//import android.util.Base64;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSON;
//import com.moxie.liveness.MXLivenessSDK;
//import com.moxie.liveness.ui.LivenessActivity;
//import com.moxie.liveness.util.MXReturnResult;
//import com.moxie.ocr.ocr.idcard.IDCardActivity;
//import com.moxie.ocr.ocr.idcard.IDCardRecognizer;
//import com.moxie.ocr.ocr.idcard.MXOCRResult;
//import com.tbruyelle.rxpermissions2.RxPermissions;
//import com.zhiyu.wallet.R;
//import com.zhiyu.wallet.base.BaseActivity;
//import com.zhiyu.wallet.bean.ImageViewID;
//import com.zhiyu.wallet.bean.News;
//import com.zhiyu.wallet.bean.User;
//import com.zhiyu.wallet.common.Constant;
//import com.zhiyu.wallet.mvp.contract.IdcardContract;
//import com.zhiyu.wallet.mvp.presenter.IdcardPresenter;
//import com.zhiyu.wallet.util.BitmapUtils;
//import com.zhiyu.wallet.util.Println;
//import com.zhiyu.wallet.util.StatusbarUtil;
//import com.zhiyu.wallet.util.UIUtils;
//import com.zhiyu.wallet.util.UtilsDialog;
//import com.zhiyu.wallet.widget.CheckBoxSample;
////import com.zmxy.ZMCertification;
////import com.zmxy.ZMCertificationListener;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import io.reactivex.functions.Consumer;
//
///**
// * 2018/7/25
// *
// * @author ZhiYu
// */
//public class IdCardActivity extends BaseActivity<IdcardPresenter> implements IdcardContract.IdcardIView{
//
//    @BindView(R.id.iv_title_back)
//    LinearLayout ivTitleBack;
//    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.iv_title_setting)
//    ImageView ivTitleSetting;
//    @BindView(R.id.id_face_checkbox)
//    CheckBoxSample idfacecheckbox;
//    @BindView(R.id.id_back_checkbox)
//    CheckBoxSample idbackcheckbox;
//    @BindView(R.id.id_card_checkbox)
//    CheckBoxSample idcardcheckbox;
//
//
//
//    /**
//     * 扫描身份证正面请求码
//     */
//    private static final int MX_SCAN_ID_CARD_FRONT_REQUEST = 100;
//    /**
//     * 扫描身份证反面请求码
//     */
//    private static final int MX_SCAN_ID_CARD_BACK_REQUEST = 101;
//    /**
//     * 扫描身份证正反面请求码
//     */
//    private static final int MX_SCAN_ID_CARD_BOTH_REQUEST = 102;
//    /**
//     * 活体检测请求码
//     */
//    private static final int KEY_TO_DETECT_REQUEST_CODE = 104;
//    private String pathResult;
//    private Uri selectedImage;
//    private Bitmap bitmap;
//    private Dialog dialog;
//    private EventBus eventBus;
//    private int IDCARD_FRONT_RESULT = 0;
//    private int IDCARD_BACK_RESULT = 0;
//    private Dialog loadingdialog;
//
//    @Override
//    protected void initData() {
//        presenter.attachview(this);
//
//        StatusbarUtil.setdropBarSystemWindows(this,getResources().getColor(android.R.color.white));
//        eventBus = EventBus.getDefault();
//        eventBus.register(this);
////        requestPermissions();
//        requestPermissionsScan();
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//    }
//
//    @Override
//    protected void initTitle() {
//        ivTitleBack.setVisibility(View.VISIBLE);
//        tvTitle.setText("身份证认证");
//        ivTitleSetting.setVisibility(View.GONE);
//    }
//
//    @OnClick(R.id.iv_title_back)
//    public void back(View view) {
//        removeCurrentActivity();
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_authen_idcard;
//    }
//
//    @Override
//    protected IdcardPresenter createPresenter() {
//        return new IdcardPresenter();
//    }
//
//    public void requestPermissionsScan() {
//        RxPermissions rxPermission = new RxPermissions(IdCardActivity.this);
//        rxPermission.request(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.CAMERA
//        ).subscribe(new Consumer<Boolean>() {
//            @Override
//            public void accept(Boolean granted) {
//                if (!granted) {
//                    UIUtils.toast("应用未能获取全部需要的相关权限，部分功能可能不能正常使用.", true);
//                    finish();
//                }else {
//
//                    toScanIdCardBoth();
//                }
//
//            }
//        });
//
//    }
//
//    public void requestPermissions() {
//        RxPermissions rxPermission = new RxPermissions(IdCardActivity.this);
//        rxPermission.request(
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.CAMERA
//        ).subscribe(new Consumer<Boolean>() {
//            @Override
//            public void accept(Boolean granted) {
//                if (!granted) {
//                    UIUtils.toast("应用未能获取全部需要的相关权限，部分功能可能不能正常使用.", true);
//                    finish();
//                }else {
//
//                    startMXLivenessActivity();
//                }
//
//            }
//        });
//
//    }
//
//
//    /**
//     * 跳转IDCard正反面扫描界面
//     *
//     */
//    @Override
//    public void toScanIdCardBoth() {
//        // 扫描双面时，从正面开始扫描
//
//        Intent scanIntent = new Intent(this, IDCardActivity.class);
//
//        // KEY_PRODUCTION_MODE，true为生产环境，false为开发环境，默认是false
//        // 注意上线产品设置为生产环境
//        scanIntent.putExtra(IDCardActivity.KEY_PRODUCTION_MODE,false);
//
//        //设置返回按钮图片
//        scanIntent.putExtra(IDCardActivity.EXTRA_BACK_DRAWABLE_ID, R.mipmap.icon_scan_back);
//
//        //设置身份证扫描类型
//        scanIntent.putExtra(IDCardActivity.EXTRA_RECOGNIZE_MODE, IDCardRecognizer.Mode.FRONT);
//
//        //设置是否连续扫描正反面
//        scanIntent.putExtra(IDCardActivity.KEY_SCAN_BOTH_MODE,true);
//
//        //设置身份证扫描文字
//        scanIntent.putExtra(IDCardActivity.EXTRA_SCAN_TIPS, "请将身份证正面放入扫描框内");
//
//        //设置标题
//        scanIntent.putExtra(IDCardActivity.EXTRA_SCAN_TITLE, "身份证认证");
//
//        //设置是否开启扫描光标
//        scanIntent.putExtra(IDCardActivity.EXTRA_SCAN_LINE_STATUS, true);
//
//        //扫描取景框边界颜色
//        scanIntent.putExtra(IDCardActivity.EXTRA_SCAN_GUIDE_COLOR, Color.parseColor("#78FFFFFF"));
//
//        //设置扫描的超时时间
//        scanIntent.putExtra(IDCardActivity.EXTRA_SCAN_LINE_STATUS, 30);
//
//        startActivityForResult(scanIntent, MX_SCAN_ID_CARD_BOTH_REQUEST);
//    }
//
//
//    @Override
//    public void startMXLivenessActivity(){
//        Intent intent  =new Intent();
//        intent.setClass(this, LivenessActivity.class);
//        // KEY_PRODUCTION_MODE，true为生产环境，false为开发环境，默认是false
//        // 注意上线产品设置为生产环境
//        intent.putExtra(LivenessActivity.KEY_PRODUCTION_MODE,false);
//        //OUTPUT_TYPE 配置, 传入的outputType类型为singleImg （单图），multiImg （多图），video（低质量视频），fullvideo（高质量视频）
//        intent.putExtra(LivenessActivity.OUTTYPE, Constant.VIDEO);
//        //EXTRA_MOTION_SEQUENCE 动作检测序列配置，支持四种检测动作， BLINK(眨眼), MOUTH（张嘴）, NOD（点头）, YAW（摇头）, 各个动作以空格隔开。 推荐第一个动作为BLINK。
//        //默认配置为"BLINK MOUTH NOD YAW"
//        intent.putExtra(LivenessActivity.EXTRA_MOTION_SEQUENCE, "BLINK MOUTH NOD YAW");
//        //SOUND_NOTICE 配置, 传入的soundNotice为boolean值，true为打开, false为关闭。
//        intent.putExtra(LivenessActivity.SOUND_NOTICE, true);
//        //COMPLEXITY 配置, 传入的complexity类型为normal,支持四种难度，easy, normal, hard, hell.
//        intent.putExtra(LivenessActivity.COMPLEXITY, Constant.HARD);
//        //设置返回protobuf结果,默认为true
//        intent.putExtra(LivenessActivity.KEY_DETECT_PROTO_BUF_RESULT, true);
//        startActivityForResult(intent, KEY_TO_DETECT_REQUEST_CODE);
//
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case KEY_TO_DETECT_REQUEST_CODE:
//                // 活体检测的结果回调
//                dealDetectResult(data, resultCode);
//                return ;
//            case MX_SCAN_ID_CARD_FRONT_REQUEST:
//            case MX_SCAN_ID_CARD_BOTH_REQUEST:
//                switch (resultCode) {
//                    // OCR识别成功
//                    case IDCardActivity.RESULT_CARD_INFO:
//                        dealScanIDCardResult(requestCode, data);
//                        Println.out("onActivityResult" + "   resultCode:" ,resultCode+",OCR识别成功");
//                        return ;
//                    // OCR相机权限获取失败
//                    case IDCardActivity.RESULT_CAMERA_NOT_AVAILABLE:
//                        showToast(Constant.ERROR_CAMERA_REFUSE);
//                        Println.out( "onActivityResult" + "   resultCode:" , resultCode+",OCR识别相机权限获取失败");
//                        break;
//                    //  扫描取消
//                    case IDCardActivity.RESULT_CANCELED:
//                        showToast(Constant.ERROR_SCAN_CANCEL);
//                        Println.out( "onActivityResult" + "   resultCode:" , resultCode+",扫描被取消");
//                        break;
//                    //  算法SDK初始化失败
//                    case IDCardActivity.RESULT_RECOGNIZER_INIT_FAILED:
//                        showToast(Constant.ERROR_SDK_INITIALIZE);
//                        Println.out( "onActivityResult" + "   resultCode:" ,resultCode+",算法SDK初始化失败");
//                        break;
//                    // 扫描超时
//                    case IDCardActivity.RESULT_RECOGNIZER_FAIL_SCAN_TIME_OUT:
//                        showToast(Constant.ERROR_TIME_OUT);
//                        Println.out( "onActivityResult" + "   resultCode:" , resultCode+",扫描超时");
//                        break;
//                    default:
//                        break;
//                }break;
//
//        }
//        finish();
//    }
//
//    private void dealScanIDCardResult(int requestCode, Intent data) {
//        switch (requestCode) {
//            case MX_SCAN_ID_CARD_FRONT_REQUEST:
//                dealScanIDCardBothResult(data);
//                break;
//            case MX_SCAN_ID_CARD_BACK_REQUEST:
//                //dealScanIDCardBackResult(data);
//                break;
//            case MX_SCAN_ID_CARD_BOTH_REQUEST:
//                dealScanIDCardBothResult(data);
//                break;
//        }
//    }
//
//    private void dealScanIDCardBothResult(final Intent data) {
//        // 扫描双面通过IDCardActivity.KEY_FRONT_CARD_DATA取出正面扫描结果
////        MXOCRResult idCardFrontResult = (MXOCRResult) data.getParcelableExtra(IDCardActivity.EXTRA_SCAN_RESULT);
//        MXOCRResult idCardFrontResult = (MXOCRResult) data.getParcelableExtra(IDCardActivity.KEY_FRONT_CARD_DATA);
//        // 扫描双面通过IDCardActivity.KEY_BACK_CARD_DATA取出正面扫描结果
//        MXOCRResult idCardBackResult = (MXOCRResult) data.getParcelableExtra(IDCardActivity.KEY_BACK_CARD_DATA);
//
//        loadingdialog = UtilsDialog.createLoadingDialog(IdCardActivity.this,"认证中");
////        TextView tv1 = findViewById(R.id.tv1);
////        String imageBase64 = Base64.encodeToString(idCardFrontResult.getCropImage(),Base64.NO_WRAP);
////        tv1.setText(imageBase64);
//
//        Bitmap Idfacebitmap = BitmapFactory.decodeByteArray(idCardFrontResult.getCropImage(), 0, idCardFrontResult.getCropImage().length);
//        ImageViewID .setIdfacebitmap(Idfacebitmap);
//
//        Bitmap Idbackbitmap = BitmapFactory.decodeByteArray(idCardBackResult.getCropImage(), 0, idCardBackResult.getCropImage().length);
//        ImageViewID .setIdbackbitmap(Idbackbitmap);
//        presenter.requestGetIdOcrFrontInfo(idCardFrontResult.getCropImage());
//
//        presenter.requestGetIdOcrBackInfo(idCardBackResult.getCropImage());
//
//    }
//
//    public void dealDetectResult(Intent data, int resultCode) {
//        switch (resultCode) {
//            case LivenessActivity.RESULT_LIVENESS_OK:
//                detectSuccess(data);
//                Println.out( "onActivityResult" + "   resultCode:" ,resultCode+",活体检测成功");
//                return;
//            case LivenessActivity.RESULT_SDK_INIT_FAIL_APPLICATION_ID_ERROR:
//                showToast(Constant.ERROR_PACKAGE);
//                Println.out(  "onActivityResult" + "   resultCode:" , resultCode+",未替换包名或包名错误");
//                break;
//            case LivenessActivity.RESULT_SDK_INIT_FAIL_LICENSE_OUT_OF_DATE:
//                showToast(Constant.ERROR_LICENSE_OUT_OF_DATE);
//                Println.out( "onActivityResult" + "   resultCode:" , resultCode+",授权文件过期");
//                break;
//            case LivenessActivity.RESULT_SDK_INIT_FAIL_OUT_OF_DATE:
//                showToast(Constant.ERROR_SDK_INITIALIZE);
//                Println.out(  "onActivityResult" + "   resultCode:" , resultCode+",算法SDK初始化失败：可能是授权文件或模型路径错误，SDK权限过期，包名绑定错误");
//                break;
//            case LivenessActivity.RESULT_CREATE_HANDLE_ERROR:
//                showToast(Constant.ERROR_SDK_INITIALIZE);
//                Println.out(  "onActivityResult" + "   resultCode:", resultCode+",算法SDK初始化失败：可能是授权文件或模型路径错误，SDK权限过期，包名绑定错误");
//                break;
//            case LivenessActivity.RESULT_CAMERA_ERROR_NOPRERMISSION_OR_USED:
//                showToast(Constant.ERROR_CAMERA_REFUSE);
//                Println.out( "onActivityResult" + "   resultCode:", resultCode+",相机权限获取失败或权限被拒绝");
//                break;
//            case LivenessActivity.RESULT_CANCELED:
//                showToast("检测取消");
//                Println.out(  "onActivityResult" + "   resultCode:" , resultCode+",检测取消");
//                break;
//        }
//        finish();
//    }
//    /**
//     * 活体检测成功
//     * @param data
//     */
//    public void detectSuccess(Intent data) {
//        /***********************************************
//         * 请将图片上传到自己服务端,调用魔蝎接口进行实人认证*
//         *                                             *
//         ***********************************************/
//
//        MXReturnResult returnResult = new MXReturnResult();
//        if (data != null) {
//            returnResult = (MXReturnResult) data.getSerializableExtra(LivenessActivity.KEY_DETECT_RESULT);
//        }
//        MXLivenessSDK.MXLivenessImageResult[] images = returnResult.getImageResults();
//
//        Bitmap imageBitmap = BitmapFactory.decodeByteArray(images[0].image, 0, images[0].length);
//        ImageViewID.setIdcardbitmap(imageBitmap);
//        presenter.requestSaveLiveness(images[0].image);
//    }
//
//    @Override
//    public void livenessSuccess() {
//
//
//        List<File> fileList = new ArrayList<>();
//
//        String idforntpath = BitmapUtils.saveCompressBitmap(this,"idfornt");
//        String idbackpath = BitmapUtils.saveCompressBitmap(this,"idback");
//        String livenesspath = BitmapUtils.saveCompressBitmap(this,"liveness");
//        BitmapUtils.compressImage(ImageViewID.getIdfacebitmap(),idforntpath);
//        BitmapUtils.compressImage(ImageViewID.getIdbackbitmap(),idbackpath);
//        BitmapUtils.compressImage(ImageViewID.getIdcardbitmap(),livenesspath);
//        File filefornt = new File(idforntpath);
//        File fileback = new File(idbackpath);
//        File fileliveness = new File(livenesspath);
//        fileList.add(filefornt);
//        fileList.add(fileback);
//        fileList.add(fileliveness);
//        presenter.requestSaveImgae(fileList);
//        UIUtils.toast("认证成功",false);
//        User.getInstance().setIdcar(User.getInstance().getIdcarbackup());
//        finish();
//
//    }
//
//    @Override
//    public void livenessFailure(String msg) {
//        UIUtils.toast(msg+",请重试",false);
//        requestPermissions();
//    }
//
//    @Override
//    public void idcardFrontOcrResult(boolean success) {
//        if(success){
//            IDCARD_FRONT_RESULT = 1;
//        }else {
//            IDCARD_FRONT_RESULT = 2;
//        }
//
//        eventBus.post(new News());
//
//    }
//
//    @Override
//    public void idcardBackOcrResult(boolean success) {
//        if(success){
//            IDCARD_BACK_RESULT = 1;
//        }else {
//            IDCARD_BACK_RESULT = 2;
//        }
//
//        eventBus.post(new News());
//
//    }
//
//    @Override
//    public void saveImageSuccess() {
//        UIUtils.toast("认证成功",false);
//        User.getInstance().setIdcar(User.getInstance().getIdcarbackup());
//        finish();
//    }
//
//    @Override
//    public void showdialog() {
//
//    }
//
//    public void showToast(final String message) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//               UIUtils.toast(message,false);
//            }
//        });
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(News news){
//        int failure = 2;
//        int success = 1;
//        int loading = 0;
//
//        if((IDCARD_BACK_RESULT==success) && (IDCARD_FRONT_RESULT ==success)){
//            loadingdialog.dismiss();
//             requestPermissions();
//
//        }else if((IDCARD_BACK_RESULT==failure) && (IDCARD_FRONT_RESULT ==success)){
//            loadingdialog.dismiss();
//            UIUtils.toast("身份证反面认证失败，请重试",true);
//            UIUtils.getHandler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                }
//            },1000);
//
//
//        }else if((IDCARD_BACK_RESULT==success) && (IDCARD_FRONT_RESULT ==failure)){
//            loadingdialog.dismiss();
//            UIUtils.toast("身份证正面认证失败，请重试",true);
//            UIUtils.getHandler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                }
//            },1000);
//
//        }else if((IDCARD_BACK_RESULT !=loading) && (IDCARD_FRONT_RESULT !=loading)){
//            loadingdialog.dismiss();
//            UIUtils.toast("认证失败，请重试",true);
//            UIUtils.getHandler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                }
//            },1000);
//
//        }
//
//    }
//
//
//
//
//
//}

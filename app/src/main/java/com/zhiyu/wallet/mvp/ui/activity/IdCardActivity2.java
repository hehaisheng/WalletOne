package com.zhiyu.wallet.mvp.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.authreal.api.AuthBuilder;
import com.authreal.api.OnResultListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.bean.Credit;
import com.zhiyu.wallet.bean.ImageViewID;
import com.zhiyu.wallet.bean.News;
import com.zhiyu.wallet.bean.User;
import com.zhiyu.wallet.bean.YDResult;
import com.zhiyu.wallet.common.Constant;
import com.zhiyu.wallet.mvp.contract.AuthenticateContract;
import com.zhiyu.wallet.mvp.contract.IdcardContract;
import com.zhiyu.wallet.mvp.contract.IdcardContract2;
import com.zhiyu.wallet.mvp.presenter.AuthenticatePresenter;
import com.zhiyu.wallet.mvp.presenter.IdcardPresenter;
import com.zhiyu.wallet.mvp.presenter.IdcardPresenter2;
import com.zhiyu.wallet.util.BitmapUtils;
import com.zhiyu.wallet.util.Println;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;
import com.zhiyu.wallet.util.UtilsDialog;
import com.zhiyu.wallet.widget.CheckBoxSample;
//import com.zmxy.ZMCertification;
//import com.zmxy.ZMCertificationListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * 2018/7/25
 *
 * @author ZhiYu
 */
public class IdCardActivity2 extends BaseActivity<IdcardPresenter2> implements IdcardContract2.IdcardIView2 {

    @BindView(R.id.iv_title_back)
    LinearLayout ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R.id.id_face_checkbox)
    CheckBoxSample idfacecheckbox;
    @BindView(R.id.id_back_checkbox)
    CheckBoxSample idbackcheckbox;
    @BindView(R.id.id_card_checkbox)
    CheckBoxSample idcardcheckbox;
    @BindView(R.id.iv_face)
    ImageView imageView;


    private static final int PICTURE = 100;
    private static final int CAMERA = 200;
    private static final int FACE = 1001;
    private String pathResult;
//    private ZMCertification zmCertification;
    private Uri selectedImage;
    private Bitmap bitmap;
    private Dialog dialog;
    public EventBus eventBus;
    public String bizNo;
    private String picPath;
    private Uri mImagepictureUri;


    @Override
    protected void initData() {
        presenter.attachview(this);

        StatusbarUtil.setdropBarSystemWindows(this,getResources().getColor(android.R.color.white));
//        eventBus = EventBus.getDefault();
//        eventBus.register(this);
//        ImageViewID.setImageViewName("idFace");
//        UIUtils.toast("请拍下身份证正面照片，确保照片清晰",true, Gravity.CENTER);
//        requestPermissions();

        String urlNotify = null;
        String id = "orderid"+new Date().getTime();
        AuthBuilder mAuthBuilder = new AuthBuilder(id, Constant.authKey, urlNotify, new OnResultListener() {
            @Override
            public void onResult(String s) {
                Gson gson = new Gson();
                YDResult ydResult = gson.fromJson(s,YDResult.class);
                Println.out("result:",gson.toJson(ydResult));
                switch (  ydResult.getRet_code() ) {
                    case "000000":
                        if("T".equals(ydResult.getResult_auth())){
                            User.getInstance().setIdcarbackup(ydResult.getId_no());

                            showdialog();

                            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
                            ImageViewID.setIdfacepath(BitmapUtils.saveCompressBitmap(IdCardActivity2.this,User.getInstance().getPhone()+simpleDateFormat.format(new Date())));

                            ImageViewID.setIdbackpath(BitmapUtils.saveCompressBitmap(IdCardActivity2.this,User.getInstance().getPhone()+simpleDateFormat.format(new Date())+1));

                            ImageViewID.setIdcardpath(BitmapUtils.saveCompressBitmap(IdCardActivity2.this,User.getInstance().getPhone()+simpleDateFormat.format(new Date())+2));

                            Glide.with(IdCardActivity2.this).asBitmap().load(ydResult.getUrl_frontcard()).into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                   BitmapUtils.compressImage(bitmap,ImageViewID.getIdfacepath());
                                }
                            });

                            Glide.with(IdCardActivity2.this).asBitmap().load(ydResult.getUrl_backcard()).into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                    BitmapUtils.compressImage(bitmap,ImageViewID.getIdbackpath());
                                }
                            });

                            Glide.with(IdCardActivity2.this).asBitmap().load(ydResult.getUrl_photoliving()).into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                                    BitmapUtils.compressImage(bitmap,ImageViewID.getIdcardpath());
                                }
                            });

                            UIUtils.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    presenter.saveIdcardpicture(ImageViewID.getIdfacepath(),ImageViewID.getIdbackpath(),ImageViewID.getIdcardpath());
                                }
                            },1000);

                        }else {
                            UIUtils.toast(ydResult.getRet_msg(), true );
                            finish();break;
                        }
                        break;
                    case "900001":finish();break;
                    default:
                        UIUtils.toast(ydResult.getRet_msg(), true );
                        finish();break;
                }

            }

        });
        mAuthBuilder.isShowConfirm(false);

        mAuthBuilder.faceAuth(IdCardActivity2.this);

    }

    @Override
    protected void initTitle() {
        ivTitleBack.setVisibility(View.VISIBLE);
        tvTitle.setText("身份证认证");
        ivTitleSetting.setVisibility(View.GONE);
    }

    @OnClick(R.id.iv_title_back)
    public void back(View view) {
        removeCurrentActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_authen_idcard;
    }

    @Override
    protected IdcardPresenter2 createPresenter() {
        return new IdcardPresenter2();
    }


    private void selectId() {
        String[] items = new String[]{"图库", "相机"};
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("选择来源")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(picture, PICTURE);
                                break;
                            case 1:
                                open_carmera();
                                break;
                        }
                    }
                })
                .setCancelable(true)
                .show();
        dialog.setCanceledOnTouchOutside(false);
    }

    private void open_carmera(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        pathResult = BitmapUtils.saveCompressBitmap(this,"");

        // 判断7.0android系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //临时添加一个拍照权限
            camera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //通过FileProvider获取uri
            mImagepictureUri = FileProvider.getUriForFile(IdCardActivity2.this,
                    "com.zhiyu.wallet.fileprovider",
                    new File(pathResult));
        } else {
            mImagepictureUri = Uri.fromFile(new File(pathResult));
        }

        camera.putExtra(MediaStore.EXTRA_OUTPUT, mImagepictureUri);
        startActivityForResult(camera, CAMERA);
    }

    public void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(IdCardActivity2.this);
        rxPermission.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) {
                if (!granted) {
                    UIUtils.toast("应用未能获取全部需要的相关权限，部分功能可能不能正常使用.", false);
                    finish();
                }else {
                        open_carmera();
//                      selectId();
                }

            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA && resultCode == RESULT_OK ) {//相机
            //获取intent中的图片对象
//            Bundle extras = data.getExtras();
//            bitmap = (Bitmap) extras.get("data");
//            try {
//                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImagepictureUri));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
            //对获取到的bitmap进行压缩、圆形处理
            bitmap = BitmapUtils.getSmallBitmap(this, mImagepictureUri, UIUtils.dp2px(400),UIUtils.dp2px(300));

        } else if (requestCode == PICTURE && resultCode == RESULT_OK && data != null) {//图库
            selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picPath = cursor.getString(columnIndex);   // 图片的绝对路径

            bitmap = BitmapUtils.getSmallBitmap(this, picPath, UIUtils.dp2px(400),UIUtils.dp2px(300));
            pathResult = BitmapUtils.saveCompressBitmap(this,"");
            //ImageViewID.getImageView().setImageBitmap(bitmap);
            cursor.close();
        }

        if(resultCode == RESULT_OK ){
            setConfirm();
            return;
        }
        finish();

    }


    public void setConfirm(){
        showdialog();
        switch (ImageViewID.getImageViewName()){
            case "idFace":
                ImageViewID.setIdfacebitmap(bitmap);
                ImageViewID.setIdfacepath(pathResult);
                if(TextUtils.isEmpty(ImageViewID.getIdfacepath())){
                    UIUtils.toast("请重新拍摄身份证正面照片",true, Gravity.CENTER);
                    closedialog();
                    requestPermissions();
                    return;
                }

                BitmapUtils.compressImage(ImageViewID.getIdfacebitmap(),ImageViewID.getIdfacepath());
                presenter.requestIdCardAIORC(ImageViewID.getIdfacepath(),0);
                break;
            case "idBack":
                ImageViewID.setIdbackbitmap(bitmap);
                ImageViewID.setIdbackpath(pathResult);
                if(TextUtils.isEmpty(ImageViewID.getIdbackpath())){
                    UIUtils.toast("请重新拍摄身份证反面照片",true, Gravity.CENTER);
                    closedialog();
                    requestPermissions();
                    return;
                }

                BitmapUtils.compressImage(ImageViewID.getIdbackbitmap(),ImageViewID.getIdbackpath());
                presenter.requestIdCardAIORC(ImageViewID.getIdbackpath(),1);break;
            case "idCard":
                ImageViewID.setIdcardbitmap(bitmap);
                ImageViewID.setIdcardpath(pathResult);
                if(TextUtils.isEmpty(ImageViewID.getIdcardpath())){
                    UIUtils.toast("请添加手持身份证照片",true, Gravity.CENTER);
                    closedialog();
                    requestPermissions();
                    return;
                }

                BitmapUtils.compressImage(ImageViewID.getIdcardbitmap(),ImageViewID.getIdcardpath());
                idCard_detectfaceSuccess();break;
            default:break;
        }
        bitmap=null;

       // requestPermissions2();


    }

    public void requestPermissions2() {
        RxPermissions rxPermission = new RxPermissions(IdCardActivity2.this);
        rxPermission.request(
                Manifest.permission.CAMERA
        ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) {
                if (!granted) {
                    UIUtils.toast("App未能获取全部需要的相关权限，部分功能可能不能正常使用.", true);
                }else {
                    if(!idfacecheckbox.isChecked()){
                        BitmapUtils.compressImage(ImageViewID.getIdfacebitmap(),ImageViewID.getIdfacepath());
                        presenter.requestIdCardAIORC(ImageViewID.getIdfacepath(),0);
                    }
                    if(!idbackcheckbox.isChecked()){
                        BitmapUtils.compressImage(ImageViewID.getIdbackbitmap(),ImageViewID.getIdbackpath());
                        presenter.requestIdCardAIORC(ImageViewID.getIdbackpath(),1);
                    }
                    if(!idcardcheckbox.isChecked()){
                        BitmapUtils.compressImage(ImageViewID.getIdcardbitmap(),ImageViewID.getIdcardpath());
//                        presenter.requestIdCardDetectmultiface(ImageViewID.getIdcardpath());
                        idCard_detectfaceSuccess();
                    }

                }

            }
        });

    }


    @Override
    public void idTaiOCRsuccess(String name, String id) {
        //删除操作
        //BitmapUtils.delectImage(ImageViewID.getIdfacepath());
        User.getInstance().setIdcarbackup(id);
        presenter.requestZMCerification(name,id);

    }

    @Override
    public void idTaiOCRFailure(String msg) {
        closedialog();
         UIUtils.toast(msg,true);
//      Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
        requestPermissions();
    }


    @Override
    public void idTaiOCRBacksuccess() {
        closedialog();

        idbackcheckbox.setChecked(true);
//        eventBus.post(new News());
        ImageViewID.setImageViewName("idCard");
        UIUtils.toast("请拍下手持身份证照片，确保照片清晰",true, Gravity.CENTER);
        requestPermissions();
    }

    @Override
    public void idTaiOCRBackFailure(String msg) {

        closedialog();
         UIUtils.toast(msg,true, Gravity.CENTER);
//        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
        requestPermissions();
    }


    @Override
    public void zMatteInitsuccess(String msg, String bizNo) {
        //Log.d("IdcardActivity", "msg is " + msg + "bizNo is "+bizNo);
        closedialog();
        this.bizNo = bizNo;
        idfacecheckbox.setChecked(true);
//        eventBus.post(new News());
        ImageViewID.setImageViewName("idBack");
        UIUtils.toast("请拍下身份证反面照片，确保照片清晰",true, Gravity.CENTER);
        requestPermissions();

    }

    @Override
    public void zMatteInitFailure(String msg) {
        closedialog();
        UIUtils.toast(msg,true, Gravity.CENTER);
        requestPermissions();
    }

    @Override
    public void idCard_detectfaceSuccess() {

        closedialog();
//        presenter.saveIdcardpicture(ImageViewID.getIdfacepath(),ImageViewID.getIdbackpath(),ImageViewID.getIdcardpath());
//        zmCertification = ZMCertification.getInstance();
//        zmCertification.setZMCertificationListener(IdCardActivity2.this);
//        zmCertification.startCertification(IdCardActivity2.this, bizNo, Constant.MerchantID, null);
    }

    @Override
    public void idCard_detectfaceFailure(String msg) {
        closedialog();
        UIUtils.toast(msg,true, Gravity.CENTER);
        requestPermissions();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(News News){
        System.out.println("onevent");
    //if(idbackcheckbox.isChecked() && idfacecheckbox.isChecked() && idcardcheckbox.isChecked()){
    //      Println.out("startzm","!!!!!!!~~~~~~~");
    //
    // }
    }

    @Override
    public void saveIdBitmapsuccess(String msg) {
        User.getInstance().setIdcar(User.getInstance().getIdcarbackup());
        closedialog();
        UIUtils.toast(msg,false);
        finish();
    }

    @Override
    public void saveIdBitmapFailure(String msg) {
        UIUtils.toast(msg,true);
        finish();
    }

    @Override
    public void showdialog() {
        dialog= UtilsDialog.createLoadingDialog(this,"认证中...");
    }


    @Override
    public void closedialog() {
        UtilsDialog.closeDialog(dialog);
    }

//    @Override
//    public void onFinish(boolean isCanceled, boolean isPassed, int errorCode) {
//        zmCertification.setZMCertificationListener(this);
//        String msg = "认证失败";
//        if (isCanceled){
//            UIUtils.toast(msg,false);
//            finish();
//            return;
//        }
//
//        if(isPassed){
//            idcardcheckbox.setChecked(true);
////            eventBus.post(new News());
//            //认证通过
//            showdialog();
//            presenter.saveIdcardpicture(ImageViewID.getIdfacepath(),ImageViewID.getIdbackpath(),ImageViewID.getIdcardpath());
//        }else {
//            switch (errorCode){
//                case 13:
//                    msg=" bizNO和merchantID不匹配";break;
//                case 9:
//                    msg="传入的bizNO 有误";break;
//                case 15:
//                    msg="身份证号和姓名的格式不正确";break;
//                    default:break;
//            }
//            UIUtils.toast(msg,true);
//            finish();
//        }
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BitmapUtils.delectImage(pathResult);
        BitmapUtils.delectImage(ImageViewID.getIdfacepath());
        BitmapUtils.delectImage(ImageViewID.getIdbackpath());
        BitmapUtils.delectImage(ImageViewID.getIdcardpath());
    }
}

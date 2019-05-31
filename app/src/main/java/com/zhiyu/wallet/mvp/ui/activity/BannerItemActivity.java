package com.zhiyu.wallet.mvp.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.mvp.presenter.HomePresenter;
import com.zhiyu.wallet.util.BitmapUtils;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UpdateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.bean.ImageInfo;

/**
 * @
 * Created by Administrator on 2018/9/16.
 */

public class BannerItemActivity extends BaseActivity<HomePresenter> implements View.OnClickListener{

    @BindView(R.id.iv_title_back)
    LinearLayout ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Nullable
    @BindView(R.id.tv_service_text2)
    TextView tv_service_text2;
    @Nullable
    @BindView(R.id.tv_service_text3)
    TextView tv_service_text3;
    @Nullable
    @BindView(R.id.tv_service_text4)
    TextView tv_service_text4;
    @Nullable
    @BindView(R.id.tv_service_text5)
    TextView tv_service_text5;
    @Nullable
    @BindView(R.id.tv_service_text6)
    TextView tv_service_text6;

    @Nullable
    @BindView(R.id.iv_arbitration_item2)
    ImageView iv_arbitration_item2;
    @Nullable
    @BindView(R.id.iv_arbitration_item3)
    ImageView iv_arbitration_item3;
    @Nullable
    @BindView(R.id.iv_arbitration_item4)
    ImageView iv_arbitration_item4;


    private String type ="";
    private int mLayout;
    Unbinder unbinder;
    private String[] paths = new String[3];
    private final List<ImageInfo> imageInfoList = new ArrayList<>();

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void initData() {
        StatusbarUtil.setdropBarSystemWindows(this,getResources().getColor(android.R.color.white));
        unbinder = ButterKnife.bind(this);
        String text2 = getResources().getString(R.string.service_text2);
//        String ptext2 = text2.substring(text2.length()-33,text2.length()-1);
//        tv_service_text2.setText(Html.fromHtml(String.format(text2,"<font color=\"#ef2f65\">"+ptext2+"</font>")));
//
        String text3 = getResources().getString(R.string.service_text3);
//        String ptext3 = text3.substring(33,text3.length()-44);
//        tv_service_text3.setText(Html.fromHtml(String.format(text3,"<font color=\"#ef2f65\">"+ptext3+"</font>")));
//
        String text6 = getResources().getString(R.string.service_text6);
//        String ptext6 = text6.substring(text6.length()-57,text6.length()-1);
//        tv_service_text6.setText(Html.fromHtml(String.format(text6,"<font color=\"#ef2f65\">"+ptext6+"</font>")));

//        SpannableStringBuilder builder2 = new SpannableStringBuilder(tv_service_text2.getText().toString());
//        ForegroundColorSpan blueSpan2 = new ForegroundColorSpan(Color.parseColor("#ef2f65"));
//        builder2.setSpan(blueSpan2, text2.length()-33,text2.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //字体颜色
//
//        SpannableStringBuilder builder3 = new SpannableStringBuilder(tv_service_text3.getText().toString());
//        ForegroundColorSpan blueSpan3 = new ForegroundColorSpan(Color.parseColor("#ef2f65"));
//        builder3.setSpan(blueSpan3, 33,text3.length()-44, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //字体颜色
//
//        SpannableStringBuilder builder6 = new SpannableStringBuilder(tv_service_text6.getText().toString());
//        ForegroundColorSpan blueSpan6 = new ForegroundColorSpan(Color.parseColor("#ef2f65"));
//        builder6.setSpan(blueSpan6, text6.length()-57,text6.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //字体颜色



        if(type.equals("仲裁名单")){
            String item3 = BitmapUtils.saveCompressBitmap(this,"item3");
            BitmapUtils.compressImage(BitmapFactory.decodeResource(getResources(),R.mipmap.bannerarbitrationitem3),item3);

            String item4 = BitmapUtils.saveCompressBitmap(this,"item5");
            BitmapUtils.compressImage(BitmapFactory.decodeResource(getResources(),R.mipmap.bannerarbitrationitem5),item4);

            String item5 = BitmapUtils.saveCompressBitmap(this,"item4");
            BitmapUtils.compressImage(BitmapFactory.decodeResource(getResources(),R.mipmap.bannerarbitrationitem4),item5);
            paths[0]=item3;
            paths[1]=item4;
            paths[2]=item5;
            iv_arbitration_item2.setOnClickListener(this);
            iv_arbitration_item3.setOnClickListener(this);
            iv_arbitration_item4.setOnClickListener(this);
        }

    }

    @Override
    protected void initTitle() {

        ivTitleBack.setVisibility(View.VISIBLE);
        switch (type){

            case "易借服务":tvTitle.setText("易借服务");;break;
            case "仲裁名单":tvTitle.setText("易借宝仲裁名单");;  break;

        }



    }

    @OnClick(R.id.iv_title_back)
    public void back(View view) {
        //销毁当前的页面
        this.removeCurrentActivity();

    }

    @Override
    protected int getLayoutId() {
        Bundle bundle = getIntent().getBundleExtra("data");

        if(bundle!=null){
            type = bundle.getString("type");

        }
        setmLayout();

        return mLayout;
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }

    public void setmLayout(){
        switch (type){

            case "易借服务":mLayout=R.layout.activity_banner_service_item;break;
            case "仲裁名单":mLayout=R.layout.activity_banner_arbitration_item;  break;

        }

    }

    @Override
    public void onClick(View v) {
        ImageInfo imageInfo;
        final List<ImageInfo> imageInfoList = new ArrayList<>();


        switch (v.getId()){
            case R.id.iv_arbitration_item2:
                imageInfo = new ImageInfo();
                imageInfo.setOriginUrl(paths[0]);
                imageInfo.setThumbnailUrl(paths[0]);
                imageInfoList.add(imageInfo);
                imageInfo = null;break;
            case R.id.iv_arbitration_item3:
                imageInfo = new ImageInfo();
                imageInfo.setOriginUrl(paths[1]);
                imageInfo.setThumbnailUrl(paths[1]);
                imageInfoList.add(imageInfo);
                imageInfo = null;break;
            case R.id.iv_arbitration_item4:
                imageInfo = new ImageInfo();
                imageInfo.setOriginUrl(paths[2]);
                imageInfo.setThumbnailUrl(paths[2]);
                imageInfoList.add(imageInfo);
                imageInfo = null;break;
        }
        ImagePreview
                .getInstance()
                .setContext(BannerItemActivity.this)
                .setIndex(0)
                .setImageInfoList(imageInfoList)
                .setShowDownButton(true)
                .setLoadStrategy(ImagePreview.LoadStrategy.AlwaysOrigin)
                .setFolderName("")
                .setScaleLevel(1, 3, 8)
                .setZoomTransitionDuration(300)

                .setEnableClickClose(true)// 是否启用点击图片关闭。默认启用
                .setEnableDragClose(false)// 是否启用上拉/下拉关闭。默认不启用

                .setShowCloseButton(true)// 是否显示关闭页面按钮，在页面左下角。默认显示
                .setCloseIconResId(R.drawable.ic_action_close)// 设置关闭按钮图片资源，可不填，默认为：R.drawable.ic_action_close

                .setShowDownButton(false)// 是否显示下载按钮，在页面右下角。默认显示
                .setDownIconResId(R.drawable.icon_download_new)// 设置下载按钮图片资源，可不填，默认为：R.drawable.icon_download_new

                .setShowIndicator(false)// 设置是否显示顶部的指示器（1/9）。默认显示
                .setErrorPlaceHolder(R.drawable.load_failed)// 设置失败时的占位图，默认为R.drawable.load_failed，设置为0时不显示
                .start();

    }
}


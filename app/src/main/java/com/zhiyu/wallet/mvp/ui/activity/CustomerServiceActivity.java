package com.zhiyu.wallet.mvp.ui.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.mvp.presenter.HomePresenter;
import com.zhiyu.wallet.util.BitmapUtils;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;
import com.zhiyu.wallet.util.UpdateUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @
 * Created by Administrator on 2018/9/16.
 */

public class CustomerServiceActivity extends BaseActivity<HomePresenter> {

    @BindView(R.id.iv_title_back)
    LinearLayout ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ly_about)
    LinearLayout lyabout;
    @BindView(R.id.btn_copywechatId)
    Button copywechat;
    @BindView(R.id.btn_saveQRCode)
    Button saveQRcode;
    @BindView(R.id.iv_qrcode)
    ImageView qrcode;

    private String type ="";

    @Override
    protected void initData() {
        StatusbarUtil.setdropBarSystemWindows(this,getResources().getColor(android.R.color.white));
    }

    @Override
    protected void initTitle() {

        ivTitleBack.setVisibility(View.VISIBLE);
        tvTitle.setText("客服");

    }

    @OnClick(R.id.iv_title_back)
    public void back(View view) {
        //销毁当前的页面
        this.removeCurrentActivity();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_customer;
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @OnClick(R.id.btn_copywechatId)
    public void onCopy(){

        ClipboardManager clipboardManager  = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText("易借宝官方");

        UIUtils.toast("复制成功",false);

    }

    @OnClick(R.id.btn_saveQRCode)
    public void onSaveQrcode(){
        Drawable drawable = qrcode.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

        if(BitmapUtils.saveBitmap2Gallery(this,bitmap,"qrcode")){

            UIUtils.toast("保存二维码成功",false);
        };

    }


}


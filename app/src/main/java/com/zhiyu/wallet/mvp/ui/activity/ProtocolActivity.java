package com.zhiyu.wallet.mvp.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.mvp.presenter.BasePresenter;
import com.zhiyu.wallet.util.StatusbarUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 2018/7/18
 *
 * @author ZhiYu
 *
 * 认证详情界面
 */
public class ProtocolActivity extends BaseActivity {


    @BindView(R.id.iv_title_back)
    LinearLayout ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.web_view)
    WebView webView;

    private String urlpath="";
    private String url="";


    @Override
    protected void initData() {
        StatusbarUtil.setdropBarSystemWindows(this, Color.WHITE);

        init();
    }

    private void init() {
//        webView.loadUrl("file:///android_asset/web/demo1.html");
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                tvTitle.setVisibility(View.VISIBLE);
            }
        });

        switch (urlpath){
            case "userinfo2" :   url="file:///android_asset/web/UseinfoProtocol.html";break;
            case "userinfo":       url="file:///android_asset/web/UseinfoProtocol.html";break;
            case "acceeptrepay":       url="file:///android_asset/web/AcceptrepayProtocol.html";break;
            case "credit":        url="file:///android_asset/web/CreditInvestigationProtocol.html";break;
            case "repaycontract":         url="file:///android_asset/web/RepayContractProtocol.html";break;
            case "register":         url="file:///android_asset/web/RegisterProtocol.html";break;

        }
        webView.loadUrl(url);

    }

    @Override
    protected void initTitle() {
        Bundle bundle = getIntent().getBundleExtra("data");
        if(bundle!=null){
            urlpath = bundle.getString("type");
        }

        switch (urlpath){
            case "userinfo2" :   tvTitle.setText(getResources().getString(R.string.protocol));break;
            case "userinfo":        tvTitle.setText(getResources().getString(R.string.protocoluserinfo));break;
            case "acceeptrepay":        tvTitle.setText(getResources().getString(R.string.protocolacceeptrepay));break;
            case "credit":        tvTitle.setText(getResources().getString(R.string.protocolCreditInvestigation));break;
            case "repaycontract":        tvTitle.setText(getResources().getString(R.string.protocolrepaycontract));break;
            case "register":        tvTitle.setText((getResources().getString(R.string.protocolregister)).replace("《","").replace("》",""));break;

        }

        ivTitleBack.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.iv_title_back)
    public void back(View view) {
        //销毁当前的页面
        this.removeCurrentActivity();

    }





    @Override
    protected int getLayoutId() {
        return R.layout.activity_protocol;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


}

package com.zhiyu.wallet.mvp.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.adapter.NewsCentetAdapter;
import com.zhiyu.wallet.base.BaseActivity;
import com.zhiyu.wallet.bean.News;
import com.zhiyu.wallet.mvp.contract.NewsCenterContract;
import com.zhiyu.wallet.mvp.contract.UserInfoContract;
import com.zhiyu.wallet.mvp.presenter.NewsCenterPresenter;
import com.zhiyu.wallet.mvp.presenter.UserInfoPresenter;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 2018/8/12
 *
 * @author zhiyu
 */
public class NewsCenterActivity extends BaseActivity<NewsCenterPresenter> implements NewsCenterContract.NewsCenterIView {
    @BindView(R.id.iv_title_back)
    LinearLayout ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.list_news)
    ListView listNews;


    @Override
    protected void initData() {

        presenter.attachview(this);
        presenter.requestNewsData();
        StatusbarUtil.setdropBarSystemWindows(this,Color.WHITE);


    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_center;
    }

    @Override
    protected NewsCenterPresenter createPresenter() {
        return new NewsCenterPresenter();
    }


    @Override
    public void showNewsData(List<News> list) {
        TextView textView = new TextView(this);
        textView.setText("没有更多了");
        textView.setTextColor(Color.parseColor("#646464"));
        textView.setTextSize(11f);
        RelativeLayout re = new RelativeLayout(getApplicationContext());
        RelativeLayout.LayoutParams layte = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        layte.setMargins(0, UIUtils.dp2px(30), 0, UIUtils.dp2px(25));
        layte.addRule(RelativeLayout.CENTER_HORIZONTAL);
        re.addView(textView, layte);
        re.setBackgroundColor(getResources().getColor(R.color.line_bg));
        listNews.addFooterView(re, 0, false);
        listNews.setAdapter(new NewsCentetAdapter(list, this));

    }

    @Override
    public void showHelpcenterData(List<News> helplist) {

    }

    @Override
    public void showErrorMsg(String msg) {
        UIUtils.toast(msg,false);
    }

    @OnClick(R.id.iv_title_back)
    public void back(View view) {
        this.removeCurrentActivity();
    }


}

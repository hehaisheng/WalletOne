package com.zhiyu.wallet.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.zhiyu.wallet.mvp.presenter.BasePresenter;
import com.zhiyu.wallet.mvp.ui.BaseIView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 2018/7/9
 *
 * @author haomi
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseIView {


    protected P presenter;

    protected abstract P createPresenter();

    //初始化界面的数据
    protected abstract void initData();

    //初始化title
    protected abstract void initTitle();

    //提供布局
    public abstract int getLayoutId();

    public abstract void iniRefresh();

    public abstract void finishRefresh(View view);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = createPresenter();
        if (presenter == null) {
            throw new NullPointerException("Presenter is null! Do you return null in createPresenter()?");
        }

        presenter.attachview(this);
        return inflater.inflate(getLayoutId(),null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTitle();
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter == null) {
            throw new NullPointerException("Presenter is null! Do you return null in createPresenter()?");
        }else {
            presenter.detachview();
         //   unbinder.unbind();
        }
    }




}

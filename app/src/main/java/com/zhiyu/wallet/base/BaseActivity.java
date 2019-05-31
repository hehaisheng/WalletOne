package com.zhiyu.wallet.base;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;


import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhiyu.wallet.common.ActivityManager;
import com.zhiyu.wallet.mvp.presenter.BasePresenter;
import com.zhiyu.wallet.mvp.ui.activity.MainActivity;
import com.zhiyu.wallet.util.UIUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;

/**
 * 2018/7/12
 *
 * @author my
 */
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity {

    private Unbinder bind;
    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        bind = ButterKnife.bind(this);
        presenter=createPresenter();
        ActivityManager.getInstance().add(this);
        initTitle();
        initData();
    }


    protected abstract void initData();

    protected abstract void initTitle();

    protected abstract int getLayoutId();

    protected abstract P createPresenter();

    public void goToActivity(Class Activity, Bundle bundle){
        Intent intent = new Intent(this,Activity);
        //携带数据
        if(bundle != null && bundle.size() != 0){
            intent.putExtra("data",bundle);
        }
        startActivity(intent);
    }
    //销毁当前的Activity
    public void removeCurrentActivity(){
        ActivityManager.getInstance().removeCurrent();
    }

    //请求权限
    public void requestPermissions() {
        final RxPermissions rxPermission = new RxPermissions(this);
        rxPermission.request(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_SMS
//                Manifest.permission.GET_ACCOUNTS,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
        ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) {
                if (!granted) {
                    UIUtils.toast("应用未能获取全部需要的相关权限，部分功能可能不能正常使用.", true);
                    // requestPermissions();
                }

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter!=null) {
            presenter.detachview();
            bind.unbind();
        }
    }
}

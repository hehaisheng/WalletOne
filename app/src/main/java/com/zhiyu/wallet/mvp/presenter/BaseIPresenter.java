package com.zhiyu.wallet.mvp.presenter;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/11/1.
 */

public interface BaseIPresenter<V> {


    V getView();

    void _attach(V view);

    void attachview(V iview);

    void detachview();
}

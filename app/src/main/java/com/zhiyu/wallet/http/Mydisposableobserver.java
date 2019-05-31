package com.zhiyu.wallet.http;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by Administrator on 2018/11/26.
 */

public class Mydisposableobserver<T> extends DisposableObserver<T> {


    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}

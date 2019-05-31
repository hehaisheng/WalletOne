package com.zhiyu.wallet.http;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/10/29.
 */

public class BaseCommonModel {



    public <T> Observable<T> observa(Observable<T> service){

        return    service.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public <T> void observa(Observable<T> service,DisposableObserver<T> disposableObserver){
        if(service==null){
            return ;
        }

        if(disposableObserver==null){
            return ;
        }
        service.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);

    }
}

package com.zhiyu.wallet.mvp.presenter;

import com.zhiyu.wallet.mvp.ui.BaseIView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/7/17.
 */

public abstract class BasePresenter<V extends BaseIView>  implements BaseIPresenter<V>{

    protected V Iview;
    private WeakReference<V> viewRef;

    @Override
    public V getView() {
        return viewRef.get();
    }

    @Override
    public void _attach(V view) {
        viewRef = new WeakReference<V>(view);
    }

    @Override
    public void  attachview(V iview){
        Iview=iview;
        _attach(iview);
    }

    @Override
    public void detachview(){
        Iview=null;
        if(viewRef!=null){
            viewRef.clear();
            viewRef=null;
        }
    }


}

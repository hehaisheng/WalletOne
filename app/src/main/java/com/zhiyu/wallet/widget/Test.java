package com.zhiyu.wallet.widget;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2019/3/22.
 */

public class Test extends ViewGroup implements NestedScrollingParent {


    public Test(Context context) {
        super(context);
    }

    public Test(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Test(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


}

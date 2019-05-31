package com.zhiyu.wallet.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.zhiyu.wallet.R;

import java.lang.ref.SoftReference;

/**
 * Created by Administrator on 2018/11/12.
 * @author Mr.Luluxiu
 */

public class StatusbarUtil  {

    public static void setdropBarSystemWindows(Activity activity, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {      //19  4.4
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {    //21  5.0 以上直接设置状态栏颜色
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option;
                if(color==Color.WHITE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){       //23   6.0
                    option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;  //状态栏黑色字体
                }else {
                    option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                }

                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                window.setStatusBarColor(color);
                setFitsSystemWindows(activity,true);
                //window.setStatusBarColor(Color.TRANSPARENT);

            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
                getStatusViewWithColor(activity, color);
            }
        }

    }

    public static void setfullScreenSystemWindows(Activity activity, int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {      //19  4.4
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {    //21  5.0 以上直接设置状态栏颜色
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option;
                if(color==Color.WHITE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){       //23   6.0
                    option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;  //状态栏黑色字体
                }else {
                    option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                }

                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);

            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }


    }

    public static void setdropBarSystemWindowsWithTopPadding(final Activity activity, final Drawable backResorce){
        setFitsSystemWindows(activity,false);
        ViewGroup contentView =  activity.getWindow().getDecorView().findViewById(android.R.id.content);
        contentView.setPadding(0,getStatusBarHeight(activity),0,0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {      //19  4.4
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {    //21  5.0 以上直接设置状态栏颜色
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                decorView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        int identifier = activity.getResources().getIdentifier("statusBarBackground","id","android");
                        View statusView = activity.findViewById(identifier);
                        if(statusView!=null){
                            statusView.setBackground(backResorce);
                            statusView.removeOnLayoutChangeListener(this);
                        }
                    }
                });

            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
                getStatusViewWithColor(activity, Color.TRANSPARENT);
            }
        }

    }


    private static void setFitsSystemWindows(Activity activity,boolean value) {
        ViewGroup contentFrameLayout =  activity.findViewById(android.R.id.content);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 19) {
            parentView.setFitsSystemWindows(value);
        }
    }

    private static int getStatusBarHeight(Activity activity) {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private static void getStatusViewWithColor(final Activity activity,final int color) {
        // addstatusview not good
        activity.getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int identifier = activity.getResources().getIdentifier("statusBarBackground","id","android");
                View statusView = activity.findViewById(identifier);
                if(statusView!=null){
                    statusView.setBackgroundColor(color);
                    statusView.removeOnLayoutChangeListener(this);
                }

            }
        });
    }

    //..
    public static void addPaddingTopwithColor(Activity activity,int color){
        setFitsSystemWindows(activity,false);
        ViewGroup contentView =  activity.getWindow().getDecorView().findViewById(android.R.id.content);
        contentView.setPadding(0,getStatusBarHeight(activity),0,0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {      //21  5.0 直接设置状态栏颜色
            int option;
            if(color==Color.WHITE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){       //23   6.0
                option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;  //状态栏黑色字体
            }else {
                option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            }
            activity.getWindow().getDecorView().setSystemUiVisibility(option);
            activity.getWindow().setStatusBarColor(color);
        }else {
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();      //添加有颜色视图
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            decorView.addView(statusBarView, lp);

    }

    }


}

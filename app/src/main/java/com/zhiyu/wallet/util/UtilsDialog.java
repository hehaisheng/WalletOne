package com.zhiyu.wallet.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhiyu.wallet.R;

import java.security.PublicKey;

/**
 * @author Mr.Luluxiu
 * Created by Administrator on 2018/3/10.
 */

public class UtilsDialog {

    public static Dialog createLoadingDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null,false);// 得到加载view
        LinearLayout layout =  v.findViewById(R.id.dialog_loading_view);// 加载布局
        LinearLayout linearLayout =  v.findViewById(R.id.ly_loadingbg);
        linearLayout.getBackground().setAlpha(255);

        TextView tipTextView =  v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(false); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //  window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();

        return loadingDialog;
    }

    public static Dialog createFinishDialog(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_finish, null,false);// 得到加载view
        LinearLayout layout =  v.findViewById(R.id.dialog_loading_view);// 加载布局
        LinearLayout linearLayout =  v.findViewById(R.id.ly_loadingbg);
        linearLayout.getBackground().setAlpha(255);

        TextView tipTextView =  v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog finishDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        finishDialog.setCancelable(false); // 是否可以按“返回键”消失
        finishDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        finishDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = finishDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //  window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        finishDialog.show();

        return finishDialog;
    }

    public static Dialog showCustomDialog(Context context,int resId,int gravity,boolean cancelable,boolean cancelableOnout,BindView bindView1){
        AlertDialog alertDialog;
        AlertDialog.Builder builder;
        View rootView = LayoutInflater.from(context).inflate(resId,null);
        BindView bindView = bindView1;

        builder= new AlertDialog.Builder(context, R.style.repay_dialog);
        builder.setCancelable(cancelable);

        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(cancelableOnout);
        alertDialog.show();



        Window window = alertDialog.getWindow();
        window.setGravity(gravity);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
       // lp.y = 0;//设置Dialog距离底部的距离
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopRepayWindowAnimStyle);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        window.setSoftInputMode(PopupWindow.INPUT_METHOD_NOT_NEEDED );
        window.setContentView(rootView);
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED );

        if (bindView != null) bindView.onBind(rootView);

        return alertDialog;
    }

    public interface BindView{
        void onBind(View rootView);
    }


    public static void closeDialog(Dialog mDialogUtils) {
        if (mDialogUtils != null && mDialogUtils.isShowing()) {
            mDialogUtils.dismiss();
        }
    }


    public static void showDialog(String diatitle, String diameg, Context context){
        AlertDialog dialog= new AlertDialog.Builder(context)
                .setTitle(diatitle)
                .setMessage(diameg)
                .setPositiveButton("确定", null)
                .show();
//        android.support.v7.app.AlertDialog dialog =new  android.support.v7.app.AlertDialog.Builder(context)
//                                .setTitle(diatitle)
//                                        .setMessage(diameg)
//                                        .setPositiveButton("确定", null)
//                                        .show();
        Window window= dialog.getWindow();
        WindowManager.LayoutParams lp =window.getAttributes();
  //    lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        window.setAttributes(lp);
    }

    public static void showDialogCancle(String diatitle, String diameg, View item, Context context, DialogInterface.OnClickListener clickListener){
        AlertDialog dialog= new AlertDialog.Builder(context)
                .setMessage(diameg)
                .setView(item)
                .setNegativeButton("确定", clickListener).create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setWindowAnimations(R.style.PopWindowAnimStyle);
        dialog.getWindow().setAttributes(lp);

        dialog.show();


    }

    public static void showDialogForListener(String diatitle, String diameg, Context context, DialogInterface.OnClickListener clickListener){
        AlertDialog dialog= new AlertDialog.Builder(context)
                .setTitle(diatitle)
                .setMessage(diameg)
                .setNegativeButton("确定", clickListener)
                .setPositiveButton("取消", null)
                .show();


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setWindowAnimations(R.style.PopWindowAnimStyle);
        dialog.getWindow().setAttributes(lp);
    }

    public static void showDialogCommit(String diatitle, String diameg, Context context, DialogInterface.OnDismissListener dismissListener){
        AlertDialog dialog= new AlertDialog.Builder(context)
                .setTitle(diatitle)
                .setMessage(diameg)
                .setPositiveButton("确定", null)
                .setOnDismissListener(dismissListener)
                .setCancelable(false)
                .show();

        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setWindowAnimations(R.style.PopWindowAnimStyle);
        dialog.getWindow().setAttributes(lp);
    }


    public static void showDialogViewProduct(View item, Context context ){
        AlertDialog dialog= new AlertDialog.Builder(context)
                //.setTitle(diatitle)
                // .setMessage(diameg)
                .setView(item)
                .show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        //lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        //lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        lp.width = AppUtil.getScreenDispaly(context)[0];
        lp.height = AppUtil.getScreenDispaly(context)[1]-100;
        System.out.println( "width "+ lp.width + "height "+ lp.height);
        dialog.getWindow().setWindowAnimations(R.style.PopWindowAnimStyle);
        dialog.getWindow().setAttributes(lp);
    }

    public static int dp(Context context, float value) {
        if (value == 0) {
            return 0;
        }
        float density = context.getResources().getDisplayMetrics().density;
        return (int) Math.ceil(density * value);
    }


}

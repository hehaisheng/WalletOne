package com.zhiyu.wallet.util;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.zhiyu.wallet.common.App;

import java.util.UUID;


/**
 *
 * 2018/7/6
 * @author Mr.Luluxiu
 */
public class UIUtils {

    public static Toast toast= null;

    public static Toast toastgra= null;

    public static Context getContext(){
        return App.context;
    }

    public static Handler getHandler(){
        return App.handler;
    }

    //返回指定colorId对应的颜色值
    public static int getColor(int colorId){
        return getContext().getResources().getColor(colorId);
    }

    //加载指定viewId的视图对象，并返回
    public static View getView(int viewId){
        View view = View.inflate(getContext(), viewId, null);
        return view;
    }

    public static String[] getStringArr(int strArrId){
        String[] stringArray = getContext().getResources().getStringArray(strArrId);
        return stringArray;
    }

    //将dp转化为px
    public static int dp2px(int dp){
        //获取手机密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);//实现四舍五入
    }

    //将px转化为dp
    public static int px2dp(int px){
        //获取手机密度
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5);//实现四舍五入
    }

    //保证runnable中的操作在主线程中执行
    public static void runOnUiThread(Runnable runnable) {
        if(isInMainThread()){
            runnable.run();
        }else{
            UIUtils.getHandler().post(runnable);
        }
    }
    //判断当前线程是否是主线程
    private static boolean isInMainThread() {
        int currentThreadId = android.os.Process.myTid();
        return App.mainThreadId == currentThreadId;

    }

    public static void toast(String message, boolean isLengthLong){
        int duration = Toast.LENGTH_SHORT;
        if(isLengthLong){
            duration = Toast.LENGTH_LONG;
        }

        if (toast == null) {
             toast = Toast.makeText(UIUtils.getContext(), message,  duration);
        } else {
            toast.setText(message);
            toast.setDuration(duration);
        }
        toast.show();
    }

    public static void toast(String message, boolean isLengthLong,int gravity){
        int duration = Toast.LENGTH_SHORT;
        if(isLengthLong){
            duration = Toast.LENGTH_LONG;
        }

        if (toastgra == null) {
            toastgra = Toast.makeText(UIUtils.getContext(), message,  duration);
        } else {
            toastgra.setText(message);
            toastgra.setDuration(duration);
        }
        toastgra.setGravity(gravity,0,0);
        toastgra.show();
    }

    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、178(新)、182、184、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、170、173、177、180、181、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            String num2 = number.replace(" ","");
            //matches():字符串是否在给定的正则表达式匹配
            return num2.matches(num);
        }
    }

    public static void scalHomepx(){
        int width = 750 ; //宽px
        int height = 1334;//屏幕高度
        float screenInch = 4.7f;//屏幕尺寸
        //设备密度公式
        float density = (float) Math.sqrt(width * width + height * height) / screenInch / 160;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<resources>\n");
        for (int px = 0; px <= 1000; px += 2) {
            //像素值除以density
            String dp = px * 1.0f / density + "";
            //拼接成资源文件的内容，方便引用
            if (dp.indexOf(".") + 4 < dp.length()) {//保留3位小数
                dp = dp.substring(0, dp.indexOf(".") + 4);
            }
            stringBuilder.append("<dimen name=\"px").append(px + "").append("dp\">").append(dp).append("dp</dimen>\n");
        }
        stringBuilder.append("</resources>");
        System.out.println(stringBuilder.toString());

    }

    //32位的uuid
    public static String getUUID32() {

        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();


        return uuid;
    }



}

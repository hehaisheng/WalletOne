package com.zhiyu.wallet.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/11/9.
 * * @author Mr.Luluxiu
 */

public class Println {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void err(String heard,String msg){

        System.out.println("~~~T_T~~~ "+heard+" : "+msg);
    }

    public static void suc(String heard,String msg){

        System.out.println("--- ^_^ --- "+heard+" : "+msg);
    }

    public static void out(String heard,String msg){

        System.out.println("--- 0o0 --- "+heard+" : "+msg);
    }

    public static void printJson(String tag, String msg, String headString) {

        String message;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(4);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        message = headString + LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
     }
}

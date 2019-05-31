package com.zhiyu.wallet.common;

import android.app.Activity;

import java.util.Stack;

/**
 * 2018/7/6
 *
 * @author zhiyu
 *
 * 统一应用程序中所有的Activity的栈管理（单例）
 * 涉及到activity的添加、删除指定、删除当前、删除所有、返回栈大小的方法
 */
public class ActivityManager {


    private ActivityManager(){

    }

    //单例模式：内部类模式
    private static class Holder{
        private static final ActivityManager INSTANCE = new ActivityManager();
    }

    public static ActivityManager getInstance(){
        return Holder.INSTANCE;
    }

    //提供栈的对象
    private Stack<Activity> activityStack = new Stack<>();

    //activity的添加
    public void add(Activity activity){
        if(activity != null){
            activityStack.add(activity);
        }
    }


    //删除指定的activity
    public void remove(Activity activity){
        if(activity != null){
            for(int i = activityStack.size() - 1;i >= 0;i--){
                Activity currentActivity = activityStack.get(i);
                if(currentActivity.getClass().equals(activity.getClass())){
                    currentActivity.finish();//销毁当前的activity
                    activityStack.remove(i);//从栈空间移除
                }
            }
        }
    }
    public boolean isForeground(Activity activity){
        if(activity != null){

                Activity currentActivity = activityStack.get(activityStack.size()-1);
                if(currentActivity.getClass().equals(activity.getClass())){
                    return true;
                }

        }
        return false;
    }

    //删除当前的activity
    public void removeCurrent(){
        //方式一：
//        Activity activity = activityStack.get(activityStack.size() - 1);
//        activity.finish();
//        activityStack.remove(activityStack.size() - 1);

        //方式二：
        Activity activity = activityStack.lastElement();
        activity.finish();
        activityStack.remove(activity);
    }

    //删除所有的activity
    public void removeAll(){
        for (int i = activityStack.size() - 1;i >= 0;i--){
            Activity activity = activityStack.get(i);
            activity.finish();
            activityStack.remove(activity);
        }
    }

    //返回栈大小
    public int size(){
        return activityStack.size();
    }

}

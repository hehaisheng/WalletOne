package com.zhiyu.wallet.mvp.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.common.ActivityManager;
import com.zhiyu.wallet.common.Constant;
import com.zhiyu.wallet.util.StatusbarUtil;
import com.zhiyu.wallet.util.UIUtils;
import com.zhiyu.wallet.util.UpdateUtil;
import com.zhiyu.wallet.widget.gesture.GestureVerifyActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Administrator
 * Created by Administrator on 2018/11/5.
 */

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.ly_welcome)
    LinearLayout back_ly;

    private static Handler handler = new Handler();
    private Unbinder butterKnife;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        StatusbarUtil.setfullScreenSystemWindows(this,0);
        ActivityManager.getInstance().add(this);
        butterKnife=ButterKnife.bind(this);
        UpdateUtil updateUtil = new UpdateUtil(WelcomeActivity.this)
                .setOnDownloadListener(new UpdateUtil.SetUpdateListencer() {
                    @Override
                    public void shutdowntomain(long starttime) {
                        toMain(starttime);
                    }

                    @Override
                    public void downloadsuccess() {
                        ActivityManager.getInstance().remove(WelcomeActivity.this);
                    }

                    @Override
                    public void downloadfail(Dialog dialog) {
                        dialog.dismiss();
                        UIUtils.toast("更新失败",false);
                        UIUtils.getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ActivityManager.getInstance().remove(WelcomeActivity.this);
                            }
                        },1000);
                    }
                });


        setAnimation();
    }


    private void toMain(long startTime) {
        long currentTime = System.currentTimeMillis();
        //  System.out.println("update "+(currentTime-startTime));
        long delayTime = 3000 - (currentTime - startTime);
        if (delayTime < 0) {
            delayTime = 0;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isOpenGesture();
            }
        }, delayTime);
    }

    private void isOpenGesture() {
        SharedPreferences msharedPreferences = this.getSharedPreferences("secret_lock", Context.MODE_PRIVATE);
        boolean o = msharedPreferences.getBoolean("isOpen", false);
        if (o) {
            finish();
            Bundle bundle = new Bundle();
            Intent intent = new Intent(WelcomeActivity.this, GestureVerifyActivity.class);
            bundle.putInt("type", Constant.Gesture_Check);
            intent.putExtra("data", bundle);
            startActivity(intent);
        } else {
            finish();
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        }
    }


    private void setAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);//0：完全透明  1：完全不透明
        alphaAnimation.setDuration(3000);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());//设置动画的变化率

        back_ly.startAnimation(alphaAnimation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        butterKnife.unbind();
    }
}

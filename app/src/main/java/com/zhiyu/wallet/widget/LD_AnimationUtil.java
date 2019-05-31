package com.zhiyu.wallet.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2018/3/13.
 * @author Mr.Luluxiu
 */

public class LD_AnimationUtil {

    public static void translateUp(Context context, View view) {


        ObjectAnimator
                .ofFloat(view, "translationY", view.getMeasuredHeight(), 0)
                .setDuration(300).start();

    }

    public static void translateDown(Context context, View view,
                                     final LD_AnimationListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY",
                0, view.getMeasuredHeight()).setDuration(300);

        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub

            }


            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.ld_AnimationFinish();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });
        animator.start();

    }

   public interface LD_AnimationListener {

        public void ld_AnimationFinish();

   }

}

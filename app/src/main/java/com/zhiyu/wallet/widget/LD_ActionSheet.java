
package com.zhiyu.wallet.widget;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.util.AddViewUtil;


/**
 * ActionSheet
 *
 * @author Mr.Zheng
 */
public class LD_ActionSheet extends Dialog {
    private View mContentView;

    public LD_ActionSheet(Context context) {
        super(context);

    }

    public LD_ActionSheet(Context context, int theme) {
        super(context, theme);

    }

    protected LD_ActionSheet(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

    }

    public static class Builder {
        private Context context;
        private String[] titleItems;//按钮问题数组
        private String cancelTitle;//取消按钮文字
        private OnActionSheetselectListener listener;//按钮点击监听

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置显示数组
         *
         * @param items
         * @return
         */

        public Builder setItems(String[] items) {
            this.titleItems = items;
            return this;
        }

        /**
         * 设置取消按钮
         *
         * @param cancel
         * @return
         */
        public Builder setcancelString(String cancel) {
            this.cancelTitle = cancel;
            return this;
        }

        /**
         * 设置按钮点击监听
         *
         * @param listner
         * @return
         */
        public Builder setListner(OnActionSheetselectListener listner) {
            this.listener = listner;
            return this;
        }

        /**
         * 创建ActionSheet
         *
         * @return
         */
        public LD_ActionSheet create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.ld_actionsheet, null);
            LinearLayout llItem = (LinearLayout) view.findViewById(R.id.ll_items);
            final LD_ActionSheet actionSheet = new LD_ActionSheet(context, R.style.Dialog);
            if (titleItems != null) {
                for (int i = 0; i < titleItems.length; i++) {
                    final int position = i;
                    AddViewUtil addViewUtil = new AddViewUtil(context);
                    Button btn = (Button) addViewUtil.getBtactionsheet(titleItems[i]);
                    btn.setText(titleItems[i]);
                    btn.setTextSize(12f);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View V) {

                            LD_AnimationUtil.translateDown(context, view, new LD_AnimationUtil.LD_AnimationListener() {
                                @Override
                                public void ld_AnimationFinish() {
                                    if (listener != null)
                                        listener.itemSelect(actionSheet, position + 1);
                                }
                            });


                        }
                    });
                    LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(context,38));
                    LayoutParams.setMargins(0, 3, 0, 0);
                    btn.setLayoutParams(LayoutParams);
                    llItem.addView(btn);
                }
            }
            Button btnCancel = (Button) view.findViewById(R.id.actioncancel);
            if (cancelTitle != null) {
                btnCancel.setText(cancelTitle);
            }

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LD_AnimationUtil.translateDown(context, view, new LD_AnimationUtil.LD_AnimationListener() {
                        @Override
                        public void ld_AnimationFinish() {
                            if (listener != null)
                                listener.itemSelect(actionSheet, 0);
                        }
                    });
                }
            });

           // actionSheet.getWindow().setGravity(Gravity.BOTTOM);
            Window window = actionSheet.getWindow();
            window.setGravity(Gravity.BOTTOM);
            window.getDecorView().setPadding(0, 0, 0, 0);

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            actionSheet.addContentView(view, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            actionSheet.setCancelable(false);
            actionSheet.setCanceledOnTouchOutside(true);
            actionSheet.setmContentView(view);
            return actionSheet;
        }

        public interface OnActionSheetselectListener {
            void itemSelect(Dialog dialog, int index);
        }
    }

    public static int dp(Context context, float value) {
        if (value == 0) {
            return 0;
        }
        float density = context.getResources().getDisplayMetrics().density;
        return (int) Math.ceil(density * value);
    }

    @Override
    public void show() {
        super.show();
        LD_AnimationUtil.translateUp(getContext(), mContentView);
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }

    public void setmContentView(View mContentView) {
        this.mContentView = mContentView;
    }

    public static void showActionSheet(Context context, String[] items, String cancel, LD_ActionSheet.Builder.OnActionSheetselectListener listener) {
        LD_ActionSheet.Builder builder = new LD_ActionSheet.Builder(context);
        builder.setcancelString(cancel)
                .setItems(items)
                .setListner(listener);
        builder.create().show();
    }
}

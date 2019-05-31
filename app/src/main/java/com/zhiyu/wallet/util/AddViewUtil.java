package com.zhiyu.wallet.util;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhiyu.wallet.R;

/**
 * Created by Administrator on 2018/7/24.
 * @author Mr.Luluxiu
 */

public class AddViewUtil {
    private Context context;

    public AddViewUtil(Context context){

        this.context=context;
    }

    public Button getBtactionsheet(String text){
        Button button=new Button(context);
       //  ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
        //params.setMargins(30,30,30,30);
        button.setBackgroundResource(R.drawable.btn_white_bg);
       // button.setLayoutParams(params);
        button.setText(text);

        return button;
    }

    public static TextView getTextViewBank(Context context,int marginleft ,int margintop,int marginright,int marginbottom){
        TextView textView = new TextView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(dp(context,marginleft),dp(context,margintop),dp(context,marginright),dp(context,marginbottom));
        textView.setTextColor(Color.parseColor("#c0c0c0"));
        textView.setTextSize(15f);
        textView.setLayoutParams(layoutParams);

        return textView;
    }

    private static int dp(Context context,float value) {
        if (value == 0) {
            return 0;
        }
        float density = context.getResources().getDisplayMetrics().density;
        return (int) Math.ceil(density * value);
    }


}

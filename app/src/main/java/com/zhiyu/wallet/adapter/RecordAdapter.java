package com.zhiyu.wallet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.bean.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/6.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyHolder> {
    public List<Record> lists = new ArrayList<>();


    public RecordAdapter(List<Record> list){
        lists=list;
    }

    @NonNull
    @Override
    public RecordAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record,parent,false);

        return new MyHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.MyHolder holder, int position) {
        holder.loanDate.setText(lists.get(position).getTime());
        holder.loanAmount.setText(lists.get(position).getMoney());
        holder.repayDate.setText(lists.get(position).getRepaymenttime());

        switch (lists.get(position).getStatuscopy()){
            case "已还": holder.status.setTextColor(Color.parseColor("#646464"));break;
            case "审核失败": holder.status.setTextColor(Color.parseColor("#ff0000"));break;
            case "支付失败": holder.status.setTextColor(Color.parseColor("#ff0000"));break;
            case "拒绝申请": holder.status.setTextColor(Color.parseColor("#ff0000"));break;
            default:holder.status.setTextColor(Color.argb(255,248,164,66));break;
        }
        holder.status.setText(lists.get(position).getStatuscopy());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        private TextView loanDate;
        private TextView loanAmount;
        private TextView repayDate;
        private TextView status;

        public MyHolder(View itemView) {
            super(itemView);
            loanDate = itemView.findViewById(R.id.tv_rloanDate);
            loanAmount = itemView.findViewById(R.id.tv_rloanAmount);
            repayDate = itemView.findViewById(R.id.tv_rrepayDate);
            status = itemView.findViewById(R.id.tv_rstatus);
        }


    }
}

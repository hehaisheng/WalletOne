package com.zhiyu.wallet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyu.wallet.R;

import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2018/11/28.
 */

public class AccrualDetailAdapter extends RecyclerView.Adapter<AccrualDetailAdapter.MyaccViewHolder> {
   public List<Map<String ,Object >> acclist ;

   public AccrualDetailAdapter (List<Map<String ,Object>> list){
       this.acclist=list;

   }

    @NonNull
    @Override
    public MyaccViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_accrualdetail,null);
        return new MyaccViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyaccViewHolder holder, int position) {
        holder.costname.setText(acclist.get(position).get("costname").toString());
        holder.costmoney.setText(acclist.get(position).get("costmoney").toString());
    }

    @Override
    public int getItemCount() {
        return acclist.size();
    }

    public class MyaccViewHolder extends RecyclerView.ViewHolder{
       private TextView costname;
       private TextView costmoney;


        public MyaccViewHolder(View itemView) {
            super(itemView);
            costname = itemView.findViewById(R.id.tv_costname);
            costmoney = itemView.findViewById(R.id.tv_costmoney);

        }
    }
}

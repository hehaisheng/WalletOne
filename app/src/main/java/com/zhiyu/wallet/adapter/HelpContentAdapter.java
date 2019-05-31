package com.zhiyu.wallet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.bean.News;

import java.util.List;

/**
 * @
 * Created by Administrator on 2018/11/13.
 */

public class HelpContentAdapter extends RecyclerView.Adapter<HelpContentAdapter.HelpViewHolder> {
    private List<News> list;
    private int opened= -1;
    private Context context;


    public HelpContentAdapter(List<News> list,Context context){
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public HelpContentAdapter.HelpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_help_center,parent,false);

        return  new HelpViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpViewHolder holder, int position) {
        holder.bindview(position,list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HelpViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        private LinearLayout lytitle;
        private LinearLayout lycontent;
        private TextView tvhelptitle;
        private TextView tvhelpcontent;
        private ImageView ivReturnup;


         HelpViewHolder(View itemView) {
            super(itemView);
            lycontent = itemView.findViewById(R.id.ly_helpcontent);
            lytitle = itemView.findViewById(R.id.ly_helptitile);
            tvhelptitle = itemView.findViewById(R.id.tv_helptitle);
            tvhelpcontent = itemView.findViewById(R.id.tv_helpcontent);
            ivReturnup = itemView.findViewById(R.id.iv_returnup);
            lytitle.setOnClickListener(this);
         }

        void bindview (int position , News news){
             tvhelptitle.setText(news.getTitle());
             tvhelpcontent.setText(news.getContent());
             if(position == opened){
                 ivReturnup.setImageDrawable(context.getResources().getDrawable(R.mipmap.return_up));
                 lycontent.setVisibility(View.VISIBLE);
             }else {
                 ivReturnup.setImageDrawable(context.getResources().getDrawable(R.mipmap.return_down));
                 lycontent.setVisibility(View.GONE);
             }


        }
        @Override
        public void onClick(View v) {
            if (opened == getAdapterPosition()) {
                //当点击的item已经被展开了, 就关闭.
                opened = -1;
                notifyItemChanged(getAdapterPosition());
            } else {
                int oldOpened = opened;
                opened = getAdapterPosition();
                notifyItemChanged(oldOpened);
                notifyItemChanged(opened);
            }

        }
    }
}

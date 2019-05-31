package com.zhiyu.wallet.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhiyu.wallet.R;
import com.zhiyu.wallet.bean.News;
import com.zhiyu.wallet.util.Println;

import java.util.ArrayList;
import java.util.List;

/**
 * 2018/8/12
 *
 * @author zhiyu
 */
public class NewsCentetAdapter extends BaseAdapter {

    private List<News> list = new ArrayList<>();
    private Context mContext;


    public NewsCentetAdapter(List<News> list, Context context) {
        this.list = list;
        mContext = context;

    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list == null ? null : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsViewHolder newsViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.news_center_item, null);
            newsViewHolder = new NewsViewHolder(convertView);
            convertView.setTag(newsViewHolder);
        } else {
            newsViewHolder = (NewsViewHolder) convertView.getTag();
        }

        newsViewHolder.inidata(list.get(position));
        return convertView;

    }


    public class NewsViewHolder {

        TextView news_content, news_time, news_title;

        public NewsViewHolder(View view) {
            news_content = (TextView) view.findViewById(R.id.news_content);
            news_time = (TextView) view.findViewById(R.id.news_time);
            news_title = (TextView) view.findViewById(R.id.news_title);

        }


        public void inidata(News news) {
            news_content.setText(news.getContent());
            news_time.setText(news.getNewstime());
            news_title.setText(news.getTitle());
        }
    }
}


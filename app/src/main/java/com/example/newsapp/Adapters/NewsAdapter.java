package com.example.newsapp.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newsapp.API.Model.NewsResponse.ArticlesItem;
import com.example.newsapp.R;

import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    List<ArticlesItem> newsList;

    public NewsAdapter(List<ArticlesItem> newsList) {
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_news,viewGroup,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        ArticlesItem news = newsList.get(pos);
        viewHolder.title.setText(news.getTitle());
        viewHolder.date.setText(news.getPublishedAt());
        if (news.getSource()==null)
            viewHolder.source.setText(news.getSourceName());
        else
        viewHolder.source.setText(news.getSource().getName());
        Glide.with(viewHolder.itemView)
                .load(news.getUrlToImage()).into(viewHolder.imageView);

    }

    public void changeData(List<ArticlesItem> newItems){
        this.newsList=newItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(newsList==null)return 0;
        return newsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title,date,source;

        public ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            date = view.findViewById(R.id.date);
            source = view.findViewById(R.id.source_name);
        }

    }
}

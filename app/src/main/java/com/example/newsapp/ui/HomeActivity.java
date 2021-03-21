package com.example.newsapp.ui;

import android.os.Bundle;

import com.example.newsapp.API.Model.NewsResponse.ArticlesItem;
import com.example.newsapp.API.Model.NewsSourcesResponse.SourcesItem;
import com.example.newsapp.Adapters.NewsAdapter;
import com.example.newsapp.Base.BaseActivity;
import com.example.newsapp.ui.Repos.NewsRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TableLayout;

import com.example.newsapp.R;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class HomeActivity extends BaseActivity {
    TabLayout tablayout;
    RecyclerView recyclerView;
    NewsRepository newsRepository;
    NewsAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String lang ="en";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tablayout=findViewById(R.id.tablayout);
        recyclerView =  findViewById(R.id.recycler_view);
        newsRepository = new NewsRepository(lang);
        adapter=new NewsAdapter(null);
        layoutManager = new LinearLayoutManager(activity);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        showProgressBar(R.string.loading);
        newsRepository.getNewsSources(mySourcesListener);

    }
    NewsRepository.OnSourcesPreparedListener mySourcesListener =
            new NewsRepository.OnSourcesPreparedListener() {
                @Override
                public void onSourcesPrepared(final List<SourcesItem> sourcesList) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressBar();
                            addSourcesToTabLayout(sourcesList);

                        }


                    });

                }
            };
    NewsRepository.OnNewsPreparedListener myNewspreparedListner= new NewsRepository.OnNewsPreparedListener() {
        @Override
        public void onNewsPrepared(final List<ArticlesItem> newsList) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.changeData(newsList);
                }
            });


        }
    };

    private void addSourcesToTabLayout(final List<SourcesItem> sourcesList) {
        if(sourcesList==null)
            return;
        tablayout.removeAllTabs();
        for(int i=0;i<sourcesList.size();i++){
            SourcesItem sourcesItem = sourcesList.get(i);
            TabLayout.Tab tab=tablayout.newTab();
            tab.setText(sourcesItem.getName());
            tab.setTag(sourcesItem);
            tablayout.addTab(tab);
        }
        tablayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                SourcesItem sourcesItem = ((SourcesItem) tab.getTag());
                newsRepository.getNewsBySourceId(lang,sourcesItem.getId(),myNewspreparedListner);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                SourcesItem sourcesItem = ((SourcesItem) tab.getTag());
                newsRepository.getNewsBySourceId(lang,sourcesItem.getId(),myNewspreparedListner);

            }
        });
        //when app launcher select the first tab
        tablayout.getTabAt(0).select();

    }

}
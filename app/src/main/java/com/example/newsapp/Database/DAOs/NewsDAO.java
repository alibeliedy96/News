package com.example.newsapp.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.newsapp.API.Model.NewsResponse.ArticlesItem;
import com.example.newsapp.API.Model.NewsSourcesResponse.SourcesItem;

import java.util.List;

@Dao
public interface NewsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertNewsLst(List<ArticlesItem> articles);


    @Query("select * from ArticlesItem where sourceId=:sourcesId")
    List<ArticlesItem> getNewsBySourcesId(String sourcesId);
}

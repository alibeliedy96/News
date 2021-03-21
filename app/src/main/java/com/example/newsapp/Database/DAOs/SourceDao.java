package com.example.newsapp.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.newsapp.API.Model.NewsSourcesResponse.SourcesItem;

import java.util.List;

@Dao
public interface SourceDao {
    @Insert
    public void addSources(List<SourcesItem> items);
    @Query("select * from SourcesItem")
    List<SourcesItem>getAllSources();
}

package com.example.newsapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.newsapp.API.Model.NewsResponse.ArticlesItem;
import com.example.newsapp.API.Model.NewsSourcesResponse.SourcesItem;
import com.example.newsapp.Database.DAOs.NewsDAO;
import com.example.newsapp.Database.DAOs.SourceDao;

@Database(entities = {SourcesItem.class, ArticlesItem.class},version = 2,exportSchema = false)
public abstract  class MyDatabase extends RoomDatabase {
    public abstract SourceDao sourcesDAO();
    public abstract NewsDAO newsDAO();
    private static MyDatabase myDataBase;
    public static void init(Context context){
        if(myDataBase==null){//Create new object
            myDataBase =
                    Room.databaseBuilder(context.getApplicationContext(),
                            MyDatabase.class, "NEWS-DataBase")
                            .fallbackToDestructiveMigration()
                            .build();
        }

    }
    public static MyDatabase getInstance(){
      return myDataBase;
    }

}

package com.example.newsapp.ui;

import android.app.Application;

import com.example.newsapp.Database.MyDatabase;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MyDatabase.init(this);

    }
}

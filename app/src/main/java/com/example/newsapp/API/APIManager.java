package com.example.newsapp.API;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIManager {
    public static final String BASE_URL="https://newsapi.org/v2/";
    private static Retrofit retrofitInstance;
    private static Retrofit getInstance()
    {

        HttpLoggingInterceptor loggingInterceptor =new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.e("api",message);
                    }
                });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        if(retrofitInstance==null)
        {

        retrofitInstance = new Retrofit.Builder()
                //URL must finished with / in end of URL
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
    return retrofitInstance;
    }
    public static Services getApi()
    {
        Services services= getInstance().create(Services.class);
        return services;
    }

}

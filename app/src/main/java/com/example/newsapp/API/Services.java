package com.example.newsapp.API;



import com.example.newsapp.API.Model.NewsResponse.NewsResponse;
import com.example.newsapp.API.Model.NewsSourcesResponse.SourcesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Services {
   @GET("sources")
   Call<SourcesResponse> getNewsSources(@Query("apiKey")String apiKey,@Query("language")String language);
   @GET("everything")
   Call<NewsResponse>getNewsBySourceId(@Query("apiKey")String apiKey,
                                       @Query("language")String language,
                                       @Query("sources") String sourceId);


}

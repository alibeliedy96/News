package com.example.newsapp.ui.Repos;

import android.util.Log;

import com.example.newsapp.API.APIManager;
import com.example.newsapp.API.Model.NewsResponse.ArticlesItem;
import com.example.newsapp.API.Model.NewsResponse.NewsResponse;
import com.example.newsapp.API.Model.NewsSourcesResponse.SourcesItem;
import com.example.newsapp.API.Model.NewsSourcesResponse.SourcesResponse;
import com.example.newsapp.Database.MyDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewsRepository {

    String lang;
    private static String APIKey="a5397c59d2c84efd9d728e71b8dae98b";


    public NewsRepository(String lang) {
        lang = lang;
    }

    public void getNewsSources(final OnSourcesPreparedListener onSourcesPreparedListener){

        APIManager.getApi()
                .getNewsSources(APIKey,lang)
                .enqueue(new Callback<SourcesResponse>() {
                    @Override
                    public void onResponse(Call<SourcesResponse> call,
                                           Response<SourcesResponse> response) {
                        if (response.isSuccessful() &&
                                "ok".equals(response.body().getStatus())) {

                            if (onSourcesPreparedListener != null)
                                onSourcesPreparedListener.onSourcesPrepared(response.body().getSources());

                               InsertIntoSourcesThread thread =
                                      new InsertIntoSourcesThread(response.body().getSources());
                             thread.start();
                        }
                    }

                    @Override
                    public void onFailure(Call<SourcesResponse> call, Throwable t) {
                        //handle database Call
                        GetSourcesFromDB th =
                               new GetSourcesFromDB(onSourcesPreparedListener);
                        th.start();
                    }
                });
    }

    public void getNewsBySourceId(String lang, String sourceId, final OnNewsPreparedListener onNewsPreparedListener){


        APIManager.getApi()
                .getNewsBySourceId(APIKey,lang,sourceId)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {

                        if(response.isSuccessful()&&"ok".equals(response.body().getStatus())){
                            if(onNewsPreparedListener!=null)


                                onNewsPreparedListener.onNewsPrepared(response.body().getArticles());
                            InsertIntoNewsThread insert=new InsertIntoNewsThread(response.body().getArticles());
                            insert.start();
                        }

                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                          GetArticlesFromDB getArticlesFromDB=new GetArticlesFromDB(sourceId,onNewsPreparedListener);
                          getArticlesFromDB.start();
                    }
                });


    }

    public interface OnSourcesPreparedListener{

        public void onSourcesPrepared(List<SourcesItem> sourcesList);
    }

    public interface OnNewsPreparedListener{
        public void onNewsPrepared(List<ArticlesItem> newsList);
    }
   //use thread to insert into db in background thread because can not insert in mainThread
    class InsertIntoSourcesThread extends Thread {

        List<SourcesItem> items;

        public InsertIntoSourcesThread(List<SourcesItem> items){
            this.items = items;
        }

        public void run(){
            MyDatabase.getInstance()
                    .sourcesDAO()
                    .addSources(items);
            Log.e("sourceThread","insertion success");
        }
    }
    class InsertIntoNewsThread extends Thread {

        List<ArticlesItem> items;

        public InsertIntoNewsThread(List<ArticlesItem> items){
            this.items = items;
        }

        public void run(){
            for (int i=0;i<items.size();i++){
                ArticlesItem item=items.get(i);
                item.setSourceId(item.getSource().getId());
                item.setSourceName(item.getSource().getName());
            }
            MyDatabase.getInstance()
            .newsDAO().insertNewsLst(items);
            Log.e("NewsThread","insertion success");
        }
    }
    //use thread to GetSourcesFromDB  in background thread because can not GetSourcesFromDB in mainThread directly

    class GetSourcesFromDB extends Thread {
        // using this callback because if data returned from db getting the data and return it in mainActivity
        OnSourcesPreparedListener listener;

        public GetSourcesFromDB(OnSourcesPreparedListener listener){
            this.listener=listener;
        }
        public void run(){
            List<SourcesItem> items=
                    MyDatabase.getInstance()
                    .sourcesDAO()
                    .getAllSources();
            listener .onSourcesPrepared(items);
            Log.e("sourceThread","insertion success");
        }
    }
    class GetArticlesFromDB extends Thread {
        // using this callback because if data returned from db getting the data and return it in mainActivity
        OnNewsPreparedListener onNewsPreparedListener;
        String sourceId;
        public GetArticlesFromDB(String sourceId,OnNewsPreparedListener listener){
            this.onNewsPreparedListener=listener;
            this.sourceId=sourceId;
        }
        public void run(){
            List<ArticlesItem> list=
                    MyDatabase.getInstance()
                    .newsDAO().getNewsBySourcesId(sourceId);
            onNewsPreparedListener.onNewsPrepared(list);
            Log.e("sourceThread","insertion success");
        }
    }

}

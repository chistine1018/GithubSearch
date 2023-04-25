package com.github.api;

import com.github.util.LiveDataCallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {

    private static RetrofitManager mInstance = new RetrofitManager();

    private GithubService githubService;

    public RetrofitManager() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();

        githubService = retrofit.create(GithubService.class);
    }

    public static GithubService getAPI() {
        return mInstance.githubService;
    }
}

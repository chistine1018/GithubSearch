package com.github.api;

import androidx.lifecycle.LiveData;

import com.github.data.model.RepoSearchResponse;
import com.github.data.model.User;


import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GithubService {
    @GET("search/repositories")
    LiveData<ApiResponse<RepoSearchResponse>> searchRepos(@Query("q") String query);

    @GET("search/repositories")
    Observable<Response<RepoSearchResponse>> searchReposRX(@Query("q") String query);

    @GET("users/{login}")
    Observable<Response<User>> getUser(@Path("login") String login);
}

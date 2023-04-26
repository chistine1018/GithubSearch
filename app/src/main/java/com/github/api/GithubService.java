package com.github.api;

import androidx.lifecycle.LiveData;

import com.github.data.model.RepoSearchResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubService {
    @GET("search/repositories")
    LiveData<ApiResponse<RepoSearchResponse>> searchRepos(@Query("q") String query);
}

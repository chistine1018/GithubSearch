package com.github.api;

import com.github.data.model.RepoSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GithubService {
    @GET("search/repositories")
    Call<RepoSearchResponse> searchRepos(@Query("q") String query);
}

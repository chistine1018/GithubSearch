package com.github.data;


import androidx.lifecycle.LiveData;
import com.github.api.ApiResponse;
import com.github.api.GithubService;
import com.github.data.model.RepoSearchResponse;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class DataModel {

    private GithubService githubService;

    @Inject
    public DataModel(GithubService githubService) {
        this.githubService = githubService;
    }

    public LiveData<ApiResponse<RepoSearchResponse>> searchRepo(String query) {
        return githubService.searchRepos(query);
    }
}

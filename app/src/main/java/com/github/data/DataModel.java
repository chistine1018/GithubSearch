package com.github.data;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.github.api.ApiResponse;
import com.github.api.GithubService;
import com.github.api.RetrofitManager;
import com.github.data.model.Repo;
import com.github.data.model.RepoSearchResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataModel {

    private GithubService githubService = RetrofitManager.getAPI();

    public LiveData<ApiResponse<RepoSearchResponse>> searchRepo(String query) {
        return githubService.searchRepos(query);
    }
}

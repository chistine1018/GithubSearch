package com.github.data;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

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

    public MutableLiveData<List<Repo>> searchRepo(String query) {
        final MutableLiveData<List<Repo>> repos = new MutableLiveData<>();
        githubService.searchRepos(query).enqueue(new Callback<RepoSearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<RepoSearchResponse> call, @NonNull Response<RepoSearchResponse> response) {
                repos.setValue(response.body().getItems());
            }

            @Override
            public void onFailure(@NonNull Call<RepoSearchResponse> call, @NonNull Throwable t) {
                //TODO: error handle
            }
        });
        return repos;
    }

    public interface onDataReadyCallback {
        void onDataReady(List<Repo> data);
    }
}

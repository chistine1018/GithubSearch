package com.github.data;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    public void searchRepo(String query, final onDataReadyCallback callback) {
        githubService.searchRepos(query).enqueue(new Callback<RepoSearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<RepoSearchResponse> call, @NonNull Response<RepoSearchResponse> response) {
                callback.onDataReady(response.body().getItems());
            }

            @Override
            public void onFailure(@NonNull Call<RepoSearchResponse> call, @NonNull Throwable t) {
                //TODO: error handle
            }
        });
    }

   public interface onDataReadyCallback {
        void onDataReady(List<Repo> data);
    }
}

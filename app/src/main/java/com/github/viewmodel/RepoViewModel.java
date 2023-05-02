package com.github.viewmodel;


import android.text.TextUtils;

import androidx.arch.core.util.Function;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.github.api.ApiResponse;
import com.github.data.RepoRepository;
import com.github.data.model.Repo;
import com.github.data.model.RepoSearchResponse;
import com.github.data.model.Resource;
import com.github.util.AbsentLiveData;

import java.util.List;

import javax.inject.Inject;

public class RepoViewModel extends ViewModel {

    private final MutableLiveData<String> query = new MutableLiveData<>();

    private final LiveData<Resource<List<Repo>>> repos;

    private RepoRepository mRepoRepository;

    @Inject
    public RepoViewModel(RepoRepository repoRepository) {
        super();
        this.mRepoRepository = repoRepository;
        repos = Transformations.switchMap(query, new Function<String, LiveData<Resource<List<Repo>>>>() {
            @Override
            public LiveData<Resource<List<Repo>>> apply(String userInput) {
                if (userInput == null || userInput.isEmpty()) {
                    return AbsentLiveData.create();
                } else {
                    return repoRepository.search(userInput);
                }
            }
        });
    }

    public LiveData<Resource<List<Repo>>> getRepos() {
        return repos;
    }


    public void searchRepo(String userInput) {
        query.setValue(userInput);
    }
}

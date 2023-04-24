package com.github.viewmodel;


import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.data.DataModel;
import com.github.data.model.Repo;

import java.util.List;

public class RepoViewModel extends ViewModel {

    public final ObservableBoolean isLoading = new ObservableBoolean(false);

    public final MutableLiveData<List<Repo>> repos = new MutableLiveData<>();


    private DataModel dataModel = new DataModel();

    public LiveData<List<Repo>> getRepos() {
        return repos;
    }


    public void searchRepo(String query) {

        isLoading.set(true);

        dataModel.searchRepo(query, new DataModel.onDataReadyCallback() {
            @Override
            public void onDataReady(List<Repo> data) {
                repos.setValue(data);
                isLoading.set(false);
            }
        });
    }
}

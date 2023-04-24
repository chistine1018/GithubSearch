package com.github.viewmodel;


import android.text.TextUtils;

import androidx.arch.core.util.Function;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.github.data.DataModel;
import com.github.data.model.Repo;
import com.github.util.AbsentLiveData;

import java.util.List;

public class RepoViewModel extends ViewModel {

    public final ObservableBoolean isLoading = new ObservableBoolean(false);

    public final MutableLiveData<String> query = new MutableLiveData<>();


    private DataModel dataModel = new DataModel();

    private final LiveData<List<Repo>> repos = Transformations.switchMap(query, new Function<String, LiveData<List<Repo>>>() {
        @Override
        public LiveData<List<Repo>> apply(String userInput) {
            if (TextUtils.isEmpty(userInput)) {
                return AbsentLiveData.create();
            } else {
                return dataModel.searchRepo(userInput);
            }
        }
    });

    public LiveData<List<Repo>> getRepos() {
        return repos;
    }


    public void searchRepo(String userInput) {
        query.setValue(userInput);
    }
}

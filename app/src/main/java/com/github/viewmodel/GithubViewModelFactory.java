package com.github.viewmodel;



import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.github.data.DataModel;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class GithubViewModelFactory implements ViewModelProvider.Factory {

    private DataModel dataModel;

    @Inject
    public GithubViewModelFactory(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RepoViewModel.class)) {
            return (T) new RepoViewModel(dataModel);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
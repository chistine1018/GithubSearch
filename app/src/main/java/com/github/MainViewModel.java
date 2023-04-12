package com.github;


import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.util.SingleLiveEvent;

public class MainViewModel extends ViewModel {

    public final ObservableBoolean isLoading = new ObservableBoolean(false);

    public final MutableLiveData<String> mData = new MutableLiveData<>();

    public final SingleLiveEvent<String> toastText = new SingleLiveEvent<>();

    private DataModel dataModel = new DataModel();


    public void refresh() {

        isLoading.set(true);

        dataModel.retrieveData(new DataModel.onDataReadyCallback() {
            @Override
            public void onDataReady(String data) {
                mData.setValue(data);
                toastText.setValue("下載完成");
                isLoading.set(false);
            }
        });
    }
}

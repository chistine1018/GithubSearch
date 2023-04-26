package com.github.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.api.ApiResponse;
import com.github.data.model.Repo;
import com.github.data.model.RepoSearchResponse;
import com.github.databinding.RepoFragmentBinding;
import com.github.di.Injectable;
import com.github.viewmodel.GithubViewModelFactory;
import com.github.viewmodel.RepoViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class RepoFragment extends Fragment implements Injectable {

    public static final String TAG = "REPO";

    @Inject
    GithubViewModelFactory factory;

    private RepoFragmentBinding binding;

    private RepoViewModel viewModel;

    private RepoAdpater repoAdpater = new RepoAdpater(new ArrayList<>());

    public static RepoFragment newInstance() {
        return new RepoFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RepoFragmentBinding.inflate(inflater, container, false);

        binding.edtQuery.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    doSearch();
                    return true;
                }
                return false;

            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(repoAdpater);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(RepoViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.getRepos().observe(getViewLifecycleOwner(), new Observer<ApiResponse<RepoSearchResponse>>() {
            @Override
            public void onChanged(ApiResponse<RepoSearchResponse> response) {
                viewModel.isLoading.set(false);
                if (response == null) {
                    repoAdpater.swapItems(null);
                    return;
                }
                if (response.isSuccessful()) {
                    repoAdpater.swapItems(response.body.getItems());
                } else {
                    Toast.makeText(getContext(), "連線發生錯誤", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void doSearch() {
        String query = binding.edtQuery.getText().toString();
        viewModel.searchRepo(query);
        viewModel.isLoading.set(true);
        dismissKeyboard();
    }

    private void dismissKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

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

import com.github.data.model.Repo;
import com.github.data.model.Resource;
import com.github.data.model.Status;
import com.github.databinding.RepoFragmentBinding;
import com.github.di.Injectable;
import com.github.viewmodel.RepoViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;


public class RepoFragment extends Fragment implements Injectable {

    public static final String TAG = "REPO";

    @Inject
    ViewModelProvider.Factory factory;

    private RepoFragmentBinding binding;

    private RepoViewModel viewModel;

    private RepoAdpater repoAdapter = new RepoAdpater(new ArrayList<>());

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
        binding.recyclerView.setAdapter(repoAdapter);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, factory).get(RepoViewModel.class);
        viewModel.getRepos().observe(getViewLifecycleOwner(), new Observer<Resource<List<Repo>>>() {
            @Override
            public void onChanged(Resource<List<Repo>> resource) {
                Timber.e("status" + resource.status);
                if (resource.status == Status.ERROR) {
                    Toast.makeText(getContext(), "NetWork Fail", Toast.LENGTH_SHORT).show();
                } else if (resource.status == Status.SUCCESS) {
                    binding.setResource(resource);
                    binding.executePendingBindings();
                    repoAdapter.swapItems(resource.data);
                }
            }
        });
    }

    private void doSearch() {
        String query = binding.edtQuery.getText().toString();
        if (TextUtils.isEmpty(query)) {
            Toast.makeText(getContext(), "Empty", Toast.LENGTH_SHORT).show();
            repoAdapter.clearItems();
        } else {
            viewModel.searchRepo(query);
        }
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

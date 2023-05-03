package com.github.ui;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.github.R;
import com.github.data.model.Repo;
import com.github.databinding.RepoItemBinding;

import java.util.List;
import java.util.Objects;

public class RepoAdpater extends PagedListAdapter<Repo, RepoAdpater.RepoViewHolder> {


    public RepoAdpater() {
        super(DIFF_CALLBACK);
    }

    class RepoViewHolder extends RecyclerView.ViewHolder {

        private final RepoItemBinding binding;

        RepoViewHolder(RepoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Repo repo) {
            binding.setRepo(repo);
            binding.executePendingBindings();
        }
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RepoItemBinding binding = RepoItemBinding.inflate(layoutInflater, parent, false);
        return new RepoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        Repo repo = getItem(position);
        holder.bind(repo);
    }

    private static final DiffUtil.ItemCallback<Repo> DIFF_CALLBACK = new DiffUtil.ItemCallback<Repo>() {


        @Override
        public boolean areItemsTheSame(@NonNull Repo oldRepo, @NonNull Repo newRepo) {
            return Objects.equals(oldRepo.id, newRepo.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Repo oldRepo, @NonNull Repo newRepo) {
            return Objects.equals(oldRepo.name, newRepo.name) &&
                    Objects.equals(oldRepo.descrption, newRepo.descrption);
        }
    };
}

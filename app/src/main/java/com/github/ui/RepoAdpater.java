package com.github.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.data.model.Repo;
import com.github.databinding.RepoItemBinding;

import java.util.List;
import java.util.Objects;

public class RepoAdpater extends RecyclerView.Adapter<RepoAdpater.RepoViewHolder> {

    private List<Repo> items;

    public RepoAdpater(List<Repo> items) {
        this.items = items;
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
        Repo repo = items.get(position);
        holder.bind(repo);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    void clearItems() {
        int size = this.items.size();
        this.items.clear();
        notifyItemRangeRemoved(0, size);
    }

    void swapItems(List<Repo> newItems) {
        if (newItems == null) {
            int oldSize = this.items.size();
            this.items.clear();
            notifyItemRangeRemoved(0, oldSize);
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new RepoDiffCallback(this.items, newItems));
            this.items.clear();
            this.items.addAll(newItems);
            result.dispatchUpdatesTo(this);
        }
    }

    private class RepoDiffCallback extends DiffUtil.Callback {

        private List<Repo> mOldList;
        private List<Repo> mNewList;

        public RepoDiffCallback(List<Repo> oldList, List<Repo> newList) {
            this.mOldList = oldList;
            this.mNewList = newList;
        }

        @Override
        public int getOldListSize() {
            return mOldList != null ? mOldList.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return mNewList != null ? mNewList.size() : 0;
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            int oldId = mOldList.get(oldItemPosition).id;
            int newId = mNewList.get(newItemPosition).id;
            return Objects.equals(oldId, newId);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Repo oldRepo = mOldList.get(oldItemPosition);
            Repo newRepo = mNewList.get(newItemPosition);
            return Objects.equals(oldRepo, newRepo);
        }
    }

}

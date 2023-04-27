package com.github.data.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RepoSearchResponse {

    @SerializedName("total_count")
    private int total;

    @SerializedName("items")
    private List<Repo> items;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Repo> getItems() {
        return items;
    }

    public void setItems(List<Repo> items) {
        this.items = items;
    }

    @NonNull
    public List<Integer> getRepoIds() {
        List<Integer> repoIds = new ArrayList<>();
        for (Repo repo : items) {
            repoIds.add(repo.id);
        }
        return repoIds;
    }
}

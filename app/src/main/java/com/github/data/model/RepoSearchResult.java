package com.github.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.github.data.db.GithubTypeConverters;

import java.util.List;

@Entity
@TypeConverters(GithubTypeConverters.class)
public class RepoSearchResult {
    @NonNull
    @PrimaryKey
    public final String query;
    public final List<Integer> repoIds;
    public final int totalCount;

    public RepoSearchResult(@NonNull String query, List<Integer> repoIds, int totalCount) {
        this.query = query;
        this.repoIds = repoIds;
        this.totalCount = totalCount;
    }
}

package com.github.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.github.data.model.Repo;
import com.github.data.model.RepoSearchResult;

/**
 * Main database description.
 */
@Database(entities = {RepoSearchResult.class, Repo.class}, version = 1)
public abstract class GithubDb extends RoomDatabase {
    abstract public RepoDao repoDao();
}

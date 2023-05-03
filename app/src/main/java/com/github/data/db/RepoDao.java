package com.github.data.db;

import android.util.SparseIntArray;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.github.data.model.Repo;
import com.github.data.model.RepoSearchResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Interface for database access on Repo related operations.
 */
@Dao
public abstract class RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Repo... repos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertRepos(List<Repo> repositories);

    @Query("SELECT * FROM repo WHERE owner_login = :login AND name = :name")
    public abstract LiveData<Repo> load(String login, String name);

    @Query("SELECT * FROM Repo " + "WHERE owner_login = :owner " + "ORDER BY stars DESC")
    public abstract LiveData<List<Repo>> loadRepositories(String owner);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(RepoSearchResult result);

    @Query("SELECT * FROM RepoSearchResult WHERE query = :query")
    public abstract LiveData<RepoSearchResult> search(String query);

    @Query("SELECT * FROM Repo WHERE id in (:repoIds)")
    public abstract DataSource.Factory<Integer, Repo> loadById(List<Integer> repoIds);

    @Query("SELECT * FROM RepoSearchResult WHERE query = :query")
    public abstract RepoSearchResult findSearchResult(String query);
}

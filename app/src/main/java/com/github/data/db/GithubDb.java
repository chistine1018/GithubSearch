package com.github.data.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.github.data.model.Repo;
import com.github.data.model.RepoSearchResult;

/**
 * Main database description.
 */
@Database(entities = {RepoSearchResult.class, Repo.class}, version = 2)
public abstract class GithubDb extends RoomDatabase {
    abstract public RepoDao repoDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(" ALTER TABLE Repo" + " ADD COLUMN html_url TEXT");
        }
    };
}

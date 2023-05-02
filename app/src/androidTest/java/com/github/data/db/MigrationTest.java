package com.github.data.db;

import static com.github.data.db.GithubDb.MIGRATION_1_2;
import static com.github.util.LiveDataTestUtil.getValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.github.data.model.Repo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;


@RunWith(AndroidJUnit4.class)
public class MigrationTest {

    private static final String TEST_DB_NAME = "migration-test";
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();
    
    @Rule
    public MigrationTestHelper testHelper =
            new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                    GithubDb.class.getCanonicalName(),
                    new FrameworkSQLiteOpenHelperFactory());

    @Test
    public void migrate1To2() throws IOException, InterruptedException {
        SupportSQLiteDatabase db = testHelper.createDatabase(TEST_DB_NAME, 1);

        // db has schema version 1. insert some data using SQL queries.
        // You cannot use DAO classes because they expect the latest schema.
        insertRepo("foo_name", "foo_login", db);

        // Prepare for the next version.
        db.close();

        // Re-open the database with version 2 and provide
        // MIGRATION_1_2 as the migration process.
        db = testHelper.runMigrationsAndValidate(TEST_DB_NAME, 2, true, MIGRATION_1_2);

        // MigrationTestHelper automatically verifies the schema changes,
        // but you need to validate that the data was migrated properly.

        // open the db with Room.
        GithubDb githubDb = getMigratedRoomDatabase();
        final Repo loaded = getValue(githubDb.repoDao().load("foo_login", "foo_name"));
        assertThat(loaded.owner.login, is("foo_login"));
        assertThat(loaded.name, is("foo_name"));
    }

    private void insertRepo(String name, String owner_login, SupportSQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("id", 1);
        values.put("name", name);
        values.put("stars", 50);
        values.put("owner_login", owner_login);

        db.insert("Repo", SQLiteDatabase.CONFLICT_REPLACE, values);
    }

    private GithubDb getMigratedRoomDatabase() {
        GithubDb database = Room.databaseBuilder(InstrumentationRegistry.getTargetContext(),
                        GithubDb.class, TEST_DB_NAME)
                .addMigrations(MIGRATION_1_2)
                .build();
        // close the database and release any stream resources when the test finishes
        testHelper.closeWhenFinished(database);
        return database;
    }
}
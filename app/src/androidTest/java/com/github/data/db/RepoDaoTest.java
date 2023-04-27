package com.github.data.db;

import static com.github.util.LiveDataTestUtil.getValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.github.data.model.Owner;
import com.github.data.model.Repo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)

public class RepoDaoTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule =
            new InstantTaskExecutorRule();
    
    private GithubDb db;
    private Repo repo;


    @Before
    public void setUp() throws Exception {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                GithubDb.class).build();

        Owner owner = new Owner("foo", null, null);
        repo = new Repo(1, "foo", "foo/bar", "desc", 50, owner);
    }

    @After
    public void closeDb() throws Exception {
        db.close();
    }

    @Test
    public void insertAndLoad() throws InterruptedException {
        // Insert repo
        db.repoDao().insert(repo);
        // Query repo
        final Repo loaded = getValue(db.repoDao().load("foo", "foo"));
        // Assert query result
        assertThat(loaded.owner.login, is("foo"));
        assertThat(loaded.name, is("foo"));
    }
}
package com.github.di;

import androidx.room.Room;

import com.github.GithubApplication;
import com.github.api.GithubService;
import com.github.data.db.GithubDb;
import com.github.data.db.RepoDao;
import com.github.util.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
class AppModule {

    @Provides
    @Singleton
    GithubService provideGithubService() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(GithubService.class);
    }

    @Provides
    @Singleton
    GithubDb provideDb(GithubApplication application) {
        return Room.databaseBuilder(application, GithubDb.class, "github.db")
                .addMigrations(GithubDb.MIGRATION_1_2)
                .build();
    }

    @Provides
    @Singleton
    RepoDao provideRepoDao(GithubDb db) {
        return db.repoDao();
    }
}

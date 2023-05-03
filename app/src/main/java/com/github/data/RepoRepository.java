package com.github.data;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.github.api.ApiResponse;
import com.github.api.GithubService;
import com.github.data.db.RepoDao;
import com.github.data.model.Repo;
import com.github.data.model.RepoSearchResponse;
import com.github.data.model.RepoSearchResult;
import com.github.data.model.Resource;
import com.github.util.AbsentLiveData;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class RepoRepository {

    private RepoDao repoDao;

    private GithubService githubService;

    @Inject
    public RepoRepository(RepoDao repoDao, GithubService githubService) {
        this.repoDao = repoDao;
        this.githubService = githubService;
    }

    public LiveData<Resource<PagedList<Repo>>> search(final String query) {
        return new NetworkBoundResource<PagedList<Repo>, RepoSearchResponse>() {
            @NonNull
            @Override
            protected LiveData<PagedList<Repo>> loadFromDb() {
                return Transformations.switchMap(repoDao.search(query), new Function<RepoSearchResult, LiveData<PagedList<Repo>>>() {
                    @Override
                    public LiveData<PagedList<Repo>> apply(RepoSearchResult searchData) {
                        if (searchData == null) {
                            return AbsentLiveData.create();
                        } else {
                            return new LivePagedListBuilder<>(repoDao.loadById(searchData.repoIds),30).build();
                        }
                    }
                });
            }

            @Override
            protected boolean shouldFetch(@Nullable PagedList<Repo> data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RepoSearchResponse>> createCall() {
                return githubService.searchRepos(query);
            }

            @Override
            protected void saveCallResult(@NonNull RepoSearchResponse item) {
                List<Integer> repoIds = item.getRepoIds();
                RepoSearchResult repoSearchResult =
                        new RepoSearchResult(query, repoIds, item.getTotal());

                repoDao.insertRepos(item.getItems());
                repoDao.insert(repoSearchResult);
            }
        }.asLiveData();
    }
}

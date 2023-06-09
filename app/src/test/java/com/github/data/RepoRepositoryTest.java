package com.github.data;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.github.api.ApiResponse;
import com.github.api.GithubService;
import com.github.data.db.RepoDao;
import com.github.data.model.Repo;
import com.github.data.model.RepoSearchResponse;
import com.github.data.model.RepoSearchResult;
import com.github.data.model.Resource;
import com.github.util.AbsentLiveData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@SuppressWarnings("unchecked")
@RunWith(JUnit4.class)
public class RepoRepositoryTest {

    private RepoRepository repository;
    private RepoDao repoDao;
    private GithubService githubService;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void init() {
        repoDao = mock(RepoDao.class);
        githubService = mock(GithubService.class);
        repository = new RepoRepository(repoDao, githubService);
    }

    @Test
    public void search() {
        repository.search("abc");
        verify(repoDao).search("abc");
    }

    @Test
    public void search_fromDb() {
        MutableLiveData<RepoSearchResult> dbSearchResult = new MutableLiveData<>();
        when(repoDao.search("foo")).thenReturn(dbSearchResult);

        Observer<Resource<List<Repo>>> observer = mock(Observer.class);
        repository.search("foo").observeForever(observer);

        verify(observer).onChanged(Resource.<List<Repo>>loading(null));
        verifyNoMoreInteractions(githubService);
        reset(observer);

        List<Integer> ids = Arrays.asList(1, 2);
        RepoSearchResult dbResult = new RepoSearchResult("foo", ids, 2);

        MutableLiveData<List<Repo>> repos = new MutableLiveData<>();
        when(repoDao.loadOrdered(ids)).thenReturn(repos);

        dbSearchResult.postValue(dbResult);

        List<Repo> repoList = new ArrayList<>();
        repos.postValue(repoList);
        verify(observer).onChanged(Resource.success(repoList));
        verifyNoMoreInteractions(githubService);
    }

    @Test
    public void search_fromServer() {
        MutableLiveData<RepoSearchResult> dbSearchResult = new MutableLiveData<>();
        when(repoDao.search("foo")).thenReturn(dbSearchResult);

        Observer<Resource<List<Repo>>> observer = mock(Observer.class);
        repository.search("foo").observeForever(observer);

        verify(observer).onChanged(Resource.<List<Repo>>loading(null));
        verifyNoMoreInteractions(githubService);
        reset(observer);

        MutableLiveData<ApiResponse<RepoSearchResponse>> callLiveData = new MutableLiveData<>();
        when(githubService.searchRepos("foo")).thenReturn(callLiveData);

        dbSearchResult.postValue(null);

        verify(repoDao, never()).loadOrdered((List<Integer>) any());
        verify(githubService).searchRepos("foo");
    }

    @Test
    public void search_fromServer_error() {
        when(repoDao.search("foo")).thenReturn(AbsentLiveData.<RepoSearchResult>create());
        MutableLiveData<ApiResponse<RepoSearchResponse>> apiResponse = new MutableLiveData<>();
        when(githubService.searchRepos("foo")).thenReturn(apiResponse);

        Observer<Resource<List<Repo>>> observer = mock(Observer.class);
        repository.search("foo").observeForever(observer);

        apiResponse.postValue(new ApiResponse<RepoSearchResponse>(new Exception("idk")));
        verify(observer).onChanged(Resource.<List<Repo>>error(null, "idk"));
    }
}
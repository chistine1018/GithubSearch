package com.github.ui;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.github.R;
import com.github.data.model.Repo;
import com.github.data.model.Resource;
import com.github.testing.SingleFragmentActivity;
import com.github.util.EspressoTestUtil;
import com.github.util.RecyclerViewMatcher;
import com.github.util.TaskExecutorWithIdlingResourceRule;
import com.github.util.TestUtil;
import com.github.util.ViewModelUtil;
import com.github.viewmodel.RepoViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;



@RunWith(AndroidJUnit4.class)
public class RepoFragmentTest {

    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

    @Rule
    public TaskExecutorWithIdlingResourceRule executorRule =
            new TaskExecutorWithIdlingResourceRule();

    private RepoViewModel viewModel;

    private MutableLiveData<Resource<List<Repo>>> repos = new MutableLiveData<>();

    @Before
    public void init() {
        EspressoTestUtil.disableProgressBarAnimations(activityRule);
        RepoFragment repoFragment = new RepoFragment();
        viewModel = mock(RepoViewModel.class);
        when(viewModel.getRepos()).thenReturn(repos);
        repoFragment.factory = ViewModelUtil.createFor(viewModel);
        activityRule.getActivity().setFragment(repoFragment);
    }

    @Test
    public void search() {
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.edtQuery)).perform(typeText("foo"));
        onView(withId(R.id.btnSearch)).perform(click());
        verify(viewModel).searchRepo("foo");
        repos.postValue(Resource.<List<Repo>>loading(null));

        //Anson bug
        //onView(withId(R.id.progressBar)).check(matches(isDisplayed()));
    }

    @Test
    public void loadResults() {
        Repo repo = TestUtil.createRepo("foo", "bar", "desc");
        repos.postValue(Resource.success(Arrays.asList(repo)));
        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("foo/bar"))));
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())));
    }

    @NonNull
    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.recyclerView);
    }
}
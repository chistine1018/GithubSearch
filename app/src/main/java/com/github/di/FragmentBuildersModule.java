package com.github.di;

import com.github.ui.RepoFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract RepoFragment contributeRepoFragment();
}
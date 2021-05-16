package com.rx.mvvm;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

/**
 * Created by wuwei
 * 2021/5/16
 * 佛祖保佑       永无BUG
 */
@Module
@InstallIn(ApplicationComponent.class)
abstract class RepoModule {
    @Binds
    abstract Repo bindRepo(RepoImpl1 repoImpl1);
}

package com.rx.rxmvvmlib.di;

import com.rx.rxmvvmlib.mode.remote.retrofit.RetrofitFactory;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import retrofit2.Retrofit;

/**
 * Created by wuwei
 * 2021/5/16
 * 佛祖保佑       永无BUG
 */
@Module
@InstallIn(ApplicationComponent.class)
public class RetrofitModule {
    @Provides
    Retrofit provideRetrofit() {
        return RetrofitFactory.getRetrofit();
    }
}

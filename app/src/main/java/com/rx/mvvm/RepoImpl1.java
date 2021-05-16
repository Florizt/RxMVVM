package com.rx.mvvm;

import android.util.Log;

import javax.inject.Inject;

import retrofit2.Retrofit;

/**
 * Created by wuwei
 * 2021/5/16
 * 佛祖保佑       永无BUG
 */
public class RepoImpl1 implements Repo {


    @Inject
    public RepoImpl1() {

    }

    @Inject
    Retrofit retrofit;

    @Override
    public void get() {
        Log.i("TAG", "gesasat---111: " +retrofit);
        retrofit.create(TestApi.class).add();
    }
}

package com.rx.mvvm.repository.impl;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.rx.mvvm.repository.IUserRepository;
import com.rx.mvvm.repository.datasource.local.IUserLocalService;
import com.rx.mvvm.repository.datasource.remote.IUserService;
import com.rx.mvvm.repository.entity.HttpResult;
import com.rx.rxmvvmlib.mode.BaseEntity;
import com.rx.rxmvvmlib.mode.remote.retrofit.TFunc;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wuwei
 * 2021/5/19
 * 佛祖保佑       永无BUG
 */
public class UserRepository implements IUserRepository {

    private IUserService userService;
    private IUserLocalService userLocalService;

    public UserRepository(IUserService userService, IUserLocalService userLocalService) {
        this.userService = userService;
        this.userLocalService = userLocalService;
    }

    @SuppressLint("CheckResult")
    @Override
    public Observable login() {
        return userService.login()
                .map(new TFunc<HttpResult, BaseEntity>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void saveUserCache(int uid, String token) {
        userLocalService.saveUserCache(uid, token);
    }

    @Override
    public int getUid() {
        return userLocalService.getUid();
    }

    @Override
    public String getToken() {
        return userLocalService.getToken();
    }

    @Override
    public boolean isLogin() {
        return !TextUtils.isEmpty(getToken());
    }
}

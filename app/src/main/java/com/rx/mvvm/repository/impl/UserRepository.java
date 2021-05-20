package com.rx.mvvm.repository.impl;

import android.annotation.SuppressLint;

import com.rx.mvvm.repository.IUserRepository;
import com.rx.mvvm.repository.entity.HttpResult;
import com.rx.mvvm.repository.service.IUserService;
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

    public UserRepository(IUserService userService) {
        this.userService = userService;
    }

    @SuppressLint("CheckResult")
    @Override
    public Observable login() {
        return userService.login()
                .map(new TFunc<HttpResult, BaseEntity>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

package com.rx.mvvm.repository;

import com.rx.mvvm.repository.impl.UserRepository;
import com.rx.mvvm.repository.service.IUserService;
import com.rx.rxmvvmlib.mode.remote.retrofit.RetrofitFactory;

/**
 * Created by wuwei
 * 2021/5/19
 * 佛祖保佑       永无BUG
 */
public class RepositoryFactory {
    public static IUserRepository getUserRepository() {
        IUserService userService = RetrofitFactory.create(IUserService.class);
        return new UserRepository(userService);
    }
}

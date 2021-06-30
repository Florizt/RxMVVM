package com.rx.mvvm.repository;

import com.rx.mvvm.repository.datasource.local.IUserLocalService;
import com.rx.mvvm.repository.greendao.DaoManager;
import com.rx.mvvm.repository.impl.UserRepository;
import com.rx.mvvm.repository.datasource.remote.IUserService;
import com.rx.rxmvvmlib.repository.datasource.locate.LocalFactory;
import com.rx.rxmvvmlib.repository.datasource.remote.retrofit.RetrofitFactory;
import com.rx.rxmvvmlib.util.UIUtil;

/**
 * Created by wuwei
 * 2021/5/19
 * 佛祖保佑       永无BUG
 */
public class RepositoryFactory {
    public static IUserRepository getUserRepository() {
        IUserService userService = RetrofitFactory.create(IUserService.class);
        IUserLocalService userLocalService = LocalFactory.create(IUserLocalService.class,
                UIUtil.getContext(), DaoManager.getInstance().getDaoSession().getUserDao());
        return new UserRepository(userService, userLocalService);
    }
}

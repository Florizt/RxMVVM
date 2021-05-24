package com.rx.mvvm.repository;

import io.reactivex.Observable;

/**
 * Created by wuwei
 * 2021/5/19
 * 佛祖保佑       永无BUG
 */
public interface IUserRepository {
    Observable login();

    void saveUserCache(int uid, String token);

    int getUid();

    String getToken();

    boolean isLogin();
}

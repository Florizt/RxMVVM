package com.rx.mvvm.repository.service;

import com.rx.mvvm.repository.entity.HttpResult;
import com.rx.rxmvvmlib.mode.BaseEntity;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by wuwei
 * 2021/5/19
 * 佛祖保佑       永无BUG
 */
public interface IUserService {
    @POST("test/login")
    Observable<HttpResult<BaseEntity>> login();
}

package com.rx.mvvm;

import com.rx.rxmvvmlib.mode.BaseEntity;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by wuwei
 * 2020/12/28
 * 佛祖保佑       永无BUG
 */
public interface TestApi {
    @POST("add")
    Observable<HttpResultImpl<BaseEntity>> add();
}

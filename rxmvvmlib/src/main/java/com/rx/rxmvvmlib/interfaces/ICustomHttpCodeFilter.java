package com.rx.rxmvvmlib.interfaces;

import com.rx.rxmvvmlib.http.TObserver;

/**
 * Created by wuwei
 * 2021/3/4
 * 佛祖保佑       永无BUG
 */
public interface ICustomHttpCodeFilter {
    void onFilter(TObserver observer, String code, String message);
}

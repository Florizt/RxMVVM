package com.rx.rxmvvmlib.interfaces;

/**
 * Created by wuwei
 * 2021/3/4
 * 佛祖保佑       永无BUG
 */
public interface ICustomHttpCodeFilter {
    void onFilter(String code, String message);
}

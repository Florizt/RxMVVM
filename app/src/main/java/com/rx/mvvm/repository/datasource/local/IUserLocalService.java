package com.rx.mvvm.repository.datasource.local;

import com.rx.rxmvvmlib.mode.locate.L_GET;
import com.rx.rxmvvmlib.mode.locate.LocalType;
import com.rx.rxmvvmlib.mode.locate.L_POST;

/**
 * Created by wuwei
 * 2021/5/24
 * 佛祖保佑       永无BUG
 */
public interface IUserLocalService {
    @L_POST(type = LocalType.SP, key = {"uid", "token"})
    void saveUserCache(int uid, String token);

    @L_GET(type = LocalType.SP, key = {"uid"})
    int getUid();

    @L_GET(type = LocalType.SP, key = {"token"})
    String getToken();
}

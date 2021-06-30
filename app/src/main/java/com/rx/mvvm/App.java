package com.rx.mvvm;

import android.app.Application;

import com.rx.mvvm.aop.anno.ArouterDestroy;
import com.rx.mvvm.aop.anno.ArouterInit;
import com.rx.mvvm.aop.anno.DBInit;
import com.rx.mvvm.repository.config.AppCfgsAdapter;
import com.rx.mvvm.repository.greendao.UserDao;
import com.rx.rxmvvmlib.aop.anno.RxMVVMInit;

/**
 * Created by wuwei
 * 2021/5/19
 * 佛祖保佑       永无BUG
 */
public class App extends Application {
    @RxMVVMInit(clazz = AppCfgsAdapter.class)
    @ArouterInit
    @DBInit(dbName = "test", daoClasses = {UserDao.class})
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @ArouterDestroy
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}

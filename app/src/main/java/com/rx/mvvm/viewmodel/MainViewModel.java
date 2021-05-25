package com.rx.mvvm.viewmodel;

import android.app.Application;
import android.util.Log;

import com.rx.mvvm.repository.IUserRepository;
import com.rx.mvvm.repository.RepositoryFactory;
import com.rx.mvvm.repository.entity.User;
import com.rx.rxmvvmlib.viewmodel.base.RxBaseViewModel;

import androidx.annotation.NonNull;

/**
 * Created by wuwei
 * 2021/5/19
 * 佛祖保佑       永无BUG
 */
public class MainViewModel extends RxBaseViewModel {

    private final IUserRepository userRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        userRepository = RepositoryFactory.getUserRepository();
    }

    @Override
    public void onCreate() {
        super.onCreate();
      /*  userRepository.login().subscribe(new TObserver<BaseEntity>() {
            @Override
            public void onRequestStart() {

            }

            @Override
            public void onRequestEnd() {

            }

            @Override
            public void onSuccees(BaseEntity baseEntity) {

            }

            @Override
            public void onFailure(String code, String message) {

            }
        });*/
        userRepository.saveUserCache(123, "哈哈哈哈哈哈");
        Log.i("TAG", "saveUserCsasache-111: " + userRepository.getUid());
        Log.i("TAG", "saveUserCsasache-222: " + userRepository.getToken());
        Log.i("TAG", "saveUserCsasache-333---------------------------: " );
        userRepository.saveUser(new User((long) 1,"aaa","张三",20));
        Log.i("TAG", "saveUserCsasache-444: " + userRepository.getUser(1).toString());
    }
}

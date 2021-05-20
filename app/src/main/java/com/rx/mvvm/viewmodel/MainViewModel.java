package com.rx.mvvm.viewmodel;

import android.app.Application;

import com.rx.mvvm.repository.IUserRepository;
import com.rx.mvvm.repository.RepositoryFactory;
import com.rx.rxmvvmlib.mode.BaseEntity;
import com.rx.rxmvvmlib.mode.remote.retrofit.TObserver;
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
        userRepository.login().subscribe(new TObserver<BaseEntity>() {
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
        });
    }
}

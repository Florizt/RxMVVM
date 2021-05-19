package com.rx.mvvm;

import android.app.Application;

import com.rx.rxmvvmlib.viewmodel.base.RxBaseViewModel;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

/**
 * Created by wuwei
 * 2020/10/24
 * 佛祖保佑       永无BUG
 */


public class LoginViewModel extends RxBaseViewModel {

    public ObservableField<String> account = new ObservableField<>();
    public ObservableField<String> psw = new ObservableField<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }
}

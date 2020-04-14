package com.rx.mvvm.viewmodel;

import android.app.Application;

import com.rx.rxmvvmlib.base.BaseViewModel;
import com.rx.rxmvvmlib.binding.command.BindingAction;
import com.rx.rxmvvmlib.binding.command.BindingCommand;
import com.rx.rxmvvmlib.util.UIUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;

/**
 * Created by wuwei
 * 2020/4/14
 * 佛祖保佑       永无BUG
 */
public class MainViewModel extends BaseViewModel {
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public int marginTop() {
        return UIUtils.getStatusBarHeight();
    }

    public ObservableBoolean isExit = new ObservableBoolean(true);

    public BindingCommand setExit = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isExit.set(!isExit.get());
        }
    });
}

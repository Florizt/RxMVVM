package com.rx.mvvm.ui.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rx.mvvm.R;
import com.rx.mvvm.databinding.ActivityMainBinding;
import com.rx.mvvm.repository.config.ArouterConfig;
import com.rx.mvvm.viewmodel.MainViewModel;
import com.rx.rxmvvmlib.ui.base.RxBaseActivity;

@Route(path = ArouterConfig.ACTIVITY_URL_MAIN)
public class MainActivity extends RxBaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

    }

    @Override
    public int initLoadingLayoutId() {
        return 0;
    }

    @Override
    public boolean loadingCancelable() {
        return false;
    }

    @Override
    public boolean isExit() {
        return false;
    }

    @Override
    public void doSthIsExit() {

    }

    @Override
    public boolean immersionBarEnabled() {
        return false;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public boolean statusBarDarkFont() {
        return false;
    }

    @Override
    public boolean fitsSystemWindows() {
        return false;
    }

    @Override
    public int statusBarColor() {
        return 0;
    }

    @Override
    public int navigationBarColor() {
        return 0;
    }
}

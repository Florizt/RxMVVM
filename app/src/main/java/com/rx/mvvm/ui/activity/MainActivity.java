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
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

    }
}

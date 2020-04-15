package com.rx.mvvm.ui;

import android.os.Bundle;

import com.rx.mvvm.R;
import com.rx.mvvm.databinding.ActivityMainBinding;
import com.rx.mvvm.viewmodel.MainViewModel;
import com.rx.rxmvvmlib.base.BaseActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;


public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    @Override
    public int initLayoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return com.rx.mvvm.BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        binding.recyc.setLayoutManager(linearLayoutManager);
        binding.setAdapter(new BindingRecyclerViewAdapter());
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

    }

    @Override
    protected boolean isExit() {
        return viewModel.isExit.get();
    }

    @Override
    protected void doSthIsExit() {
        finish();
    }

    @Override
    protected boolean immersionBarEnabled() {
        return true;
    }

    @Override
    protected boolean isFullScreen() {
        return false;
    }

    @Override
    protected boolean statusBarDarkFont() {
        return true;
    }
}

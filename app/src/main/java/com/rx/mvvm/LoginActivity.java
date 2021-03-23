package com.rx.mvvm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rx.mvvm.databinding.ActivityLoginBinding;
import com.rx.rxmvvmlib.base.BaseActivity;


/**
 * Created by wuwei
 * 2020/10/24
 * 佛祖保佑       永无BUG
 */
public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> {

    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    public int initLayoutId(Bundle bundle) {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initParam() {

    }

    @Override
    public void initView(Bundle bundle) {
        binding.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void initViewObservable() {

    }

    @Override
    protected boolean statusBarDarkFont() {
        return false;
    }
}

package com.rx.mvvm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rx.mvvm.databinding.ActivityLoginBinding;
import com.rx.rxmvvmlib.base.BaseActivity;
import com.rx.rxmvvmlib.base.BaseEntity;
import com.rx.rxmvvmlib.http.RetrofitFactory;
import com.rx.rxmvvmlib.http.TFunc;
import com.rx.rxmvvmlib.http.TObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


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

    }

    @Override
    public void initData() {
        RetrofitFactory.apiService(TestApi.class).add()
                .map(new TFunc<HttpResultImpl, BaseEntity>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new TObserver<BaseEntity>() {
                    @Override
                    protected void onRequestStart() {

                    }

                    @Override
                    protected void onRequestEnd() {

                    }

                    @Override
                    protected void onSuccees(BaseEntity baseEntity) {

                    }

                    @Override
                    protected void onFailure(String code, String message) {

                    }
                });
    }

    @Override
    public void initViewObservable() {

    }

    @Override
    protected boolean statusBarDarkFont() {
        return false;
    }
}

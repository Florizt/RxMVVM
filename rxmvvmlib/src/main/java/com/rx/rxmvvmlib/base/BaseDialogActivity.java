package com.rx.rxmvvmlib.base;

import android.os.Bundle;
import android.view.Gravity;

import androidx.databinding.ViewDataBinding;

/**
 * Created by wuwei
 * 2021/3/23
 * 佛祖保佑       永无BUG
 */
public abstract class BaseDialogActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity {
    protected V binding;
    protected VM viewModel;

    @Override
    public void initView(Bundle savedInstanceState) {
        this.binding = (V) super.binding;
        this.viewModel = (VM) super.viewModel;

        getWindow().setGravity(setGravity());
        getWindow().setWindowAnimations(setWindowAnimations());
        getWindow().setDimAmount(setDimAmount());
    }

    protected int setGravity() {
        return Gravity.CENTER;
    }

    protected int setWindowAnimations() {
        return android.R.style.Animation_Dialog;
    }

    protected float setDimAmount() {
        return 1.0f;
    }
}

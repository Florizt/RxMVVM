package com.rx.rxmvvmlib.view.base;

import android.os.Bundle;

import com.rx.rxmvvmlib.viewmodel.base.RxBaseViewModel;
import com.rx.rxmvvmlib.view.IBaseDialogView;

import androidx.databinding.ViewDataBinding;

/**
 * Created by wuwei
 * 2021/3/23
 * 佛祖保佑       永无BUG
 */
public abstract class RxBaseDialogActivity<V extends ViewDataBinding, VM extends RxBaseViewModel>
        extends RxBaseActivity implements IBaseDialogView {
    protected V binding;
    protected VM viewModel;

    @Override
    public void initView(Bundle savedInstanceState) {
        this.binding = (V) super.binding;
        this.viewModel = (VM) super.viewModel;

        getWindow().setGravity(setGravity());
        getWindow().setWindowAnimations(setWindowAnimations());
        getWindow().setDimAmount(setDimAmount());
        setFinishOnTouchOutside(setCanceledOnTouchOutside());
    }
}

package com.rx.rxmvvmlib.ui.binding.viewadapter.viewgroup;

import androidx.databinding.ViewDataBinding;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */
public interface IBindingItemViewModel<V extends ViewDataBinding> {
    void injecDataBinding(V binding);
}

package com.rx.rxmvvmlib.viewmodel.base;


import androidx.annotation.NonNull;

/**
 * *  Created by wuwei
 * *  2019/12/6
 * *   佛祖保佑
 * <p>
 * ItemViewModel
 */

public class SubItemViewModel<VM extends ItemViewModel> {
    protected VM viewModel;

    public SubItemViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
    }
}

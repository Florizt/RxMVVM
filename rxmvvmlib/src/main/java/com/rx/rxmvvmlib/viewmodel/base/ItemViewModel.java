package com.rx.rxmvvmlib.viewmodel.base;


import androidx.annotation.NonNull;

/**
 * *  Created by wuwei
 * *  2019/12/6
 * *   佛祖保佑
 * <p>
 * ItemViewModel
 */

public class ItemViewModel<VM extends RxBaseViewModel> {
    protected VM viewModel;

    public ItemViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
    }
}

package com.rx.rxmvvmlib.base;


import androidx.annotation.NonNull;

/**
 * *  Created by wuwei
 * *  2019/12/6
 * *   佛祖保佑
 * <p>
 * ItemViewModel
 */

public class ItemViewModel<VM extends BaseViewModel> {
    protected VM viewModel;

    public ItemViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
    }
}

package com.rx.rxmvvmlib.viewmodel.base;


import androidx.annotation.NonNull;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 * <p>
 * Description：RecycleView多布局ItemViewModel是封装
 */

public class MultiItemViewModel<VM extends RxBaseViewModel> extends ItemViewModel<VM> {
    protected Object multiType;

    public Object getItemType() {
        return multiType;
    }

    public void multiItemType(@NonNull Object multiType) {
        this.multiType = multiType;
    }

    public MultiItemViewModel(@NonNull VM viewModel) {
        super(viewModel);
    }
}

package com.rx.rxmvvmlib.ui;

import android.os.Bundle;


public interface IBaseView {

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    int initLayoutId(Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    int initVariableId();

    /**
     * 初始化界面传递参数
     */
    void initParam();

    /**
     * 初始化界面
     */
    void initView(Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    void initData();

    /**
     * 初始化界面观察者的监听
     */
    void initViewObservable();

    /**
     * loading弹出布局
     *
     * @return
     */
    int initLoadingLayoutId();

    /**
     * loading弹出是否消失
     *
     * @return
     */
    boolean loadingCancelable();

    /**
     * 按返回键是否只是返回
     *
     * @return
     */
    boolean isBack();

    /**
     * 按返回键仅仅只是返回上个界面时要做的操作
     */
    void doSthIsBack();
}

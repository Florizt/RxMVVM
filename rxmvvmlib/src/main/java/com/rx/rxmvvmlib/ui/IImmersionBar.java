package com.rx.rxmvvmlib.ui;

public interface IImmersionBar {
    /**
     * 是否开启沉浸式
     *
     * @return
     */
    boolean immersionBarEnabled();

    /**
     * 沉浸式下是否全屏
     *
     * @return
     */
    boolean isFullScreen();

    /**
     * 沉浸式非全屏下状态栏字体是否深色
     *
     * @return
     */
    boolean statusBarDarkFont();

    /**
     * 是否fitsSystemWindows
     *
     * @return
     */
    boolean fitsSystemWindows();

    /**
     * 沉浸式非全屏下状态栏背景颜色
     *
     * @return
     */
    int statusBarColor();

    /**
     * 沉浸式非全屏下底部导航栏背景颜色
     *
     * @return
     */
    int navigationBarColor();

    /**
     * 解决EditText与软键盘冲突
     *
     * @return true：EditText跟随软键盘弹起，false反之
     */
    boolean keyboardEnable();

    /**
     * 软键盘模式
     *
     * @return
     */
    int keyboardMode();
}

package com.rx.rxmvvmlib.base;

import android.os.Bundle;
import android.view.Gravity;

/**
 * Created by wuwei
 * 2021/3/23
 * 佛祖保佑       永无BUG
 */
public abstract class BaseDialogActivity extends BaseActivity {
    @Override
    public void initView(Bundle savedInstanceState) {
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

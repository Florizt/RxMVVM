package com.rx.mvvm;

import com.rx.rxmvvmlib.aop.anno.LoginReExecute;
import com.rx.rxmvvmlib.aop.ILoginCheck;
import com.rx.rxmvvmlib.util.LogUtil;
import com.rx.rxmvvmlib.util.UIUtils;

/**
 * Created by wuwei
 * 2021/4/28
 * 佛祖保佑       永无BUG
 */
public class LoginCheckImpl implements ILoginCheck {
    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public void toLogin() {
        LogUtil.i("开始登录");
        UIUtils.postDelayTask(new Runnable() {
            @Override
            public void run() {
                loginSuccess();
            }
        }, 1000);
    }

    @LoginReExecute
    private void loginSuccess() {
        LogUtil.i("登录成功");
    }
}

package com.rx.rxmvvmlib.ui.base;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.gyf.barlibrary.OnKeyboardListener;
import com.rx.rxmvvmlib.R;
import com.rx.rxmvvmlib.RMEngine;
import com.rx.rxmvvmlib.databinding.ActivityBaseBinding;
import com.rx.rxmvvmlib.ui.IBaseView;
import com.rx.rxmvvmlib.ui.IImmersionBar;
import com.rx.rxmvvmlib.ui.IKeyboardChange;
import com.rx.rxmvvmlib.util.SoftKeyboardUtil;
import com.rx.rxmvvmlib.util.ToastUtil;
import com.rx.rxmvvmlib.util.UIUtil;
import com.rx.rxmvvmlib.viewmodel.base.RxBaseViewModel;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import me.jessyan.autosize.AutoSizeConfig;

/**
 * * Created by wuwei
 * * 2019/12/6
 * * 佛祖保佑       永无BUG
 * <p>
 * 一个拥有DataBinding框架的基Activity
 */

public abstract class RxBaseActivity<V extends ViewDataBinding, VM extends RxBaseViewModel>
        extends RxAppCompatActivity implements IBaseView, IImmersionBar, IKeyboardChange {
    protected RxBaseActivity activity;
    protected V binding;
    protected VM viewModel;
    private RxBaseLoadingDialog loadingDialog;
    private boolean isExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        //页面接受的参数方法
        initParam();
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding(savedInstanceState);
        //初始化界面
        initView(savedInstanceState);
        //页面数据初始化方法
        initData();
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
        //注册EventBus
        viewModel.registerEventBus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initImmersionBar();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftKeyBoard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.removeEventBus();
        if (binding != null) {
            binding.unbind();
        }
        ImmersionBar.with(this).destroy();
        dismissLoading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (RMEngine.getConfig().getActivityLifecycleCallbacks() != null) {
                RMEngine.getConfig().getActivityLifecycleCallbacks().onActivityResult(activity, requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initImmersionBar() {
        if (immersionBarEnabled()) {
            if (isFullScreen()) {
                ImmersionBar.with(this)
                        .hideBar(BarHide.FLAG_HIDE_BAR)
                        .init();
            } else {
                ImmersionBar.with(this)
                        .fitsSystemWindows(fitsSystemWindows())
                        .statusBarDarkFont(statusBarDarkFont(), 0.2f)
                        .statusBarColor(statusBarColor())
                        .navigationBarColor(navigationBarColor())
                        .keyboardEnable(keyboardEnable())
                        .keyboardMode(keyboardMode())
                        .setOnKeyboardListener(new OnKeyboardListener() {
                            @Override
                            public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                                RxBaseActivity.this.onKeyboardChange(isPopup, keyboardHeight);
                            }
                        })
                        .init();
            }
        }
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        ActivityBaseBinding baseBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_base, null, false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        binding = DataBindingUtil.inflate(getLayoutInflater(), initLayoutId(savedInstanceState), null, false);
        baseBinding.baseView.addView(binding.getRoot(), params);
        setContentView(baseBinding.getRoot());

        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = RxBaseViewModel.class;
            }
            viewModel = (VM) ViewModelProviders.of(this).get(modelClass);
        }
        //关联ViewModel
        binding.setVariable(initVariableId(), viewModel);

        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this);

        binding.setLifecycleOwner(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isBack()) {
                doSthIsBack();
            } else {
                exit();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void exit() {
        if (!isExit) {
            isExit = true;
            ToastUtil.showToast(activity, getString(R.string.press_the_exit_procedure_again));
            UIUtil.postDelayTask(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 1500);
        } else {
            try {
                if (RMEngine.getConfig().getActivityLifecycleCallbacks() != null) {
                    RMEngine.getConfig().getActivityLifecycleCallbacks().onAppExit(activity);
                }
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                AppManager.getInstance().appExit();
                System.exit(0);
            } catch (Exception e) {

            }
        }
    }

    /**
     * =====================================================================
     **/
    //注册ViewModel与View的契约UI回调事件
    private void registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel.getUC().getShowDialogEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                showLoading();
            }
        });

        //加载对话框消失
        viewModel.getUC().getDismissDialogEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                dismissLoading();
            }
        });

        viewModel.getUC().getHideSoftKeyBoardEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                hideSoftKeyBoard();
            }
        });

        viewModel.getUC().getBackEvent().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                doSthIsBack();
            }
        });
    }

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new RxBaseLoadingDialog(this,
                    initLoadingLayoutId() == 0 ? R.layout.loading_dialog : initLoadingLayoutId(),
                    loadingCancelable());
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    loadingDialog.show();
                }
            }
        });
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing()) {
                        loadingDialog.dismiss();
                    }
                }
            });
        }
    }

    public void hideSoftKeyBoard() {
        View v = getCurrentFocus();
        if (v != null) {
            SoftKeyboardUtil.hiddenSoftKeyboard(this, v.getWindowToken());
        }
    }

    public void stopAutoSize() {
        AutoSizeConfig.getInstance().stop(this);
    }

    public void restartAutoSize() {
        AutoSizeConfig.getInstance().restart();
    }

    protected void loadRootFragment(int containerId, @NonNull Fragment toFragment) {
        getSupportFragmentManager().beginTransaction()
                .add(containerId, toFragment).hide(toFragment).commitAllowingStateLoss();
    }

    protected void showFragment(@NonNull Fragment showFragment) {
        getSupportFragmentManager().beginTransaction()
                .show(showFragment).commitAllowingStateLoss();
    }

    protected void showFragment(@NonNull Fragment hideFragment, @NonNull Fragment showFragment) {
        getSupportFragmentManager().beginTransaction()
                .hide(hideFragment).show(showFragment).commitAllowingStateLoss();
    }

    protected void hideFragment(@NonNull Fragment showFragment) {
        getSupportFragmentManager().beginTransaction()
                .hide(showFragment).commitAllowingStateLoss();
    }

    //-----------------------------------------------默认接口------------------------------------------------------

    /**
     * 是否开启沉浸式
     *
     * @return
     */
    @Override
    public boolean immersionBarEnabled() {
        return true;
    }

    /**
     * 沉浸式下是否全屏
     *
     * @return
     */
    @Override
    public boolean isFullScreen() {
        return false;
    }

    /**
     * 沉浸式非全屏下状态栏字体是否深色
     *
     * @return
     */
    @Override
    public boolean statusBarDarkFont() {
        return true;
    }

    /**
     * 是否fitsSystemWindows
     *
     * @return
     */
    @Override
    public boolean fitsSystemWindows() {
        return true;
    }

    /**
     * 沉浸式非全屏下状态栏背景颜色
     *
     * @return
     */
    @Override
    public int statusBarColor() {
        return R.color.rx_ffffff;
    }

    /**
     * 沉浸式非全屏下底部导航栏背景颜色
     *
     * @return
     */
    @Override
    public int navigationBarColor() {
        return R.color.rx_ffffff;
    }

    /**
     * 按返回键是否只是返回
     *
     * @return
     */
    @Override
    public boolean isBack() {
        return true;
    }

    /**
     * 按返回键仅仅只是返回上个界面时要做的操作
     */
    @Override
    public void doSthIsBack() {
        finish();
    }

    /**
     * 初始化界面传递参数
     */
    @Override
    public void initParam() {

    }

    /**
     * loading弹出布局
     *
     * @return
     */
    @Override
    public int initLoadingLayoutId() {
        return R.layout.loading_dialog;
    }

    /**
     * loading弹出是否消失
     *
     * @return
     */
    @Override
    public boolean loadingCancelable() {
        return false;
    }

    @Override
    public boolean keyboardEnable() {
        return true;
    }

    @Override
    public int keyboardMode() {
        return WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
    }

    @Override
    public void onKeyboardChange(boolean isPopup, int keyboardHeight) {

    }
}

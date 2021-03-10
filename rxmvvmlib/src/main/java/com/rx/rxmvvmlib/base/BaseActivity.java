package com.rx.rxmvvmlib.base;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.rx.rxmvvmlib.R;
import com.rx.rxmvvmlib.databinding.ActivityBaseBinding;
import com.rx.rxmvvmlib.interfaces.IBaseView;
import com.rx.rxmvvmlib.util.SoftKeyboardUtil;
import com.rx.rxmvvmlib.util.UIUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import io.reactivex.functions.Consumer;
import me.jessyan.autosize.AutoSizeConfig;

/**
 * * Created by wuwei
 * * 2019/12/6
 * * 佛祖保佑       永无BUG
 * <p>
 * 一个拥有DataBinding框架的基Activity
 */
public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends RxAppCompatActivity implements IBaseView {
    protected BaseActivity activity;
    private ActivityBaseBinding baseBinding;
    protected V binding;
    protected VM viewModel;
    private int viewModelId;
    private BaseLoadingDialog loadingDialog;
    private boolean isExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        //注册ARouter
        ARouter.getInstance().inject(this);
        //页面接受的参数方法
        initParam();
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding(savedInstanceState);
        //初始化界面
        initView(savedInstanceState);
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack();
        //页面数据初始化方法
        initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
        //注册EventBus
        viewModel.registerEventBus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (immersionBarEnabled()) {
            if (isFullScreen()) {
                ImmersionBar.with(this)
                        .fullScreen(true)
                        .navigationBarColor(R.color.rx_transparent)
                        .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
                        .init();
            } else {
                ImmersionBar.with(this)
                        .fitsSystemWindows(true)
                        .statusBarDarkFont(statusBarDarkFont(), 0.2f)
                        .statusBarColor(statusBarColor())
                        .navigationBarColor(navigationBarColor())
                        .init();
            }
        }
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
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        baseBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_base, null, false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        binding = DataBindingUtil.inflate(getLayoutInflater(), initLayoutId(savedInstanceState), null, false);
        baseBinding.baseView.addView(binding.getRoot(), params);
        setContentView(baseBinding.getRoot());
        viewModelId = initVariableId();
        viewModel = initViewModel();
        if (viewModel == null) {
            Class modelClass;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            } else {
                //如果没有指定泛型参数，则默认使用BaseViewModel
                modelClass = BaseViewModel.class;
            }
            viewModel = (VM) createViewModel(this, modelClass);
        }
        //关联ViewModel
        binding.setVariable(viewModelId, viewModel);
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this);
    }

    //刷新布局
    public void refreshLayout() {
        if (viewModel != null) {
            binding.setVariable(viewModelId, viewModel);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit()) {
                doSthIsExit();
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
            UIUtils.showToast(getString(R.string.press_the_exit_procedure_again));
            UIUtils.postDelayTask(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 1500);
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                AppManager.getAppManager().appExit();
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
    }

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new BaseLoadingDialog(this, initLoadingLayoutId(), loadingCancelable());
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

    public void requestPermission(final int requestCode, final boolean showDialog, String... permissions) {
        try {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions
                    .request(permissions)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {
                                permissionGranted(requestCode);
                            } else {
                                permissionDenied(requestCode);
                            }
                        }
                    });
        } catch (Exception e) {

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

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    public void startAppSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception e) {

        }
    }


    /**
     * =====================================================================
     **/

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initLayoutId(Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();

    /**
     * 初始化ViewModel
     *
     * @return 继承BaseViewModel的ViewModel
     */
    public VM initViewModel() {
        return null;
    }

    public int initLoadingLayoutId() {
        return R.layout.loading_dialog;
    }

    public boolean loadingCancelable() {
        return false;
    }

    /**
     * 是否开启沉浸式
     *
     * @return
     */
    protected boolean immersionBarEnabled() {
        return true;
    }

    /**
     * 沉浸式下是否全屏
     *
     * @return
     */
    protected boolean isFullScreen() {
        return false;
    }

    /**
     * 沉浸式非全屏下状态栏字体是否深色
     *
     * @return
     */
    protected boolean statusBarDarkFont() {
        return true;
    }

    /**
     * 沉浸式非全屏下状态栏背景颜色
     *
     * @return
     */
    protected int statusBarColor() {
        return R.color.rx_ffffff;
    }

    /**
     * 沉浸式非全屏下底部导航栏背景颜色
     *
     * @return
     */
    protected int navigationBarColor() {
        return R.color.rx_000000;
    }

    /**
     * 按返回键是否只是返回
     *
     * @return
     */
    protected boolean isExit() {
        return true;
    }

    /**
     * 按返回键仅仅只是返回上个界面时要做的操作
     */
    protected void doSthIsExit() {

    }

    /**
     * 权限申请成功执行
     *
     * @param requestCode
     */
    protected void permissionGranted(int requestCode) {

    }

    /**
     * 权限申请失败执行
     *
     * @param requestCode
     */
    protected void permissionDenied(int requestCode) {

    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(FragmentActivity activity, Class<T> cls) {
        return ViewModelProviders.of(activity).get(cls);
    }
}

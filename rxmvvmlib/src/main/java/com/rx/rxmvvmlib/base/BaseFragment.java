package com.rx.rxmvvmlib.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rx.rxmvvmlib.R;
import com.rx.rxmvvmlib.util.LogUtil;
import com.rx.rxmvvmlib.util.SoftKeyboardUtil;
import com.rx.rxmvvmlib.util.keyboardutil.callback.IkeyBoardCallback;
import com.rx.rxmvvmlib.util.keyboardutil.core.KeyBoardEventBus;
import com.rx.rxmvvmlib.view.LoadingDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import io.reactivex.functions.Consumer;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */
public abstract class BaseFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends RxFragment implements IBaseView, IkeyBoardCallback {
    protected BaseActivity activity;
    protected V binding;
    protected VM viewModel;
    private int viewModelId;
    private LoadingDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseActivity) getActivity();
        initParam();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, initLayoutId(savedInstanceState), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //页面接受的参数方法
        initParam();
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();
        //初始化界面
        initView(savedInstanceState);
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack();
        //页面数据初始化方法
        initData();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
        //注册RxBus
        viewModel.registerEventBus();
        KeyBoardEventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (viewModel != null) {
            viewModel.removeEventBus();
        }
        if (binding != null) {
            binding.unbind();
        }
        if (loadingDialog != null) {
            dismissLoading();
        }
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding() {
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
    public void onKeyBoardShow(int keyBoardHeight) {
        LogUtil.i("ghy", "键盘显示-" + keyBoardHeight);
        BaseFragment.this.onKeyboardChange(true, keyBoardHeight);
    }

    @Override
    public void onKeyBoardHidden() {
        LogUtil.i("ghy", "键盘隐藏");
        BaseFragment.this.onKeyboardChange(false, 0);
    }

    public void requestPermission(final int requestCode, final boolean showDialog, String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions
                .request(permissions)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            permissionGranted(requestCode);
                        } else {
                            if (showDialog) {
                                showPermissionDialog(requestCode);
                            } else {
                                permissionDenied(requestCode);
                            }
                        }
                        permissionGrantedOrDenineCanDo(requestCode);
                    }
                });
    }

    /**
     * =====================================================================
     **/
    //注册ViewModel与View的契约UI回调事件
    protected void registorUIChangeLiveDataCallBack() {
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
            loadingDialog = new LoadingDialog(activity, initCustomLoadingDialog(), loadingDialogCancelable());
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!activity.isFinishing()) {
                    loadingDialog.show();
                }
            }
        });
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!activity.isFinishing()) {
                        loadingDialog.dismiss();
                    }
                }
            });
        }
    }

    public void hideSoftKeyBoard() {
        View v = getActivity().getCurrentFocus();
        if (v != null) {
            SoftKeyboardUtil.hiddenSoftKeyboard(getActivity(), v.getWindowToken());
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

    /**
     * 软键盘改变
     *
     * @param isPopup
     * @param keyboardHeight
     */
    protected void onKeyboardChange(boolean isPopup, int keyboardHeight) {

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
     * 权限申请成功或者失败都要执行
     */
    protected void permissionGrantedOrDenineCanDo(int requestCode) {

    }

    /**
     * 权限未通过的弹窗，自己实现
     *
     * @param requestCode
     */
    protected void showPermissionDialog(int requestCode) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

    }

    /**
     * 初始化loading弹窗布局，默认为R.layout.loading_dialog
     *
     * @return
     */
    protected int initCustomLoadingDialog() {
        return R.layout.loading_dialog;
    }

    /**
     * 初始化loading弹窗是否可点击外部消失，默认为false
     *
     * @return
     */
    protected boolean loadingDialogCancelable() {
        return false;
    }

    /**
     * 创建ViewModel
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends ViewModel> T createViewModel(Fragment fragment, Class<T> cls) {
        return ViewModelProviders.of(fragment).get(cls);
    }
}

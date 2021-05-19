package com.rx.rxmvvmlib.ui.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rx.rxmvvmlib.viewmodel.base.RxBaseViewModel;
import com.rx.rxmvvmlib.ui.IBaseView;
import com.rx.rxmvvmlib.util.SoftKeyboardUtil;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */
public abstract class RxBaseFragment<V extends ViewDataBinding, VM extends RxBaseViewModel>
        extends RxFragment implements IBaseView {
    protected RxBaseActivity activity;
    protected V binding;
    protected VM viewModel;
    private RxBaseLoadingDialog loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (RxBaseActivity) getActivity();
        initParam();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissLoading();
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
        //页面数据初始化方法
        initData();
        //私有的ViewModel与View的契约事件回调逻辑
        registorUIChangeLiveDataCallBack();
        //页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
        initViewObservable();
        //注册RxBus
        viewModel.registerEventBus();
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
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding() {
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
        binding.setVariable(initVariableId(), viewModel);

        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(viewModel);
        //注入RxLifecycle生命周期
        viewModel.injectLifecycleProvider(this);

        binding.setLifecycleOwner(this);
    }

    /**
     * =====================================================================
     **/
    //注册ViewModel与View的契约UI回调事件
    private void registorUIChangeLiveDataCallBack() {
        //加载对话框显示
        viewModel.getUC().getShowDialogEvent().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                showLoading();
            }
        });
        //加载对话框消失
        viewModel.getUC().getDismissDialogEvent().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(@Nullable Void v) {
                dismissLoading();
            }
        });

        viewModel.getUC().getHideSoftKeyBoardEvent().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                hideSoftKeyBoard();
            }
        });
    }

    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new RxBaseLoadingDialog(activity,initLoadingLayoutId(),loadingCancelable());
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
        if (getActivity() != null) {
            View v = getActivity().getCurrentFocus();
            if (v != null) {
                SoftKeyboardUtil.hiddenSoftKeyboard(getActivity(), v.getWindowToken());
            }
        }
    }

    protected void loadRootFragment(int containerId, @NonNull Fragment toFragment) {
        getChildFragmentManager().beginTransaction()
                .add(containerId, toFragment).hide(toFragment).commitAllowingStateLoss();
    }

    protected void showFragment(@NonNull Fragment showFragment) {
        getChildFragmentManager().beginTransaction()
                .show(showFragment).commitAllowingStateLoss();
    }

    protected void showFragment(@NonNull Fragment hideFragment, @NonNull Fragment showFragment) {
        getChildFragmentManager().beginTransaction()
                .hide(hideFragment).show(showFragment).commitAllowingStateLoss();
    }

    protected void hideFragment(@NonNull Fragment showFragment) {
        getChildFragmentManager().beginTransaction()
                .hide(showFragment).commitAllowingStateLoss();
    }
}

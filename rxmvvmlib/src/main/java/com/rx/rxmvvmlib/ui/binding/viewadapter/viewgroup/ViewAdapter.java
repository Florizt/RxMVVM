package com.rx.rxmvvmlib.ui.binding.viewadapter.viewgroup;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */
public final class ViewAdapter {

    @BindingAdapter({"itemView", "observableList"})
    public static void addViews(ViewGroup viewGroup, final ItemBinding itemBinding, final ObservableList<IBindingItemViewModel> viewModelList) {
        if (viewModelList != null && !viewModelList.isEmpty()) {
            viewGroup.removeAllViews();
            for (IBindingItemViewModel viewModel : viewModelList) {
                ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        itemBinding.layoutRes(), viewGroup, true);
                binding.setVariable(itemBinding.variableId(), viewModel);
                viewModel.injecDataBinding(binding);
            }
        }
    }
}


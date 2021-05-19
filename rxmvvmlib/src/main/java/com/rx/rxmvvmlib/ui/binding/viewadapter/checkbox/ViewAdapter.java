package com.rx.rxmvvmlib.ui.binding.viewadapter.checkbox;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.rx.rxmvvmlib.ui.binding.command.BindingCommand;

import androidx.databinding.BindingAdapter;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */
public class ViewAdapter {
    /**
     * @param bindingCommand //绑定监听
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"onCheckedChangedCommand"}, requireAll = false)
    public static void setCheckedChanged(final CheckBox checkBox, final BindingCommand<Boolean> bindingCommand) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bindingCommand.execute(b);
            }
        });
    }
}

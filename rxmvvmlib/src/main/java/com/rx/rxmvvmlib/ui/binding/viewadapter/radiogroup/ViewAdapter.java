package com.rx.rxmvvmlib.ui.binding.viewadapter.radiogroup;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.rx.rxmvvmlib.ui.binding.command.BindingCommand;

import androidx.annotation.IdRes;
import androidx.databinding.BindingAdapter;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */
public class ViewAdapter {
    @BindingAdapter(value = {"onCheckedChangedCommand"}, requireAll = false)
    public static void onCheckedChangedCommand(final RadioGroup radioGroup, final BindingCommand<String> bindingCommand) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                bindingCommand.execute(radioButton.getText().toString());
            }
        });
    }
}

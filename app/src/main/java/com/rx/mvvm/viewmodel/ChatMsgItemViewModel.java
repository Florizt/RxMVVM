package com.rx.mvvm.viewmodel;

import android.text.TextUtils;
import android.view.View;

import com.rx.mvvm.entity.IMMessage;
import com.rx.rxmvvmlib.base.MultiItemViewModel;
import com.rx.rxmvvmlib.binding.command.BindingAction;
import com.rx.rxmvvmlib.binding.command.BindingCommand;
import com.rx.rxmvvmlib.binding.command.BindingConsumer;
import com.rx.rxmvvmlib.util.TimeUtil;
import com.rx.rxmvvmlib.util.UIUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

/**
 * Created by wuwei
 * 2019/12/10
 * 佛祖保佑       永无BUG
 */
public class ChatMsgItemViewModel extends MultiItemViewModel<MainViewModel> {
    public ObservableField<IMMessage> entity = new ObservableField<>();
    public ObservableBoolean showTime = new ObservableBoolean(false);
    public View view;

    public ChatMsgItemViewModel(@NonNull MainViewModel viewModel, IMMessage imMessage, boolean showTime) {
        super(viewModel);
        this.entity.set(imMessage);
        this.showTime.set(showTime);
    }

    public static String time(IMMessage imMessage) {
        return TimeUtil.getTimeShowString(imMessage.getTime(), false);
    }

    public int marginTop() {
        if (entity.get() != null && getItemPosition() + 1 < viewModel.messageList.size()
                && viewModel.messageList.get(getItemPosition() + 1).entity.get() != null
                && TextUtils.equals(entity.get().getFromAccount(), viewModel.messageList.get(getItemPosition() + 1).entity.get().getFromAccount())) {
            return showTime.get() ? UIUtils.dip2px(16) : UIUtils.dip2px(4);
        }
        return UIUtils.dip2px(16);
    }

    public int getItemPosition() {
        return viewModel.messageList.indexOf(this);
    }

    public BindingCommand<View> clickView = new BindingCommand(new BindingConsumer<View>() {
        @Override
        public void call(View view) {
            ChatMsgItemViewModel.this.view = view;
        }
    });

    public BindingCommand longClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            viewModel.uc.longClick.postValue(ChatMsgItemViewModel.this);
        }
    });

    public BindingCommand click = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            viewModel.uc.click.postValue(ChatMsgItemViewModel.this);
        }
    });
}

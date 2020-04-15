package com.rx.mvvm.viewmodel;

import android.app.Application;

import com.rx.mvvm.R;
import com.rx.mvvm.entity.IMMessage;
import com.rx.rxmvvmlib.base.BaseViewModel;
import com.rx.rxmvvmlib.base.SingleLiveEvent;
import com.rx.rxmvvmlib.binding.command.BindingAction;
import com.rx.rxmvvmlib.binding.command.BindingCommand;
import com.rx.rxmvvmlib.util.UIUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

/**
 * Created by wuwei
 * 2020/4/14
 * 佛祖保佑       永无BUG
 */
public class MainViewModel extends BaseViewModel {
    public static final String MultiRecycleType_Left = "left";
    public static final String MultiRecycleType_Right = "right";

    public static final int MSG_TYPE_TEXT = 0;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public int marginTop() {
        return UIUtils.getStatusBarHeight();
    }

    public ObservableBoolean isExit = new ObservableBoolean(true);

    //给RecyclerView添加ObservableList
    public ObservableList<ChatMsgItemViewModel> messageList = new ObservableArrayList<>();
    //RecyclerView多布局添加ItemBinding
    public ItemBinding<ChatMsgItemViewModel> itemBinding = ItemBinding.of(new OnItemBind<ChatMsgItemViewModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, ChatMsgItemViewModel item) {
            //通过item的类型, 动态设置Item加载的布局
            String itemType = (String) item.getItemType();
            IMMessage imMessage = item.entity.get();
            if (imMessage == null) {
                return;
            }
            if (MultiRecycleType_Left.equals(itemType)) {
                //设置左布局
                if (imMessage.getMsgType() == MSG_TYPE_TEXT) {
                    itemBinding.set(com.rx.mvvm.BR.viewModel, R.layout.nim_item_text_left);
                }
            } else if (MultiRecycleType_Right.equals(itemType)) {
                //设置右布局
                if (imMessage.getMsgType() == MSG_TYPE_TEXT) {
                    itemBinding.set(com.rx.mvvm.BR.viewModel, R.layout.nim_item_text_right);
                }
            }
        }
    });

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        public SingleLiveEvent<Void> back = new SingleLiveEvent<>();
        public SingleLiveEvent<Void> scrollToBottom = new SingleLiveEvent<>();
        public SingleLiveEvent<ChatMsgItemViewModel> click = new SingleLiveEvent<>();
        public SingleLiveEvent<ChatMsgItemViewModel> longClick = new SingleLiveEvent<>();
    }

    public BindingCommand setExit = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isExit.set(!isExit.get());
        }
    });
}

package com.rx.rxmvvmlib.ui.binding.viewadapter.swiperefresh;


import com.rx.rxmvvmlib.ui.binding.command.BindingCommand;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */
public class ViewAdapter {
    //下拉刷新命令
    @BindingAdapter({"onRefreshCommand"})
    public static void onRefreshCommand(SwipeRefreshLayout swipeRefreshLayout, final BindingCommand onRefreshCommand) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onRefreshCommand != null) {
                    onRefreshCommand.execute();
                }
            }
        });
    }

    //是否刷新中
    @BindingAdapter({"refreshing"})
    public static void setRefreshing(SwipeRefreshLayout swipeRefreshLayout, boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

}

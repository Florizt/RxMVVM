package com.rx.rxmvvmlib.binding.viewadapter.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.rx.rxmvvmlib.binding.command.BindingCommand;

import java.util.concurrent.TimeUnit;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.functions.Consumer;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */

public class ViewAdapter {
    //防重复点击间隔(秒)
    public static final int CLICK_INTERVAL = 1;

    /**
     * requireAll 是意思是是否需要绑定全部参数, false为否
     * View的onClick事件绑定
     * onClickCommand 绑定的命令,
     * isThrottleFirst 是否开启防止过快点击
     */
    @BindingAdapter(value = {"onClickCommand", "isThrottleFirst"}, requireAll = false)
    public static void onClickCommand(View view, final BindingCommand clickCommand, final boolean isThrottleFirst) {
        if (isThrottleFirst) {
            RxView.clicks(view)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object object) throws Exception {
                            if (clickCommand != null) {
                                clickCommand.execute();
                            }
                        }
                    });
        } else {
            RxView.clicks(view)
                    .throttleFirst(CLICK_INTERVAL, TimeUnit.SECONDS)//1秒钟内只允许点击1次
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object object) throws Exception {
                            if (clickCommand != null) {
                                clickCommand.execute();
                            }
                        }
                    });
        }
    }

    @BindingAdapter(value = {"onScalTouchCommand"}, requireAll = false)
    public static void onScalTouchCommand(final View view, final BindingCommand clickCommand) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.setPivotX(view.getWidth() / 2);
                        view.setPivotY(view.getHeight() / 2);
                        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1, 0.9f);
                        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1, 0.9f);
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(scaleX, scaleY);
                        animatorSet.setDuration(100);
                        animatorSet.start();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        view.setPivotX(view.getWidth() / 2);
                        view.setPivotY(view.getHeight() / 2);
                        ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(view, "scaleX", 0.9f, 1.03f);
                        ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(view, "scaleY", 0.9f, 1.03f);
                        AnimatorSet animatorSet2 = new AnimatorSet();
                        animatorSet2.playTogether(scaleX2, scaleY2);
                        animatorSet2.setDuration(100);
                        animatorSet2.start();
                        animatorSet2.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                ObjectAnimator scaleX3 = ObjectAnimator.ofFloat(view, "scaleX", 1.03f, 1);
                                ObjectAnimator scaleY3 = ObjectAnimator.ofFloat(view, "scaleY", 1.03f, 1);
                                AnimatorSet animatorSet3 = new AnimatorSet();
                                animatorSet3.playTogether(scaleX3, scaleY3);
                                animatorSet3.setDuration(100);
                                animatorSet3.start();
                                animatorSet3.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        if (clickCommand != null) {
                                            clickCommand.execute();
                                        }
                                    }
                                });
                            }
                        });
                        break;
                }
                return true;
            }
        });
    }

    /**
     * view的onLongClick事件绑定
     */
    @BindingAdapter(value = {"onLongClickCommand"}, requireAll = false)
    public static void onLongClickCommand(View view, final BindingCommand clickCommand) {
        RxView.longClicks(view)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        if (clickCommand != null) {
                            clickCommand.execute();
                        }
                    }
                });
    }

    /**
     * 回调控件本身
     *
     * @param currentView
     * @param bindingCommand
     */
    @BindingAdapter(value = {"currentView"}, requireAll = false)
    public static void replyCurrentView(View currentView, BindingCommand bindingCommand) {
        if (bindingCommand != null) {
            bindingCommand.execute(currentView);
        }
    }

    /**
     * view是否需要获取焦点
     */
    @BindingAdapter({"requestFocus"})
    public static void requestFocusCommand(View view, final Boolean needRequestFocus) {
        if (needRequestFocus) {
            view.setFocusableInTouchMode(true);
            view.requestFocus();
        } else {
            view.clearFocus();
        }
    }

    /**
     * view的焦点发生变化的事件绑定
     */
    @BindingAdapter({"onFocusChangeCommand"})
    public static void onFocusChangeCommand(View view, final BindingCommand<Boolean> onFocusChangeCommand) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (onFocusChangeCommand != null) {
                    onFocusChangeCommand.execute(hasFocus);
                }
            }
        });
    }

    /**
     * view的显示隐藏
     */
    @BindingAdapter(value = {"isVisible"}, requireAll = false)
    public static void isVisible(View view, final Boolean visibility) {
        if (visibility) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("layout_width")
    public static void setLayoutWidth(View view, float width) {
        if (width == 0) {
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) width;
        view.setLayoutParams(params);
    }

    @BindingAdapter("layout_height")
    public static void setLayoutHeight(View view, float height) {
        if (height == 0) {
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int) height;
        view.setLayoutParams(params);
    }

    @BindingAdapter("margin_top")
    public static void setLayoutMarginTop(View view, int marginTop) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if(params instanceof LinearLayout.LayoutParams){
            ((LinearLayout.LayoutParams) params).topMargin = marginTop;
        }else if(params instanceof RelativeLayout.LayoutParams){
            ((RelativeLayout.LayoutParams) params).topMargin = marginTop;
        }else if(params instanceof FrameLayout.LayoutParams){
            ((FrameLayout.LayoutParams) params).topMargin = marginTop;
        }else if(params instanceof RecyclerView.LayoutParams){
            ((RecyclerView.LayoutParams) params).topMargin = marginTop;
        }
        view.setLayoutParams(params);
    }

    @BindingAdapter("margin_bottom")
    public static void setLayoutMarginBottom(View view, int marginBottom) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if(params instanceof LinearLayout.LayoutParams){
            ((LinearLayout.LayoutParams) params).bottomMargin = marginBottom;
        }else if(params instanceof RelativeLayout.LayoutParams){
            ((RelativeLayout.LayoutParams) params).bottomMargin = marginBottom;
        }else if(params instanceof FrameLayout.LayoutParams){
            ((FrameLayout.LayoutParams) params).bottomMargin = marginBottom;
        }else if(params instanceof RecyclerView.LayoutParams){
            ((RecyclerView.LayoutParams) params).bottomMargin = marginBottom;
        }
        view.setLayoutParams(params);
    }

    //    @BindingAdapter({"onTouchCommand"})
    //    public static void onTouchCommand(View view, final ResponseCommand<MotionEvent, Boolean> onTouchCommand) {
    //        view.setOnTouchListener(new View.OnTouchListener() {
    //            @Override
    //            public boolean onTouch(View v, MotionEvent event) {
    //                if (onTouchCommand != null) {
    //                    return onTouchCommand.execute(event);
    //                }
    //                return false;
    //            }
    //        });
    //    }
}

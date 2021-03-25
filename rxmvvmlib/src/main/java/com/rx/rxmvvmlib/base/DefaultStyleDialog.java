package com.rx.rxmvvmlib.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.rx.rxmvvmlib.R;
import com.rx.rxmvvmlib.binding.command.BindingAction;
import com.rx.rxmvvmlib.binding.command.BindingCommand;
import com.rx.rxmvvmlib.databinding.DialogDefaultBinding;
import com.rx.rxmvvmlib.util.UIUtils;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

/**
 * Created by wuwei
 * 2020/5/20
 * 佛祖保佑       永无BUG
 */
public class DefaultStyleDialog extends Dialog {
    private DialogDefaultBinding binding;
    private String title;
    private String body;
    private String negativeText;
    private String positiveText;
    private DialogListener listener;
    private boolean cancelable = true;
    private boolean canceledOnTouchOutside = true;
    private int negativeTextColor = UIUtils.getColor(R.color.rx_555555);
    private int negativeTextBackground = UIUtils.getColor(R.color.rx_ffffff);
    private int positiveTextColor = UIUtils.getColor(R.color.rx_ff8008);
    private int positiveTextBackground = UIUtils.getColor(R.color.rx_ffffff);

    public DefaultStyleDialog(@NonNull Context context) {
        super(context, R.style.Theme_Light_Dialog);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.getDecorView().setBackgroundColor(UIUtils.getColor(R.color.rx_transparent));
        window.getDecorView().setPadding(0, 0, 0, 0);
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.dialog_default, null, false);
        setContentView(binding.getRoot());

        binding.setDialog(this);

        setCancelable(cancelable);
        setCanceledOnTouchOutside(canceledOnTouchOutside);

        binding.title.setText(title);
        binding.body.setText(body);
        binding.negativeText.setText(negativeText);
        binding.positiveText.setText(positiveText);

        if (TextUtils.isEmpty(title)) {
            binding.title.setVisibility(View.GONE);
            binding.body.setGravity(Gravity.CENTER);
        } else {
            binding.title.setVisibility(View.VISIBLE);
            binding.body.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        if (TextUtils.isEmpty(negativeText)) {
            binding.negativeText.setVisibility(View.GONE);
            binding.line.setVisibility(View.GONE);
        } else {
            binding.negativeText.setVisibility(View.VISIBLE);
            binding.line.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(positiveText)) {
            binding.positiveText.setVisibility(View.GONE);
            binding.line.setVisibility(View.GONE);
        } else {
            binding.positiveText.setVisibility(View.VISIBLE);
            binding.line.setVisibility(View.VISIBLE);
        }

        binding.negativeText.setTextColor(negativeTextColor);
        binding.negativeText.setBackgroundColor(negativeTextBackground);
        binding.positiveText.setTextColor(positiveTextColor);
        binding.positiveText.setBackgroundColor(positiveTextBackground);

        binding.getRoot().post(new Runnable() {
            @Override
            public void run() {
                Window window = getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = binding.getRoot().getHeight();
                window.setAttributes(lp);
            }
        });
    }

    public BindingCommand negative = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (listener != null) {
                listener.negative(DefaultStyleDialog.this);
            }
        }
    });

    public BindingCommand positive = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (listener != null) {
                listener.positive(DefaultStyleDialog.this);
            }
        }
    });

    public DefaultStyleDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public DefaultStyleDialog setBody(String body) {
        this.body = body;
        return this;
    }

    public DefaultStyleDialog setNegativeText(String negativeText) {
        this.negativeText = negativeText;
        return this;
    }

    public DefaultStyleDialog setPositiveText(String positiveText) {
        this.positiveText = positiveText;
        return this;
    }

    public DefaultStyleDialog setIsCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    public DefaultStyleDialog setIsCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public DefaultStyleDialog setOnDialogListener(DialogListener listener) {
        this.listener = listener;
        return this;
    }

    public DefaultStyleDialog setNegativeTextColor(int negativeTextColor) {
        this.negativeTextColor = negativeTextColor;
        return this;
    }

    public DefaultStyleDialog setNegativeTextBackground(int negativeTextBackground) {
        this.negativeTextBackground = negativeTextBackground;
        return this;
    }

    public DefaultStyleDialog setPositiveTextColor(int positiveTextColor) {
        this.positiveTextColor = positiveTextColor;
        return this;
    }

    public DefaultStyleDialog setPositiveTextBackground(int positiveTextBackground) {
        this.positiveTextBackground = positiveTextBackground;
        return this;
    }

    public interface DialogListener {
        void negative(Dialog dialog);

        void positive(Dialog dialog);
    }
}

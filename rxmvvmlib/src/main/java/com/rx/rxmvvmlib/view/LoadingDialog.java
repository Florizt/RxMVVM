package com.rx.rxmvvmlib.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.rx.rxmvvmlib.R;


public class LoadingDialog extends Dialog {
    public Context context;
    private int layoutId;

    public LoadingDialog(Context context) {
        this(context, R.layout.loading_dialog, false);
    }

    public LoadingDialog(Context context, int layoutId, boolean cancelable) {
        super(context, R.style.loading_dialog);
        this.context = context;
        this.layoutId = layoutId;
        setCancelable(cancelable);
        setCanceledOnTouchOutside(cancelable);
        Window window = getWindow();
        window.setWindowAnimations(R.style.LoadingDialogWindowStyle);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
    }
}
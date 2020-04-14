package com.rx.rxmvvmlib.view;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.rx.rxmvvmlib.R;


public class LoadingDialog extends Dialog {
    public Context context;

    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog);
        this.context = context;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setWindowAnimations(R.style.LoadingDialogWindowStyle);
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laoding_dialog);
    }
}
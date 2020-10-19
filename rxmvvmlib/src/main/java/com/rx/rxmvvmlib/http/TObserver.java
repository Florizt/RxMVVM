package com.rx.rxmvvmlib.http;

import com.dgrlucky.log.LogX;
import com.google.gson.JsonSyntaxException;
import com.rx.rxmvvmlib.BuildConfig;
import com.rx.rxmvvmlib.R;
import com.rx.rxmvvmlib.util.UIUtils;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


/**
 * Created by wuwei
 * 2018/1/12
 * 佛祖保佑       永无BUG
 */

public abstract class TObserver<T> implements Observer<T> {
    public TObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
        onRequestStart();
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T t) {
        onRequestEnd();
        if (t != null) {
            onSuccees(t);
        } else {
            onFailure("ret 不是0");
        }
    }

    @Override
    public void onError(Throwable e) {
        onRequestEnd();
        if (e instanceof SocketTimeoutException) {
            if (BuildConfig.DEBUG) {
                LogX.e("请求超时，请检查您的网络");
            }
            onFailure(UIUtils.getString(R.string.http_timeout_exception));
        } else if (e instanceof JsonSyntaxException) {
            if (BuildConfig.DEBUG) {
                LogX.e("数据解析异常");
            }
            onFailure(UIUtils.getString(R.string.json_exception));
        } else if (e instanceof HttpException) {
            if (BuildConfig.DEBUG) {
                LogX.e("服务器异常");
            }
            onFailure(UIUtils.getString(R.string.http_exception));
        } else if (e instanceof ResultException) {
            ResultException resultException = (ResultException) e;
            if (BuildConfig.DEBUG) {
                LogX.e(resultException.getErrMsg());
            }
            onFailure(resultException.getErrMsg());
        }
    }

    /**
     * 请求开始
     */
    protected abstract void onRequestStart();

    /**
     * 请求结束
     */
    protected abstract void onRequestEnd();

    /**
     * 返回成功
     *
     * @param t
     * @throws Exception
     */
    protected abstract void onSuccees(T t);

    /**
     * 返回失败
     *
     * @param
     * @throws Exception
     */
    protected abstract void onFailure(String message);
}

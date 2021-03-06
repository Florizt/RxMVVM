package com.rx.rxmvvmlib.repository.datasource.remote.retrofit;

import com.google.gson.JsonSyntaxException;
import com.rx.rxmvvmlib.R;
import com.rx.rxmvvmlib.RMEngine;
import com.rx.rxmvvmlib.util.LogUtil;
import com.rx.rxmvvmlib.util.UIUtil;

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
            onFailure("-1", "data is null");
        }
    }

    @Override
    public void onError(Throwable e) {
        onRequestEnd();
        if (e instanceof SocketTimeoutException) {
            LogUtil.e(UIUtil.getString(R.string.http_timeout_exception));
            onFailure("-2", UIUtil.getString(R.string.http_timeout_exception));
        } else if (e instanceof JsonSyntaxException) {
            LogUtil.e(UIUtil.getString(R.string.json_exception));
            onFailure("-3", UIUtil.getString(R.string.json_exception));
        } else if (e instanceof HttpException) {
            LogUtil.e(UIUtil.getString(R.string.http_exception));
            onFailure("-4", UIUtil.getString(R.string.http_exception));
        } else if (e instanceof ResultException) {
            try {
                ResultException resultException = (ResultException) e;
                LogUtil.e(resultException.getErrMsg());
                if (RMEngine.getConfig().getCustomHttpCodeFilter() != null) {
                    RMEngine.getConfig().getCustomHttpCodeFilter().onFilter(this,
                            resultException.getErrCode(), resultException.getErrMsg());
                } else {
                    onFailure(resultException.getErrCode(), resultException.getErrMsg());
                }
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 请求开始
     */
    public abstract void onRequestStart();

    /**
     * 请求结束
     */
    public abstract void onRequestEnd();

    /**
     * 返回成功
     *
     * @param t
     * @throws Exception
     */
    public abstract void onSuccees(T t);

    /**
     * 返回失败
     *
     * @param
     * @param code
     * @throws Exception
     */
    public abstract void onFailure(String code, String message);
}

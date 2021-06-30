package com.rx.rxmvvmlib.repository.datasource.locate;

import android.content.Context;

import org.greenrobot.greendao.AbstractDao;

/**
 * Created by wuwei
 * 2021/5/24
 * 佛祖保佑       永无BUG
 */
public class LocalFactory {

    public static <S> S create(final Class<S> service, Context context, AbstractDao dao) {
        return createEncrypt(service, context, null, dao);
    }

    public static <S> S createEncrypt(final Class<S> service, Context context, String psw, AbstractDao dao) {
        return new Localfit.Builder()
                .context(context)
                .encryptPsw(psw)
                .dao(dao)
                .build()
                .create(service);
    }
}

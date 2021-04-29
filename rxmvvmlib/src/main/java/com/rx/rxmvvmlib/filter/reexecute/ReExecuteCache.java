package com.rx.rxmvvmlib.filter.reexecute;

import android.util.SparseArray;

/**
 * Created by wuwei
 * 2021/4/28
 * 佛祖保佑       永无BUG
 */
public class ReExecuteCache {
    public static SparseArray<ReExecuteEntity> array = new SparseArray<>();
    public static final int KEY_REEXECUTE = 0;

    public static void addCache(ReExecuteEntity entity) {
        array.put(KEY_REEXECUTE, entity);
    }

    public static ReExecuteEntity getCache() {
        return array.get(KEY_REEXECUTE);
    }

    public static void clearCache() {
        array.delete(KEY_REEXECUTE);
    }
}

package com.rx.mvvm.repository.greendao;

/**
 * Created by wuwei
 * 2021/5/25
 * 佛祖保佑       永无BUG
 */

import android.content.Context;

import org.greenrobot.greendao.AbstractDao;


/**
 * 创建数据库、创建数据库表、包含增删改查的操作
 */
public class DaoManager {
    private volatile static DaoManager manager = new DaoManager();
    private DaoMaster daoMaster;
    private SqliteOpenHelper helper;
    private DaoSession daoSession;

    /**
     * 单例模式获得操作数据库对象
     */
    public static DaoManager getInstance() {
        return manager;
    }

    private DaoManager() {

    }

    public void init(Context context, String dbName, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        getDaoMaster(context.getApplicationContext(), dbName, daoClasses);
    }

    /**
     * 判断是否存在数据库，如果没有则创建
     */
    private DaoMaster getDaoMaster(Context context, String dbName, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        if (daoMaster == null) {
            helper = new SqliteOpenHelper(context, dbName, null, daoClasses);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 完成对数据库的添加、删除、修改、查询操作，仅仅是一个接口
     */
    public DaoSession getDaoSession() {
        if (daoMaster == null) {
            throw new IllegalArgumentException("please must init daoMaster");
        }
        if (daoSession == null) {
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    /**
     * 关闭所有的操作，数据库开启后，使用完毕要关闭
     */
    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }

    private void closeHelper() {
        if (helper != null) {
            helper.close();
            helper = null;
        }
    }

    private void closeDaoSession() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
    }
}
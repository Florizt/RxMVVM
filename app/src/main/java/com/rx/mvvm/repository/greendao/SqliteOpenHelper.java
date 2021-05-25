package com.rx.mvvm.repository.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;

/**
 * Created by wuwei
 * 2021/5/25
 * 佛祖保佑       永无BUG
 */
public class SqliteOpenHelper<T> extends DaoMaster.OpenHelper {

    private Class<? extends AbstractDao<?, ?>>[] daoClasses;

    public SqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                            Class<? extends AbstractDao<?, ?>>... daoClasses) {
        super(context, name, factory);
        this.daoClasses = daoClasses;
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, daoClasses);
    }
}

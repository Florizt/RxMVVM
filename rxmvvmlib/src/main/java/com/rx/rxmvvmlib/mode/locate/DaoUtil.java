package com.rx.rxmvvmlib.mode.locate;

import org.greenrobot.greendao.AbstractDao;

import java.util.List;

/**
 * Created by wuwei
 * 2021/5/25
 * 佛祖保佑       永无BUG
 */
public class DaoUtil {

    private AbstractDao dao;

    public DaoUtil(AbstractDao dao) {
        this.dao = dao;
    }

    /**
     * 插入记录，如果表未创建，先创建表
     */
    public <T extends Object> boolean insert(T pEntity) {
        return dao.insert(pEntity) != -1;
    }

    /**
     * 插入记录，如果表未创建，先创建表
     */
    public <T extends Object> boolean insertOrReplace(T pEntity) {
        return dao.insertOrReplace(pEntity) != -1;
    }

    /**
     * 修改一条数据
     */
    public <T extends Object> boolean update(T entity) {
        try {
            dao.update(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除单条记录
     */
    public <T extends Object> boolean deleteByKey(long id) {
        try {
            //按照id删除
            dao.deleteByKey(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除单条记录
     */
    public <T extends Object> boolean delete(T entity) {
        try {
            //按照对象删除
            dao.delete(entity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除所有记录
     */
    public boolean deleteAll() {
        try {
            dao.deleteAll();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查询所有记录
     */
    public List queryAll() {
        return dao.loadAll();
    }

    /**
     * 根据主键id查询记录
     */
    public <T extends Object> T queryById(long id) {
        return (T) dao.load(id);
    }
}
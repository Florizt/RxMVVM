package com.rx.mvvm.repository.entity;

import com.rx.rxmvvmlib.repository.datasource.BaseEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wuwei
 * 2021/5/25
 * 佛祖保佑       永无BUG
 */
@Entity(nameInDb = "user_table")
public class User extends BaseEntity {

    @Id(autoincrement = true)
    private Long id;

    @Unique
    private String userId;

    @Property
    private String userName;

    @Property
    private int age;

    @Generated(hash = 1975221261)
    public User(Long id, String userId, String userName, int age) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.age = age;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}

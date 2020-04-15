package com.rx.mvvm.entity;

import com.rx.rxmvvmlib.entity.BaseEntity;

/**
 * Created by wuwei
 * 2020/4/15
 * 佛祖保佑       永无BUG
 */
public class IMMessage extends BaseEntity {
    private int msgType;
    private long time;
    private String headImg;
    private String fromAccount;
    private String toAccount;
    private String sessionId;
    private String content;

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
}

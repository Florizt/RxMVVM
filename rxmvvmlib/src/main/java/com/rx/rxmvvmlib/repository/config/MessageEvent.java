package com.rx.rxmvvmlib.repository.config;

import java.util.Map;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */
public class MessageEvent {
    public int type;
    public Object src;
    public Map<String, Object> extra;

    public MessageEvent() {
    }

    public MessageEvent(int type) {
        this.type = type;
    }

    public MessageEvent(int type, Object src) {
        this.type = type;
        this.src = src;
    }

    public MessageEvent(int type, Object src, Map<String, Object> extra) {
        this.type = type;
        this.src = src;
        this.extra = extra;
    }
}

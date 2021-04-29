package com.rx.rxmvvmlib.config;

import com.rx.rxmvvmlib.RxMVVMInit;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */
public class FileConfig {
    public static final String VIDEO = "/" + RxMVVMInit.getConfig().getFloderName() + "/video/";
    public static final String AUDIO = "/" + RxMVVMInit.getConfig().getFloderName() + "/audio/";
    public static final String IMAGE = "/" + RxMVVMInit.getConfig().getFloderName() + "/image/";
    public static final String DOWNLOAD = "/" + RxMVVMInit.getConfig().getFloderName() + "/download/";
    public static final String DOCUMENT = "/" + RxMVVMInit.getConfig().getFloderName() + "/document/";
}

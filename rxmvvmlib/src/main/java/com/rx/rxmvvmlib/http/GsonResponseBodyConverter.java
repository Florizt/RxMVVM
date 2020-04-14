package com.rx.rxmvvmlib.http;

import com.google.gson.Gson;
import com.rx.rxmvvmlib.entity.http.HttpErrResult;
import com.rx.rxmvvmlib.entity.http.HttpResult;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by wuwei
 * 2018/1/18
 * 佛祖保佑       永无BUG
 */

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;


    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        String response = value.string();
        //先将返回的json数据解析到Response中，如果code==0，则解析到我们的实体基类中，否则抛异常
        HttpResult httpResult = gson.fromJson(response, HttpResult.class);
        if (httpResult.getCode() == 0) {
            //0的时候就直接解析，不可能出现解析异常。因为我们实体基类中传入的泛型，就是数据成功时候的格式

            return gson.fromJson(response, type);

        } else {
            HttpErrResult errorResponse = gson.fromJson(response, HttpErrResult.class);
            //抛一个自定义ResultException 传入失败时候的状态码，和信息
            throw new ResultException(errorResponse.getErr(), errorResponse.getCode());
        }
    }
}

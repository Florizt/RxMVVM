package com.rx.rxmvvmlib.mode.locate;

import com.rx.rxmvvmlib.util.SPUtil;
import com.rx.rxmvvmlib.util.UIUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by wuwei
 * 2021/5/24
 * 佛祖保佑       永无BUG
 */
public class LocalFactory {

    @SuppressWarnings("unchecked")
    public static <S> S create(final Class<S> service) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException("API declarations must be interfaces.");
        }
        if (service.getInterfaces().length > 0) {
            throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
        }
        return (S) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service}, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {
                // If the method is a method from Object then defer to normal invocation.
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, args);
                }

                Annotation[] annotations = method.getAnnotations();
                Object o = null;
                for (Annotation annotation : annotations) {
                    o = parseMethodAnnotation(method, annotation, args);
                }
                return o;
            }
        });
    }

    private static Object parseMethodAnnotation(Method method, Annotation annotation, Object[] args) {
        if (annotation instanceof L_GET) {
            LocalType type = ((L_GET) annotation).type();
            String[] key = ((L_GET) annotation).key();

            if (type == LocalType.SP) {
                if (int.class == method.getReturnType()) {
                    return SPUtil.getInt(UIUtil.getContext(), key[0], "abc");
                } else if (long.class == method.getReturnType()) {
                    return SPUtil.getLong(UIUtil.getContext(), key[0], "abc");
                } else if (float.class == method.getReturnType()) {
                    return SPUtil.getFloat(UIUtil.getContext(), key[0], "abc");
                } else if (boolean.class == method.getReturnType()) {
                    return SPUtil.getBoolean(UIUtil.getContext(), key[0], "abc");
                } else if (String.class == method.getReturnType()) {
                    return SPUtil.getString(UIUtil.getContext(), key[0], "abc");
                } else if (Object.class == method.getReturnType()) {
                    return SPUtil.getBeanFromSp(UIUtil.getContext(), key[0], "abc");
                }
            } else {

            }
        } else if (annotation instanceof L_POST) {
            LocalType type = ((L_POST) annotation).type();
            String[] key = ((L_POST) annotation).key();

            if (key.length != args.length) {
                throw new IllegalArgumentException("annotation SP key must match args");
            }

            Class<?>[] parameterTypes = method.getParameterTypes();

            if (type == LocalType.SP) {
                for (int i = 0; i < args.length; i++) {
                    if (int.class == parameterTypes[i]) {
                        SPUtil.saveInt(UIUtil.getContext(), key[i], ((int) args[i]), "abc");
                    } else if (long.class == parameterTypes[i]) {
                        SPUtil.saveLong(UIUtil.getContext(), key[i], ((long) args[i]), "abc");
                    } else if (float.class == parameterTypes[i]) {
                        SPUtil.saveFloat(UIUtil.getContext(), key[i], ((float) args[i]), "abc");
                    } else if (boolean.class == parameterTypes[i]) {
                        SPUtil.saveBoolean(UIUtil.getContext(), key[i], ((boolean) args[i]), "abc");
                    } else if (String.class == parameterTypes[i]) {
                        SPUtil.saveString(UIUtil.getContext(), key[i], ((String) args[i]), "abc");
                    } else if (Object.class == parameterTypes[i]) {
                        SPUtil.saveBean2Sp(UIUtil.getContext(), key[i], args[i], "abc");
                    }
                }
            } else {

            }
        } else if (annotation instanceof L_PUT) {
            LocalType type = ((L_PUT) annotation).type();
            String[] key = ((L_PUT) annotation).key();

            if (key.length != args.length) {
                throw new IllegalArgumentException("annotation SP key must match args");
            }

            Class<?>[] parameterTypes = method.getParameterTypes();

            if (type == LocalType.SP) {
                for (int i = 0; i < args.length; i++) {
                    if (int.class == parameterTypes[i]) {
                        SPUtil.saveInt(UIUtil.getContext(), key[i], ((int) args[i]), "abc");
                    } else if (long.class == parameterTypes[i]) {
                        SPUtil.saveLong(UIUtil.getContext(), key[i], ((long) args[i]), "abc");
                    } else if (float.class == parameterTypes[i]) {
                        SPUtil.saveFloat(UIUtil.getContext(), key[i], ((float) args[i]), "abc");
                    } else if (boolean.class == parameterTypes[i]) {
                        SPUtil.saveBoolean(UIUtil.getContext(), key[i], ((boolean) args[i]), "abc");
                    } else if (String.class == parameterTypes[i]) {
                        SPUtil.saveString(UIUtil.getContext(), key[i], ((String) args[i]), "abc");
                    } else if (Object.class == parameterTypes[i]) {
                        SPUtil.saveBean2Sp(UIUtil.getContext(), key[i], args[i], "abc");
                    }
                }
            } else {

            }
        } else if (annotation instanceof L_DELETE) {
            LocalType type = ((L_DELETE) annotation).type();
            String[] key = ((L_DELETE) annotation).key();

            if (type == LocalType.SP) {
                for (int i = 0; i < key.length; i++) {
                    SPUtil.remove(UIUtil.getContext(), key[i]);
                }
            } else {

            }
        }
        return null;
    }
}

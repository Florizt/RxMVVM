package com.rx.rxmvvmlib.mode.locate;

import android.content.Context;

import org.greenrobot.greendao.AbstractDao;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by wuwei
 * 2021/5/25
 * 佛祖保佑       永无BUG
 */
public class Localfit {

    private Context context;
    private String psw;
    private AbstractDao dao;

    Localfit(Context context, String psw, AbstractDao dao) {
        this.context = context;
        this.psw = psw;
        this.dao = dao;
    }

    @SuppressWarnings("unchecked")
    public <S> S create(final Class<S> service) {
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
                    o = parseMethodAnnotation(method, annotation, args, context, psw);
                    if (o != null) {
                        break;
                    }
                }
                return o;
            }
        });
    }

    private Object parseMethodAnnotation(Method method, Annotation annotation, Object[] args,
                                         Context context, String psw) {
        if (annotation instanceof L_GET) {
            LocalType type = ((L_GET) annotation).type();
            String[] key = ((L_GET) annotation).key();

            if (type == LocalType.SP) {
                return SPUtil.getDecrypt(context, key[0], psw, method.getReturnType());
            } else {
                if (args != null && args.length == 1 && args[0] instanceof Long) {
                    return new DaoUtil(dao).queryById((Long) args[0]);
                } else {
                    return new DaoUtil(dao).queryAll();
                }
            }
        } else if (annotation instanceof L_POST) {
            LocalType type = ((L_POST) annotation).type();
            String[] key = ((L_POST) annotation).key();

            if (key.length != args.length) {
                throw new IllegalArgumentException("annotation SP key must match args");
            }

            if (type == LocalType.SP) {
                for (int i = 0; i < key.length; i++) {
                    SPUtil.putEncrypt(context, key[i], args[i], psw);
                }
            } else if (type == LocalType.DB) {
                for (int i = 0; i < key.length; i++) {
                    new DaoUtil(dao).insertOrReplace(args[i]);
                }
            }
        } else if (annotation instanceof L_PUT) {
            LocalType type = ((L_PUT) annotation).type();
            String[] key = ((L_PUT) annotation).key();

            if (key.length != args.length) {
                throw new IllegalArgumentException("annotation SP key must match args");
            }

            if (type == LocalType.SP) {
                for (int i = 0; i < key.length; i++) {
                    SPUtil.putEncrypt(context, key[i], args[i], psw);
                }
            } else if (type == LocalType.DB) {
                for (int i = 0; i < key.length; i++) {
                    new DaoUtil(dao).update(args[i]);
                }
            }
        } else if (annotation instanceof L_DELETE) {
            LocalType type = ((L_DELETE) annotation).type();
            String[] key = ((L_DELETE) annotation).key();

            if (type == LocalType.SP) {
                for (int i = 0; i < key.length; i++) {
                    SPUtil.remove(context, key[i]);
                }
            } else if (type == LocalType.DB) {
                for (int i = 0; i < key.length; i++) {
                    if (args[i] instanceof Long) {
                        new DaoUtil(dao).deleteByKey((Long) args[i]);
                    } else {
                        new DaoUtil(dao).delete(args[i]);
                    }
                }
            }
        }
        return null;
    }

    public static final class Builder {
        private Context context;
        private String psw;
        private AbstractDao dao;

        public Builder() {

        }

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder encryptPsw(String psw) {
            this.psw = psw;
            return this;
        }

        public Builder dao(AbstractDao dao) {
            this.dao = dao;
            return this;
        }

        public Localfit build() {
            return new Localfit(context, psw, dao);
        }
    }
}

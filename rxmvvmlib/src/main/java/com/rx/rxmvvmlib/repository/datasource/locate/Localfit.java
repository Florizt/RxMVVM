package com.rx.rxmvvmlib.repository.datasource.locate;

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

    final Context context;
    final String psw;
    private final DaoUtil daoUtil;

    Localfit(Context context, String psw, AbstractDao dao) {
        this.context = context;
        this.psw = psw;
        if (dao == null) {
            throw new IllegalArgumentException("db dao is null");
        }
        daoUtil = new DaoUtil(dao);
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
            return parseGET(method, annotation, args, context, psw);
        } else if (annotation instanceof L_POST) {
            return parsePOST(annotation, args, context, psw);
        } else if (annotation instanceof L_PUT) {
            return parsePUT(annotation, args, context, psw);
        } else if (annotation instanceof L_DELETE) {
            return parseDELETE(annotation, args, context);
        }
        return null;
    }

    private Object parseGET(Method method, Annotation annotation, Object[] args,
                            Context context, String psw) {
        LocalType type = ((L_GET) annotation).type();
        if (type == LocalType.SP) {
            String[] key = ((L_GET) annotation).key();
            if (key.length != 1) {
                throw new IllegalArgumentException("parameter must be one");
            }
            return SPUtil.getDecrypt(context, key[0], psw, method.getReturnType());
        } else if (type == LocalType.DB) {
            if (args != null && args.length == 1 && args[0] instanceof Long) {
                return daoUtil.queryById((Long) args[0]);
            } else {
                return daoUtil.queryAll();
            }
        }
        return null;
    }

    private Object parsePOST(Annotation annotation, Object[] args,
                             Context context, String psw) {
        LocalType type = ((L_POST) annotation).type();
        if (type == LocalType.SP) {
            String[] key = ((L_GET) annotation).key();
            if (key.length != args.length) {
                throw new IllegalArgumentException("annotation SP key must match args");
            }
            for (int i = 0; i < args.length; i++) {
                SPUtil.putEncrypt(context, key[i], args[i], psw);
            }
        } else if (type == LocalType.DB) {
            for (int i = 0; i < args.length; i++) {
                daoUtil.insertOrReplace(args[i]);
            }
        }
        return null;
    }

    private Object parsePUT(Annotation annotation, Object[] args,
                            Context context, String psw) {
        LocalType type = ((L_PUT) annotation).type();
        if (type == LocalType.SP) {
            String[] key = ((L_GET) annotation).key();
            if (key.length != args.length) {
                throw new IllegalArgumentException("annotation SP key must match args");
            }
            for (int i = 0; i < args.length; i++) {
                SPUtil.putEncrypt(context, key[i], args[i], psw);
            }
        } else if (type == LocalType.DB) {
            for (int i = 0; i < args.length; i++) {
                daoUtil.update(args[i]);
            }
        }
        return null;
    }

    private Object parseDELETE(Annotation annotation, Object[] args, Context context) {
        LocalType type = ((L_DELETE) annotation).type();
        if (type == LocalType.SP) {
            String[] key = ((L_GET) annotation).key();
            for (int i = 0; i < args.length; i++) {
                SPUtil.remove(context, key[i]);
            }
        } else if (type == LocalType.DB) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Long) {
                    daoUtil.deleteByKey((Long) args[i]);
                } else {
                    daoUtil.delete(args[i]);
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

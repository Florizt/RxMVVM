package com.rx.rxmvvmlib.repository.datasource.locate;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.rx.rxmvvmlib.util.EncryptUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by wuwei on 2017/12/17.
 */

public class SPUtil {

    private synchronized static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    /**
     * 不加密存储
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     */
    public static void put(Context context, String key, Object value) {
        putEncrypt(context, key, value, null);
    }

    /**
     * 加密存储
     *
     * @param context 上下文
     * @param key     键
     * @param value   值
     * @param psw     加密密码
     */
    public static void putEncrypt(Context context, String key, Object value, String psw) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            String v = null;
            if (value instanceof String) {
                v = ((String) value);
            } else if (value instanceof Long) {
                v = Long.toString((Long) value);
            } else if (value instanceof Boolean) {
                v = Boolean.toString((Boolean) value);
            } else if (value instanceof Float) {
                v = Float.toString((Float) value);
            } else if (value instanceof Integer) {
                v = Integer.toString((Integer) value);
            } else if (value instanceof byte[]) {
                v = new String((byte[]) value);
            } else {
                bos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(bos);
                oos.writeObject(value);
                byte[] bytes = bos.toByteArray();
                v = Base64.encodeToString(bytes, Base64.DEFAULT);
            }
            if (!TextUtils.isEmpty(v) && !TextUtils.isEmpty(psw)) {
                v = EncryptUtil.encrypt3DES((v).getBytes(), psw);
            }
            editor.putString(key, v);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SharedPreferencesCompat.apply(editor);
            try {
                if (bos != null) {
                    bos.close();
                }
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 不解密获取
     *
     * @param context 上下文
     * @param key     键
     * @param convert 转换类型
     * @return 值
     */
    public static Object get(Context context, String key, Class convert) {
        return getDecrypt(context, key, null, convert);
    }

    /**
     * 解密获取
     *
     * @param context 上下文
     * @param key     键
     * @param psw     解密密码
     * @param convert 转换类型
     * @return 值
     */
    public static Object getDecrypt(Context context, String key, String psw, Class convert) {
        String v = getPrefs(context).getString(key, "");
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            if (!TextUtils.isEmpty(v) && !TextUtils.isEmpty(psw)) {
                v = EncryptUtil.decrypt3DES(v, psw);
            }
            if (convert == String.class) {
                return v;
            } else if (convert == Long.class || convert == long.class) {
                return Long.parseLong(v);
            } else if (convert == Boolean.class || convert == boolean.class) {
                return Boolean.parseBoolean(v);
            } else if (convert == Float.class || convert == float.class) {
                return Float.parseFloat(v);
            } else if (convert == Integer.class || convert == int.class) {
                return Integer.parseInt(v);
            } else if (convert == Byte[].class || convert == byte[].class) {
                return v.getBytes();
            } else {
                byte[] bytes = Base64.decode(v, Base64.DEFAULT);
                bis = new ByteArrayInputStream(bytes);
                ois = new ObjectInputStream(bis);
                return ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return v;
    }

    /**
     * 移除某个key值已经对应的值
     */
    public static void remove(Context context, String key) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context context) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }
}

package com.rx.rxmvvmlib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by wuwei on 2017/12/17.
 */

public class SPUtil {

    public synchronized static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void remove(Context context, String key) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.remove(key);
        editor.apply();
    }

    public static void saveString(Context context, String key, String value, String psw) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        if (TextUtils.isEmpty(value)) {
            editor.putString(key, value);
            editor.apply();
        } else {
            try {
                editor.putString(key, EncryptUtil.encrypt3DES(value.getBytes(), psw));
                editor.apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getString(Context context, String key, String psw) {
        String string = getPrefs(context).getString(key, "");
        if (TextUtils.isEmpty(string)) {
            return string;
        } else {
            try {
                return EncryptUtil.decrypt3DES(string, psw);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    public static void saveLong(Context context, String key, long value, String psw) {
        saveString(context, key, Long.toString(value), psw);
    }

    public static long getLong(Context context, String key, String psw) {
        if (TextUtils.equals(getString(context, key, psw), "")) {
            return 0;
        } else {
            return Long.parseLong(getString(context, key, psw));
        }
    }

    public static void saveBoolean(Context context, String key, Boolean value, String psw) {
        saveString(context, key, Boolean.toString(value), psw);
    }

    public static boolean getBoolean(Context context, String key, String psw) {
        if (TextUtils.equals(getString(context, key, psw), "")) {
            return false;
        } else {
            return Boolean.parseBoolean(getString(context, key, psw));
        }
    }

    public static void saveFloat(Context context, String key, float value, String psw) {
        saveString(context, key, Float.toString(value), psw);
    }

    public static float getFloat(Context context, String key, String psw) {
        if (TextUtils.equals(getString(context, key, psw), "")) {
            return 0f;
        } else {
            return Float.parseFloat(getString(context, key, psw));
        }
    }

    public static void saveInt(Context context, String key, int value, String psw) {
        saveString(context, key, Integer.toString(value), psw);
    }

    public static int getInt(Context context, String key, String psw) {
        if (TextUtils.equals(getString(context, key, psw), "")) {
            return 0;
        } else {
            return Integer.parseInt(getString(context, key, psw));
        }
    }

    public static void saveBytes(Context context, String key, byte[] value, String psw) {
        saveString(context, key, new String(value), psw);
    }

    public static byte[] getBytes(Context context, String key, byte[] defValue, String psw) {
        if (TextUtils.equals(getString(context, key, psw), "")) {
            return null;
        } else {
            return getString(context, key, psw).getBytes();
        }
    }

    public static <T> void saveBean2Sp(Context context, String key, T t, String psw) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(t);
            byte[] bytes = bos.toByteArray();
            String ObjStr = Base64.encodeToString(bytes, Base64.DEFAULT);
            saveString(context, key, ObjStr, psw);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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

    public static <T extends Object> T getBeanFromSp(Context context, String key, String psw) {
        String value = getString(context, key, psw);
        if (TextUtils.isEmpty(value)) {
            return null;
        }
        byte[] bytes = Base64.decode(value, Base64.DEFAULT);
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        T obj = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = (T) ois.readObject();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
        return obj;
    }

}

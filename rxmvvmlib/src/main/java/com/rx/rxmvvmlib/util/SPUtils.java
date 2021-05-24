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

public class SPUtils {
    private static SharedPreferences mPrefs = null;

    public synchronized static SharedPreferences getPrefs() {
        if (mPrefs == null) {
            mPrefs = UIUtils.getContext().getSharedPreferences(UIUtils.getContext().getPackageName(), Context.MODE_PRIVATE);
        }
        return mPrefs;
    }

    public static void remove(String key) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.remove(key);
        editor.apply();
    }

    public static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getPrefs().edit();
        if (TextUtils.isEmpty(value)) {
            editor.putString(key, value);
            editor.apply();
        } else {
            try {
                editor.putString(key, EncryptUtil.encrypt3DES(value.getBytes()));
                editor.apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getString(String key) {
        String string = getPrefs().getString(key, "");
        if (TextUtils.isEmpty(string)) {
            return string;
        } else {
            try {
                return EncryptUtil.decrypt3DES(string);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static void saveLong(String key, long value) {
        saveString(key, Long.toString(value));
    }

    public static long getLong(String key) {
        if (TextUtils.equals(getString(key), "")) {
            return 0;
        } else {
            return Long.parseLong(getString(key));
        }
    }

    public static void saveBoolean(String key, Boolean value) {
        saveString(key, Boolean.toString(value));
    }

    public static boolean getBoolean(String key) {
        if (TextUtils.equals(getString(key), "")) {
            return false;
        } else {
            return Boolean.parseBoolean(getString(key));
        }
    }

    public static void saveFloat(String key, float value) {
        saveString(key, Float.toString(value));
    }

    public static float getFloat(String key) {
        if (TextUtils.equals(getString(key), "")) {
            return 0f;
        } else {
            return Float.parseFloat(getString(key));
        }
    }

    public static void saveInt(String key, int value) {
        saveString(key, Integer.toString(value));
    }

    public static int getInt(String key) {
        if (TextUtils.equals(getString(key), "")) {
            return 0;
        } else {
            return Integer.parseInt(getString(key));
        }
    }

    public static void saveBytes(String key, byte[] value) {
        if (value == null) {
            getPrefs().edit().putString(key, null).apply();
        } else {
            getPrefs().edit().putString(key, Base64.encodeToString(value, Base64.DEFAULT)).apply();
        }
    }

    public static byte[] getBytes(String key, byte[] defValue) {
        String result = getPrefs().getString(key, null);
        return result == null ? defValue : Base64.decode(result, Base64.DEFAULT);
    }


    public static <T> void saveBean2Sp(T t, String keyName) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(t);
            byte[] bytes = bos.toByteArray();
            String ObjStr = Base64.encodeToString(bytes, Base64.DEFAULT);
            saveString(keyName, ObjStr);
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

    public static <T extends Object> T getBeanFromSp(String keyNme) {
        String value = getString(keyNme);
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

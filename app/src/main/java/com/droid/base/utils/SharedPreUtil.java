package com.droid.base.utils;

import android.content.SharedPreferences;

import com.droid.base.BaseApplication;

import java.util.Set;

public class SharedPreUtil {

    private static final String SP_NAME = "petsknow_merchant";
    private static final String SP_USER_CONFIG = "petsknow_merchant";

    private static SharedPreferences sp;

    public static void putBoolean(String key, Boolean value) {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(SP_NAME, BaseApplication.getInstance().MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key, Boolean defValue) {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(SP_NAME, BaseApplication.getInstance().MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }


    public static void saveUser(String userStr) {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(SP_USER_CONFIG, BaseApplication.getInstance().MODE_PRIVATE);
        }
        sp.edit().putString("user", userStr).commit();
    }

    public static String getUser() {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(SP_USER_CONFIG, BaseApplication.getInstance().MODE_PRIVATE);
        }
        return sp.getString("user", "");
    }

    /**
     * 缓存字符串
     *
     * @param key
     * @param value
     */
    public static void putString(String key, String value) {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(SP_NAME, BaseApplication.getInstance().MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    /**
     * 获取缓存字符串
     *
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(String key, String defValue) {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(SP_NAME, BaseApplication.getInstance().MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    /**
     * 存储数字标示符
     *
     * @param key
     * @param value
     */
    public static void putInt(String key, int value) {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(SP_NAME, BaseApplication.getInstance().MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).commit();
    }

    /**
     * 获取存储的数字标示符
     *
     * @param key
     * @param defValue
     * @return
     */

    public static int getInt(String key, int defValue) {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(SP_NAME, BaseApplication.getInstance().MODE_PRIVATE);
        }
        return sp.getInt(key, defValue);
    }

    /**
     * 存储一个时间值
     * @param key
     * @param value
     */

    public static void putLong(String key, long value) {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(SP_NAME, BaseApplication.getInstance().MODE_PRIVATE);
        }
        sp.edit().putLong(key, value).commit();
    }

    /**
     * 获取一个时间值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static long getLong(String key, long defValue) {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(SP_NAME, BaseApplication.getInstance().MODE_PRIVATE);
        }
        return sp.getLong(key, defValue);
    }

    /**
     * @param key
     * @param defValue
     * @return
     */
    public static float getFloat(String key, float defValue) {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(SP_NAME, BaseApplication.getInstance().MODE_PRIVATE);
        }
        return sp.getFloat(key, defValue);
    }

    /**
     * @param key
     * @param value
     */

    public static void putFloat(String key, float value) {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(SP_NAME, BaseApplication.getInstance().MODE_PRIVATE);
        }
        sp.edit().putFloat(key, value).commit();
    }

    public static Set<String> getSet(String key, Set<String> defValue) {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(SP_NAME, BaseApplication.getInstance().MODE_PRIVATE);
        }
        return sp.getStringSet(key, defValue);
    }

    public static void putSet(String key, Set<String> value) {
        if (sp == null) {
            sp = BaseApplication.getInstance().getSharedPreferences(SP_NAME, BaseApplication.getInstance().MODE_PRIVATE);
        }
        sp.edit().putStringSet(key, value).commit();
    }
}

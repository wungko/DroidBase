package com.droid.base.utils;

/**
 * Created by wungko on 16/1/14.
 */
public class Config {
    /**
     * 配置log
     */
    private static boolean mIsLogOpen = false;

    /**
     * 配置是否上线
     */
    private static boolean mIsRelease = false;

    /**
     * 配置log 是否打开
     * @param isOpen
     */
    public static void setIsLogOpen(boolean isOpen) {
        mIsLogOpen = isOpen;
    }

    /**
     * 配置项目是否上线
     * @param isRelease
     */
    public static void setIsRelease(boolean isRelease) {
        mIsRelease = isRelease;
    }

    /**
     * 获取log 配置状态
     * @return
     */
    public static boolean isLogOpen() {
        return mIsLogOpen;
    }

    /**
     * 获取release 配置状态
     * @return
     */
    public static boolean isRelease() {
        return mIsRelease;
    }


}

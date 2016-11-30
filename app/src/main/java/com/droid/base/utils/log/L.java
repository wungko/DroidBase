package com.droid.base.utils.log;


import android.util.Log;

import com.droid.base.BaseApplication;

/**
 * LL is a wrapper of {@link android.util.Log}
 * But more pretty, simple and powerful
 */
public final class L {
    private static final String DEFAULT_TAG = "droidBase";

    private static Printer printer = new LoggerPrinter();

    //no instance
    private L() {
    }

    /**
     * It is used to get the settings object in order to change settings
     *
     * @return the settings object
     */
    public static Settings init() {
        return init(DEFAULT_TAG);
    }

    /**
     * It is used to change the tag
     *
     * @param tag is the given string which will be used in LL as TAG
     */
    public static Settings init(String tag) {
        printer = new LoggerPrinter();
        return printer.init(tag);
    }

    public static void clear() {
        printer.clear();
        printer = null;
    }

    public static Printer t(String tag) {
        if (BaseApplication.getInstance().isLogOpen()) {
            return printer.t(tag, printer.getSettings().getMethodCount());
        } else {
            return null;
        }
    }

    public static Printer t(int methodCount) {

        if (BaseApplication.getInstance().isLogOpen()) {
            return printer.t(null, methodCount);
        } else {
            return null;
        }
    }

    public static Printer t(String tag, int methodCount) {
        if (BaseApplication.getInstance().isLogOpen()) {
            return printer.t(tag, methodCount);
        } else {
            return null;
        }
    }

    public static void d(String message, Object... args) {
        if (BaseApplication.getInstance().isLogOpen()) {
            printer.d(message, args);
        }
    }

    public static void e(String message, Object... args) {
        if (BaseApplication.getInstance().isLogOpen()) {
            printer.e(null, message, args);
        }
    }

    public static void e(Throwable throwable, String message, Object... args) {
        if (BaseApplication.getInstance().isLogOpen()) {
            printer.e(throwable, message, args);
        }
    }

    public static void i(String message, Object... args) {
        if (BaseApplication.getInstance().isLogOpen()) {
            printer.i(message, args);
        }
    }

    public static void v(String message, Object... args) {
        if (BaseApplication.getInstance().isLogOpen()) {
            printer.v(message, args);
        }
    }

    public static void w(String message, Object... args) {
        if (BaseApplication.getInstance().isLogOpen()) {
            printer.w(message, args);
        }
    }

    public static void wtf(String message, Object... args) {
        if (BaseApplication.getInstance().isLogOpen()) {
            printer.wtf(message, args);
        }
    }

    /**
     * Formats the json content and print it
     *
     * @param json the json content
     */
    public static void json(String json) {
        if (BaseApplication.getInstance().isLogOpen()) {
            printer.json(json);
        }
    }

    /**
     * Formats the json content and print it
     *
     * @param xml the xml content
     */
    public static void xml(String xml) {
        if (BaseApplication.getInstance().isLogOpen()) {
            printer.xml(xml);
        }
    }

    /**
     * 控制台 普通打印
     * @param msg
     */
    public static void out(String msg) {
        if (BaseApplication.getInstance().isLogOpen()) {
            System.out.println(msg);
        }

    }

    public static void i(String tag, String msg){
        if (BaseApplication.getInstance().isLogOpen()) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg){
        if (BaseApplication.getInstance().isLogOpen()) {
            Log.e(tag, msg);
        }
    }

}

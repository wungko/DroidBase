package com.droid.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.droid.base.utils.Config;
import com.droid.base.utils.log.L;
import com.droid.base.utils.log.LogLevel;

import java.util.UUID;

/**
 * Created by wungko on 16/1/14.
 *
 */
// TODO: 16/1/14  ApplicationErrorReport 错误回报
public abstract class BaseApplication extends Application {
    /**全局上下文**/
    private static BaseApplication mDroidBaseApplication;

    /**main handler**/
    private static Handler mainHandler;
    /**
     * 获取上下文实例
     * @return
     */
    public static BaseApplication getInstance(){
        return mDroidBaseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(this.getExceptionHandler());
        mainHandler = new Handler();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mDroidBaseApplication = BaseApplication.this;
        /**配置log**/
        Config.setIsLogOpen(isLogOpen());
        /**配置release**/
        Config.setIsRelease(isRelease());
        /**初始化log**/
        L.init().logLevel(isLogOpen()? LogLevel.FULL : LogLevel.NONE);
    }

    public static Handler getMainHandler() {
        return mainHandler;
    }

    /**
     * 是否打开log
     * @return
     */
    public abstract boolean isLogOpen();

    /**
     * 线上
     * @return
     */
    public abstract boolean isRelease();

    /**
     * 加载页面
     *
     * @return
     */
    public abstract int getLoadingLayout();

    /**
     * 空布局页面
     *
     * @return
     */
    public abstract int getEmptyLayout();

    /**
     * 业务错误
     * @return
     */
    public abstract int getBErrorLayout();

    /**
     * http 错误页面
     *
     * @return
     */
    public abstract int getHttpErrorLayout();

    protected Thread.UncaughtExceptionHandler getExceptionHandler() {
        return Thread.getDefaultUncaughtExceptionHandler();
    }


    /**
     * 获取AndroidId
     * @return
     */
    public static String getAndroidId(){

        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length()%10+ Build.BRAND.length()%10 +

                Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +

                Build.DISPLAY.length()%10 + Build.HOST.length()%10 +

                Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +

                Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +

                Build.TAGS.length()%10 + Build.TYPE.length()%10 +

                Build.USER.length()%10 ; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }


}

package com.droid.base.utils;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.droid.base.BaseApplication;

/**
 * author:wungko
 * time:16/4/27
 * display:设备帮助类
 */
public class DeviceHelper {

    /**
     * 是否开了飞行模式
     * @return
     */
    public static boolean isAirplaneMode() {
        BaseApplication instance = BaseApplication.getInstance();
        int isAirplaneMode = Settings.System.getInt(instance.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0);
        return isAirplaneMode == 1;
    }

    /**
     * 检测是否有sim 卡
     * @return
     */
    public static boolean isSimMode() {
        BaseApplication instance = BaseApplication.getInstance();
        TelephonyManager mTelephonyManager = (TelephonyManager)instance.getSystemService(Context.TELEPHONY_SERVICE);
        int state = mTelephonyManager.getSimState();
        switch(state) {
            case 0:
            case 1:
                return true;
            default:
                return false;
        }
    }

    /**
     * 检测是否有可以响应的activity
     * Verify that the intent will resolve to an activity
     * @param intent
     * @return
     */
    public static boolean checkResponseIntent(Intent intent) {
        BaseApplication instance = BaseApplication.getInstance();
        return intent.resolveActivity(instance.getPackageManager()) != null;
    }
}
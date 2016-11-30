package com.droid.base.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by wungko on 16/1/18.
 */
public class AppInfoHelper {
    private Context mContext;

    private AppInfoHelper(Context context){
        this.mContext = context;
    }

    /**
     * 初始化工具类
     * @param mContext
     * @return
     */
    public static AppInfoHelper init(Context mContext) {
        return new AppInfoHelper(mContext);
    }

    /**
     * 网络是否连接
     */
    public boolean isNetworkingConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 获取网络连接类型
     * ConnectivityManager.TYPE_WIFI;
     * @return
     */
    public int getNetworkingConnectedType() {
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        if (activeInfo != null && activeInfo.isConnected()) {
            return activeInfo.getType();
        } else {
            return -1;
        }
    }
}

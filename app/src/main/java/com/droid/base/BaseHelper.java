package com.droid.base;

import com.droid.base.utils.AppInfoHelper;

/**
 * Created by wungko on 16/1/18.
 */
public class BaseHelper {
    /**appinfo helper**/
    private AppInfoHelper appInfoHelper;
    public AppInfoHelper getAppInfoHelper() {
        if (appInfoHelper != null) {
            return appInfoHelper;
        } else {
            appInfoHelper = AppInfoHelper.init(BaseApplication.getInstance());
            return appInfoHelper;
        }
    }



}

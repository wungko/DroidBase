package com.droid.base.utils;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.droid.base.activity.BaseActivity;

/**
 * author:wungko
 * time:16/4/27
 * display:dialog 工具类
 */
public class DialogUtil {
    /**
     * 只有一个确定按钮
     * @param msg
     */
    public static void dialogOk(int confirm, int msg){
        BaseActivity droidBaseActivity = ActivityStackHelper.getInstance().currentActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(droidBaseActivity);
        builder.setMessage(msg).setNegativeButton(confirm,null);
        builder.show();
    }

    /**
     * 取消 确定按钮
     * @param msg
     */
    public static void dialogOk(int confirm, DialogInterface.OnClickListener confirmClickListener, int cancel, DialogInterface.OnClickListener cancelClickListener, int msg){
        BaseActivity droidBaseActivity = ActivityStackHelper.getInstance().currentActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(droidBaseActivity);
        builder.setMessage(msg).setNegativeButton(cancel,cancelClickListener);
        builder.setPositiveButton(confirm, confirmClickListener);
        builder.show();
    }

}
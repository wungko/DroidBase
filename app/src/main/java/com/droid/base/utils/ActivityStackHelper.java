package com.droid.base.utils;

import com.droid.base.activity.BaseActivity;
import com.droid.base.utils.log.L;

import java.util.Iterator;
import java.util.Stack;

/**
 * author:wungko
 * time:16/4/21
 * display:activity 堆栈管理
 */
public class ActivityStackHelper {

    private static ActivityStackHelper activityStackHelper = null;

    public static ActivityStackHelper getInstance() {
        if (activityStackHelper == null) {
            activityStackHelper = new ActivityStackHelper();
            return activityStackHelper;
        } else {
            return activityStackHelper;
        }
    }

    private ActivityStackHelper() {
    }

    private Stack<BaseActivity> activityStack = new Stack<>();

    /**
     * activity 入栈
     *
     * @param droidBaseActivity
     */
    public void add(BaseActivity droidBaseActivity) {
        L.out(droidBaseActivity.getClass().getSimpleName() + "-->onCreate()->add to activityStack");
        activityStack.addElement(droidBaseActivity);
        L.out(activityStack());
    }

    private String activityStack() {
        StringBuilder sb = new StringBuilder("activityStack->[");
        for (BaseActivity tmp : activityStack) {
            sb.append(tmp.getClass().getSimpleName()).append(",");
        }
        sb.deleteCharAt(sb.toString().length() - 1);
        sb.append("]");
        return sb.toString();
    }

    /**
     * 栈顶 activity
     *
     * @return
     */
    public BaseActivity currentActivity() {
        return activityStack.lastElement();
    }

    public void removeActivity(BaseActivity droidBaseActivity) {
        L.out(droidBaseActivity.getClass().getSimpleName() + "-->onDestroy()->remove from activityStack");
        activityStack.remove(droidBaseActivity);
        L.out(activityStack());
    }

    /**
     * 清空activity 栈并返回栈低activity
     *
     * @return
     */
    public BaseActivity clearActivity() {
        BaseActivity droidBaseActivity = null;
        droidBaseActivity = activityStack.pop();

        while (!activityStack.isEmpty()) {
            BaseActivity pop = activityStack.pop();
            pop.finish();
        }
        return droidBaseActivity;
    }


    public void clearActivityExcept(Class<?> clazz) {
        for (Iterator<BaseActivity> iter = activityStack.iterator(); iter.hasNext(); ) {
            BaseActivity item = iter.next();
            if (!clazz.isInstance(item)) {
                item.finish();
                iter.remove();
            }
        }
    }

    public boolean hasActivity(Class<?> clazz){
        for (Iterator<BaseActivity> iter = activityStack.iterator(); iter.hasNext(); ) {
            BaseActivity item = iter.next();
            if (clazz.isInstance(item)) {
                return  true;
            }
        }
        return false;
    }
}
package com.droid.base.threadpool;

/**
 * Created by wungko on 16/1/19.
 */
public class BaseThreadPoolManager {
    private static BaseHttpThreadPoolExecutor mBaseHttpThreadPoolExecutor;
    public static BaseHttpThreadPoolExecutor getBaseHttpThreadPoolExecutor(){
        if (mBaseHttpThreadPoolExecutor == null) {
            mBaseHttpThreadPoolExecutor = new BaseHttpThreadPoolExecutor();
        }
        return mBaseHttpThreadPoolExecutor;
    }

    public static void shutDown(){
        if (mBaseHttpThreadPoolExecutor != null && !mBaseHttpThreadPoolExecutor.isShutdown()) {
            mBaseHttpThreadPoolExecutor.shutdownNow();
        }
    }
}

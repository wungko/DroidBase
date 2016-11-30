package com.droid.base.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by wungko on 16/1/19.
 */
public class BaseHttpThreadPoolExecutor extends ThreadPoolExecutor {
    public static final int CORE_POOL_SIZE = 3;
    public static final int MAXIMUM_POOL_SIZE = 5;
    public BaseHttpThreadPoolExecutor() {
        super(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }
}

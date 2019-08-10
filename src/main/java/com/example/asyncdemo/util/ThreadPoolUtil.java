package com.example.asyncdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadPoolUtil {

    private static ExecutorService executorService;

    public static ExecutorService getThreadPool() {
        if (executorService == null) {
            synchronized (ThreadPoolUtil.class) {
                if (executorService == null) {
                    BasicThreadFactory.Builder builder = new BasicThreadFactory.Builder();
                    builder.namingPattern("zrpd-pool-");
                    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(16, 32, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100), builder.build(), new ThreadPoolExecutor.CallerRunsPolicy());
                    threadPoolExecutor.allowCoreThreadTimeOut(true);
                    executorService = threadPoolExecutor;
                }

            }

        }
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) executorService;
        //log.info("当前线程池中 存活线程数：{},总任务数：{}", threadPool.getActiveCount(), threadPool.getTaskCount());
        return executorService;
    }
}

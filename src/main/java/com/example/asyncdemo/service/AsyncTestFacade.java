package com.example.asyncdemo.service;

import com.example.asyncdemo.dto.TestRequest;
import com.example.asyncdemo.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @Description:
 * @Author: jin3pang
 * @Date: 2019/7/18 20:21
 */
@Component("asynctestfacade")
@Slf4j
public class AsyncTestFacade {

    private final List<AsyncHandleInterface> handlerList = new CopyOnWriteArrayList<>();

    //通过抽象类持有该类，通过抽象类注册到本类的handlerList
    public void register(AsyncHandleInterface handler) {
        if (handler != null) {
            handlerList.add(handler);
        }
    }

    public void asyncProcess(final TestRequest testRequest) throws InterruptedException {
        System.out.println("AsyncTestFacade---asyncProcess");
        final AcceptanceExecMonitor execMonitor = new AcceptanceExecMonitor();
        final CountDownLatch countDownLatch = new CountDownLatch(handlerList.size());

        handlerList.forEach(h -> {
            ThreadPoolUtil.getThreadPool().submit(() -> {
                try {
                    h.asyncHandle(testRequest);
                }catch (Exception e) {
                    log.error("{} 异步执行失败", e.getClass().getSimpleName(), e);
                    execMonitor.failed(e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }

            });
        });
        countDownLatch.await();
        // 检查所有子线程是否执行成功，
        execMonitor.check();
    }

    class AcceptanceExecMonitor {
        private volatile boolean success = true;
        private StringBuilder sb = new StringBuilder();

        public synchronized void failed(String error) {
            success = success & false;
            if (!StringUtils.isEmpty(error)) {
                sb.append("[").append(error).append("]");
            }
        }

        public void check() {
            if (!success) {
                throw new RuntimeException(sb.toString());
            }
        }
    }





}

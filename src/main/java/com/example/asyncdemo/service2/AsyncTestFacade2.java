package com.example.asyncdemo.service2;

import com.example.asyncdemo.dto.TestRequest;
import com.example.asyncdemo.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * @Description:
 * @Author: jin3pang
 * @Date: 2019/7/18 20:21
 */
@Component("asynctestfacade2")
@Slf4j
public class AsyncTestFacade2 implements ApplicationContextAware ,InitializingBean {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AsyncTestFacade2.applicationContext = applicationContext;
    }

    //持有spring中容器中接口的所有实现类。通过spring获取
    private final List<AsyncHandleInterface2> handlerList2 = new CopyOnWriteArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        //springBeanFactoryUtils为从容器中获取某个类型的所有bean
        //BeanFactoryUtils.beansOfTypeIncludingAncestors

        Map<String, AsyncHandleInterface2> matchingBeans = BeanFactoryUtils
                .beansOfTypeIncludingAncestors(applicationContext, AsyncHandleInterface2.class, true, false);
        matchingBeans.forEach((key,val)->{
            handlerList2.add(val);
        });
    }

    public void asyncProcess(final TestRequest testRequest) throws InterruptedException {
        System.out.println("AsyncTestFacade2---asyncProcess");
        final AcceptanceExecMonitor execMonitor = new AcceptanceExecMonitor();
        final CountDownLatch countDownLatch = new CountDownLatch(handlerList2.size());

        handlerList2.forEach(h -> {
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

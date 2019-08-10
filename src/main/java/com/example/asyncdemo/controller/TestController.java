package com.example.asyncdemo.controller;

import com.example.asyncdemo.dto.TestRequest;
import com.example.asyncdemo.service.AsyncTestFacade;
import com.example.asyncdemo.service2.AsyncTestFacade2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: jin3pang
 * @Date: 2019/7/18 19:57
 */
@RestController
@RequestMapping
public class TestController {


    @Resource(name="asynctestfacade")
    private AsyncTestFacade asyncTestFacade;

    @Resource(name="asynctestfacade2")
    private AsyncTestFacade2 asyncTestFacade2;

    @RequestMapping("/test")
    public String test() throws Exception{

        TestRequest request = new TestRequest();
        asyncTestFacade.asyncProcess(request);
        return "success-test";
    }

    @RequestMapping("/test2")
    public String test2() throws Exception{
        TestRequest request = new TestRequest();
        asyncTestFacade2.asyncProcess(request);
        return "success-test2";
    }

    private static Lock lock1 = new ReentrantLock();
    private static Lock lock2 = new ReentrantLock();

    @RequestMapping("/testdeadlock")
    public String testdeadlock() throws Exception{
        new Thread(new JstackTest.DeadLockTest(true)).start();
        new Thread(new JstackTest.DeadLockTest(false)).start();
        return "starting.......";
    }

    static class DeadLockTest implements Runnable {
        private boolean flag;

        public DeadLockTest(boolean flag) {
            this.flag = flag;
        }

        @Override
        public void run() {
            while (true) {
                if (flag) {
                    try {
                        lock1.lock();
                        System.out.println("lock1 :" + Thread.currentThread().getName());
                        lock2.lock();
                        System.out.println("lock2 :" + Thread.currentThread().getName());
                    } finally {
                        lock1.unlock();
                        lock2.unlock();
                    }
                } else {
                    try {
                        lock2.lock();
                        System.out.println("lock2 :" + Thread.currentThread().getName());
                        lock1.lock();
                        System.out.println("lock1 :" + Thread.currentThread().getName());
                    } finally {
                        lock2.unlock();
                        lock1.unlock();
                    }
                }
            }
        }
    }
}

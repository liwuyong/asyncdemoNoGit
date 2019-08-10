package com.example.asyncdemo.controller;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: jin3pang
 * @Date: 2019/8/10 14:27
 */
public class JstackTest {
    private static Lock lock1 = new ReentrantLock();
    private static Lock lock2 = new ReentrantLock();
    public static void main(String[] args) {
        new Thread(new DeadLockTest(true)).start();
        new Thread(new DeadLockTest(false)).start();
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

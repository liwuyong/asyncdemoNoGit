package com.example.asyncdemo.service2.impl;

import com.example.asyncdemo.dto.TestRequest;
import com.example.asyncdemo.service2.AsyncHandleInterface2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: jin3pang
 * @Date: 2019/7/18 20:23
 */
@Service("wifihandler2")
@Slf4j
public class WiFiHandler2 implements AsyncHandleInterface2 {
    @Override
    public void asyncHandle(TestRequest testRequest) {
        log.info("test2--"+this.getClass().getSimpleName().toString());
    }
}

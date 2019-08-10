package com.example.asyncdemo.service.impl;

import com.example.asyncdemo.dto.TestRequest;
import com.example.asyncdemo.service.AbstractAsyncHandlerAutoRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: jin3pang
 * @Date: 2019/7/18 20:23
 */
@Service("roomhandler")
@Slf4j
public class RoomHandler extends AbstractAsyncHandlerAutoRegister {
    @Override
    public void asyncHandle(TestRequest testRequest) {
        log.info("test--"+this.getClass().getSimpleName().toString());
    }
}

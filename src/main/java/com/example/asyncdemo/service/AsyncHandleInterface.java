package com.example.asyncdemo.service;

import com.example.asyncdemo.dto.TestRequest;

/**
 * @Description:
 * @Author: jin3pang
 * @Date: 2019/7/18 20:23
 */
public interface AsyncHandleInterface {
    void asyncHandle(TestRequest testRequest);

}

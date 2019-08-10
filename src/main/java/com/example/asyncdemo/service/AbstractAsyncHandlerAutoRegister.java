package com.example.asyncdemo.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

public abstract class AbstractAsyncHandlerAutoRegister implements AsyncHandleInterface {
	
	@Resource(name = "asynctestfacade")
	private  AsyncTestFacade asyncTestFacade;
	
	@PostConstruct
	public void register() {
		asyncTestFacade.register(this);
	}

}

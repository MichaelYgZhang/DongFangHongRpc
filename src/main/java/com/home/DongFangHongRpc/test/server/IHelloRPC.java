package com.home.DongFangHongRpc.test.server;

import com.home.DongFangHongRpc.test.server.bean.Person;

/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月20日 下午2:39:57
 */
public interface IHelloRPC {
	String sayHello();
	String sayHello(Person person);
}	

package com.home.DongFangHongRpc.test.server.impl;

import com.home.DongFangHongRpc.server.RpcService;
import com.home.DongFangHongRpc.test.server.IHelloRPC;
import com.home.DongFangHongRpc.test.server.bean.Person;

/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月20日 下午2:41:41
 */
@RpcService(value = IHelloRPC.class, version = "sample.hello.person")
public class HelloRPCImpl implements IHelloRPC{
	public String sayHello() {
		return "woowowo.success.sayHello.";
	}

	public String sayHello(Person person) {
		return person.getName()+","+person.getAge();
	}
}

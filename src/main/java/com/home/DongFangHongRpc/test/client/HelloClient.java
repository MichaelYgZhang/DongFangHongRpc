package com.home.DongFangHongRpc.test.client;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.home.DongFangHongRpc.client.RpcProxy;
import com.home.DongFangHongRpc.test.server.IHelloRPC;
import com.home.DongFangHongRpc.test.server.bean.Person;

public class HelloClient {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-client.xml");
        RpcProxy rpcProxy = context.getBean(RpcProxy.class);
     
        //不带version
//        IHelloRPC helloService1 = rpcProxy.create(IHelloRPC.class);
//        String result = helloService1.sayHello();
//        System.out.println("client1 print:"+result);
        
        //带version
        IHelloRPC helloService2 = rpcProxy.create(IHelloRPC.class, "sample.hello.person");
        Person person = new Person(9, "Michael");
        String result2 = helloService2.sayHello(person);
        System.out.println("client2 print:"+result2);
        System.exit(0);
    }
}

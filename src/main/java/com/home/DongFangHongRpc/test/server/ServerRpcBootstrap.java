package com.home.DongFangHongRpc.test.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServerRpcBootstrap {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-server.xml");
    }
}

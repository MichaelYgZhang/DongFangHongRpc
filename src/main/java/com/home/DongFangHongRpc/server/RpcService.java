package com.home.DongFangHongRpc.server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * @description
 * @author michael
 * @since 1.0.0
 * @datetime 2018年1月19日 上午8:30:39
 */
@Target({ElementType.TYPE})	//类，接口，枚举
@Retention(RetentionPolicy.RUNTIME)	//运行时有效
@Component //spring 容器扫描
public @interface RpcService {
	/**
	 * 服务接口类
	 * @return
	 */
	Class<?> value();
	/**
	 * 服务版本号
	 * @return
	 */
	String version() default "";
}

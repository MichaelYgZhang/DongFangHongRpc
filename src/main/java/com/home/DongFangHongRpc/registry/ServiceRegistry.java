package com.home.DongFangHongRpc.registry;
/**
 * @description 服务注册
 * @author michael
 * @since 1.0.0
 * @datetime 2018年1月18日 上午8:48:38
 */
public interface ServiceRegistry {	
	
	/**
	 * 注册服务名称与服务地址
	 * @param serviceName
	 * @param serviceAddress
	 */
	void register(String serviceName, String serviceAddress);
}

package com.home.DongFangHongRpc.registry;

/**
 * @description 服务发现
 * @author michael
 * @since 1.0.0
 * @datetime 2018年1月18日 上午8:48:38
 */
public interface IServiceDiscovery {
	/**
	 * 根据服务名称查询服务地址
	 * @param serviceName	服务名称
	 * @return 服务地址
	 */
	String discover(String serviceName);
}

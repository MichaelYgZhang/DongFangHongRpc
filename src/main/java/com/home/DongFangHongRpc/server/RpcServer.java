package com.home.DongFangHongRpc.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.home.DongFangHongRpc.common.CollectionsUtil;
import com.home.DongFangHongRpc.registry.IServiceRegistry;

/**
 * @description
 * @author michael
 * @since 1.0.0
 * @datetime 2018年1月19日 上午8:16:24
 */
public class RpcServer implements ApplicationContextAware, InitializingBean{
	private final static Log LOG = LogFactory.getLog(RpcServer.class);
	private String serverAddress;
	private IServiceRegistry serviceRegistry;
	/**
	 * 存放服务名称与对应的实现类
	 */
	private final static Map<String ,Object> handlerMap = new ConcurrentHashMap<String, Object>();
	public RpcServer(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	public RpcServer(String serverAddress, IServiceRegistry serviceRegistry) {
		this.serverAddress = serverAddress;
		this.serviceRegistry = serviceRegistry;
	}
	
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		//扫描带有RpcService的注解并初始化handlerMap
		Map<String, Object> serviceBeansMap = ctx.getBeansWithAnnotation(RpcService.class);
		if (!CollectionsUtil.isEmpty(serviceBeansMap)) {
			for (Object serviceBean : serviceBeansMap.values()) {
				RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
				String serviceName = rpcService.value().getName();
				String serviceVersion = rpcService.version();
				if (StringUtils.isNotBlank(serviceVersion)) {
					serviceName += "-" + serviceVersion;
				}
				handlerMap.put(serviceName, serviceBean);
			}
		}
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}

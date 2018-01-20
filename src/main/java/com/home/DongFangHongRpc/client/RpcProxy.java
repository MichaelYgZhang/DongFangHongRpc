package com.home.DongFangHongRpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.home.DongFangHongRpc.bean.RpcRequest;
import com.home.DongFangHongRpc.bean.RpcResponse;
import com.home.DongFangHongRpc.registry.IServiceDiscovery;
import com.home.DongFangHongRpc.registry.impl.ZookeeperServiceDiscoveryImpl;

/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月20日 上午11:39:41
 */
public class RpcProxy {
	private static final Logger LOG = Logger.getLogger(RpcProxy.class);
	private String serviceAddress;
	private IServiceDiscovery serviceDiscovery;
	public RpcProxy(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}
	
	public RpcProxy(IServiceDiscovery serviceDiscovery) {
		this.serviceDiscovery = serviceDiscovery;
	}
	
	public <T> T create(final Class<?> interfaceClass) {
		return create(interfaceClass, "");
	}

	/**
	 * @param interfaceClass
	 * @param string
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T create(final Class<?> interfaceClass, final String serviceVersion) {
		return (T) Proxy.newProxyInstance(
				interfaceClass.getClassLoader(),
				new Class<?>[]{interfaceClass},
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						RpcRequest request = new RpcRequest
											 	.RequestBuilder(
												UUID.randomUUID().toString(),
												method.getDeclaringClass().getName(),
												method.getName(),
												serviceVersion,
												method.getParameterTypes(),
												args).create();
							
						//TODO add cache
						if (null != serviceDiscovery) {
							String serviceName = interfaceClass.getName();
							if (StringUtils.isNotBlank(serviceVersion)) {
								serviceName += "-" +serviceVersion;
							}
							serviceAddress = serviceDiscovery.discover(serviceName);
						}
						if (StringUtils.isEmpty(serviceAddress)) {
							throw new RuntimeException("server address is empty");
						}
						String[] serviceAddressArray = StringUtils.splitByWholeSeparator(serviceAddress, ":");
						String host = serviceAddressArray[0];
						int port = Integer.parseInt(serviceAddressArray[1]);
						RpcClient client = new RpcClient(host, port);
						long time = System.currentTimeMillis();
						RpcResponse response = client.send(request);
						LOG.info("Request:"+request.toString()+";Response:"+response.toString()+";execu time[" + (System.currentTimeMillis()-time) +"] ms.");
						if (null == response) {
							throw new RuntimeException("response is null");
						}
						if (response.hasException()) {
							throw response.getException();
						}
						return response.getResult();
					}
				});
	}
}

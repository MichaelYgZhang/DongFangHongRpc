package com.home.DongFangHongRpc.registry.impl;

import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;

import com.home.DongFangHongRpc.common.constants.ZookeeperGlobalConstants;
import com.home.DongFangHongRpc.registry.IServiceRegistry;

/**
 * @description 服务注册实现
 * @author michael
 * @since 1.0.0
 * @datetime 2018年1月18日 上午11:25:22
 */
public class ZookeeperServiceRegistryImpl implements IServiceRegistry{
	private static final Logger LOG = Logger.getLogger(ZookeeperServiceRegistryImpl.class);
	private final ZkClient zkClient;
	
	public ZookeeperServiceRegistryImpl(String zkServers){
		zkClient = new ZkClient(zkServers, 
				ZookeeperGlobalConstants.ZK_SESSION_TIMEOUT, 
				ZookeeperGlobalConstants.ZK_CONNECTION_TIMEOUT);
	}
	
	public void register(String serviceName, String serviceAddress) {
		LOG.info("register.service-name["+serviceName+"] serviceAddress ["+serviceAddress+"]");
		String rootPath = ZookeeperGlobalConstants.ZK_ROOT_REGISTRY_ZNODE_PATH;
		if (!zkClient.exists(rootPath)) {
			zkClient.createPersistent(rootPath);
			LOG.info("create register rootPath:"+ rootPath);
		}
		String serviceNamePath = rootPath + "/" + serviceName;
		if (!zkClient.exists(serviceNamePath)) {
			zkClient.createPersistent(serviceNamePath);
			LOG.info("create serviceName node:"+serviceNamePath);
		}
		String serviceAddressPath = serviceNamePath + "/address[" + serviceAddress+"]";
		String addressNode = zkClient.createEphemeralSequential(serviceAddressPath, serviceAddress);
		LOG.info("create service address node :" + addressNode);
	}
	
}

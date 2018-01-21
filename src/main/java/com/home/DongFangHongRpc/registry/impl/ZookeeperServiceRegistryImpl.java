package com.home.DongFangHongRpc.registry.impl;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.home.DongFangHongRpc.common.constants.ZookeeperGlobalConstants;
import com.home.DongFangHongRpc.registry.IServiceRegistry;

/**
 * @description 服务注册实现
 * @author michael
 * @since 1.0.0
 * @datetime 2018年1月18日 上午11:25:22
 */
public class ZookeeperServiceRegistryImpl extends ZookeeperConnection implements IServiceRegistry {
	private static final Logger LOG = Logger.getLogger(ZookeeperServiceRegistryImpl.class);
	private final ZooKeeper zk;
	public ZookeeperServiceRegistryImpl(String zkAddress) {
		zk = connectServer(zkAddress);
	}
	public void register(String serviceName, String serviceAddress) {
		LOG.info("register.service-name[" + serviceName + "] serviceAddress ["+ serviceAddress + "] start.");
		String rootPath = ZookeeperGlobalConstants.ZK_ROOT_REGISTRY_ZNODE_PATH;
		try {
			Stat statRootPath = zk.exists(rootPath, false);
			if (null == statRootPath) {
				LOG.info("create root path["+createPersistentNode(rootPath)+"] end.");
			}
			String serviceNamePath = rootPath + "/" + serviceName;
			Stat statService = zk.exists(serviceNamePath, false);
			if (null == statService) {
				LOG.info("create service name path["+createPersistentNode(serviceNamePath)+"] end.");
			}
			String serviceAddressPath = serviceNamePath + "/address["+ serviceAddress + "]";
			Stat statServiceAddress = zk.exists(serviceAddressPath, false);
			if (null == statServiceAddress) {
				String node = createNode(serviceAddressPath, serviceAddress);
				LOG.info("create service address node[" + node+"] end.");
			}
		} catch (KeeperException e) {
			LOG.error(e.toString());
		} catch (InterruptedException e) {
			LOG.error(e.toString());
		}
	}
	
	private String createPersistentNode(String rootPath) throws KeeperException, InterruptedException {
		Stat s = zk.exists(rootPath, false);
		if (s == null) {
			return zk.create(rootPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}
		return " createPersistentNode error.";
	}

	private String createNode(String addressPath, String serviceAddress)  throws KeeperException, InterruptedException {
		byte[] bytes = serviceAddress.getBytes();
		return zk.create(addressPath, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
	}
}

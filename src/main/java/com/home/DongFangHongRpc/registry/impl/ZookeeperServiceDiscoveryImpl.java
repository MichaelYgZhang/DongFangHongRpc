package com.home.DongFangHongRpc.registry.impl;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;

import com.home.DongFangHongRpc.common.CollectionsUtil;
import com.home.DongFangHongRpc.common.constants.ZookeeperGlobalConstants;
import com.home.DongFangHongRpc.registry.IServiceDiscovery;

/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月18日 下午6:07:07
 */
public class ZookeeperServiceDiscoveryImpl implements IServiceDiscovery{
	private static final Logger LOG = Logger.getLogger(ZookeeperServiceDiscoveryImpl.class);
	private final String zkAddress;
	private final ZkClient zkClient;
	public ZookeeperServiceDiscoveryImpl (String zkAddress){
		this.zkAddress = zkAddress;
		zkClient = new ZkClient(zkAddress,
				ZookeeperGlobalConstants.ZK_SESSION_TIMEOUT,
				ZookeeperGlobalConstants.ZK_CONNECTION_TIMEOUT);
	}
	
	public String discover(String serviceName) {
		LOG.info("zookeeper service discovery serviceName:" + serviceName);
		String rootPath = ZookeeperGlobalConstants.ZK_ROOT_REGISTRY_ZNODE_PATH;
		String servicePath = rootPath + "/" + serviceName;
		if (!zkClient.exists(servicePath)) {
			throw new RuntimeException("zookeeper service discovery can't get any service path :"+ servicePath);
		}
		List<String> addressCollection = zkClient.getChildren(servicePath);
		if (CollectionsUtil.isEmpty(addressCollection)) {
			throw new RuntimeException("zookeeper service discovery can't get any children service path :"+ servicePath);
		}
		String subNodeAddress;
		int size = addressCollection.size();
		if (size == 1) {
			subNodeAddress = addressCollection.get(0);
		} else {
			subNodeAddress = addressCollection.get(ThreadLocalRandom.current().nextInt(size));
		}
		// 获取 address 节点的值
        String addressPath = servicePath + "/" + subNodeAddress;
        LOG.info("zookeeper service discovery get addressPaht [" + addressPath +"] data");
		return zkClient.readData(addressPath);
	}

}

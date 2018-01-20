package com.home.DongFangHongRpc.registry.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;

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
	private ZooKeeper zk = null;
	//缓存服务避免每次都调用Zookeeper
	private final static Map<String, List<String>> servers = new ConcurrentHashMap<String, List<String>>();
	private final static Object lock = new Object();//TODO opt ReentrantReadWriteLock
	
	public ZookeeperServiceDiscoveryImpl(String zkAddress) {
		this.zkAddress = zkAddress;
		connectionZK();
	}
	
	private void connectionZK() {
		synchronized (lock) {
			try {
				// 服务器在需求中并不需要做任何监听
				zk = new ZooKeeper(this.zkAddress,
						ZookeeperGlobalConstants.ZK_SESSION_TIMEOUT,
						new Watcher() {
							public void process(WatchedEvent event) {
								if (event.getType() == EventType.None)
									return;
								try {
									// 获取新的服务器列表,重新注册监听
									updateServers();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
			} catch (IOException e) {
				e.printStackTrace();
				LOG.error("connection exception."+e.getMessage(), e);
			}
		}
	}
	
	private void updateServers() throws KeeperException, InterruptedException {
		LOG.info("updateServers begin,time["+System.currentTimeMillis()+"]");
		// 从servers父节点下获取到所有子节点，并注册监听
		String rootPath = ZookeeperGlobalConstants.ZK_ROOT_REGISTRY_ZNODE_PATH;
		List<String> serviceNameList = zk.getChildren(rootPath, true);
		if (CollectionsUtil.isEmpty(serviceNameList)) {
			throw new RuntimeException("can't find any serviceName.");
		}
		for (String serviceName : serviceNameList) {
			List<String> addressList = new ArrayList<String>();
			String servicePath = rootPath+"/"+serviceName;
			List<String> serviceAddressList = zk.getChildren(servicePath, false);
			if (CollectionsUtil.isEmpty(serviceAddressList)) {
				LOG.error("updateServers serviceName["+servicePath+"] can't find any child.");
				continue;
			}
			for (String address : serviceAddressList) {
				String addressPath = servicePath + "/" + address ;
				byte[] data = zk.getData(addressPath, false, null);
				LOG.info("updateServers servicePaht["+addressPath+"] data["+data.toString()+"]");
				addressList.add(data.toString());
			}
			servers.put(servicePath, addressList);
		}
	}
	
	public String discover(String serviceName) {
		LOG.info("zookeeper service discovery serviceName:" + serviceName +" start.");
		String rootPath = ZookeeperGlobalConstants.ZK_ROOT_REGISTRY_ZNODE_PATH;
		String servicePath = rootPath + "/" + serviceName;
		List<String> addressList = servers.get(servicePath);
		if (CollectionUtils.isEmpty(addressList)) {
			throw new RuntimeException("discover can't find serviceName["+serviceName+"]");
		}
		//load balance
		int size = addressList.size();
		if (size == 1) {
			return addressList.get(0);
		} else {
			return addressList.get(ThreadLocalRandom.current().nextInt(size));
		}
	}
	
	/**
	private String discoverExecu(String serviceName){
		LOG.info("zookeeper service discovery serviceName:" + serviceName +" start.");
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
        LOG.info("zookeeper service discovery get addressPath [" + addressPath +"] data.");
		return zkClient.readData(addressPath);
	}
	*/
	
}

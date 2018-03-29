package com.home.DongFangHongRpc.registry.impl;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.home.DongFangHongRpc.common.CollectionsUtil;
import com.home.DongFangHongRpc.common.constants.ZookeeperGlobalConstants;
import com.home.DongFangHongRpc.registry.IServiceDiscovery;

/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月18日 下午6:07:07
 */
public class ZookeeperServiceDiscoveryImpl extends ZookeeperConnection implements IServiceDiscovery{
	private static final Logger LOG = Logger.getLogger(ZookeeperServiceDiscoveryImpl.class);
    private final Map<String ,List<String>> servers = new ConcurrentHashMap<String, List<String>>();
    private final ZooKeeper zk;
	public ZookeeperServiceDiscoveryImpl(String zkAddress) {
		zk = connectServer(zkAddress);
        if (zk != null) {
            watchNode(zk);
        }
	}
	
	public String discover(String serviceName){
		LOG.info("zookeeper service discovery serviceName:" + serviceName +" start.");
		String rootPath = ZookeeperGlobalConstants.ZK_ROOT_REGISTRY_ZNODE_PATH;
		String servicePath = rootPath + "/" + serviceName;
		List<String> adress = servers.get(servicePath);
		int size = adress.size();
		if (size == 1) {
			return adress.get(0);
		} else {
			return adress.get(ThreadLocalRandom.current().nextInt(size));
		}
	}
	
	private void watchNode(final ZooKeeper zk) {
		try {
			Map<String, List<String>> servers = new ConcurrentHashMap<String, List<String>>();
			String rootPath = ZookeeperGlobalConstants.ZK_ROOT_REGISTRY_ZNODE_PATH;
			List<String> serviceNameList = zk.getChildren(rootPath,
					new Watcher() {
						public void process(WatchedEvent event) {
							if (event.getType() == Event.EventType.NodeChildrenChanged) {
								watchNode(zk);
							}
						}
					});

			for (String serviceName : serviceNameList) {
				String serviceNamePath = rootPath + "/" + serviceName;
				List<String> addressList = zk.getChildren(serviceNamePath, new Watcher() {
					public void process(WatchedEvent event) {
						if (event.getType() == Event.EventType.NodeChildrenChanged) {
							watchNode(zk);
						}
					}
				});
				if (CollectionsUtil.isEmpty(addressList)) {
					LOG.error("serviceNamePath:" + serviceNamePath + " is empty.");
					continue;
				}
				List<String> tempAddress = new ArrayList<String>();
				for (String address : addressList) {
					String addressPath = serviceNamePath + "/" + address;
					byte[] bytes = zk.getData(addressPath, false, null);
					tempAddress.add(new String(bytes, Charset.defaultCharset()));
				}
				servers.put(serviceNamePath, tempAddress);
			}
			this.servers = servers;
			LOG.info("servers:" + servers.toString());
		} catch (KeeperException e) {
			LOG.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
}

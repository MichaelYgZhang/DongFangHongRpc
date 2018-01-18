package com.home.DongFangHongRpc.zookeeper.study;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import com.home.DongFangHongRpc.common.constants.ZookeeperGlobalConstants;


/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月18日 下午2:34:55
 */
public class ZookeeperConnectionClient {
	public static ZooKeeper zk;
	private final static CountDownLatch connectedSignal = new CountDownLatch(1);
	
	public final static ZooKeeper connect(String host){
		return innerConnect(host);
	}
	
	public final static ZooKeeper connect(){
		return innerConnect(null);
	}
	
	private final static ZooKeeper innerConnect(String host){
		if (StringUtils.isBlank(host)) {
			host = ZookeeperGlobalConstants.ZOOKEEPER_HOSTS;
		}
		try {
			zk = new ZooKeeper(host, ZookeeperGlobalConstants.ZK_SESSION_TIMEOUT, new Watcher() {
				public void process(WatchedEvent we) {
					if (we.getState() == KeeperState.SyncConnected) {
						connectedSignal.countDown();
					}
				}
			});
			connectedSignal.await();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return zk;
	}
	
	public final static void close() {
		try {
			zk.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

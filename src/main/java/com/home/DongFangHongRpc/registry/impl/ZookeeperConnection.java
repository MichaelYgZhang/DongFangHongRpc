package com.home.DongFangHongRpc.registry.impl;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import com.home.DongFangHongRpc.common.constants.ZookeeperGlobalConstants;

/**
 * @description
 * @author michael
 * @since 1.0.0
 * @datetime 2018年1月21日 上午11:01:10
 */
public class ZookeeperConnection {
	private static final Logger LOG = Logger.getLogger(ZookeeperConnection.class);
	private CountDownLatch latch = new CountDownLatch(1);
	public ZooKeeper connectServer(String zkAddress) {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(zkAddress, ZookeeperGlobalConstants.ZK_SESSION_TIMEOUT, 
            		new Watcher() {
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } catch (InterruptedException e) {
        		LOG.error(e.getMessage(), e);
        }
        return zk;
    }
}

package com.home.DongFangHongRpc.zookeeper.study;

import org.apache.zookeeper.KeeperException;
/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月18日 下午3:00:11
 */
public class ZKDelete{
	// Method to check existence of znode and its status, if znode is available.
	public static void delete(String path) throws KeeperException,
			InterruptedException {
		ZookeeperConnectionClient.connect().delete(path, 
				ZookeeperConnectionClient.connect().exists(path, true).getVersion());
	}

	public static void main(String[] args) throws InterruptedException,
			KeeperException {
		String path = "/MyFirstZnode"; // Assign path to the znode
		try {
			delete(path); // delete the node with the specified path
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ZookeeperConnectionClient.close();
			System.exit(0);
		}
	}
}

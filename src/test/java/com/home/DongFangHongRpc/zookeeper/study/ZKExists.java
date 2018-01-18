package com.home.DongFangHongRpc.zookeeper.study;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import com.alibaba.fastjson.JSONObject;
/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月18日 下午2:17:06
 */
public class ZKExists{
	// Method to check existence of znode and its status, if znode is available.
	public static Stat znode_exists(String path) throws KeeperException,
			InterruptedException {
		return ZookeeperConnectionClient.connect().exists(path, true);
	}

	public static void main(String[] args) throws InterruptedException, KeeperException {
		String path = "/MyFirstZnode"; // Assign znode to the specified path
		try {
			Stat stat = znode_exists(path); // Stat checks the path of the znode
			if (stat != null) {
				System.out.println("Node exists and the node is " + JSONObject.toJSONString(stat));
			} else {
				System.out.println("Node does not exists");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ZookeeperConnectionClient.close();
			System.exit(0);
		}
	}
}

package com.home.DongFangHongRpc.zookeeper.study;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月18日 下午2:09:14
 */
public class ZKCreate{
	// Method to create znode in zookeeper ensemble
	public static String create(String path, byte[] data)
			throws KeeperException, InterruptedException {
		return ZookeeperConnectionClient.connect()
				.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
	}

	public static void main(String[] args) throws InterruptedException {
		// znode path
		String path = "/MyFirstZnode"; // Assign path to znode
		String threePath = path +"/threeNode";
		// data in byte array
		byte[] data = "My first zookeeper app".getBytes(); // Declare data
		try {
			String result = create(path, data); // Create the data to the specified path
			System.out.println("root path:"+result);
			String threeresult = create(threePath, "threeHi".getBytes()); // Create the data to the specified path
			System.out.println("three path:"+threeresult);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ZookeeperConnectionClient.close();
			System.exit(0);
		}
	}
}

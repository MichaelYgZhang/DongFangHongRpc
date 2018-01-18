package com.home.DongFangHongRpc.zookeeper.study;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月18日 下午2:54:40
 */
public class ZKGetChildren{
	// Method to check existence of znode and its status, if znode is available.
	public static Stat znode_exists(String path) throws KeeperException,
			InterruptedException {
		return ZookeeperConnectionClient.connect().exists(path, true);
	}

	public static void main(String[] args) throws InterruptedException,
			KeeperException {
		String path = "/MyFirstZnode"; // Assign path to the znode
		try {
			Stat stat = znode_exists(path); // Stat checks the path
			if (stat != null) {
				// “getChildren" method- get all the children of znode.It has
				// two args, path and watch
				List<String> children = 
						ZookeeperConnectionClient.connect().getChildren(path, false);
				for (int i = 0; i < children.size(); i++)
					System.out.println(path+"/"+children.get(i)); // Print children's
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

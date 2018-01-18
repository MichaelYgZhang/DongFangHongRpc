package com.home.DongFangHongRpc.zookeeper.study;

import org.apache.zookeeper.KeeperException;
/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月18日 下午2:51:05
 */
public class ZKSetData extends ZookeeperConnectionClient{
	   // Method to update the data in a znode. Similar to getData but without watcher.
	   public static void update(String path, byte[] data) throws
	      KeeperException,InterruptedException {
	      zk.setData(path, data, zk.exists(path,true).getVersion());
	   }
	   public static void main(String[] args) throws InterruptedException,KeeperException {
	      String path= "/MyFirstZnode";
	      byte[] data = "SetDataSuccess".getBytes(); //Assign data which is to be updated.
	      try {
	         update(path, data); // Update znode data to the specified path
	      } catch(Exception e) {
	    	  e.printStackTrace();
	      } finally {
	    	  close();
	    	  System.exit(0);
	      }
	   }
}

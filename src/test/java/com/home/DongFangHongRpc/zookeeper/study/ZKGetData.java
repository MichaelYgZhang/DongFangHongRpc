package com.home.DongFangHongRpc.zookeeper.study;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月18日 下午2:32:17
 */
public class ZKGetData extends ZookeeperConnectionClient{
	public static Stat znode_exists(String path) throws KeeperException,
			InterruptedException {
		return zk.exists(path, true);
	}

	public static void main(String[] args) throws InterruptedException,
			KeeperException {
		String path = "/DongFangHongRPC-Servers/serviceName";
		final CountDownLatch connectedSignal = new CountDownLatch(1);
		try {
			connect();
			Stat stat = znode_exists(path);
			if (stat != null) {
				byte[] b = zk.getData(path, new Watcher() {
					public void process(WatchedEvent we) {
						if (we.getType() == Event.EventType.None) {
							switch (we.getState()) {
							case Expired:
								System.out.println("節點過期");
								connectedSignal.countDown();
								break;
							default:
								break;
							}
						} else {
							String path = "/MyFirstZnode";
							try {
								byte[] bn = zk.getData(path, false, null);
								String data = new String(bn, "UTF-8");
								System.out.println("修改后:"+data);
								connectedSignal.countDown();
							} catch (Exception ex) {
								System.out.println(ex.getMessage());
							}
						}
					}
				}, null);

				String data = new String(b, "UTF-8");
				System.out.println("當前 '/MyFirstZnode node value':" +data);
				connectedSignal.await();
			} else {
				System.out.println("Node does not exists");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
}

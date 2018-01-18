package com.home.DongFangHongRpc.zookeeper.test;

import org.junit.Before;
import org.junit.Test;

import com.home.DongFangHongRpc.common.constants.ZookeeperGlobalConstants;
import com.home.DongFangHongRpc.registry.IServiceDiscovery;
import com.home.DongFangHongRpc.registry.IServiceRegistry;
import com.home.DongFangHongRpc.registry.impl.ZookeeperServiceDiscoveryImpl;
import com.home.DongFangHongRpc.registry.impl.ZookeeperServiceRegistryImpl;

/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月18日 下午6:32:12
 */
public class ServiceRegistryDiscovery {
	IServiceRegistry serviceRegisty;
	IServiceDiscovery serviceDiscovery;
	@Before
	public void before() {
		serviceRegisty = new ZookeeperServiceRegistryImpl(ZookeeperGlobalConstants.ZOOKEEPER_HOSTS);
		serviceDiscovery = new ZookeeperServiceDiscoveryImpl(ZookeeperGlobalConstants.ZOOKEEPER_HOSTS);
	}
	@Test
	public void testRegistry() {
		serviceRegisty.register("serviceName", "10.9.192.110");
		serviceRegisty.register("serviceName", "10.9.192.120");
		serviceRegisty.register("serviceName", "10.9.192.130");
		serviceRegisty.register("serviceName", "10.9.192.140");
		serviceRegisty.register("serviceName", "10.9.192.150");
		try {
			Thread.sleep(5*60*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDiscovery() {
		String discover = serviceDiscovery.discover("serviceName");
		System.out.println(discover);
	}
}

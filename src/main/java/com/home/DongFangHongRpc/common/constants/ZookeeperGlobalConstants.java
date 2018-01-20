package com.home.DongFangHongRpc.common.constants;

/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月18日 下午5:54:50
 */
public interface ZookeeperGlobalConstants {
	String ZOOKEEPER_HOSTS="127.0.0.1:2181"; // ','分割可以有多個比如127.0.0.1:3000,127.0.0.1:3001...
	int ZK_SESSION_TIMEOUT = 5000;
	int ZK_CONNECTION_TIMEOUT = 1000;
	String ZK_ROOT_REGISTRY_ZNODE_PATH = "/DongFangHongRPC-Servers"; //服务在zk下的路径
}

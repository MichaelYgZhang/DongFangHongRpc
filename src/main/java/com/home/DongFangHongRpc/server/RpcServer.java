package com.home.DongFangHongRpc.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.home.DongFangHongRpc.bean.RpcRequest;
import com.home.DongFangHongRpc.bean.RpcResponse;
import com.home.DongFangHongRpc.codec.RpcDecoder;
import com.home.DongFangHongRpc.codec.RpcEncoder;
import com.home.DongFangHongRpc.common.CollectionsUtil;
import com.home.DongFangHongRpc.registry.IServiceRegistry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @description
 * @author michael
 * @since 1.0.0
 * @datetime 2018年1月19日 上午8:16:24
 */
public class RpcServer implements ApplicationContextAware, InitializingBean{
	private final static Log LOG = LogFactory.getLog(RpcServer.class);
	private String serviceAddress;
	private IServiceRegistry serviceRegistry;
	/**
	 * 存放服务名称与对应的实现类
	 */
	private final static Map<String ,Object> handlerMap = new ConcurrentHashMap<String, Object>();
	public RpcServer(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}
	public RpcServer(String serviceAddress, IServiceRegistry serviceRegistry) {
		this.serviceAddress = serviceAddress;
		this.serviceRegistry = serviceRegistry;
	}
	
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		Map<String, Object> serviceBeansMap = ctx.getBeansWithAnnotation(RpcService.class);
		if (!CollectionsUtil.isEmpty(serviceBeansMap)) {
			for (Object serviceBean : serviceBeansMap.values()) {
				RpcService rpcService = serviceBean.getClass().getAnnotation(RpcService.class);
				String serviceName = rpcService.value().getName();
				String serviceVersion = rpcService.version();
				if (StringUtils.isNotBlank(serviceVersion)) {
					serviceName += "-" + serviceVersion;
				}
				handlerMap.put(serviceName, serviceBean);
			}
		}
	}

	public void afterPropertiesSet() throws Exception {
		EventLoopGroup master = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(master, worker);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					ChannelPipeline pipeline = channel.pipeline();
					pipeline.addLast(new RpcDecoder(RpcRequest.class)); // 解码 RPC 请求
                    pipeline.addLast(new RpcEncoder(RpcResponse.class)); // 编码 RPC 响应
                    pipeline.addLast(new RpcServerHandler(handlerMap)); // 处理 RPC 请求
				}
			});
			bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			//获取RPC服务器IP地址与端口
			String[] addressAry = StringUtils.splitByWholeSeparator(serviceAddress, ":");
			String ip = addressAry[0];
			int port = Integer.parseInt(addressAry[1]);
			//启动RPC服务器
			ChannelFuture future = bootstrap.bind(ip, port).sync();
			//注册RPC服务地址
			if (serviceRegistry != null) {
				for (String interfaceName : handlerMap.keySet()) {
					serviceRegistry.register(interfaceName, serviceAddress);
					LOG.info("registry service:["+interfaceName+"-"+serviceAddress+"]");
				}
			}
			//关闭RPC服务器
			future.channel().closeFuture().sync();
		} finally {
			worker.shutdownGracefully();
			master.shutdownGracefully();
		}
	}
	

}

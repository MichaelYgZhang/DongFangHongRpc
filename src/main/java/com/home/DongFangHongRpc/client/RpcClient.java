package com.home.DongFangHongRpc.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.home.DongFangHongRpc.bean.RpcRequest;
import com.home.DongFangHongRpc.bean.RpcResponse;
import com.home.DongFangHongRpc.codec.RpcDecoder;
import com.home.DongFangHongRpc.codec.RpcEncoder;
import com.home.DongFangHongRpc.server.RpcServerHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @description
 * @author 58
 * @since 1.0.0
 * @datetime 2018年1月20日 上午11:26:18
 */
public class RpcClient extends SimpleChannelInboundHandler<RpcResponse>{
	private final static Log LOG = LogFactory.getLog(RpcServerHandler.class);
	private final String host;
	private final int port;
	private RpcResponse response;
	public RpcClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, RpcResponse response)
			throws Exception {
		this.response = response;
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		LOG.error("api exceptionCaught ", cause);
		ctx.close();
	}

	public RpcResponse send(RpcRequest request) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		try {
			bootstrap.group(group);
			bootstrap.channel(NioSocketChannel.class);
			bootstrap.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					ChannelPipeline pipeline = channel.pipeline();
					pipeline.addLast(new RpcEncoder(RpcRequest.class));//编码RPC请求
					pipeline.addLast(new RpcDecoder(RpcResponse.class));//解码RPC响应
					pipeline.addLast(RpcClient.this);//处理RPC响应
				}
			});
			bootstrap.option(ChannelOption.TCP_NODELAY, true);
			ChannelFuture future = bootstrap.connect(host, port).sync();
			Channel channel = future.channel();
			channel.writeAndFlush(request).sync();
			channel.closeFuture().sync();
			return response;
		} finally {
			group.shutdownGracefully();
		}
	}
}

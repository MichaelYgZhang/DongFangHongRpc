package com.home.DongFangHongRpc.server;

import java.util.Map;

import com.home.DongFangHongRpc.bean.RpcRequest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description
 * @author michael
 * @since 1.0.0
 * @datetime 2018年1月20日 上午8:29:00
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
	private final Map<String ,Object> handlerMap;
	public RpcServerHandler(Map<String, Object> handlerMap) {
		this.handlerMap = handlerMap;
	}
	/* 
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, RpcRequest msg)
			throws Exception {
		
	}
}

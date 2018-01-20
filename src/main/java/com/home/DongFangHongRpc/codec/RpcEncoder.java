package com.home.DongFangHongRpc.codec;

import com.home.DongFangHongRpc.common.SerializationUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @description
 * @author michael
 * @since 1.0.0
 * @datetime 2018年1月20日 上午7:54:20
 */
public class RpcEncoder extends MessageToByteEncoder{
	
	private Class<?> genericClass;
	
	public RpcEncoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}
	/* 
	 * @param ctx
	 * @param msg
	 * @param out
	 * @throws Exception
	 */
	@Override
	protected void encode(ChannelHandlerContext ctx, Object obj, ByteBuf out)
			throws Exception {
		if (genericClass.isInstance(obj)) {
			byte[] data = SerializationUtil.serialize(obj);
			out.writeInt(data.length);
			out.writeBytes(data);
		}
	}

}

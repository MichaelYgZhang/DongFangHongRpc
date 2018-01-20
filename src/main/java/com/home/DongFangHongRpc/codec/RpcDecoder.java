package com.home.DongFangHongRpc.codec;

import java.util.List;

import com.home.DongFangHongRpc.common.SerializationUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @description
 * @author michael
 * @since 1.0.0
 * @datetime 2018年1月20日 上午8:10:56
 */
public class RpcDecoder extends ByteToMessageDecoder{
	
	private Class<?> genericClass;
	
	public RpcDecoder(Class<?> genericClass) {
		this.genericClass = genericClass;
	}
	
	/* 
	 * @param ctx
	 * @param in
	 * @param out
	 * @throws Exception
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < 4) {
			return;
		}
		in.markReaderIndex();
		int dataLength = in.readInt();
		if (in.readableBytes() < dataLength) {
			in.resetReaderIndex();
			return;
		}
		byte[] data = new byte[dataLength];
		in.readBytes(data);
		out.add(SerializationUtil.deserialize(data, genericClass));
	}

}

package com.home.DongFangHongRpc.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import com.home.DongFangHongRpc.bean.RpcRequest;
import com.home.DongFangHongRpc.bean.RpcResponse;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @description
 * @author michael
 * @since 1.0.0
 * @datetime 2018年1月20日 上午8:29:00
 */
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
	private final static Log LOG = LogFactory.getLog(RpcServerHandler.class);
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
	protected void messageReceived(final ChannelHandlerContext ctx,final RpcRequest request)
			throws Exception {
		RpcResponse response = new RpcResponse();
		response.setRequestId(request.getRequestId());
		try {
			Object result = handle(request);
			response.setResult(result);
		} catch (Exception e) {
			LOG.error("handle result failure", e);
			response.setException(e);
		}
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	/**
	 * @param request
	 * @return
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	private Object handle(RpcRequest request) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String serviceName = request.getInterfaceName();
		String serviceVersion = request.getServiceVersion();
		if (StringUtils.isNotBlank(serviceVersion)) {
			serviceName += "-" + serviceVersion;
		}
		Object serviceBean = handlerMap.get(serviceName);
		if (null == serviceBean) {
			throw new RuntimeException("can't find service bean by servicename:"+serviceName);
		}
		Class<?> serviceClass = serviceBean.getClass();
		String methodName = request.getMethodName();
		Class<?>[] parameterTypes = request.getParameterTypes();
		Object[] parameters = request.getParameters();
		//反射
//		Method method = serviceClass.getMethod(methodName, parameterTypes);
//		method.setAccessible(true);
//		return method.invoke(serviceBean, parameters);
		//CGLib反射
		FastClass serviceFastClass = FastClass.create(serviceClass);
		FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
		return serviceFastMethod.invoke(serviceBean, parameters);
	}
	
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		LOG.error("server exception", cause);
		ctx.close();
	}
}

package com.home.DongFangHongRpc.bean;

import java.util.Arrays;

/**
 * RPC 请求
 * @description
 * @author michael
 * @since 1.0.0
 * @datetime 2018年1月20日 上午8:17:41
 */
public class RpcRequest {
	/**
	 * @param requestId
	 * @param interfaceName
	 * @param serviceVersion
	 * @param methodName
	 * @param parameterTypes
	 * @param parameters
	 */
	private RpcRequest(String requestId, String interfaceName,
			String serviceVersion, String methodName,
			Class<?>[] parameterTypes, Object[] parameters) {
		super();
		this.requestId = requestId;
		this.interfaceName = interfaceName;
		this.serviceVersion = serviceVersion;
		this.methodName = methodName;
		this.parameterTypes = parameterTypes;
		this.parameters = parameters;
	}
	private String requestId;
	private String interfaceName;
	private String serviceVersion;
	private String methodName;
	private Class<?>[] parameterTypes;
	private Object[] parameters;
	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}
	/**
	 * @return the interfaceName
	 */
	public String getInterfaceName() {
		return interfaceName;
	}
	/**
	 * @return the serviceVersion
	 */
	public String getServiceVersion() {
		return serviceVersion;
	}
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}
	/**
	 * @return the parameterTypes
	 */
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
	/**
	 * @return the parameters
	 */
	public Object[] getParameters() {
		return parameters;
	}
	
	@Override
	public String toString() {
		return "requestId["+this.requestId+"] "
				+"interfaceName["+this.interfaceName+"] "
				+"serviceVersion["+this.serviceVersion+"] "
				+"methodName["+this.methodName+"] "
				+"parameterTypes["+Arrays.toString(this.parameterTypes)+"] "
				+"parameters["+Arrays.toString(this.parameters)+"].";
				
	}
	
	public static class RequestBuilder{
		private final String requestId;
		private final String interfaceName;
		private final String methodName;
		private String serviceVersion;
		private Class<?>[] parameterTypes;
		private Object[] parameters;
		
		public RequestBuilder(String requestId, String interfaceName, String methodName,
				String serviceVersion, Class<?>[] parameterTypes, Object[] parameters) {
			this.requestId = requestId;
			this.interfaceName = interfaceName;
			this.methodName = methodName;
			this.serviceVersion = serviceVersion;
			this.parameterTypes = parameterTypes;
			this.parameters = parameters;
		}
		
		public RpcRequest create(){
			return new RpcRequest(requestId, interfaceName, serviceVersion, methodName, parameterTypes, parameters);
		}
	}
}

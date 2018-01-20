package com.home.DongFangHongRpc.bean;

/**
 * RPC 请求
 * @description
 * @author michael
 * @since 1.0.0
 * @datetime 2018年1月20日 上午8:17:41
 */
public class RpcRequest {
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
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	/**
	 * @return the interfaceName
	 */
	public String getInterfaceName() {
		return interfaceName;
	}
	/**
	 * @param interfaceName the interfaceName to set
	 */
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	/**
	 * @return the serviceVersion
	 */
	public String getServiceVersion() {
		return serviceVersion;
	}
	/**
	 * @param serviceVersion the serviceVersion to set
	 */
	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}
	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	/**
	 * @return the parameterTypes
	 */
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
	/**
	 * @param parameterTypes the parameterTypes to set
	 */
	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	/**
	 * @return the parameters
	 */
	public Object[] getParameters() {
		return parameters;
	}
	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}
	
}

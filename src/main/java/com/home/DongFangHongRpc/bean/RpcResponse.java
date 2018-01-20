package com.home.DongFangHongRpc.bean;

/**
 * RPC 响应
 * @description
 * @author michael
 * @since 1.0.0
 * @datetime 2018年1月20日 上午8:24:26
 */
public class RpcResponse {
	
	private String requestId;
	private Exception exception;
	private Object result;
	public boolean hasException() {
		return null != exception;
	}
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
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}

	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}
}

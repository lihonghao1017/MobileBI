package com.ruisi.bi.app.net;

/**
 * 服务器返回数据回调接�?
 */
public interface ServerCallbackInterface {  

	/**
	 * 返回成功时进行回�?
	 * @param <T>
	 * @param map 返回封装的数�?
	 * @param uuid 返回线程的唯�?��识码
	 */
	public <T> void succeedReceiveData(T object, String uuid);

	/**
	 * 返回失败时进行调�?
	 * @param errorMessage 返回封装的失败信�?
	 * @param uuid 返回线程唯一标识�?
	 */
	public void failedWithErrorInfo(ServerErrorMessage errorMessage, String uuid);

}

package com.ruisi.bi.app.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ruisi.bi.app.bean.RequestVo;
import com.ruisi.bi.app.common.APIContext;

public class ServerUrlConnection implements Runnable {
	private static final int SUCCEEND = 1001;
	private static final int FAILED = 1002;
	private static final int EXCEPTION = 1003;
	private static final int ConnectionTimeout = 10000;// 连接超时
	private static final int SoTimeout = 10000;// 响应超时
	/** 默认的套接字缓冲区大�?*/
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
	/** http请求�?��并发连接�?*/
	private static final int DEFAULT_MAX_CONNECTIONS = 10;
	private ServerCallbackInterface serverCallbackInterface;
	private ServerEngine serverEngine;
	private String finalUrl;
	private RequestVo vo;

	public static BasicHeader[] headers = new BasicHeader[2];
	static {
		headers[0] = new BasicHeader("Content-Type", "application/json");
		headers[1] = new BasicHeader("Accept", "application/json");// 手机串号
	}

	/**
	 * Handler向主线程发�?消息,进行回调
	 */
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			String uuid=bundle.getString("identifiy");
			if (serverEngine.CloseConnection(uuid)) {
//				System.out.println("关闭链接---�? + bundle.getString("identifiy"));
			}
			switch (msg.what) {
			case SUCCEEND:
				if (!Thread.interrupted()) {
					serverCallbackInterface.succeedReceiveData(msg.obj,uuid);
				}
				break;
			case FAILED:
				ServerErrorMessage errorMessage = (ServerErrorMessage) msg.obj;
				if (!Thread.interrupted())
					serverCallbackInterface.failedWithErrorInfo(errorMessage,uuid);
				break;
			case EXCEPTION:
				ServerErrorMessage errorMessage2 = (ServerErrorMessage) msg.obj;
				if (!Thread.interrupted()) {
					serverCallbackInterface.failedWithErrorInfo(
							errorMessage2, uuid);
				}
				break;
			default:
				break;
			}

		};
	};

	public ServerUrlConnection(ServerEngine serverEngine2,
			ServerCallbackInterface serverCallbackInterface2, RequestVo vo) {
		this.serverEngine = serverEngine2;
		this.serverCallbackInterface = serverCallbackInterface2;
		this.vo = vo;
		this.finalUrl = makeUrl(vo);
	}

	@Override
	public void run() {
		if (null != vo.Type && !"".equals(vo.Type)
				&& APIContext.POST.equals(vo.Type)) {
			postRequest();
		} else if (null != vo.Type && !"".equals(vo.Type)
				&& APIContext.GET.equals(vo.Type)) {
			getRequest();
		}
	}

	public String makeUrl(RequestVo vo) {
		StringBuffer finalurl = null;
		if (vo.hostType != null && "uploadRoute".equals(vo.hostType)) {
			finalurl = new StringBuffer(APIContext.HOST);
		} else {
			finalurl = new StringBuffer(APIContext.HOST);
		}
		if (null != vo.modulePath && !"".equals(vo.modulePath)) {
			finalurl.append(vo.modulePath);
			finalurl.append("/");
			finalurl.append(vo.functionPath);
		} else {
			finalurl.append(vo.functionPath);
		}
		if (APIContext.GET.equals(vo.Type)) {
			try {
				if (vo.requestDataMap != null) {
					finalurl.append(makeURL(vo.modulePath, vo.functionPath,
							vo.requestDataMap));
				} 
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		return finalurl.toString();
	}

	/**
	 * 请求为GET方式时拼揍URL地址
	 * 
	 * @param modulePath
	 *            请求的模块路�?
	 * @param functionPath
	 *            请求的具体功能路�?
	 * @param dataMap
	 *            请求时携带的数据
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String makeURL(String modulePath, String functionPath,
			Map<String, String> dataMap) throws UnsupportedEncodingException {
		StringBuilder url = new StringBuilder();
		if (url.indexOf("?") < 0)
			url.append('?');
		int count = 0;
		for (String name : dataMap.keySet()) {
			if (count > 0) {
				url.append("&");
			}
			count++;
			url.append(name);
			url.append('=');
			url.append(URLEncoder.encode(String.valueOf(dataMap.get(name)),
					"UTF-8"));
		}
		return url.toString().replace("?&", "?");
	}

	private void getRequest() {
		// TODO Auto-generated method stub
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(finalUrl);
		System.out.println("finalUrl---->" + finalUrl);
		// get.setHeaders(headers);
		BasicHttpParams httpParams = new BasicHttpParams();
		ConnManagerParams.setTimeout(httpParams, SoTimeout);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
				new ConnPerRouteBean(DEFAULT_MAX_CONNECTIONS));
		ConnManagerParams.setMaxTotalConnections(httpParams,
				DEFAULT_MAX_CONNECTIONS);
		HttpConnectionParams.setSoTimeout(httpParams, SoTimeout);
		HttpConnectionParams
				.setConnectionTimeout(httpParams, ConnectionTimeout);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams,
				DEFAULT_SOCKET_BUFFER_SIZE);
		get.setParams(httpParams);
		try {
			HttpResponse response = client.execute(get);
			int StatusLine = response.getStatusLine().getStatusCode();
			String r = response.getStatusLine().toString();
			if (StatusLine == HttpStatus.SC_OK) {
				String jsonStr = EntityUtils.toString(new BufferedHttpEntity(
						response.getEntity()), "UTF-8");
				try {
					System.out.println("jsonStr---->" + finalUrl + "---->"
							+ jsonStr);
					Object obj = vo.parser.parse(jsonStr);
					if (obj != null) {
//						if (vo.isSaveToLocation) {
//							Object dbObj=vo.parser.saveData(obj,vo.dm);
//							sendSuccendMessage(dbObj, vo.uuId);
//						}else {
							sendSuccendMessage(obj, vo.uuId);
//						}
					} else {
						sendExceptionMessage(null,"数据异常", vo.uuId);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					sendExceptionMessage(e, "数据异常", vo.uuId);
				}
			} else {
				sendExceptionMessage(null,r + "....."
						+ "服务器异", vo.uuId);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			sendExceptionMessage(e, "网络异常", vo.uuId);
		} catch (IOException e) {
			e.printStackTrace();
			sendExceptionMessage(e, "网络异常", vo.uuId);
		}
	}

	/**
	 * 处理POST方式请求
	 */
	private void postRequest() {
		// TODO Auto-generated method stub
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(finalUrl);
		BasicHttpParams httpParams = new BasicHttpParams();
		ConnManagerParams.setTimeout(httpParams, SoTimeout);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
				new ConnPerRouteBean(DEFAULT_MAX_CONNECTIONS));
		ConnManagerParams.setMaxTotalConnections(httpParams,
				DEFAULT_MAX_CONNECTIONS);
		HttpConnectionParams.setSoTimeout(httpParams, SoTimeout);
		HttpConnectionParams
				.setConnectionTimeout(httpParams, ConnectionTimeout);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams,
				DEFAULT_SOCKET_BUFFER_SIZE);
		post.setParams(httpParams);
		// post.setHeaders(headers);
		try {
			if (vo.requestDataMap != null) {
				ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
				for (Map.Entry<String, String> entry : vo.requestDataMap
						.entrySet()) {
					BasicNameValuePair pair = new BasicNameValuePair(
							entry.getKey(), entry.getValue());
					pairList.add(pair);
				}
				HttpEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
				post.setEntity(entity);
			}
			HttpResponse response = client.execute(post);// 包含响应的状态和返回的结�?=
			Log.i("responseCode--->>>", response.getStatusLine().getStatusCode()+"");
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String jsonStr = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				Log.i("jsonStr--->>>", jsonStr);
				try {
					Object obj = vo.parser.parse(jsonStr);
					if (obj != null) {
//						if (vo.isSaveToLocation) {
//							Object dbObj = vo.parser.saveData(obj,vo.dm);
//							sendSuccendMessage(dbObj, vo.uuId);
//						}else {
							sendSuccendMessage(obj, vo.uuId);
//						}
					} else {
						sendExceptionMessage(null,"数据异常", vo.uuId);
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					sendExceptionMessage(e, "数据异常", vo.uuId);
				}
			}else {
				sendExceptionMessage(null,"服务器异常：", vo.uuId);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			sendExceptionMessage(e, "网络异常", vo.uuId);
		} catch (IOException e) {
			e.printStackTrace();
			sendExceptionMessage(e, "网络异常", vo.uuId);
		}
	}

	public void sendExceptionMessage(Exception e, String des, String uuId) {
		
		ServerErrorMessage exceptionMessage = new ServerErrorMessage();
		exceptionMessage.setErrormessage(e!=null?e.getMessage():"");
		exceptionMessage.setErrorDes(des);
		Message message = Message.obtain();
		message.what = EXCEPTION;
		Bundle bundle = new Bundle();
		bundle.putString("identifiy", uuId);
		message.setData(bundle);
		message.obj=exceptionMessage;
		handler.sendMessage(message);
	}

	public void sendSuccendMessage(Object obj, String uuId) {
		Message message = Message.obtain();
		message.what = SUCCEEND;
		Bundle bundle = new Bundle();
		bundle.putString("identifiy", uuId);
		message.obj = obj;
		message.setData(bundle);
		handler.sendMessage(message);
	}

	public void sendFailedMessage(String failedMessage, String uuId) {
		Message message = Message.obtain();
		message.what = FAILED;
		Bundle bundle = new Bundle();
		bundle.putString("identifiy", uuId);
		message.obj = failedMessage;
		message.setData(bundle);
		handler.sendMessage(message);
	}
}

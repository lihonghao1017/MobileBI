package com.ruisi.bi.app.bean;

import java.io.Serializable;
import java.util.HashMap;

import android.content.Context;

import com.ruisi.bi.app.parser.BaseParser;

public class RequestVo implements Serializable{
	public String uuId;
	public String Type;
	public String hostType;
	public String modulePath;// 服务器端接口模块路径
	public String functionPath;// 服务器端接口具体功能路径
	public Context context;
	public HashMap<String, String> requestDataMap;
	public BaseParser parser;
	public boolean isSaveToLocation;

}

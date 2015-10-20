package com.ruisi.bi.app.common;

/**
 * 该类提供服务器端接口
 */
public class APIContext {

	/** POST请求方式 */
	public static final String POST = "POST";

	/** SOAP请求方式 */
	public static final String SOAP = "SOAP";

	/** GET请求方式 */
	public static final String GET = "GET";

	/** ======================接口基地�?=============================== */

	public static final String HOST = "http://112.124.13.251:8081/bi/";
	
//	public static final String HOST = "http://172.16.1.32:8090/";
	
	//public static final String HOST = "http://172.16.1.140:8090/";
	
//	public static final String HOST = "http://172.16.1.193:8070/";
	public static final String Login="app/Login!login.action";
	public static final String Menu="app/Menus!topMenu.action";
	public static final String Theme="app/Subject!list.action";
	public static final String Zhibiao="app/Cube!getKpi.action";
	public static final String Weidu="app/Cube!getDim.action";
	public static final String form="app/CompView!viewTable.action";
	public static final String tu="app/CompView!viewChart.action";
	public static final String shaixuan="app/DimFilter.action";
	
}

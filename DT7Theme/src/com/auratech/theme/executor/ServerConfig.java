package com.auratech.theme.executor;


/**
 * 
 * @author Donsen
 * @version 1.0
 * @date 2015-12-17
 * @Description 服务器的一些配置参数
 * 
 */
public class ServerConfig {
	/**与服务器的时间差值*/
	public static long SERVER_TIME_DIFF = 0;
	/**应用服务器IP */
	public static String mDefaultIpAddr = "192.168.1.30";
	 /**应用服务器IP */
	public static int mTomcatPort = 45231; //端口号
	
	public static String mWebAppContext = "/webThemeManager";
	public static String mProtocol = "http://";
	
	/** 连接超时 */
	public static final int HTTP_TIME_OUT = 30000;  ///连接超时30秒
	 
	//后台服务器地址
	public static String SERVER_URL = mProtocol + mDefaultIpAddr + ":" + mTomcatPort + mWebAppContext;    
	
	
	//1获取壁纸
	public static final String USER_THEME_DATA = SERVER_URL + "/app/queryThemes";
	
}

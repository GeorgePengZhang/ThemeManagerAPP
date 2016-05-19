package com.auratech.theme.utils;


/**
 * 
 * @author Donsen
 * @version 1.0
 * @date 2015-12-17
 * @Description 服务器的一些配置参数
 * 
 */
public class HttpConfig {
	/**应用服务器IP */
	public static final String IP = "192.168.1.30";
	 /**应用服务器IP */
	public static final int PORT = 45231; //端口号
	
	public static final String WEBAPPCONTEXT = "/webThemeManager";
	
	public static final String PROTOCOL = "http://";
	
	//后台服务器地址
	public static String SERVER_URL = PROTOCOL + IP + ":" + PORT + WEBAPPCONTEXT;    
	
	public static final String PARAM_QUERY_THEMES = "/app/queryThemes";
	
	public static final String PARAM_DOWNLOAD_THEMES = "app/downloadThemes";
	
	public static final String URL_QUERY_THEMES = SERVER_URL + PARAM_QUERY_THEMES;
	
}

package com.auratech.theme.utils;


/**
 * 
 * @author Donsen
 * @version 1.0
 * @date 2015-12-17
 * @Description ��������һЩ���ò���
 * 
 */
public class HttpConfig {
	/**Ӧ�÷�����IP */
	public static final String IP = "192.168.1.30";
	 /**Ӧ�÷�����IP */
	public static final int PORT = 45231; //�˿ں�
	
	public static final String WEBAPPCONTEXT = "/webThemeManager";
	
	public static final String PROTOCOL = "http://";
	
	//��̨��������ַ
	public static String SERVER_URL = PROTOCOL + IP + ":" + PORT + WEBAPPCONTEXT;    
	
	public static final String PARAM_QUERY_THEMES = "/app/queryThemes";
	
	public static final String PARAM_DOWNLOAD_THEMES = "app/downloadThemes";
	
	public static final String URL_QUERY_THEMES = SERVER_URL + PARAM_QUERY_THEMES;
	
}

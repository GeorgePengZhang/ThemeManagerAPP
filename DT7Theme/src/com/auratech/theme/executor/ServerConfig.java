package com.auratech.theme.executor;


/**
 * 
 * @author Donsen
 * @version 1.0
 * @date 2015-12-17
 * @Description ��������һЩ���ò���
 * 
 */
public class ServerConfig {
	/**���������ʱ���ֵ*/
	public static long SERVER_TIME_DIFF = 0;
	/**Ӧ�÷�����IP */
	public static String mDefaultIpAddr = "192.168.1.30";
	 /**Ӧ�÷�����IP */
	public static int mTomcatPort = 45231; //�˿ں�
	
	public static String mWebAppContext = "/webThemeManager";
	public static String mProtocol = "http://";
	
	/** ���ӳ�ʱ */
	public static final int HTTP_TIME_OUT = 30000;  ///���ӳ�ʱ30��
	 
	//��̨��������ַ
	public static String SERVER_URL = mProtocol + mDefaultIpAddr + ":" + mTomcatPort + mWebAppContext;    
	
	
	//1��ȡ��ֽ
	public static final String USER_THEME_DATA = SERVER_URL + "/app/queryThemes";
	
}

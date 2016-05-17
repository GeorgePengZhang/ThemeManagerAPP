package com.auratech.theme.executor;

import org.apache.http.client.CookieStore;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class XHttpUtils extends HttpUtils {
	public static CookieStore cookieStore;
//	private static PreferencesCookieStore preferencesCookieStore = null;	
	private static XHttpUtils mXHTTPUtils = null;
	
	private XHttpUtils(Context context) {
		// TODO Auto-generated constructor stub
		super();
		HttpParams params = getHttpClient().getParams();
        params.setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false);
        params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
        params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        HttpConnectionParams.setSocketBufferSize(params, 1024 * 20);
		ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(30));
        ConnManagerParams.setMaxTotalConnections(params, 50);
		configRequestThreadPoolSize(15);

		configCookieStore(new XPreferencesCookieStore(MyApp.mApp));
	}
	
	private XHttpUtils() {
		super();
	}
	
	public static HttpUtils getInstance(){
		if(mXHTTPUtils == null){
			mXHTTPUtils = new XHttpUtils();  
		} 
	
		return mXHTTPUtils;
	}
	
	public static void destroyInstance(){
//		if(mXHTTPUtils != null){	
//			mXHTTPUtils.getHttpClient().getConnectionManager().shutdown();
//			
//		} 
//		mXHTTPUtils = null;
	}
	
    // ***************************************** send request *******************************************
	@Override
    public <T> HttpHandler<T> send(HttpRequest.HttpMethod method, String url,
                                   RequestCallBack<T> callBack) {
        return send(method, url, null, callBack);
    }

    @Override
    public <T> HttpHandler<T> send(HttpRequest.HttpMethod method, String url, RequestParams params,
                                   RequestCallBack<T> callBack) {
//    	//Ôö¼ÓsessionKey
//        if (params != null && TextUtils.isNotEmpty(AppConfig.user.sessionKey)) {
//        	params.addBodyParameter("sessionKey", AppConfig.user.sessionKey);
//        }
        return super.send(method, url, params, callBack);
    }
    
}

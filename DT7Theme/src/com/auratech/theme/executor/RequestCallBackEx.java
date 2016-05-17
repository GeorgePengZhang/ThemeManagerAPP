package com.auratech.theme.executor;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.auratech.theme.utils.DateTimeUtil;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 回调之前，统一在此进行鉴权
 * 
 * @Description TODO
 * @author Robin
 * @date 
 * @Copyright:
 */
public abstract class RequestCallBackEx <T> extends RequestCallBack<T>{
	//---验证会话密钥
		private boolean checkSessionKey(ResponseInfo<T> responseInfo){

			//转换成字符串
			String content = responseInfo.result + "";
			boolean pass = true;
			try {
				JSONObject jsonObject = new JSONObject(content);
				
				if (jsonObject != null){
					// 解析JSON
					int backCode      = jsonObject.optInt("ResultCode");      ///返回码
					String backInfo   = jsonObject.optString("ErrorMsg");      ///返回信息
					String serverTime = jsonObject.optString("timeNow");      ///返回时间
					
					///计算本地时间与服务器之间的差值
					Date srvDate = DateTimeUtil.parseDate(serverTime, DateTimeUtil.DEFAULTFORMAT);	
					if(srvDate != null){
						ServerConfig.SERVER_TIME_DIFF = srvDate.getTime() - new Date().getTime();
					}
								
					if (2 == backCode) {
						pass = false;
						if (TextUtils.isEmpty(backInfo)){
							backInfo = "您的帐号需要重新登录" + ":" + backInfo;
						} else {
							backInfo = "您的帐号需要重新登录";
						}
					}	
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				//时间格式错误
				e.printStackTrace();
			}
			
			return pass;
		}
			
	    public void onSuccess(ResponseInfo<T> responseInfo){
	    	//鉴权处理
	    	checkSessionKey(responseInfo);
	    	
	   		onSuccessEx(responseInfo);
	    }

	    //
	    public abstract void onSuccessEx(ResponseInfo<T> responseInfo);

	    public abstract void onFailure(HttpException error, String msg);
	    
	    
	    
}

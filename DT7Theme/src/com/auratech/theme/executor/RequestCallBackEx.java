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
 * �ص�֮ǰ��ͳһ�ڴ˽��м�Ȩ
 * 
 * @Description TODO
 * @author Robin
 * @date 
 * @Copyright:
 */
public abstract class RequestCallBackEx <T> extends RequestCallBack<T>{
	//---��֤�Ự��Կ
		private boolean checkSessionKey(ResponseInfo<T> responseInfo){

			//ת�����ַ���
			String content = responseInfo.result + "";
			boolean pass = true;
			try {
				JSONObject jsonObject = new JSONObject(content);
				
				if (jsonObject != null){
					// ����JSON
					int backCode      = jsonObject.optInt("ResultCode");      ///������
					String backInfo   = jsonObject.optString("ErrorMsg");      ///������Ϣ
					String serverTime = jsonObject.optString("timeNow");      ///����ʱ��
					
					///���㱾��ʱ���������֮��Ĳ�ֵ
					Date srvDate = DateTimeUtil.parseDate(serverTime, DateTimeUtil.DEFAULTFORMAT);	
					if(srvDate != null){
						ServerConfig.SERVER_TIME_DIFF = srvDate.getTime() - new Date().getTime();
					}
								
					if (2 == backCode) {
						pass = false;
						if (TextUtils.isEmpty(backInfo)){
							backInfo = "�����ʺ���Ҫ���µ�¼" + ":" + backInfo;
						} else {
							backInfo = "�����ʺ���Ҫ���µ�¼";
						}
					}	
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				//ʱ���ʽ����
				e.printStackTrace();
			}
			
			return pass;
		}
			
	    public void onSuccess(ResponseInfo<T> responseInfo){
	    	//��Ȩ����
	    	checkSessionKey(responseInfo);
	    	
	   		onSuccessEx(responseInfo);
	    }

	    //
	    public abstract void onSuccessEx(ResponseInfo<T> responseInfo);

	    public abstract void onFailure(HttpException error, String msg);
	    
	    
	    
}

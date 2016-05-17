package com.auratech.theme.executor;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.auratech.theme.bean.ThemeModel;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * ���⴦����
 * 
 * @Description TODO
 * @author Robin
 * @date 
 * @Copyright:
 */
public class ThemeControl {

	/**
	 * ��ȡ������Ϣ
	 * 
	 * @Description TODO
	 * @author Robin
	 * @date 
	 * @Copyright:
	 */
	public static void getThemeData(RequestCallBackEx<String> requestCallBackEx) {
		// ��װ����
		XHttpUtils.getInstance().send(HttpMethod.POST,
				ServerConfig.USER_THEME_DATA, requestCallBackEx);
		
	}

	/**
	 * �����Ƿ�ɹ�
	 * 
	 * @Description TODO
	 * @author Robin
	 * @date 
	 * @Copyright:
	 */
	public static int parseStutas(String result) {
		int stutas = 1 ;
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(result);

			int nRetCode = jsonObj.optInt("ResultCode");
			if (0 == nRetCode) {
				stutas = 0 ;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return stutas;
	}
	
	/**
	 * ��������
	 * 
	 * @Description TODO
	 * @author Robin
	 * @date 
	 * @Copyright:
	 */
	public static ArrayList<ThemeModel> parseQueryThemeList(String result) {
		ArrayList<ThemeModel> orderLst = new ArrayList<ThemeModel>();
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(result); 

			int nRetCode = jsonObj.optInt("ResultCode");
			if (0 == nRetCode) {		//����ɹ�
				JSONObject jsonResult = jsonObj.getJSONObject("Result");
				JSONArray jsonArray = jsonResult.getJSONArray("ThemeInfo") ;
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonO = (JSONObject) jsonArray.opt(i);
					ThemeModel orderModel = new ThemeModel();
					orderModel.setName(jsonO.getString("name"));
					orderModel.setPreviewPath(jsonO.getString("previewPath"));
					orderModel.setThemefile(jsonO.getString("themefile"));
					orderModel.setThumbnails(jsonO.getString("thumbnails"));
					orderLst.add(orderModel);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return orderLst;
	}
	
	

}

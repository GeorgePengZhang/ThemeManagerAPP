package com.auratech.theme.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfigManager {
	
	
	private static String getConfigText(String path) {
		String result = "";
		File file = new File(path);
		if (file.exists()) {
			FileInputStream fis = null;
			ByteArrayOutputStream baos = null;
			try {
				fis = new FileInputStream(file);
				baos = new ByteArrayOutputStream();
				
				byte[] buffer = new byte[1024];
				int len = 0;
				while((len = fis.read(buffer)) > 0) {
					baos.write(buffer, 0, len);
				}
				
				result = baos.toString("GBK");
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				closeStream(baos);
				closeStream(fis);
			}
		}
		
		return result;
	}
	
	public static ConfigBean parseConfig(String path) {
		String result = getConfigText(path);
		
		ConfigBean bean = null;
		try {
			bean = new ConfigBean();
			
			JSONObject jo = new JSONObject(result);
			int updateTime = jo.getInt("updateTime");
			
			bean.setUpdateTime(updateTime);
			
		} catch (JSONException e) {
			bean = null;
		}
		
		return bean;
	}
	
	public static void closeStream(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

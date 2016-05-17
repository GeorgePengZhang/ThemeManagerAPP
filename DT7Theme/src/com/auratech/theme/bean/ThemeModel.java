package com.auratech.theme.bean;

import java.io.Serializable;

import android.net.sip.SipRegistrationListener;

/**
 * 主题bean对象
 * 
 * @Description TODO
 * @author Robin
 * @date 
 * @Copyright:
 */
public class ThemeModel implements Serializable{
	
	private String name ;				//主题名字
	private String thumbnails ; 		//主题的默认封面path
	private String themefile ;			//主题压缩包的路径
	private String previewPath ;		//主题的预览图片路径
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getThumbnails() {
		return thumbnails;
	}
	public void setThumbnails(String thumbnails) {
		this.thumbnails = thumbnails;
	}
	public String getThemefile() {
		return themefile;
	}
	public void setThemefile(String themefile) {
		this.themefile = themefile;
	}
	public String getPreviewPath() {
		return previewPath;
	}
	public void setPreviewPath(String previewPath) {
		this.previewPath = previewPath;
	}
	
	
	
}

package com.auratech.theme.bean;

import java.io.Serializable;

import android.net.sip.SipRegistrationListener;

/**
 * ����bean����
 * 
 * @Description TODO
 * @author Robin
 * @date 
 * @Copyright:
 */
public class ThemeModel implements Serializable{
	
	private String name ;				//��������
	private String thumbnails ; 		//�����Ĭ�Ϸ���path
	private String themefile ;			//����ѹ������·��
	private String previewPath ;		//�����Ԥ��ͼƬ·��
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

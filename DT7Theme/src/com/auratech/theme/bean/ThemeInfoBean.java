package com.auratech.theme.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * �����ȡ���� bean����
 * 
 * @Description TODO
 * @author steven zhang
 * @date 
 * @Copyright:
 */
public class ThemeInfoBean implements Parcelable{
	
	public static final String RESULTCODE 	= "ResultCode";
	public static final String ErrorMsg 	= "ErrorMsg";
	public static final String TIMENOW 		= "timeNow";
	public static final String RESULT 		= "Result";
	public static final String THEMEINFO 	= "ThemeInfo";
	public static final String NAME 		= "name";
	public static final String THUMBNAILS 	= "thumbnails";
	public static final String THEMEFILE 	= "themefile";
	public static final String PREVIEWPATH	= "previewPath";
	
	private String name ;				//��������
	private String thumbnails ; 		//�����Ĭ�Ϸ���path
	private String themefile ;			//����ѹ������·��
	private String previewPath ;		//�����Ԥ��ͼƬ·��
	
	public ThemeInfoBean() {
	}
	
	public ThemeInfoBean(Parcel source) {
		name = source.readString();
		thumbnails = source.readString();
		themefile = source.readString();
		previewPath = source.readString();
	}
	
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
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(thumbnails);
		dest.writeString(themefile);
		dest.writeString(previewPath);
	}
	
	public static Parcelable.Creator<ThemeInfoBean> CREATOR = new Parcelable.Creator<ThemeInfoBean>() {

		@Override
		public ThemeInfoBean createFromParcel(Parcel source) {
			return new ThemeInfoBean(source);
		}

		@Override
		public ThemeInfoBean[] newArray(int size) {
			return new ThemeInfoBean[size];
		}
	};
}

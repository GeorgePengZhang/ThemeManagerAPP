package com.auratech.theme.utils;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;


public class DescriptionBean implements Parcelable{
	private String mTheme;
	
	private String mAuthors;
	
	private String mDesigners;
	
	private String mTitles;

	private String mDescriptions;
	
	private String mThumbnails;
	
	private String mWallPaper;
	
	private String mPath;
	
	private ArrayList<String> previews = new ArrayList<String>();
	
	public DescriptionBean() {
	}
	
	public DescriptionBean(Parcel source) {
		mTheme = source.readString();
		mAuthors = source.readString();
		mDesigners = source.readString();
		mDescriptions = source.readString();
		mThumbnails = source.readString();
		mWallPaper = source.readString();
		mPath = source.readString();
		source.readList(previews, ClassLoader.getSystemClassLoader());
	}

	public String getTheme() {
		return mTheme;
	}

	public void setTheme(String theme) {
		this.mTheme = theme;
	}

	public String getAuthors() {
		return mAuthors;
	}
	
	public void setAuthors(String authors) {
		this.mAuthors = authors;
	}
	
	public String getDesigners() {
		return mDesigners;
	}
	
	public void setDesigners(String designers) {
		this.mDesigners = designers;
	}
	
	public String getTitles() {
		return mTitles;
	}
	
	public void setTitles(String titles) {
		this.mTitles = titles;
	}
	
	public String getDescriptions() {
		return mDescriptions;
	}
	
	public void setDescriptions(String descriptions) {
		this.mDescriptions = descriptions;
	}
	
	public String getThumbnails() {
		return mThumbnails;
	}
	
	public void setThumbnails(String thumbnails) {
		this.mThumbnails = thumbnails;
	}
	
	public ArrayList<String> getPreviews() {
		return previews;
	}
	
	public void setPreviews(String preview) {
		previews.add(preview);
	}
	
	public String getPath() {
		return mPath;
	}

	public void setPath(String path) {
		if (path == null) {
			path = ThemeResouceManager.THEME_PATH;
		}
		this.mPath = path;
	}

	public String getWallPaper() {
		return mWallPaper;
	}

	public void setWallPaper(String wallPaper) {
		this.mWallPaper = wallPaper;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mTheme);
		dest.writeString(mAuthors);
		dest.writeString(mDesigners);
		dest.writeString(mDescriptions);
		dest.writeString(mThumbnails);
		dest.writeString(mWallPaper);
		dest.writeString(mPath);
		dest.writeList(previews);
	}
	
	public static Parcelable.Creator<DescriptionBean> CREATOR = new Parcelable.Creator<DescriptionBean>() {

		@Override
		public DescriptionBean createFromParcel(Parcel source) {
			return new DescriptionBean(source);
		}

		@Override
		public DescriptionBean[] newArray(int size) {
			return new DescriptionBean[size];
		}
		
	};
}

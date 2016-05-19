package com.auratech.theme.utils;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.auratech.theme.utils.ThemeResouceManager;

/**
 * @author steven Zhang
 */
public class DescriptionBean implements Parcelable{
	
	public static final String AUTHORS = "authors";
	public static final String DESIGNERS = "designers";
	public static final String TITLES = "titles";
	public static final String DESCRIPTIONS = "descriptions";
	public static final String THUMBNAILS = "thumbnails";
	public static final String WALLPAPER = "wallpaper";
	public static final String PREVIEWS = "previews";
	public static final String PREVIEW = "preview";
	public static final String FONT = "font";
	public static final String SIZE = "size";
	public static final String COLORALPHA = "colorAlpha";
	public static final String COLORRED = "colorRed";
	public static final String COLORGREEN = "colorGreen";
	public static final String COLORBLUE = "colorBlue";
	public static final String ICON = "icon";
	public static final String PADDINGLEFT = "paddingLeft";
	public static final String PADDINGTOP = "paddingTop";
	public static final String PADDINGRIGHT = "paddingRight";
	public static final String PADDINGBOTTOM = "paddingBottom";
	
	private String mTheme;
	
	private String mAuthors;
	
	private String mDesigners;
	
	private String mTitles;

	private String mDescriptions;
	
	private String mThumbnails;
	
	private String mWallPaper;
	
	private String mPath;
	
	private int mFontSize;

	private int mFontColorAlpha;

	private int mFontColorRed;
	
	private int mFontColorGreen;
	
	private int mFontColorBlue;
	
	private int mIconPadLeft;
	
	private int mIconPadTop;
	
	private int mIconPadRight;
	
	private int mIconPadBottom;
	
	
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
		
		mFontSize = source.readInt();
		mFontColorAlpha = source.readInt();
		mFontColorRed = source.readInt();
		mFontColorGreen = source.readInt();
		mFontColorBlue = source.readInt();
		
		mIconPadLeft = source.readInt();
		mIconPadTop = source.readInt();
		mIconPadRight = source.readInt();
		mIconPadBottom = source.readInt();
		
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

	public int getFontSize() {
		return mFontSize;
	}

	public void setFontSize(int fontSize) {
		this.mFontSize = fontSize;
	}

	public int getFontColorAlpha() {
		return mFontColorAlpha;
	}

	public void setFontColorAlpha(int fontColorAlpha) {
		this.mFontColorAlpha = fontColorAlpha;
	}

	public int getFontColorRed() {
		return mFontColorRed;
	}

	public void setFontColorRed(int fontColorRed) {
		this.mFontColorRed = fontColorRed;
	}

	public int getFontColorGreen() {
		return mFontColorGreen;
	}

	public void setFontColorGreen(int fontColorGreen) {
		this.mFontColorGreen = fontColorGreen;
	}

	public int getFontColorBlue() {
		return mFontColorBlue;
	}

	public void setFontColorBlue(int fontColorBlue) {
		this.mFontColorBlue = fontColorBlue;
	}

	public int getIconPadLeft() {
		return mIconPadLeft;
	}

	public void setIconPadLeft(int iconPadLeft) {
		this.mIconPadLeft = iconPadLeft;
	}

	public int getIconPadTop() {
		return mIconPadTop;
	}

	public void setIconPadTop(int iconPadTop) {
		this.mIconPadTop = iconPadTop;
	}

	public int getIconPadRight() {
		return mIconPadRight;
	}

	public void setIconPadRight(int iconPadRight) {
		this.mIconPadRight = iconPadRight;
	}

	public int getIconPadBottom() {
		return mIconPadBottom;
	}

	public void setIconPadBottom(int iconPadBottom) {
		this.mIconPadBottom = iconPadBottom;
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
		
		dest.writeInt(mFontSize);
		dest.writeInt(mFontColorAlpha);
		dest.writeInt(mFontColorRed);
		dest.writeInt(mFontColorGreen);
		dest.writeInt(mFontColorBlue);
		
		dest.writeInt(mIconPadLeft);
		dest.writeInt(mIconPadTop);
		dest.writeInt(mIconPadRight);
		dest.writeInt(mIconPadBottom);
		
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

package com.auratech.launcher;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.auratech.launcher.UtilDBOption;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
public class UtilDBOption {
	private static UtilDBOption mDuplicateHandler;
	private AssetManager mAssetManager = null;
	private Context mContext;

	public static UtilDBOption getInstance(Context context) {
	      if (mDuplicateHandler == null) {
	          mDuplicateHandler = new UtilDBOption(context);
	      }
	      return mDuplicateHandler;
	}

		public UtilDBOption(Context context) {
			this.mContext = context;
			initPath();
		}

//		private String[] getDBFile(String mDirectory) {
//			String[] tempImages = null;
//			try {
//				mAssetManager = mContext.getAssets();
//				if (null != mAssetManager) {
//					tempImages = mAssetManager.list(mDirectory);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return tempImages;
//		}

	public void initPath() {
			try {
	                           //初始化即将保存到的文件夹目录
				String mLastPath = "/data/data/com.auratech.launcher/databases/";
				File mLastFolder = new File(mLastPath);
				if (!mLastFolder.exists()) {
					mLastFolder.mkdirs();
				}
				mLastFolder = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		public  void copyFileOrDir(String path) {
		    AssetManager assetManager = mContext.getAssets();
		    String assets[] = null;
		    try {
		        assets = assetManager.list(path);
		        if (assets.length == 0) {
		      Log.e("123", "copyFileOrDir-path="+path);
		            copyFile(path);
		        } else {
		            String fullPath = "/data/data/" + mContext.getPackageName() + "/"+"databases"+"/" + path;
		            File dir = new File(fullPath);
		            if (!dir.exists())
		                dir.mkdir();
		            for (int i = 0; i < assets.length; ++i) {
		                copyFileOrDir(path + "/" + assets[i]);
		            }
		        }
		    } catch (IOException ex) {
		        Log.e("123", "I/O Exception", ex);
		    }
		}

		private void copyFile(String filename) {
		    AssetManager assetManager = mContext.getAssets();

		    InputStream in = null;
		    OutputStream out = null;
		    try {
		        in = assetManager.open(filename);
		        String newFileName = "/data/data/" + mContext.getPackageName() + "/"+"databases"+"/" + filename;
		        out = new FileOutputStream(newFileName);
		        byte[] buffer = new byte[1024];
		        int read;
		        while ((read = in.read(buffer)) != -1) {
		            out.write(buffer, 0, read);
		        }
		        in.close();
		        in = null;
		        out.flush();
		        out.close();
		        out = null;
		    } catch (Exception e) {
		        Log.e("123", e.getMessage());
		    }

		}

	



}

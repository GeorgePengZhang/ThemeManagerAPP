package com.auratech.theme.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;



public class FileUtils {
	/** 
     * sd���ĸ�Ŀ¼ 
     */  
    public static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();  
    /** 
     * �ֻ��Ļ����Ŀ¼ 
     */  
//    private static String mDataRootPath = null;  
    /** 
     * ����Image��Ŀ¼�� 
     */   
//    private final static String FOLDER_NAME = File.separator + Comm.TAG +"/ImageCache";  
      
      
    public FileUtils(Context context){  
//        mDataRootPath = context.getCacheDir().getPath();  
    }  
      
  
    /** 
     * ��ȡ����Image��Ŀ¼ 
     * @return 
     */  
    private String getStorageDirectory(){  
//    	return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?  
//                mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;  
    	return android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "aurn" +"/ImageCache";  
    }  
      
    /** 
     * ����Image�ķ�������sd���洢��sd����û�оʹ洢���ֻ�Ŀ¼ 
     * @param fileName  
     * @param bitmap    
     * @throws IOException 
     */  
    public void savaBitmap(String fileName, Bitmap bitmap) throws IOException{  
        if(bitmap == null){  
            return;  
        }  
        String path = getStorageDirectory();  
        File folderFile = new File(path);  
        if(!folderFile.exists()){  
            folderFile.mkdir();  
        }  
        File file = new File(path + File.separator + fileName);  
        file.createNewFile();  
        FileOutputStream fos = new FileOutputStream(file);  
        bitmap.compress(CompressFormat.PNG, 100, fos);  
        fos.flush();  
        fos.close();  
    }  
      
    /** 
     * ���ֻ�����sd����ȡBitmap 
     * @param fileName 
     * @return 
     */  
    public Bitmap getBitmap(String fileName){
    	BitmapFactory.Options bfOptions=new BitmapFactory.Options();
    	bfOptions.inDither=false;                     //Disable Dithering mode   
       	bfOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared   
        bfOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future   
        bfOptions.inTempStorage=new byte[1024];

	
		fileName = getStorageDirectory()+"/" +fileName;
        File file = new File(fileName);
        if(!file.exists())return null;
        FileInputStream fs=null;
        try {
        	fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
        Bitmap bmp = null;
        if(fs != null) {
        	try {
        		bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
        	} catch (IOException e) {
        		e.printStackTrace();
        	} finally { 
        		if(fs!=null) {
        			try {
        				fs.close();
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        		}
        	}
        }
        
        return bmp; 
    }  
      
    /** 
     * �ж��ļ��Ƿ���� 
     * @param fileName 
     * @return 
     */  
    public boolean isFileExists(String fileName){  
        return new File(getStorageDirectory() + File.separator + fileName).exists();  
    }  
      
    /** 
     * ��ȡ�ļ��Ĵ�С 
     * @param fileName 
     * @return 
     */   
    public long getFileSize(String fileName) {  
    	return new File(getStorageDirectory() + File.separator + fileName).length();  
    }  
      
      
    /** 
     * ɾ��SD�������ֻ��Ļ���ͼƬ��Ŀ¼ 
     */  
    public void deleteFile() {  
        File dirFile = new File(getStorageDirectory());  
        if(!dirFile.exists()){  
            return;  
        }  
        if (dirFile.isDirectory()) {  
            String[] children = dirFile.list();  
            for (int i = 0; i < children.length; i++) {  
                new File(dirFile, children[i]).delete();  
            }  
        }
        dirFile.delete();  
    }  
    
    public static Bitmap getBitMap(String path){
    	BitmapFactory.Options bfOptions=new BitmapFactory.Options();
    	bfOptions.inDither=false;                     //Disable Dithering mode   
        bfOptions.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared   
        bfOptions.inInputShareable=true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future   
        bfOptions.inTempStorage=new byte[512];
        
        File file = new File(path);
        if(!file.exists())
        	return null;
        FileInputStream fs=null;
        
        try {
        	fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
        Bitmap mBitmap = null;
        if(fs != null) {
        	try {
         	   mBitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
            } catch (IOException e) {
         	   e.printStackTrace();
            }finally{ 
                if(fs!=null) {
                    try {
                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
           
        return mBitmap;
    }
	/**
	 * ����ѹ��������Bitmap������ѹ��
	 * 
	 * @param image �ϴ��Bitmap
	 * @param size  Ŀ��ͼƬ������С(kb)
	 * @return JPEG��ʽ��Bitmap
	 */
	public static Bitmap compressImage(Bitmap image, int size) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 0-100��0����ѹ�����С,100��ʾ������ѡ�һЩ��ʽ��PNG������ģ����������ѹ�����ã���ѹ��������ݴ�ŵ�baos��
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int quality = 85;
		while (baos.toByteArray().length / 1024 > size) { // ѭ���ж����ѹ����ͼƬ�Ƿ����200kb,���ڼ���ѹ��
			// ����baos
			baos.reset();
			image.compress(Bitmap.CompressFormat.JPEG, quality, baos);// ����ѹ��quality%����ѹ��������ݴ�ŵ�baos��
			quality -= 10;// ÿ��ѹ��10%
		}
		Log.i("aaaaa", baos.toByteArray().length / 1024+"");
		
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ
		try {
			isBm.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
    
    public static Bitmap getSmallBitmap(String filePath) {  
        
        final BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;  
        BitmapFactory.decodeFile(filePath, options);  
  
        // Calculate inSampleSize  
        options.inSampleSize = calculateInSampleSize(options, 480, 800);  
  
        // Decode bitmap with inSampleSize set  
        options.inJustDecodeBounds = false;  
          
        Bitmap bm = BitmapFactory.decodeFile(filePath, options);  
        if(bm == null){  
            return  null;  
        }  
        int degree = readPictureDegree(filePath);  
        bm = rotateBitmap(bm,degree) ;  
        ByteArrayOutputStream baos = null ;  
        try{  
            baos = new ByteArrayOutputStream();  
            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);  
              
        }finally{  
            try {  
                if(baos != null)  
                    baos.close() ;  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return bm ;  
  
    }  
    
    public static Bitmap getSmallBitmap(String filePath,int width,int height) {  
    	final BitmapFactory.Options options = new BitmapFactory.Options();  
    	options.inJustDecodeBounds = true;  
    	BitmapFactory.decodeFile(filePath, options);  
    	
    	// Calculate inSampleSize  
    	options.inSampleSize = calculateInSampleSize(options, width, height);  
    	
    	// Decode bitmap with inSampleSize set  
    	options.inJustDecodeBounds = false;  
    	
    	Bitmap bm = BitmapFactory.decodeFile(filePath, options);  
    	if(bm == null){  
    		return  null;  
    	}  
    	int degree = readPictureDegree(filePath);  
    	bm = rotateBitmap(bm,degree) ; 
    	ByteArrayOutputStream baos = null ;   
    	try{  
    		baos = new ByteArrayOutputStream();  
    		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
    		
    	}finally{  
    		try {  
    			if(baos != null)  
    				baos.close() ;  
    		} catch (IOException e) {  
    			e.printStackTrace();  
    		}  
    	}  
    	return bm ;  
    }
    
    private static int calculateInSampleSize(BitmapFactory.Options options,  
            int reqWidth, int reqHeight) {  
        // Raw height and width of image  
        final int height = options.outHeight;  
        final int width = options.outWidth;  
        int inSampleSize = 1;  
  
        if (height > reqHeight || width > reqWidth) {  
  
            // Calculate ratios of height and width to requested height and  
            // width  
            final int heightRatio = Math.round((float) height / (float) reqHeight);  
            final int widthRatio = Math.round((float) width / (float) reqWidth);  
  
            // Choose the smallest ratio as inSampleSize value, this will  
            // guarantee  
            // a final image with both dimensions larger than or equal to the  
            // requested height and width.  
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;  
        }  
  
        return inSampleSize;  
    } 
    
    private static int readPictureDegree(String path) {    
        int degree  = 0;    
        try {    
                ExifInterface exifInterface = new ExifInterface(path);    
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);    
                switch (orientation) {    
                case ExifInterface.ORIENTATION_ROTATE_90:    
                        degree = 90;    
                        break;    
                case ExifInterface.ORIENTATION_ROTATE_180:    
                        degree = 180;    
                        break;    
                case ExifInterface.ORIENTATION_ROTATE_270:    
                        degree = 270;    
                        break;    
                }    
        } catch (IOException e) {    
                e.printStackTrace();    
        }    
        return degree;    
    }  
    
    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate){  
        if(bitmap == null)  
            return null ;  
          
        int w = bitmap.getWidth();  
        int h = bitmap.getHeight();  
  
        // Setting post rotate to 90  
        Matrix mtx = new Matrix();  
        mtx.postRotate(rotate);  
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);  
    } 
}

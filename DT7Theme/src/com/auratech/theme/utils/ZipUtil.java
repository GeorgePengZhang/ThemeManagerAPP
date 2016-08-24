package com.auratech.theme.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.Deflater;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtil {

//	private static final String TAG = "ZipUtil";

	private ZipFile zipFile;
	private ZipOutputStream zipOut; // ѹ��Zip
	private int bufSize; // size of bytes
	private byte[] buf;
	private int readedBytes;

	public ZipUtil() {
		this(512);
	}

	public ZipUtil(int bufSize) {
		this.bufSize = bufSize;
		this.buf = new byte[this.bufSize];
	}

	/**
	 * @param srcFile ��Ҫѹ����Ŀ¼�����ļ�
	 * @param destFile ѹ���ļ��Ĵ洢·��
	 */
	public void doZip(String srcFile, String destFile) {
		File zipDir;
		String dirName;
		zipDir = new File(srcFile);
		dirName = zipDir.getName();
		try {
			this.zipOut = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(destFile)));
			// ����ѹ����ע��
			zipOut.setComment("comment");
			// ����ѹ���ı��룬���Ҫѹ����·���������ģ���������ı���
			zipOut.setEncoding("GBK");
			// ����ѹ��
			zipOut.setMethod(ZipOutputStream.DEFLATED);
			// ѹ������Ϊ��ǿѹ������ʱ��Ҫ���ö�һ��
			zipOut.setLevel(Deflater.BEST_COMPRESSION);
			handleDir(zipDir, this.zipOut, dirName);
			this.zipOut.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * ��doZip����,�ݹ����Ŀ¼�ļ���ȡ
	 * 
	 * @param dir
	 * @param zipOut
	 * @param dirName
	 *            �����Ҫ��������¼ѹ���ļ���һ��Ŀ¼��νṹ��
	 * @throws IOException
	 */
	private void handleDir(File dir, ZipOutputStream zipOut, String dirName)
			throws IOException {
		System.out.println("����Ŀ¼��" + dir.getName());
		FileInputStream fileIn;
		File[] files;
		files = dir.listFiles();
		if (files.length == 0) {// ���Ŀ¼Ϊ��,�򵥶�����֮.
			// ZipEntry��isDirectory()������,Ŀ¼��"/"��β.
			System.out.println("ѹ���ġ�Name:" + dirName);
			this.zipOut.putNextEntry(new ZipEntry(dirName));
			this.zipOut.closeEntry();
		} else {// ���Ŀ¼��Ϊ��,��ֱ���Ŀ¼���ļ�.
			for (File fileName : files) {
				// System.out.println(fileName);
				if (fileName.isDirectory()) {
					handleDir(fileName, this.zipOut, dirName + File.separator
							+ fileName.getName() + File.separator);
				} else {
					System.out.println("ѹ���ġ�Name:" + dirName + File.separator
							+ fileName.getName());
					fileIn = new FileInputStream(fileName);
					this.zipOut.putNextEntry(new ZipEntry(dirName
							+ File.separator + fileName.getName()));
					while ((this.readedBytes = fileIn.read(this.buf)) > 0) {
						this.zipOut.write(this.buf, 0, this.readedBytes);
					}
					this.zipOut.closeEntry();
				}
			}
		}
	}

	/**
	 * ��ѹָ��zip�ļ�
	 * @param unZipfile ѹ���ļ���·��
	 * @param destFile ��ѹ����Ŀ¼��
	 */
	public void unZip(String unZipfile, String destFile) {// unZipfileName��Ҫ��ѹ��zip�ļ���
		FileOutputStream fileOut;
		File file;
		InputStream inputStream;

		try {
			this.zipFile = new ZipFile(unZipfile);

			for (Enumeration entries = this.zipFile.getEntries(); entries
					.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				file = new File(destFile + File.separator + entry.getName());
//				Log.d(TAG, "unZip:" + unZipfile + ",name:" + entry.getName());

				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
					// ���ָ���ļ���Ŀ¼������,�򴴽�֮.
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}
					parent.setReadable(true, false);
					parent.setWritable(true, false);
					parent.setExecutable(true, false);

					inputStream = zipFile.getInputStream(entry);

					fileOut = new FileOutputStream(file);
					while ((this.readedBytes = inputStream.read(this.buf)) > 0) {
						fileOut.write(this.buf, 0, this.readedBytes);
					}
					fileOut.close();
					inputStream.close();
				}
				file.setReadable(true, false);
				file.setWritable(true, false);
				file.setExecutable(true, false);
			}
			this.zipFile.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * @param zipFilePath
	 * @param resoucesPathName
	 * @return
	 */
	public InputStream getResoucesFromZip(String zipFilePath,
			String resoucesPathName) {
		InputStream is = null;
		try {
			this.zipFile = new ZipFile(zipFilePath);

//			Log.d(TAG,"getResoucesFromZip:"+zipFilePath+",resoucesPathName:"+resoucesPathName);
			ZipEntry entry = this.zipFile.getEntry(resoucesPathName);
			if (entry != null) {
				is = this.zipFile.getInputStream(entry);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} 
		return is;
	}
	
	public boolean isExist(String zipFilePath, String resoucePathName) {
		boolean ret = true;
		
		try {
			this.zipFile = new ZipFile(zipFilePath);
			
			ZipEntry entry = this.zipFile.getEntry(resoucePathName);
//			Log.d(TAG,"getResoucesFromZip:"+zipFilePath+",resoucesPathName:"+resoucePathName+",entry:"+entry);
			if (entry == null) {
				ret = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			ret = false;
		}
		
		return ret;
	}
	

	// ���û�������С
	public void setBufSize(int bufSize) {
		this.bufSize = bufSize;
	}
}

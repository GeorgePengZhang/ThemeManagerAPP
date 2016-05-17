package com.auratech.theme.utils;

import java.security.MessageDigest;

/**
 * Md5加密与校验
 * 
 */
public class Md5 {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 进行MD5加密
	 * 
	 * @param str
	 * @return
	 */
	public final static String getMD5(String str) {
		return encode(str, "MD5");
	}

	/**
	 * 检查MD5值是否相等
	 * 
	 * @param md5
	 *            MD5原始值
	 * @param str
	 *            待检测字符
	 * @return
	 */
	public final static boolean checkMD5(String md5, String str) {
		return md5.equals(encode(str, "MD5"));
	}

	/**
	 * md5加密
	 * 
	 * @param str
	 *            待加密字符串
	 * @param algorithm
	 *            算法
	 * @return
	 */
	public final static String encode(String str, String algorithm) {
		if (str == null || algorithm == null) {
			return null;
		}
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			// 16进制转换
			result = byteArrayToHexString(md.digest(str.getBytes("UTF-8")));
		} catch (Exception ex) {
		}
		return result;
	}

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b
	 *            字节数组
	 * 
	 * @return 16进制字串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * 转换byte到16进制
	 * 
	 * @param b
	 * @return
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * MD532位加密
	 * 
	 * @param b
	 * @return
	 */
	public final static String threeMDfor(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

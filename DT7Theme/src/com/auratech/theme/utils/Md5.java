package com.auratech.theme.utils;

import java.security.MessageDigest;

/**
 * Md5������У��
 * 
 */
public class Md5 {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * ����MD5����
	 * 
	 * @param str
	 * @return
	 */
	public final static String getMD5(String str) {
		return encode(str, "MD5");
	}

	/**
	 * ���MD5ֵ�Ƿ����
	 * 
	 * @param md5
	 *            MD5ԭʼֵ
	 * @param str
	 *            ������ַ�
	 * @return
	 */
	public final static boolean checkMD5(String md5, String str) {
		return md5.equals(encode(str, "MD5"));
	}

	/**
	 * md5����
	 * 
	 * @param str
	 *            �������ַ���
	 * @param algorithm
	 *            �㷨
	 * @return
	 */
	public final static String encode(String str, String algorithm) {
		if (str == null || algorithm == null) {
			return null;
		}
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			// 16����ת��
			result = byteArrayToHexString(md.digest(str.getBytes("UTF-8")));
		} catch (Exception ex) {
		}
		return result;
	}

	/**
	 * ת���ֽ�����Ϊ16�����ִ�
	 * 
	 * @param b
	 *            �ֽ�����
	 * 
	 * @return 16�����ִ�
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * ת��byte��16����
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
	 * MD532λ����
	 * 
	 * @param b
	 * @return
	 */
	public final static String threeMDfor(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','A', 'B', 'C', 'D', 'E', 'F' };
		try {
			byte[] btInput = s.getBytes();
			// ���MD5ժҪ�㷨�� MessageDigest ����
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// ʹ��ָ�����ֽڸ���ժҪ
			mdInst.update(btInput);
			// �������
			byte[] md = mdInst.digest();
			// ������ת����ʮ�����Ƶ��ַ�����ʽ
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

package com.scchuangtou.utils;

import java.security.MessageDigest;

public class MD5Utils {
	public enum MD5Type {
		MD5_16, MD5_32
	}

	public static String md5(byte[] datas, MD5Type type) {
		String result = null;
		try {
			MessageDigest mMessageDigest = MessageDigest.getInstance("MD5");
			mMessageDigest.update(datas);
			byte b[] = mMessageDigest.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			switch (type) {
			case MD5_16:
				result = buf.toString().substring(8, 24);
				break;
			case MD5_32:
				result = buf.toString();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}

package com.scchuangtou.utils;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import com.scchuangtou.config.Config;

public class DESUtils {
	private static final String ALGORITHM = "DES";

	private DESUtils() {

	}

	public static String createKey() {
		String str = UUID.randomUUID().toString();
		return MD5Utils.md5(str.getBytes(Charset.forName(Config.CHARSET)), MD5Utils.MD5Type.MD5_32);
	}

	public static String encryptStr(byte[] datasource, String password) {
		byte[] ed = encrypt(datasource, password);
		if (ed != null) {
			return Base64Utils.encode(ed);
		}
		return null;
	}

	public static byte[] encrypt(byte[] datasource, String password) {
		if (datasource == null || datasource.length == 0) {
			return null;
		}
		try {
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			SecretKey securekey = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(desKey);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, securekey, new SecureRandom());
			return cipher.doFinal(datasource);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] decryptStr(String src, String password) {
		try {
			byte[] ed = Base64Utils.decode(src);
			return decrypt(ed, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] decrypt(byte[] src, String password) {
		if (src == null || src.length == 0) {
			return null;
		}
		try {
			DESKeySpec desKey = new DESKeySpec(password.getBytes());
			SecretKey securekey = SecretKeyFactory.getInstance(ALGORITHM).generateSecret(desKey);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, securekey, new SecureRandom());
			return cipher.doFinal(src);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		String key = DESUtils.createKey();
		System.out.println("key:" + key);

		String s = "abc";
		String ed = DESUtils.encryptStr(s.getBytes(), key);
		System.out.println("encry:[" + ed + "]");
		byte[] d = DESUtils.decryptStr(ed, key);
		System.out.println("decrypt:[" + new String(d) + "]");
	}
}

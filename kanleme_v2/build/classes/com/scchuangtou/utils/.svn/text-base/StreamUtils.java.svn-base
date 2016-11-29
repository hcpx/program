package com.scchuangtou.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class StreamUtils {

	public static byte[] readInputStream(InputStream in) {
		byte[] buffer = new byte[10240];
		int len = -1;
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			while (true) {
				len = in.read(buffer);
				if (len > 0) {
					out.write(buffer, 0, len);
				} else {
					break;
				}
			}
			out.flush();
			return out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeIO(out);
		}
		return null;
	}

	public static boolean saveUrl(URL url, int timeoutMs, File file) {
		URLConnection con = null;
		InputStream is = null;
		OutputStream out = null;
		try {
			con = url.openConnection();
			con.setConnectTimeout(timeoutMs);
			con.setReadTimeout(timeoutMs);
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.connect();
			is = con.getInputStream();
			byte[] buffer = new byte[10240];
			
			out = new BufferedOutputStream(new FileOutputStream(file),buffer.length*2);
			int len = -1;
			while (true) {
				len = is.read(buffer);
				if (len > 0) {
					out.write(buffer, 0, len);
				} else {
					break;
				}
			}
			out.flush();
			return true;
		} catch (Exception t) {
			t.printStackTrace();
		} finally {
			closeIO(is);
			closeIO(out);
		}
		return false;
	}

	public static void closeIO(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
			}
			io = null;
		}
	}
}

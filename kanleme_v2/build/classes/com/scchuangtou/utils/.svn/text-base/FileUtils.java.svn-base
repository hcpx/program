package com.scchuangtou.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

public class FileUtils {
	public static boolean isImage(File file) {
		FileInputStream is = null;
		String value = null;
		try {
			is = new FileInputStream(file);
			byte[] b = new byte[3];
			is.read(b, 0, b.length);
			value = Arrays.toString(b);
		} catch (Exception e) {
		} finally {
			StreamUtils.closeIO(is);
		}
		if (value == null) {
			return false;
		}
		if ("[-1, -40, -1]".equals(value)) {// jpg
			return true;
		} else if ("[-119, 80, 78]".equals(value)) {// png
			return true;
		} else if ("[71, 73, 70]".equals(value)) {// gif
			return true;
		} else if ("[66, 77, -122]".equals(value)) {// bmp
			return true;
		} else {
			return false;
		}
	}

	public static byte[] getFileData(File file) {
		if (file == null || !file.exists()) {
			return null;
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			return StreamUtils.readInputStream(fis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			StreamUtils.closeIO(fis);
		}
		return null;
	}

}

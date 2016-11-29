package com.scchuangtou.helper;

import java.awt.Dimension;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.guoli.utils.ImageUtils;
import com.scchuangtou.config.AccountConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.servlet.MainServletContextListener;
import com.scchuangtou.utils.StreamUtils;
import com.scchuangtou.utils.StringUtils;

public class ImageHelper {
	private static String imageDir = null;

	public static class ImageClipInfo {
		public int x;
		public int y;
		public int width;
		public int height;
	}

	private static String getDir() {
		if (imageDir != null) {
			return imageDir;
		}
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.startsWith("windows")) {
			imageDir = AccountConfig.ImageDirConfig.WINDOWS;
		} else {
			imageDir = AccountConfig.ImageDirConfig.LINUX;
		}
		return imageDir;
	}

	private static void deleteFile(File file) {
		if (file == null || !file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			String[] fs = file.list();
			if (fs != null) {
				for (String f : fs) {
					deleteFile(new File(file, f));
				}
			}
		}
		file.delete();
	}

	public static File getImageFile(String key) {
		File outFile = new File(getDir(), key);
		File dir = outFile.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return outFile;
	}

	public static String getImageUrl(HttpServletRequest request, String key) throws Exception {
		if (StringUtils.emptyString(key))
			return null;
		StringBuffer sb = new StringBuffer();
		String requestUrl = request.getRequestURL().toString();
		requestUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/"));
		sb.append(requestUrl).append("/image_get?").append(AccountConfig.ImageDirConfig.REQUEST_PARAMETER_KEY)
				.append("=").append(URLEncoder.encode(key, Config.CHARSET));
		return sb.toString();
	}

	public static String getImageNameByUrl(String url) throws Exception {
		String key = url.substring(url.indexOf("=") + 1);
		key = URLDecoder.decode(key, Config.CHARSET);
		return key;
	}

	public static boolean upload(String key, MyMutiPart part, ImageClipInfo cinfo) {
		File outFile = getImageFile(key);
		File tmpFile = getImageFile(key + "_old");
		boolean flag = false;
		try {
			part.save(tmpFile);
			if (cinfo != null) {
				ImageUtils.clip(tmpFile, cinfo.x, cinfo.y, cinfo.width, cinfo.height, outFile);
			}
			flag = outFile.exists();
		} catch (Exception e) {
			flag = false;
		}
		tmpFile.delete();
		if (!flag) {
			outFile.delete();
		}
		return flag;
	}

	public static boolean upload(String key, URL url) {
		File outFile = getImageFile(key);
		return StreamUtils.saveUrl(url, 5000, outFile);
	}

	public static Dimension upload(String key, MyMutiPart part, boolean isMark) {
		Map<String, MyMutiPart> mutipart = new HashMap<>();
		mutipart.put(key, part);
		Map<String, Dimension> mDimensions = upload(mutipart, isMark);
		if (mDimensions == null) {
			return null;
		} else {
			return mDimensions.get(key);
		}
	}

	public static Map<String, Dimension> upload(Map<String, MyMutiPart> parts) {
		return upload(parts, true);
	}

	public static Map<String, Dimension> upload(Map<String, MyMutiPart> parts, boolean isMark) {
		if (parts == null || parts.isEmpty()) {
			return null;
		}
		Map<String, Dimension> mDimensions = new HashMap<>(parts.size());
		Set<String> keys = parts.keySet();
		ArrayList<File> uploadFiles = new ArrayList<>(parts.size());

		boolean isOk = true;
		File outFile, tmpFile;
		for (String key : keys) {
			outFile = getImageFile(key);
			tmpFile = getImageFile(key + "_old");
			boolean flag = false;
			try {
				parts.get(key).save(tmpFile);
				Dimension mDimension = ImageUtils.dispose(tmpFile, AccountConfig.ImageDirConfig.maxW,
						AccountConfig.ImageDirConfig.maxH, outFile, isMark ? MainServletContextListener.markImg : null);
				flag = outFile.exists();
				if (flag) {
					mDimensions.put(key, mDimension);
				}
			} catch (Exception e) {
				flag = false;
			}
			tmpFile.delete();
			uploadFiles.add(outFile);
			if (!flag) {
				isOk = false;
				break;
			}
		}
		if (!isOk) {
			for (File uploadFile : uploadFiles) {
				uploadFile.delete();
			}
			return null;
		} else {
			return mDimensions;
		}
	}

	public static void deleteByNames(List<String> keys) {
		for (String key : keys)
			new File(getDir(), key).delete();
	}

	public static void deleteByName(String key) {
		new File(getDir(), key).delete();
	}

	public static void deleteByDir(String dirName) {
		File dir = new File(getDir(), dirName);
		deleteFile(dir);
	}

}

//package com.scchuangtou.helper;
//
//import java.io.File;
//import java.io.OutputStream;
//import java.net.URL;
//
//import com.guoli.utils.ImageUtils;
//import com.qiniu.utils.Auth;
//import com.qiniu.utils.BucketManager;
//import com.scchuangtou.config.AccountConfig;
//import com.scchuangtou.http.MyMutiPart;
//import com.scchuangtou.http.UrlHttpClint;
//import com.scchuangtou.servlet.MainServletContextListener;
//import com.scchuangtou.utils.LogUtils;
//import com.scchuangtou.utils.StreamUtils;
//
//public class QiniuImageServer {
//	private static final Auth auth = Auth.create(AccountConfig.QiniuConfig.AK, AccountConfig.QiniuConfig.SK);
//
//	public static class ImageClipInfo {
//		public int x;
//		public int y;
//		public int width;
//		public int height;
//	}
//
//	private static File getTempFile(String name) {
//		File dir = new File(MainServletContextListener.projectDir, "tmp");
//		if (!dir.exists()) {
//			dir.mkdirs();
//		}
//		return new File(dir, name);
//	}
//
//	public static boolean upload(MyMutiPart part, String key) {
//		return upload(part, key, null);
//	}
//
//	public static boolean upload(MyMutiPart part, String key, ImageClipInfo clipInfo) {
//		File tempFile = getTempFile(key);
//		try {
//			part.save(tempFile);
//			return upload(tempFile, key, clipInfo);
//		} catch (Exception e) {
//			LogUtils.log(e);
//		} finally {
//			tempFile.delete();
//		}
//		return false;
//	}
//
//	public static boolean upload(URL url, String key) {
//		File tempFile = getTempFile(key);
//		try {
//			if (StreamUtils.saveUrl(url, 5000, tempFile)) {
//				return upload(tempFile, key, null);
//			}
//		} catch (Exception e) {
//			LogUtils.log(e);
//		} finally {
//			tempFile.delete();
//		}
//		return false;
//	}
//
//	private static boolean upload(File file, String key, ImageClipInfo clipInfo) throws Exception {
//		UrlHttpClint mUrlHttpClint = new UrlHttpClint(AccountConfig.QiniuConfig.UPLOAD_URL);
//		String token = auth.uploadToken(AccountConfig.QiniuConfig.BUCKET);
//		mUrlHttpClint.addPart("token", token);
//		mUrlHttpClint.addPart("key", key);
//		mUrlHttpClint.addPart("file", key, file, clipInfo);
//		mUrlHttpClint.setUploadFileListener(mUploadFileListener);
//		mUrlHttpClint.setTimeout(10 * 1000);
//		UrlHttpClint.Response r = mUrlHttpClint.post();
//		// 200 上传成功。
//		// 400 请求报文格式错误，报文构造不正确或者没有完整发送。
//		// 401 上传凭证无效。
//		// 413 上传内容长度大于 fsizeLimit 中指定的长度限制。
//		// 579 回调业务服务器失败。
//		// 599 服务端操作失败。
//		// 614 目标资源已存在。
//		boolean isok = r.statusCode == 200 || r.statusCode == 614;
//		if (!isok) {
//			if (r.data != null) {
//				LogUtils.log("upload status:" + r.statusCode + "," + new String(r.data));
//			} else {
//				LogUtils.log("upload status:" + r.statusCode);
//			}
//		}
//		return isok;
//	}
//
//	private static UrlHttpClint.UploadFileListener mUploadFileListener = new UrlHttpClint.UploadFileListener() {
//		@Override
//		public void upload(UrlHttpClint.UploadParam p, OutputStream os) throws Exception {
//			if (p.userObj instanceof ImageClipInfo) {
//				ImageClipInfo cinfo = (ImageClipInfo) p.userObj;
//				ImageUtils.clip(p.file, cinfo.x, cinfo.y, cinfo.width, cinfo.height, os);
//			} else {
//				ImageUtils.dispose(p.file, 720, 1080, os);
//			}
//		}
//	};
//
//	public static boolean delete(String key) {
//		if (key == null || key.trim().length() == 0) {
//			return false;
//		}
//		BucketManager.BucketManagerParam param = BucketManager.delete(AccountConfig.QiniuConfig.MANAGER_URL,auth, AccountConfig.QiniuConfig.BUCKET, key);
//		UrlHttpClint mUrlHttpClint = new UrlHttpClint(param.url);
//		mUrlHttpClint.addRequestProperty("Authorization", param.authorization);
//		boolean flag = false;
//		try {
//			UrlHttpClint.Response r = mUrlHttpClint.post();
//			// 200 删除成功
//			// 400 请求报文格式错误
//			// 401 管理凭证无效
//			// 599 服务端操作失败
//			// 612 待删除资源不存在
//			flag = r.statusCode == 200 || r.statusCode == 612;
//		} catch (Exception e) {
//			LogUtils.log("delete image error:" + e.getMessage());
//			flag = false;
//		}
//		return flag;
//	}
//}

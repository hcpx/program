package com.scchuangtou.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

public class UrlHttpClint {
	private final String DELIMITER = "--";
	private final String LINE = "\r\n";
	private String charset = "utf-8";

	private HashMap<String, String> params = new HashMap<String, String>();
	private Hashtable<String, UploadParam> paramFiles = new Hashtable<String, UploadParam>();
	private Hashtable<String, String> requestPropertys = new Hashtable<String, String>();
	private String url;
	private String boundary;
	private UploadFileListener mUploadFileListener;
	private int timeout = 5 * 1000;

	public UrlHttpClint(String url) {
		this.url = url;
		this.boundary = new StringBuffer("SwA").append(System.currentTimeMillis()).append("SwA").toString();
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setCharSet(String charset) {
		this.charset = charset;
	}

	public void addPart(String name, String value) {
		params.put(name, value);
	}

	public void addPart(String name, String fileName, File file) {
		addPart(name, fileName, file, null);
	}

	public void addPart(String name, String fileName, File file, Object userObj) {
		UploadParam param = new UploadParam();
		param.fileName = fileName;
		param.file = file;
		param.userObj = userObj;
		paramFiles.put(name, param);
	}

	public void addRequestProperty(String key, String value) {
		requestPropertys.put(key, value);
	}

	public void addRequestProperty(Map<String, String> requestPropertys) {
		this.requestPropertys.putAll(requestPropertys);
	}

	public void clearParam() {
		params.clear();
		paramFiles.clear();
	}

	public void setUploadFileListener(UploadFileListener mUploadFileListener) {
		this.mUploadFileListener = mUploadFileListener;
	}

	private HttpURLConnection perfromPost() {
		HttpURLConnection con = null;
		for (int i = 0; i < 3; i++) {
			try {
				con = (HttpURLConnection) (new URL(url)).openConnection();
				con.setConnectTimeout(timeout);
				con.setReadTimeout(timeout);
				con.setRequestMethod("POST");
				con.setUseCaches(false);
				con.setDoInput(true);
				con.setDoOutput(true);

				if (paramFiles.size() > 0) {
					con.addRequestProperty("Connection", "Keep-Alive");
					con.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
				} else {
					con.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				}
				Set<String> propertys = requestPropertys.keySet();
				for (String property : propertys) {
					con.addRequestProperty(property, requestPropertys.get(property));
				}
				con.connect();
				break;
			} catch (Exception e) {
				if (con != null)
					con.disconnect();
				con = null;
			}
		}
		return con;
	}

	private OutputStream getOutputStream(HttpURLConnection con) throws IOException {
		OutputStream os = con.getOutputStream();
		String acceptEncoding = con.getRequestProperty("Accept-Encoding");
		if (acceptEncoding != null) {
			acceptEncoding = acceptEncoding.toLowerCase();
			if ("gzip".equals(acceptEncoding)) {
				os = new GZIPOutputStream(os);
			}
		}
		return os;
	}

	public Response post(byte[] postData) throws Exception {
		HttpURLConnection con = perfromPost();
		if (con == null) {
			throw new IOException("can't connection " + url);
		}
		OutputStream os = null;
		try {
			os = getOutputStream(con);
			os.write(postData);
			return getResponse(con);
		} finally {
			closeIO(os);
			con.disconnect();
		}
	}

	public Response post() throws Exception {
		HttpURLConnection con = perfromPost();
		if (con == null) {
			throw new IOException("can't connection " + url);
		}
		OutputStream os = null;
		try {
			os = getOutputStream(con);

			if (paramFiles.isEmpty() && !params.isEmpty()) {
				Set<String> keys = params.keySet();
				StringBuffer postData = new StringBuffer();
				for (String key : keys) {
					if (postData.length() > 0) {
						postData.append("&");
					}
					postData.append(key);
					postData.append("=");
					postData.append(params.get(key));
				}
				os.write(postData.toString().getBytes(charset));
			} else if (!paramFiles.isEmpty()) {
				Set<String> keys = params.keySet();
				for (String key : keys) {
					writeParamData(os, key, params.get(key));
				}
				keys = paramFiles.keySet();
				for (String key : keys) {
					UploadParam p = paramFiles.get(key);
					writeParamData(os, key, p);
				}
				os.write((DELIMITER + boundary + DELIMITER + LINE).getBytes(charset));
			}
			return getResponse(con);
		} finally {
			closeIO(os);
			con.disconnect();
		}
	}

	private void writeParamData(OutputStream os, String paramName, String value) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(DELIMITER).append(boundary).append(LINE);
		sb.append("Content-Type: text/plain").append(LINE);
		sb.append("Content-Disposition: form-data; name=\"").append(paramName).append("\"").append(LINE);
		sb.append(LINE).append(value).append(LINE);
		os.write(sb.toString().getBytes(charset));
	}

	private void writeParamData(OutputStream os, String key, UploadParam p) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(DELIMITER).append(boundary).append(LINE);
		sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"; filename=\"").append(p.fileName)
				.append("\"").append(LINE);
		sb.append("Content-Type: application/octet-stream").append(LINE);
		sb.append("Content-Transfer-Encoding: binary").append(LINE);
		sb.append(LINE);
		os.write(sb.toString().getBytes(charset));

		if (mUploadFileListener != null) {
			mUploadFileListener.upload(p, os);
		} else {
			byte[] buffer = new byte[10 * 1024];
			InputStream is = null;
			try {
				is = new BufferedInputStream(new FileInputStream(p.file), buffer.length*2);
				int rlen;
				while (true) {
					rlen = is.read(buffer, 0, buffer.length);
					if (rlen == -1) {
						break;
					}
					os.write(buffer, 0, rlen);
				}
			} finally {
				closeIO(is);
			}
		}
		os.write(LINE.getBytes(charset));
		os.flush();
	}

	private Response getResponse(HttpURLConnection con) throws Exception {
		InputStream is = null;
		try {
			int responseCode = con.getResponseCode();
			Response mResponse = new Response();
			mResponse.statusCode = responseCode;

			if (responseCode >= 200 && responseCode != 204 && responseCode != 304) {
				try {
					is = con.getInputStream();
				} catch (IOException ioe) {
					is = con.getErrorStream();
				}
				mResponse.data = getResponse(is);
			}
			return mResponse;
		} finally {
			closeIO(is);
		}
	}

	private byte[] getResponse(InputStream is) throws IOException {
		if (is == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] bytes = new byte[10240];
		while (true) {
			int len = is.read(bytes);
			if (len < 0) {
				break;
			}
			baos.write(bytes, 0, len);
		}
		return baos.toByteArray();
	}

	private void closeIO(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
			}
			io = null;
		}
	}

	public static class UploadParam {
		public File file;
		public String fileName;
		public Object userObj;
	}

	public static class Response {
		public byte[] data;
		public int statusCode;
	}

	public interface UploadFileListener {
		public void upload(UploadParam p, OutputStream os) throws Exception;
	}
}

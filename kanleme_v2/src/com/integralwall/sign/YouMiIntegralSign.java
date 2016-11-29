package com.integralwall.sign;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.config.AccountConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.security.MessageDigest;
import java.security.GeneralSecurityException;

public class YouMiIntegralSign {
	public final static String[] YOUMISIGNPARAMS=new String[]{"order","app","user","chn","ad","points"};
	public final static String YOUMISIGN="sig";
	public final static String YOUMIDECOLLATOR="||";
	
	/**
	 * 签名生成算法
	 *
	 * @param HashMap<String,String>
	 *            params 请求参数集，所有参数必须已转换为字符串类型
	 * @param String
	 *            dev_server_secret 开发者在有米后台设置的密钥
	 * @return String
	 * @throws IOException
	 */
	public static String getSignature(HashMap<String, String> params, String dev_server_secret) throws IOException {
		// 先将参数以其参数名的字典序升序进行排序
		Map<String, String> sortedParams = new TreeMap<String, String>(params);

		Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();
		// 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
		StringBuilder basestring = new StringBuilder();
		for (Map.Entry<String, String> param : entrys) {
			basestring.append(param.getKey()).append("=").append(param.getValue());
		}
		basestring.append(dev_server_secret);
		// System.out.println(basestring.toString());
		// 使用MD5对待签名串求签
		byte[] bytes = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			bytes = md5.digest(basestring.toString().getBytes("UTF-8"));
		} catch (GeneralSecurityException ex) {
			throw new IOException(ex);
		}
		// 将MD5输出的二进制结果转换为小写的十六进制
		StringBuilder sign = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(bytes[i] & 0xFF);
			if (hex.length() == 1) {
				sign.append("0");
			}
			sign.append(hex);
		}
		return sign.toString();
	}

	/**
	 * 对一条完整的未签名的URL做签名，并将签名结果添加到URL的末尾
	 * 
	 * @param String
	 *            url 未做签名的完整URL
	 * @param String
	 *            dev_server_secret 签名秘钥
	 * @return String
	 * @throws IOException,
	 *             MalformedURLException
	 */
	public static String getUrlSignature(String url, String dev_server_secret)
			throws IOException, MalformedURLException {
		try {
			URL urlObj = new URL(url);
			String query = urlObj.getQuery();
			String[] params = query.split("&");
			Map<String, String> map = new HashMap<String, String>();
			for (String each : params) {
				String name = each.split("=")[0];
				String value;
				try {
					value = URLDecoder.decode(each.split("=")[1], "UTF-8");
				} catch (UnsupportedEncodingException e) {
					value = "";
				}
				map.put(name, value);
			}
			String signature = getSignature((HashMap<String, String>) map, dev_server_secret);
			return url + "&sign=" + signature;
		} catch (MalformedURLException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * 检查一条完整的包含签名参数的URL，其签名是否正确
	 * 
	 * @param String
	 *            url 已经签名的完整URL
	 * @param String
	 *            dev_server_secret 签名秘钥
	 * @return boolean
	 */
	public static boolean checkUrlSignature(String signedUrl, String dev_server_secret) {
		String urlSign = "";
		try {
			URL urlObj = new URL(signedUrl);
			String query = urlObj.getQuery();
			String[] params = query.split("&");
			Map<String, String> map = new HashMap<String, String>();
			for (String each : params) {
				String name = each.split("=")[0];
				String value;
				try {
					value = URLDecoder.decode(each.split("=")[1], "UTF-8");
				} catch (UnsupportedEncodingException e) {
					value = "";
				}
				if ("sign".equals(name)) {
					urlSign = value;
				} else {
					map.put(name, value);
				}
			}
			if ("".equals(urlSign)) {
				return false;
			} else {
				String signature = getSignature((HashMap<String, String>) map, dev_server_secret);
				return urlSign.equals(signature);
			}
		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 有米安卓签名算法验证
	 * @param url
	 * @param dev_server_secret
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean checkYoumiUrlSig(HttpServletRequest req, String dev_server_secret) throws UnsupportedEncodingException{
		boolean flage=false;
		StringBuffer sb=new StringBuffer(dev_server_secret).append(YOUMIDECOLLATOR);
		for(int i=0;i<YOUMISIGNPARAMS.length;i++){
			String param=req.getParameter(YOUMISIGNPARAMS[i]);
			param=URLDecoder.decode(param, AccountConfig.IntegralWallConfig.CHARSET);
			if(i!=YOUMISIGNPARAMS.length-1)
				sb.append(param).append(YOUMIDECOLLATOR);
			else
				sb.append(param);
		}
		String md5Str=toMD5(sb.toString()).substring(12, 20);
		if(md5Str.equals(req.getParameter(YOUMISIGN))){
			flage=true;
		}
		return flage;
	}
	
	/**
	 * 有米ios签名算法验证
	 * @param req
	 * @param dev_server_secret
	 * @return
	 * @throws IOException 
	 * @throws MalformedURLException
	 */
	public static boolean checkYoumiIosUrlSig(String url, String dev_server_secret,String sig) throws MalformedURLException, IOException{
		boolean flage=false;
		String reultSig=getUrlSignature(url,dev_server_secret);
		if(reultSig.equals(sig))
			flage=true;
		return flage;
	}
	
	/**
	 * 字符串转MD5
	 * 
	 * @param plainText
	 * @return
	 */
	private static String toMD5(String plainText) {
		StringBuffer buf = new StringBuffer("");
		try {
			// 生成实现指定摘要算法的 MessageDigest 对象。
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 使用指定的字节数组更新摘要。
			md.update(plainText.getBytes(AccountConfig.IntegralWallConfig.CHARSET));
			// 通过执行诸如填充之类的最终操作完成哈希计算。
			byte b[] = md.digest();
			// 生成具体的md5密码到buf数组
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf.toString();
	}

}

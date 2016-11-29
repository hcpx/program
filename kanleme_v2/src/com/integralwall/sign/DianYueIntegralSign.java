package com.integralwall.sign;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.config.AccountConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.utils.MD5Utils;
import com.scchuangtou.utils.StringUtils;

public class DianYueIntegralSign {
	/**
	 * 点乐安卓签名算法验证
	 * @param url
	 * @param dev_server_secret
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean checkDianYueUrlSig(HttpServletRequest req, String dev_server_secret) throws UnsupportedEncodingException{
		boolean flage=false;
		String token=req.getParameter("token");
		String time_stamp=req.getParameter("time_stamp");
		String md5Str="";
		if(!StringUtils.emptyString(token) && !StringUtils.emptyString(time_stamp)){
			StringBuffer sb=new StringBuffer(time_stamp).append(dev_server_secret);
			md5Str=toMD5(sb.toString());
		}
		if(md5Str.equals(token)){
			flage=true;
		}
		return flage;
	}
	
	public static String createDianYueOrderid(String device_id, String pack_name, String trade_type, String task_id) {
		return MD5Utils.md5((device_id + pack_name + trade_type+task_id).getBytes(Charset.forName(Config.CHARSET)),
				MD5Utils.MD5Type.MD5_16);
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

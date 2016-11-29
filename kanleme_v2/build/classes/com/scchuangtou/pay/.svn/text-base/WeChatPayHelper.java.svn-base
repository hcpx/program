package com.scchuangtou.pay;

import java.math.BigDecimal;
import java.util.Map;

import com.wechatpay.core.WeChatUtil;

/**
 * Created by SYT on 2016/3/29.
 */
public class WeChatPayHelper {
	private static long multiplyLong(float v1, float v2) {
		BigDecimal sub = new BigDecimal(Float.toString(v1));
		sub = sub.multiply(new BigDecimal(Float.toString(v2)));
		return sub.longValue();
	}

	public static WeChatPayRes pay(WeChatParam param) throws Exception {

		long assect = multiplyLong(param.price, 100);
		Map<String, String> orderInfo = WeChatUtil.getPayPreId(param.orderNo, param.body, param.notifyUrl, param.ip,
				String.valueOf(assect), param.api_key, param.app_id, param.mch_id, param.charset, param.url,
				param.api_key);
		if (orderInfo == null) {
			return null;
		}
		WeChatPayRes res = new WeChatPayRes();
		res.appid = orderInfo.get("appid");
		res.noncestr = orderInfo.get("noncestr");
		res._package = orderInfo.get("package");
		res.partnerid = orderInfo.get("partnerid");
		res.prepayid = orderInfo.get("prepayid");
		res.timestamp = orderInfo.get("timestamp");
		res.sign = orderInfo.get("sign");
		return res;
	}

	public static class WeChatPayRes {
		public String appid;
		public String partnerid;
		public String _package;
		public String noncestr;
		public String prepayid;
		public String timestamp;
		public String sign;
	}

	public static class WeChatParam {
		public String url;
		public String app_id;
		public String mch_id;
		public String api_key;
		public String notifyUrl;
		public String charset;

		public String orderNo;
		public String body;
		public float price;// 单位元
		public String ip;
	}
}

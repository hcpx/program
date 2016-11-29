package com.wechatpay.core;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jdom2.JDOMException;

import com.scchuangtou.http.UrlHttpClint;
import com.scchuangtou.utils.MD5Utils;

public class WeChatUtil {
	private static String getNonceStr() {
		Random random = new Random();
		return MD5Utils.md5(String.valueOf(random.nextInt(10000)).getBytes(), MD5Utils.MD5Type.MD5_32);
	}

	private static String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}

	/**
	 * 装配xml,生成请求prePayId所需参数
	 *
	 * @param params
	 * @return
	 */
	private static String toXml(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");

		for (Map.Entry<?, ?> param : params.entrySet()) {
			sb.append("<" + param.getKey() + ">");
			sb.append(param.getValue());
			sb.append("</" + param.getKey() + ">");
		}

		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 生成app支付签名
	 *
	 * @param p
	 * @return
	 */
	private static String genAppSign(Map<String, String> param, String api_key, String charset) {

		StringBuilder sb = new StringBuilder();

		List<String> list = new ArrayList<String>(param.keySet());
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			String key = list.get(i);

			sb.append(key);
			sb.append('=');
			sb.append(param.get(key));
			sb.append('&');
		}

		sb.append("key=");
		sb.append(api_key);
		return MD5Utils.md5(sb.toString().getBytes(Charset.forName(charset)), MD5Utils.MD5Type.MD5_32).toUpperCase();
	}

	/**
	 * @param orderNo
	 * @param body
	 * @param notifyUrl
	 * @param ip
	 * @param totalFee
	 * @return
	 * @throws Exception
	 */
	private static String genProductArgs(String orderNo, String body, String notifyUrl, String ip, String totalFee,
			String apid_key, String app_id, String mch_id, String charset) {
		String nonceStr = getNonceStr();

		LinkedHashMap<String, String> packageParams = new LinkedHashMap<>();
		packageParams.put("appid", app_id);
		packageParams.put("body", body);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonceStr);
		packageParams.put("notify_url", notifyUrl);
		packageParams.put("out_trade_no", orderNo);
		packageParams.put("spbill_create_ip", ip);
		packageParams.put("total_fee", totalFee);
		packageParams.put("trade_type", "APP");
		String sign = genAppSign(packageParams, apid_key, charset);
		packageParams.put("sign", sign);

		return toXml(packageParams);
	}

	/**
	 * 生成调用微信app支付所需参数
	 *
	 * @param prepayId
	 * @return
	 */
	private static Map<String, String> genPayReq(String prepayId, String app_id, String mch_id, String api_key,
			String charset) {
		String timeStamp = getTimeStamp();
		String nonceStr = getNonceStr();
		LinkedHashMap<String, String> signParams = new LinkedHashMap<>();
		signParams.put("appid", app_id);
		signParams.put("noncestr", nonceStr);
		signParams.put("package", "Sign=WXPay");
		signParams.put("partnerid", mch_id);
		signParams.put("prepayid", prepayId);
		signParams.put("timestamp", timeStamp);

		String sign = genAppSign(signParams, api_key, charset);
		signParams.put("sign", sign);
		return signParams;
	}

	private static float divide(float v1, float v2) {
		BigDecimal sub = new BigDecimal(Float.toString(v1));
		sub = sub.divide(new BigDecimal(Float.toString(v2)));
		return sub.floatValue();
	}

	/**
	 * 校验订单是否支付成功,返回支付的金额(单位元)
	 * 
	 * @param url
	 * @param api_key
	 * @param appid
	 * @param mch_id
	 * @param out_trade_no
	 * @param charset
	 * @return 0<=支付失败,单位元
	 * @throws Exception
	 */
	public static float getPayMoney(String url, String api_key, String appid, String mch_id, String out_trade_no,
			String charset) throws Exception {
		String nonceStr = getNonceStr();
		LinkedHashMap<String, String> signParams = new LinkedHashMap<>();
		signParams.put("appid", appid);
		signParams.put("mch_id", mch_id);
		signParams.put("out_trade_no", out_trade_no);
		signParams.put("nonce_str", nonceStr);
		String sign = genAppSign(signParams, api_key, charset);
		signParams.put("sign", sign);
		String xml = toXml(signParams);

		UrlHttpClint client = new UrlHttpClint(url);
		client.setCharSet(charset);
		UrlHttpClint.Response mResponse = client.post(xml.getBytes(charset));
		if (mResponse.data != null) {
			String xmlStr = new String(mResponse.data, Charset.forName(charset));
			Map<String, String> maps = XMLUtil.doXMLParse(xmlStr, charset);
			if (maps != null && "SUCCESS".equals(maps.get("trade_state"))) {
				return divide(Long.valueOf(maps.get("total_fee")), 100);
			}
		}
		return 0;
	}

	/**
	 * 微信支付生成预支付订单
	 * 
	 * @throws Exception
	 *
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static Map<String, String> getPayPreId(String goodOrderNo, String body, String noticeUrl, String ip,
			String totalFee, String apid_key, String app_id, String mch_id, String charset, String url, String api_key)
					throws Exception {
		String paramsXml = genProductArgs(goodOrderNo, body, noticeUrl, ip, totalFee, apid_key, app_id, mch_id,
				charset);

		UrlHttpClint clint = new UrlHttpClint(url);
		clint.setCharSet(charset);
		clint.addRequestProperty("Pragma:", "no-cache");
		clint.addRequestProperty("Cache-Control", "no-cache");
		clint.addRequestProperty("Content-Type", "text/xml");

		UrlHttpClint.Response response = clint.post(paramsXml.getBytes(charset));
		if (response.data == null) {
			return null;
		}
		String contentXml = new String(response.data, charset);

		Map<String, String> maps = XMLUtil.doXMLParse(contentXml, charset);
		if (maps != null) {
			String prePayId = maps.get("prepay_id");
			if (prePayId != null) {
				// 生成调用微信APP参数
				return WeChatUtil.genPayReq(prePayId, app_id, mch_id, api_key, charset);
			}
		}
		return maps;
	}

	/**
	 * 根据反馈回来的信息，生成签名结果
	 *
	 * @param Params
	 *            通知返回来的参数数组
	 * @param sign
	 *            比对的签名结果
	 * @return 生成的签名结果
	 */
	public static boolean getSignVeryfy(Map<String, String> params, String sign, String api_key, String charset) {
		Map<String, String> map = new HashMap<>(params);
		map.remove("sign");
		String resultSign = genAppSign(map, api_key, charset);
		return sign.equals(resultSign);
	}

}
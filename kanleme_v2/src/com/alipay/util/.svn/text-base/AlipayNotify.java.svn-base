package com.alipay.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.alipay.sign.RSA;

/* *
 *类名：AlipayNotify
 *功能：支付宝通知处理类
 *详细：处理支付宝各接口通知返回
 *版本：3.3
 *日期：2012-08-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考

 *************************注意*************************
 *调试通知返回时，可查看或改写log日志的写入TXT里的数据，来检查通知返回是否正常
 */
public class AlipayNotify {

	public static class AlipayNotifyParam {
		public String sign_type;
		public String input_charset;
		public String partner;
		public String https_verify_url;
		public String ali_public_key;
	}

	/**
	 * 验证消息是否是支付宝发出的合法消息
	 * 
	 * @param params
	 *            通知返回来的参数数组
	 * @return 验证结果
	 */
	public static boolean verify(Map<String, String> requestParams, AlipayNotifyParam param) {
		// 判断responsetTxt是否为true，isSign是否为true
		// responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
		// isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
		String responseTxt = "false";
		if (requestParams.get("notify_id") != null) {
			String notify_id = requestParams.get("notify_id");
			responseTxt = verifyResponse(param, notify_id);
		}
		if (getSignVeryfy(requestParams, param) && responseTxt.equals("true")) {
			return true;
		} else {
			return false;
		}
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
	private static boolean getSignVeryfy(Map<String, String> params, AlipayNotifyParam param) {
		String sign = "";
		if (params.get("sign") != null) {
			sign = params.get("sign");
		}
		// 过滤空值、sign与sign_type参数
		Map<String, String> sParaNew = AlipayCore.paraFilter(params);
		// 获取待签名字符串
		String preSignStr = AlipayCore.createLinkString(sParaNew);
		// 获得签名验证结果
		boolean isSign = false;
		if (param.sign_type.equals("RSA")) {
			isSign = RSA.verify(preSignStr, sign, param.ali_public_key, param.input_charset);
		}
		return isSign;
	}

	/**
	 * 获取远程服务器ATN结果,验证返回URL
	 * 
	 * @param notify_id
	 *            通知校验ID
	 * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
	 *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	 */
	private static String verifyResponse(AlipayNotifyParam param, String notify_id) {
		// 获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
		String veryfy_url = param.https_verify_url + "partner=" + param.partner + "&notify_id=" + notify_id;

		return checkUrl(veryfy_url);
	}

	/**
	 * 获取远程服务器ATN结果
	 * 
	 * @param urlvalue
	 *            指定URL路径地址
	 * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
	 *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	 */
	private static String checkUrl(String urlvalue) {
		String inputLine = "";
		BufferedReader in = null;
		try {
			int timeout = 5 * 1000;
			URL url = new URL(urlvalue);
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setConnectTimeout(timeout);
			urlConnection.setReadTimeout(timeout);
			urlConnection.setUseCaches(false);
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			inputLine = in.readLine().toString();
		} catch (Exception e) {
			inputLine = "";
		} finally {
			closeIO(in);
		}

		return inputLine;
	}

	private static void closeIO(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
			}
			io = null;
		}
	}

//	private static String getTimestamp() {
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
//
//		Calendar calendar = Calendar.getInstance();
//		return format.format(calendar.getTime());
//	}
//
//	private static String genAppSign(Map<String, String> param, String private_key, String charset) throws Exception {
//
//		StringBuilder sb = new StringBuilder();
//
//		List<String> list = new ArrayList<String>(param.keySet());
//		Collections.sort(list);
//		for (int i = 0; i < list.size(); i++) {
//			String key = list.get(i);
//
//			if (i > 0) {
//				sb.append("&");
//			}
//			sb.append(key);
//			sb.append("=");
//			sb.append(param.get(key));
//		}
//		String sign = RSA.sign(sb.toString(), private_key, charset);
//		return URLEncoder.encode(sign, charset);
//	}
//
//	public static class Param {
//		public String app_id;
//		public String charset;
//		public String out_trade_no;
//		public String private_key;
//	}
//
//	/**
//	 * 统一收单线下交易查询
//	 * @param p
//	 * @return 0<=支付失败,单位元
//	 * @throws Exception
//	 */
//	public static float getPayMoney(Param p) throws Exception {
//		HashMap<String, String> params = new HashMap<>();
//		params.put("app_id", p.app_id);
//		params.put("method", "alipay.trade.query");
//		params.put("charset", p.charset);
//		params.put("sign_type", "RSA");
//		params.put("timestamp", getTimestamp());
//		params.put("version", "1.0");
//		params.put("out_trade_no", p.out_trade_no);
//		String sign = genAppSign(params, p.private_key, p.charset);
//		params.put("sign", sign);
//
//		UrlHttpClint client = new UrlHttpClint("https://openapi.alipay.com/gateway.do");
//		client.setCharSet(p.charset);
//		Set<String> keys = params.keySet();
//		for (String key : keys) {
//			client.addPart(key, params.get(key));
//		}
//		UrlHttpClint.Response mResponse = client.post();
//		if (mResponse.data != null) {
//			String str = new String(mResponse.data, Charset.forName("GBK"));
//
//			Response res = JSON.parseObject(str, Response.class);
//			if (res != null && res.alipay_trade_query_response != null
//					&& "TRADE_SUCCESS".equals(res.alipay_trade_query_response.trade_status)) {
//				return res.alipay_trade_query_response.total_amount;
//			}
//		}
//		return 0;
//	}
//
//	private static class Response {
//		public Data alipay_trade_query_response;
//
//		public static class Data {
//			public String trade_status;
//			public float total_amount;
//
//		}
//	}
}

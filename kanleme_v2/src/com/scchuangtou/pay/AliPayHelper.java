package com.scchuangtou.pay;

import java.net.URLEncoder;

import com.alipay.sign.RSA;

/**
 * Created by SYT on 2016/3/29.
 */
public class AliPayHelper {

	public static String pay( AlipayReqParam mAlipayReqParam) throws Exception {
		StringBuffer orderInfo = new StringBuffer();
		
		// 签约合作者身份ID
		orderInfo.append("partner=\"").append(mAlipayReqParam.partner).append("\"");

		// 签约卖家支付宝账号
		orderInfo.append("&seller_id=\"").append(mAlipayReqParam.seller).append("\"");

		// 商户网站唯一订单号
		orderInfo.append("&out_trade_no=\"").append(mAlipayReqParam.orderNo).append("\"");

		// 商品名称
		orderInfo.append("&subject=\"").append(mAlipayReqParam.subject).append("\"");

		// 商品详情
		orderInfo.append("&body=\"").append(mAlipayReqParam.body).append("\"");

		// 商品金额
		orderInfo.append("&total_fee=\"").append(mAlipayReqParam.price).append("\"");

		// 服务器异步通知页面路径
		orderInfo.append("&notify_url=\"").append(mAlipayReqParam.notifyUrl).append("\"");

		// 服务接口名称， 固定值
		orderInfo.append("&service=\"mobile.securitypay.pay\"");

		// 支付类型， 固定值
		orderInfo.append("&payment_type=\"1\"");

		// 参数编码， 固定值
		orderInfo.append("&_input_charset=\"").append(mAlipayReqParam.input_charset).append("\"");

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo.append("&it_b_pay=\"").append(mAlipayReqParam.time_out).append("\"");

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		// orderInfo += "&return_url=\"" + AlipayConfig.RETURN_URL + "\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";
		

		String sign = RSA.sign(orderInfo.toString(), mAlipayReqParam.private_key, mAlipayReqParam.input_charset);

		sign = URLEncoder.encode(sign, mAlipayReqParam.input_charset);

		return orderInfo.append("&sign=\"").append(sign).append( "\"&").append("sign_type=\"RSA\"").toString();
	}
	public static class AlipayReqParam{
		public String private_key;
		public String input_charset;// 字符编码格式 目前支持 gbk 或 utf-8
		public String partner;
		public String seller;
		public String notifyUrl;
		public String time_out;
		
		public String orderNo;
		public String subject;
		public String body;
		public float price;//单位元
	}
}

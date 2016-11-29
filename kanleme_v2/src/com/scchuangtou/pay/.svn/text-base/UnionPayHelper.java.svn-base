package com.scchuangtou.pay;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.unionpay.sdk.AcpService;
import com.unionpay.sdk.SDKConfig;

/**
 * Created by SYT on 2016/3/30.
 */
public class UnionPayHelper {
	public static class UnionPayParam {
		public float price;//单位元
		public String notify_url;
		public String charset;
		public String orderNo;

		public String merId;
		public String version;
	}

	private static String formatDate(long time, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.US);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		return format.format(calendar.getTime());
	}

	private static long multiplyLong(float v1, float v2) {
		BigDecimal sub = new BigDecimal(Float.toString(v1));
		sub = sub.multiply(new BigDecimal(Float.toString(v2)));
		return sub.longValue();
	}

	public static String pay(UnionPayParam param) throws Exception {
		String time_start = formatDate(System.currentTimeMillis(), "yyyyMMddhhmmss");

		Map<String, String> contentData = new HashMap<String, String>();

		/*** 银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改 ***/
		contentData.put("version", param.version); // 版本号 全渠道默认值
		contentData.put("encoding", param.charset); // 字符集编码 可以使用UTF-8,GBK两种方式
		contentData.put("signMethod", "01"); // 签名方法 目前只支持01：RSA方式证书加密
		contentData.put("txnType", "01"); // 交易类型 01:消费
		contentData.put("txnSubType", "01"); // 交易子类 01：消费
		contentData.put("bizType", "000201"); // 填写000201
		contentData.put("channelType", "08"); // 渠道类型 08手机

		/*** 商户接入参数 ***/
		contentData.put("merId", param.merId); // 商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		contentData.put("accessType", "0"); // 接入类型，商户接入填0 ，不需修改（0：直连商户， 1： 收单机构
											// 2：平台商户）
		contentData.put("orderId", param.orderNo); // 商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则
		contentData.put("txnTime", time_start); // 订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		contentData.put("accType", "01"); // 账号类型 01：银行卡02：存折03：IC卡帐号类型(卡介质)
		contentData.put("txnAmt", String.valueOf(multiplyLong(param.price, 100))); // 交易金额
																					// 单位为分，不能带小数点
		contentData.put("currencyCode", "156"); // 境内商户固定 156 人民币
		// contentData.put("reqReserved", "透传字段"); //商户自定义保留域，交易应答时会原样返回

		// 后台通知地址（需设置为外网能访问 http
		// https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，【支付失败的交易银联不会发送后台通知】
		// 后台通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知
		// 注意:1.需设置为外网能访问，否则收不到通知 2.http https均可 3.收单后台通知后需要10秒内返回http200或302状态码
		// 4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200或302，那么银联会间隔一段时间再次发送。总共发送5次，银联后续间隔1、2、4、5
		// 分钟后会再次通知。
		// 5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d
		// 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		contentData.put("backUrl", param.notify_url);

		/** 对请求参数进行签名并发送http post请求，接收同步应答报文 **/
		Map<String, String> reqData = AcpService.sign(contentData, param.charset); // 报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		String requestAppUrl = SDKConfig.getConfig().getAppRequestUrl(); // 交易请求url从配置文件读取对应属性文件acp_sdk.properties中的
																			// acpsdk.backTransUrl
		Map<String, String> rspData = AcpService.post(reqData, requestAppUrl, param.charset); // 发送请求报文并接受同步应答（默认连接超时时间30秒，读取返回结果超时时间30秒）;这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过

		/** 对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考-------------> **/
		if (!rspData.isEmpty()) {
			if (AcpService.validate(rspData, param.charset)) {
				String respCode = rspData.get("respCode");
				if (("00").equals(respCode)) {
					return rspData.get("tn");
				}
			}
		}
		return null;
	}
}

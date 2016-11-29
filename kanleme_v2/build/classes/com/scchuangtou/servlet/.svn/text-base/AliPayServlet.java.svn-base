package com.scchuangtou.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alipay.util.AlipayNotify;
import com.scchuangtou.config.AccountConfig.AlipayConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.TopUpOrderDao;
import com.scchuangtou.model.TopUpOrderInfo;
import com.scchuangtou.utils.LogUtils;

/**
 * Created by SYT on 2016/3/15.
 */
public class AliPayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String RESPONSE_SUCCESS = "success";
	private final String RESPONSE_FAILD = "faild";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String res = null;
		StringBuffer logStr = new StringBuffer();
		try {
			logStr.append("\r\n").append("---------------------alipay start------------------------------");
			// 交易状态
			String trade_status = request.getParameter("trade_status");

			logStr.append("\r\n").append("trade_status:").append(trade_status);
			if (trade_status != null && !trade_status.equals("WAIT_BUYER_PAY")) {
				// 商户订单号
				String out_trade_no = request.getParameter("out_trade_no");
				// 卖家支付宝用户号
				String seller_id = request.getParameter("seller_id");
				// 买家支付宝
				String seller_email = request.getParameter("seller_email");
				// 交易金额
				float total_fee = Float.valueOf(request.getParameter("total_fee"));

				logStr.append("\r\n").append("out_trade_no:").append(out_trade_no);
				TopUpOrderInfo orderEntity = TopUpOrderDao.getOrderByOrderNo(out_trade_no);
				if (orderEntity == null) {
					logStr.append("\r\n").append("orderNo:").append(out_trade_no).append(" not exits");
				} else if (orderEntity.status == Config.TopUpOrderStatusType.TRADE_FINISHED) {
					logStr.append("\r\n").append("orderNo: ").append(out_trade_no).append(" is pay success");
				} else if (total_fee < orderEntity.topup_money) {
					logStr.append("\r\ntotal_fee:").append(total_fee).append(",异常订单，交易金额和支付金额不匹配");
				} else {
					if (AlipayConfig.partner.equals(seller_id) && AlipayConfig.SELLER.equals(seller_email)
							&& verify(request)) {// 验证
						if (trade_status.equals("TRADE_SUCCESS") || trade_status.equals("TRADE_FINISHED")) {
							// 支付宝交易号
							String trade_no = request.getParameter("trade_no");
							if (TopUpOrderDao.paySuccess(orderEntity, trade_no)) {
								logStr.append("\r\n").append("交易成功，成功更新数据库状态");
							} else {
								res = RESPONSE_FAILD;
								logStr.append("\r\n").append("交易失败，成功更新数据库状态");
							}
						} else {
							TopUpOrderDao.payFaild(out_trade_no);
							logStr.append("\r\n").append("交易关闭:").append(trade_status);
						}
					} else {// 验证失败
						logStr.append("\r\n").append("验证失败");
					}
				}
			}
			if (res == null)
				res = RESPONSE_SUCCESS;
			logStr.append("\r\n---------------------alipay end------------------------------");
			LogUtils.log(logStr.toString());
		} catch (Exception e) {
			res = null;
			LogUtils.log(logStr.toString(), e);
		}
		if (res != null) {
			OutputStream os = response.getOutputStream();
			os.write(res.getBytes("UTF-8"));
			os.flush();
		}
	}

	private static boolean verify(HttpServletRequest request) {
		AlipayNotify.AlipayNotifyParam param = new AlipayNotify.AlipayNotifyParam();
		param.sign_type = AlipayConfig.sign_type;
		param.input_charset = Config.CHARSET;
		param.partner = AlipayConfig.partner;
		param.https_verify_url = AlipayConfig.HTTPS_VERIFY_URL;
		param.ali_public_key = AlipayConfig.ali_public_key;

		Map<String, String> requestParams = new HashMap<String, String>();
		Map<String, String[]> rmap = request.getParameterMap();
		Set<String> keys = rmap.keySet();
		for (String key : keys) {
			String[] values = rmap.get(key);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			requestParams.put(key, valueStr);
		}
		return AlipayNotify.verify(requestParams, param);
	}
}

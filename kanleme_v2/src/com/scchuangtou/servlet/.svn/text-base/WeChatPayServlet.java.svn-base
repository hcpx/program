package com.scchuangtou.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scchuangtou.config.AccountConfig.WeChatPayConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.TopUpOrderDao;
import com.scchuangtou.model.TopUpOrderInfo;
import com.scchuangtou.utils.LogUtils;
import com.wechatpay.core.WeChatUtil;
import com.wechatpay.core.XMLUtil;

/**
 * Created by SYT on 2016/3/29.
 */
public class WeChatPayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String SUCCESS = "SUCCESS";
	private final String FAIL = "FAIL";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String res = null;
		StringBuffer logStr = new StringBuffer();
		logStr.append("\r\n").append("---------------------wechat pay start------------------------------");
		try {
			Map<String, String> params = XMLUtil.parseXml(request, Config.CHARSET);
			String return_code = params.get("return_code");
			logStr.append("\r\n").append("return_code:").append(return_code);
			if (SUCCESS.equals(return_code)) {
				String resultCode = params.get("result_code");
				String orderNo = params.get("out_trade_no");

				logStr.append("\r\n").append("resultCode:").append(resultCode);
				logStr.append("\r\n").append("out_trade_no:").append(orderNo);

				if (SUCCESS.equals(resultCode)) {

					TopUpOrderInfo orderEntity = TopUpOrderDao.getOrderByOrderNo(orderNo);
					if (orderEntity == null) {
						logStr.append("\r\n").append("orderNo:").append(orderNo).append(" not exits");
					} else if (orderEntity.status == Config.TopUpOrderStatusType.TRADE_FINISHED) {
						logStr.append("\r\n").append("orderNo: ").append(orderNo).append(" is pay success");
					} else {
						float payMoney = WeChatUtil.getPayMoney(WeChatPayConfig.ORDER_CHECK_URL,
								WeChatPayConfig.API_KEY, WeChatPayConfig.APP_ID, WeChatPayConfig.MCH_ID, orderNo,
								Config.CHARSET);
						if (payMoney >= orderEntity.topup_money) {
							String trade_no = params.get("transaction_id");
							if (TopUpOrderDao.paySuccess(orderEntity, trade_no)) {
								logStr.append("\r\n").append("交易成功，成功更新数据库状态");
							} else {
								res = FAIL;
								logStr.append("\r\n").append("交易失败，成功更新数据库状态");
							}
						} else {
							TopUpOrderDao.payFaild(orderNo);

							logStr.append("\r\n").append("payMoney: ").append(payMoney).append(",top_up_money:")
									.append(orderEntity.topup_money).append(",验证失败");
						}
					}
				} else {
					TopUpOrderDao.payFaild(orderNo);
				}
			}
			if (res == null) {
				res = SUCCESS;
			}
			logStr.append("\r\n").append("---------------------wechat pay end------------------------------");
			LogUtils.log(logStr.toString());
		} catch (Exception e) {
			res = null;
			LogUtils.log(logStr.toString(), e);
		}
		if (res != null)
			responseResult(response, res);
	}

	private void responseResult(HttpServletResponse response, String result) throws IOException {
		PrintWriter writer = response.getWriter();
		writer.print("<xml><return_code><![CDATA[" + result + "]]></return_code>" + "<return_msg><![CDATA["
				+ (result.equals(SUCCESS) ? "OK" : "FAIL") + "]]></return_msg></xml>");
		writer.flush();
	}
}

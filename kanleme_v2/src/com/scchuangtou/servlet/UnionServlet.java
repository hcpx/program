package com.scchuangtou.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scchuangtou.config.Config;
import com.scchuangtou.dao.TopUpOrderDao;
import com.scchuangtou.model.TopUpOrderInfo;
import com.scchuangtou.utils.LogUtils;
import com.unionpay.sdk.AcpService;
import com.unionpay.sdk.SDKConstants;

/**
 * Created by SYT on 2016/4/11.
 */
public class UnionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String SUCCESS = "ok";
	private final String FAILD = "FAILD";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String res = null;
		StringBuffer logStr = new StringBuffer();
		logStr.append("\r\n").append("---------------------union pay start------------------------------");
		Map<String, String> valideData = getParams(req);
		try {
			String encoding = req.getParameter(SDKConstants.param_encoding);
			// 重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
			if (!AcpService.validate(valideData, encoding)) {
				logStr.append("\r\n").append("验证签名结果[失败].");
			} else {
				logStr.append("\r\n").append("验证签名结果[成功].");
				// 【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态
				String orderId = valideData.get("orderId"); // 获取后台通知的数据，其他字段也可用类似方式获取
				float total_fee = Long.valueOf(valideData.get("txnAmt")) / 100f;

				logStr.append("\r\n").append("orderId:").append(orderId);
				logStr.append("\r\n").append("total_fee:").append(total_fee);

				TopUpOrderInfo orderEntity = TopUpOrderDao.getOrderByOrderNo(orderId);
				if (orderEntity == null) {
					logStr.append("\r\n").append("orderNo:").append(orderId).append(" not exits");
				} else if (orderEntity.status == Config.TopUpOrderStatusType.TRADE_FINISHED) {
					logStr.append("\r\n").append("orderNo: ").append(orderId).append(" is pay success");
				} else if (orderEntity.topup_money != total_fee) {
					logStr.append("\r\n").append("异常订单，交易金额和支付金额不匹配");
				} else {
					String queryId = valideData.get("queryId");
					if (TopUpOrderDao.paySuccess(orderEntity,queryId)) {
						logStr.append("\r\n").append("交易成功，成功更新数据库状态");
					} else {
						logStr.append("\r\n").append("交易失败，成功更新数据库状态");
						res = FAILD;
					}
				}
			}
			if (res == null) {
				res = SUCCESS;
			}
			logStr.append("\r\n").append("---------------------union pay end------------------------------");
		} catch (Exception e) {
			res = null;
			LogUtils.log(logStr.toString(), e);
		}
		if (res != null) {
			// 返回给银联服务器http 200 状态码
			resp.setStatus(200);
			PrintWriter writer = resp.getWriter();
			writer.print(res);
			writer.flush();
		}
	}

	private static Map<String, String> getParams(final HttpServletRequest request) throws UnsupportedEncodingException {
		String encoding = request.getParameter(SDKConstants.param_encoding);

		Map<String, String> reqParam = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				if (value != null && value.length() > 0) {
					value = new String(value.getBytes(encoding), encoding);
					reqParam.put(en, value);
				}
			}
		}
		return reqParam;
	}
}

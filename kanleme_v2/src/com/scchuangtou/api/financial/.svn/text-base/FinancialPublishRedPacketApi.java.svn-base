package com.scchuangtou.api.financial;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.RedPacketDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.FinancialPublishRedPacketReqEntity;
import com.scchuangtou.entity.FinancialPublishRedPacketResEntity;
import com.scchuangtou.utils.StringUtils;

public class FinancialPublishRedPacketApi extends BaseFinancialApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		if(StringUtils.emptyString(token)){
			BaseResEntity res = new BaseResEntity();
			res.status = Config.STATUS_TOKEN_ERROR;
			return res;
		}
		FinancialPublishRedPacketReqEntity req = JSON.parseObject(json, FinancialPublishRedPacketReqEntity.class);
		if (req == null || StringUtils.emptyString(req.password)) {
			return null;
		}
		req.financial_token=token;
		FinancialPublishRedPacketResEntity res = RedPacketDao.financialAddRedPacket(req);
		return res;
	}

}

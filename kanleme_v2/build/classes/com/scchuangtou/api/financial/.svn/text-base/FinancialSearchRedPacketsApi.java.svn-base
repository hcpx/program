package com.scchuangtou.api.financial;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.RedPacketDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.FinancialSearchRedPacketsReqEntity;
import com.scchuangtou.utils.StringUtils;

public class FinancialSearchRedPacketsApi extends BaseFinancialApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		if (StringUtils.emptyString(token)) {
			BaseResEntity res = new BaseResEntity();
			res.status = Config.STATUS_TOKEN_ERROR;
			return res;
		}
		FinancialSearchRedPacketsReqEntity req = JSON.parseObject(json, FinancialSearchRedPacketsReqEntity.class);
		if (req == null) {
			return null;
		}
		req.financial_token = token;
		return RedPacketDao.searchRedPackets(req);
	}

}

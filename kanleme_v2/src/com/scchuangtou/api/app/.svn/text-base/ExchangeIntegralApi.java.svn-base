package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.IntegralDetailDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.ExchangeIntegralReqEntity;
import com.scchuangtou.entity.ExchangeIntegralResEntity;
import com.scchuangtou.utils.StringUtils;

public class ExchangeIntegralApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		ExchangeIntegralReqEntity req = JSON.parseObject(json, ExchangeIntegralReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token)) {
			return null;
		}
		ExchangeIntegralResEntity res = IntegralDetailDao.exchangeIntegral(req);
		return res;
	}

}

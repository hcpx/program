package com.scchuangtou.api.financial;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.FinancialDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.FinancialGetGoldInfoReqEntity;
import com.scchuangtou.utils.StringUtils;

public class FinancialGetGoldInfoApi extends BaseFinancialApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (json == null) {
			return null;
		}
		if(StringUtils.emptyString(token)){
			BaseResEntity res = new BaseResEntity();
			res.status = Config.STATUS_TOKEN_ERROR;
			return res;
		}
		FinancialGetGoldInfoReqEntity reqEntity = JSON.parseObject(json, FinancialGetGoldInfoReqEntity.class);
		if (reqEntity == null || StringUtils.emptyString(reqEntity.user_id)) {
			return null;
		}
		reqEntity.financial_token = token;
		return FinancialDao.getGoldInfo(reqEntity);
	}

}

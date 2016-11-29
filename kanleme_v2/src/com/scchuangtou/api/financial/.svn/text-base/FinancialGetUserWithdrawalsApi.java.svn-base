package com.scchuangtou.api.financial;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.WithdrawalsDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetUserWithdrawalsReqEntity;
import com.scchuangtou.utils.StringUtils;

public class FinancialGetUserWithdrawalsApi extends BaseFinancialApi {

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
		GetUserWithdrawalsReqEntity reqEntity = JSON.parseObject(json, GetUserWithdrawalsReqEntity.class);
		if (reqEntity == null) {
			return null;
		}
		reqEntity.financial_token=token;
		return WithdrawalsDao.adminGetUserWithdrawals(reqEntity,request);
	}

}

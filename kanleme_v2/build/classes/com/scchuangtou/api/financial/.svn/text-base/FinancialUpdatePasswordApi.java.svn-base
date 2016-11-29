package com.scchuangtou.api.financial;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.FinancialDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.UpdateFinancialPasswordReqEntity;
import com.scchuangtou.utils.StringUtils;

public class FinancialUpdatePasswordApi extends BaseFinancialApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (json == null) {
			return null;
		}
		if (StringUtils.emptyString(token)) {
			BaseResEntity res = new BaseResEntity();
			res.status = Config.STATUS_TOKEN_ERROR;
			return res;
		}
		UpdateFinancialPasswordReqEntity reqEntity = JSON.parseObject(json, UpdateFinancialPasswordReqEntity.class);
		if (reqEntity == null || StringUtils.emptyString(reqEntity.password)
				|| StringUtils.emptyString(reqEntity.new_password)) {
			return null;
		}
		reqEntity.financial_token = token;
		return FinancialDao.updateFinancialPassword(reqEntity);
	}

}

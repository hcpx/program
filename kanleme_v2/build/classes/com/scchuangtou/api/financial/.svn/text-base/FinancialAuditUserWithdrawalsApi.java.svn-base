package com.scchuangtou.api.financial;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.WithdrawalsDao;
import com.scchuangtou.entity.AuditUserWithdrawalsReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.utils.StringUtils;

public class FinancialAuditUserWithdrawalsApi extends BaseFinancialApi {

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
		AuditUserWithdrawalsReqEntity reqEntity = JSON.parseObject(json, AuditUserWithdrawalsReqEntity.class);
		if (reqEntity == null ||StringUtils.emptyString(reqEntity.withdrawals_id)) {
			return null;
		}
		reqEntity.financial_token = token;
		String userIp = BaseApi.getClientIP(request);
		return WithdrawalsDao.auditUserWithdrawals(reqEntity, userIp);
	}

}

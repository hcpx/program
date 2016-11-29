package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.WithdrawalsDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.WithdrawalsReqEntity;
import com.scchuangtou.utils.StringUtils;

public class WithdrawalsApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (json == null) {
			return null;
		}
		WithdrawalsReqEntity req = JSON.parseObject(json, WithdrawalsReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.withdrawals_account) ||
				StringUtils.emptyString(req.trade_password)) {
			return null;
		}
		return WithdrawalsDao.userWithdrawals(req);
	}
}
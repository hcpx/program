package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.WithdrawalsDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetwithdrawalsReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetWithdrawalsApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (json == null) {
			return null;
		}
		GetwithdrawalsReqEntity req = JSON.parseObject(json, GetwithdrawalsReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token)) {
			return null;
		}
		return WithdrawalsDao.getWithdrawals(req);
	}
}
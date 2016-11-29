package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.UserDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.FindTradePassReqEntity;
import com.scchuangtou.utils.StringUtils;

public class FindTradePasswordApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		FindTradePassReqEntity req = JSON.parseObject(json, FindTradePassReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.trade_password)
				|| StringUtils.emptyString(req.verify_code)) {
			return null;
		}
		return UserDao.findTradePassword(req);
	}

}

package com.scchuangtou.api.communal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.TopUpOrderDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.CheckTopUpReqEntity;
import com.scchuangtou.utils.StringUtils;

public class CheckTopUpApi extends BaseApi {
	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);

		if (StringUtils.emptyString(json)) {
			return null;
		}

		CheckTopUpReqEntity req = JSON.parseObject(json, CheckTopUpReqEntity.class);

		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.order_number)) {
			return null;
		}

		return TopUpOrderDao.checkTopUp(req);
	}
}

package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.ActivityDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetActivitysReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetActivitysApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetActivitysReqEntity req = JSON.parseObject(json, GetActivitysReqEntity.class);
		if (req == null) {
			return null;
		}
		return ActivityDao.listActivitys(req, request);
	}

}

package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.ActivityDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetParticipationActivitysReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetParticipationActivitysApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetParticipationActivitysReqEntity req = JSON.parseObject(json, GetParticipationActivitysReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token)) {
			return null;
		}
		return ActivityDao.listParticipationActivitys(req,request);
	}

}

package com.scchuangtou.api.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.HealthProjectEventDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetHealthProjectEventDetailReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetHealthProjectEventDetailApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetHealthProjectEventDetailReqEntity req = JSON.parseObject(json, GetHealthProjectEventDetailReqEntity.class);
		if (req == null || StringUtils.emptyString(req.health_project_event_id)) {
			return null;
		}
		return HealthProjectEventDao.getHealthProjectEventDetail(req);
	}

}

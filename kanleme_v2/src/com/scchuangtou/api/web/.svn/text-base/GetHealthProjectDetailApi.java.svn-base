package com.scchuangtou.api.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.HealthProjectDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetHealthProjectDetailReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetHealthProjectDetailApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetHealthProjectDetailReqEntity req = JSON.parseObject(json, GetHealthProjectDetailReqEntity.class);
		if (req == null || StringUtils.emptyString(req.health_project_id)) {
			return null;
		}
		return HealthProjectDao.getHealthProjectDetail(req,request);
	}

}

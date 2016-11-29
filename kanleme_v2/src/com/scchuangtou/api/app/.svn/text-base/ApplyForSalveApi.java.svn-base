package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.HealthProjectEventDao;
import com.scchuangtou.entity.ApplyForSalveReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.utils.StringUtils;

public class ApplyForSalveApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		ApplyForSalveReqEntity req = JSON.parseObject(json, ApplyForSalveReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.health_project_id)
				|| StringUtils.emptyString(req.id_num) || StringUtils.emptyString(req.contact_name)
				|| StringUtils.emptyString(req.contact_phone) || StringUtils.emptyString(req.event_infomation) || req.event_infomation.length()>1000
				|| StringUtils.emptyString(req.publish_time) || StringUtils.emptyString(req.health_project_member_id)) {
			return null;
		}
	    return HealthProjectEventDao.addHealthProjectEvent(req);
	}

}

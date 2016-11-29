package com.scchuangtou.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.HealthProjectEventDao;
import com.scchuangtou.entity.AdminGetMyHealthProjectEventsReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.utils.StringUtils;

public class AdminGetMyHealthProjectEventsApi extends BaseAdminApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		AdminGetMyHealthProjectEventsReqEntity req = JSON.parseObject(json, AdminGetMyHealthProjectEventsReqEntity.class);
		if (req == null || StringUtils.emptyString(req.user_id)) {
			return null;
		}
		return HealthProjectEventDao.adminlistMyHealthProjectEvents(req,token);
	}

}

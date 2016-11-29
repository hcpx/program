package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.HealthProjectGoldChangeDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.HealthProjectMemberTopUpReqEntity;
import com.scchuangtou.utils.StringUtils;

public class HealthProjectMemberTopUpApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		HealthProjectMemberTopUpReqEntity req = JSON.parseObject(json, HealthProjectMemberTopUpReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.health_project_member_id)) {
			return null;
		}
		return HealthProjectGoldChangeDao.memberTopUp(req);
	}

}

package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.HealthProjectMemberDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.VerifyIdcarIsjoinReqEntity;
import com.scchuangtou.utils.StringUtils;

public class HealthProjectIdcarVerifyApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		VerifyIdcarIsjoinReqEntity req = JSON.parseObject(json, VerifyIdcarIsjoinReqEntity.class);
		if (req == null || StringUtils.emptyString(req.id_num) || StringUtils.emptyString(req.health_project_id)) {
			return null;
		}
		return HealthProjectMemberDao.verifyIdcarIsjoin(req);
	}

}

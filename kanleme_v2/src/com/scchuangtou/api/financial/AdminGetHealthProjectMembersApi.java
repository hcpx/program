package com.scchuangtou.api.financial;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.admin.BaseAdminApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.HealthProjectMemberDao;
import com.scchuangtou.entity.AdminGetHealthProjectMembersReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.utils.StringUtils;

public class AdminGetHealthProjectMembersApi extends BaseAdminApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		if (StringUtils.emptyString(token)) {
			BaseResEntity res = new BaseResEntity();
			res.status = Config.STATUS_TOKEN_ERROR;
			return res;
		}
		AdminGetHealthProjectMembersReqEntity req = JSON.parseObject(json, AdminGetHealthProjectMembersReqEntity.class);
		if (req == null) {
			return null;
		}
		return HealthProjectMemberDao.adminGetHealthProjectMembers(req, request, token);
	}

}

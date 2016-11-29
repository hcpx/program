package com.scchuangtou.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.UserDao;
import com.scchuangtou.entity.AdminUsersInfoReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.utils.StringUtils;

public class AdminUsersInfoApi extends BaseAdminApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		if(StringUtils.emptyString(token)){
			BaseResEntity res = new BaseResEntity();
			res.status = Config.STATUS_TOKEN_ERROR;
			return res;
		}
		AdminUsersInfoReqEntity req = JSON.parseObject(json,AdminUsersInfoReqEntity.class);
		if (req == null || StringUtils.emptyString(req.user_id)) {
			return null;
		}
		return UserDao.adminGetUserInfo(req,request, token);
	}

}

package com.scchuangtou.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.AdminDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.UpdateAdminInfoReqEntity;
import com.scchuangtou.utils.StringUtils;

public class EditAdminInfoApi extends BaseAdminApi{
	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (json == null) {
			return null;
		}
		if (StringUtils.emptyString(token)) {
			BaseResEntity res = new BaseResEntity();
			res.status = Config.STATUS_TOKEN_ERROR;
			return res;
		}
		UpdateAdminInfoReqEntity req = JSON.parseObject(json, UpdateAdminInfoReqEntity.class);
		return AdminDao.updateAdminInfo(req, token);
	}
}

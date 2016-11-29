package com.scchuangtou.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.AdminConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.AdminDao;
import com.scchuangtou.entity.AdminAddChildReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.utils.StringUtils;

public class AdminAddChildApi extends BaseAdminApi {

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
		AdminAddChildReqEntity req = JSON.parseObject(json, AdminAddChildReqEntity.class);

		if (StringUtils.emptyString(req.user_name) || StringUtils.emptyString(req.password) || req.modes == null
				|| req.modes.isEmpty()) {
			return null;
		}
		for (String mode : req.modes) {
			if (!AdminConfig.getAdminModes().containsKey(mode)) {
				return null;
			}
		}
		req.admin_token = token;
		return AdminDao.addAdminChild(req);
	}

}

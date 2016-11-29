package com.scchuangtou.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.AdminDao;
import com.scchuangtou.entity.AdminLoginReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.helper.ImageVerifyCodeHelper;
import com.scchuangtou.utils.StringUtils;

public class AdminLoginApi extends BaseAdminApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (json == null) {
			return null;
		}
		AdminLoginReqEntity req = JSON.parseObject(json, AdminLoginReqEntity.class);
		if (req == null || StringUtils.emptyString(req.user_name) || StringUtils.emptyString(req.password)) {
			return null;
		}
		if (!ImageVerifyCodeHelper.checkCode(request, req.image_verify_code)) {
			BaseResEntity res = new BaseResEntity();
			res.status = Config.STATUS_VERIFY_CODE_ERROR;
			return res;
		}
		return AdminDao.login(req.user_name, req.password);
	}

}

package com.scchuangtou.api.communal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.UserDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.RegisterReqEntity;
import com.scchuangtou.utils.StringUtils;

public class RegisterApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		RegisterReqEntity req = JSON.parseObject(json, RegisterReqEntity.class);
		if (req == null || StringUtils.emptyString(req.phone_number) || StringUtils.emptyString(req.verify_code)
				|| StringUtils.emptyString(req.login_password) || StringUtils.emptyString(req.login_password)) {
			return null;
		}
		return UserDao.register(req);
	}
}

package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.UserDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.FindPasswordReqEntity;
import com.scchuangtou.utils.StringUtils;

public class FindPasswordApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		FindPasswordReqEntity req = JSON.parseObject(json, FindPasswordReqEntity.class);
		if (req == null || StringUtils.emptyString(req.user_name) || StringUtils.emptyString(req.new_passwrod)
				|| StringUtils.emptyString(req.verify_code)) {
			return null;
		}
		return UserDao.findPassword(req);
	}

}

package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.UserDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.ThirdLoginReqEntity;
import com.scchuangtou.utils.StringUtils;

public class ThirdLoginApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		ThirdLoginReqEntity req = JSON.parseObject(json, ThirdLoginReqEntity.class);
		if (req == null || StringUtils.emptyString(req.id) || StringUtils.emptyString(req.nickname)) {
			return null;
		}
		if (req.type != Config.ThirdLoginType.THIRD_LOGIN_TYPE_SINA
				&& req.type != Config.ThirdLoginType.THIRD_LOGIN_TYPE_WECHAT
				&& req.type != Config.ThirdLoginType.THIRD_LOGIN_TYPE_QQ) {
			return null;
		}
		return UserDao.thirdLogin(request, req);
	}

}

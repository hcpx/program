package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.SiginDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetSigninInfoReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetSigninInfoApi extends BaseApi {
	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetSigninInfoReqEntity req = JSON.parseObject(json, GetSigninInfoReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token)) {
			return null;
		}
		return SiginDao.getSignInfo(req);
	}
}

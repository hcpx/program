package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.SiginDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.SignInReqEntity;
import com.scchuangtou.utils.StringUtils;

public class SignInApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if(StringUtils.emptyString(json)){
			return null;
		}
		SignInReqEntity req = JSON.parseObject(json, SignInReqEntity.class);
		if(req == null || StringUtils.emptyString(req.token)){
			return null;
		}
		
		return SiginDao.signin(req);
	}

}

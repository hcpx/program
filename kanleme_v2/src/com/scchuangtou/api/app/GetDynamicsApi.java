package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.DynamicsDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetDynamicsReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetDynamicsApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetDynamicsReqEntity req = JSON.parseObject(json, GetDynamicsReqEntity.class);
		if (req == null) {
			return null;
		}
		return DynamicsDao.listDynamics(req,request);
	}

}

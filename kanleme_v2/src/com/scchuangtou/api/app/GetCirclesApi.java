package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.CircleDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetCirclesReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetCirclesApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetCirclesReqEntity req = JSON.parseObject(json, GetCirclesReqEntity.class);
		if (req == null) {
			return null;
		}
		return CircleDao.listCircles(req,request);
	}
	
}

package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.ActivityDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetActivityDetailReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetActivityDetailApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetActivityDetailReqEntity req = JSON.parseObject(json, GetActivityDetailReqEntity.class);
		if (req == null || StringUtils.emptyString(req.activity_id)) {
			return null;
		}
		return ActivityDao.getActivityDetail(req, request);
	}

}

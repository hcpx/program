package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.HealthProjectEventDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.ParticipateHealthProjectEventReqEntity;
import com.scchuangtou.utils.StringUtils;

public class ParticipateHealthProjectEventApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		ParticipateHealthProjectEventReqEntity req = JSON.parseObject(json, ParticipateHealthProjectEventReqEntity.class);
		if (req == null) {
			return null;
		}
		return HealthProjectEventDao.participateHealthProjectEvents(req,request);
	}

}

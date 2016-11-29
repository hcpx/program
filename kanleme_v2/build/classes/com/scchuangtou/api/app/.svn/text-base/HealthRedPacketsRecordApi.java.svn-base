package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.HealthProjectGoldChangeDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.HealthRedPacketsRecordReqEntity;
import com.scchuangtou.utils.StringUtils;

public class HealthRedPacketsRecordApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		HealthRedPacketsRecordReqEntity req = JSON.parseObject(json, HealthRedPacketsRecordReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token)) {
			return null;
		}
		return HealthProjectGoldChangeDao.healthRedPacketsRecord(req);
	}

}

package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.HealthProjectGoldChangeDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.PayRecordReqEntity;
import com.scchuangtou.utils.StringUtils;

public class HealthMemberTopupRecordApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		PayRecordReqEntity req = JSON.parseObject(json, PayRecordReqEntity.class);
		if (req == null || StringUtils.emptyString(req.health_project_member_id) || StringUtils.emptyString(req.token)) {
			return null;
		}
		return HealthProjectGoldChangeDao.listPayRecord(req);
	}
}

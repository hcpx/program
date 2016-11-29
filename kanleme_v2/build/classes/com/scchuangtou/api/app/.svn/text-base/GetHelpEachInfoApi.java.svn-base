package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.ProjectDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetHelpEachInfoReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetHelpEachInfoApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetHelpEachInfoReqEntity req = JSON.parseObject(json, GetHelpEachInfoReqEntity.class);
		if (req == null || StringUtils.emptyString(req.help_each_id)) {
			return null;
		}
		return ProjectDao.getHelpEachInfo(req,request);
	}

}

package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.ProjectDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetHelpEachsReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetHelpEachsApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetHelpEachsReqEntity req = JSON.parseObject(json, GetHelpEachsReqEntity.class);
		if (req == null) {
			return null;
		}
		return ProjectDao.listProjects(req,request);
	}

}

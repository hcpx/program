package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.ProjectDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetCollectHelpEachsReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetCollectHelpEachsApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetCollectHelpEachsReqEntity req = JSON.parseObject(json, GetCollectHelpEachsReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token)
				|| !(req.searchType == Config.HelpEachTSearch.PROJECT_SEARCH_PROCEED
						|| req.searchType == Config.HelpEachTSearch.PROJECT_SEARCH_SOLDOUT)) {
			return null;
		}
		return ProjectDao.listCollectProjects(req, request);
	}

}

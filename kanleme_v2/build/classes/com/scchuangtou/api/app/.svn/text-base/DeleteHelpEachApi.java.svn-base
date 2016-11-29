package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.ProjectDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.DeleteHelpEachReqEntity;
import com.scchuangtou.utils.StringUtils;

public class DeleteHelpEachApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		DeleteHelpEachReqEntity req = JSON.parseObject(json, DeleteHelpEachReqEntity.class);
		if (req == null || StringUtils.emptyString(req.help_each_id) || StringUtils.emptyString(req.token)) {
			return null;
		}
		return ProjectDao.deleteHelpEach(req);
	}

}

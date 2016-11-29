package com.scchuangtou.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.FeedBackDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.ListFeedbackReqEntity;
import com.scchuangtou.utils.StringUtils;

public class AdminGetFeedBackApi extends BaseAdminApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		if (StringUtils.emptyString(token)) {
			BaseResEntity res = new BaseResEntity();
			res.status = Config.STATUS_TOKEN_ERROR;
			return res;
		}
		ListFeedbackReqEntity req = JSON.parseObject(json, ListFeedbackReqEntity.class);
		if (req == null) {
			return null;
		}
		req.admin_token = token;
		return FeedBackDao.adminListFeedBack(request, req);
	}

}

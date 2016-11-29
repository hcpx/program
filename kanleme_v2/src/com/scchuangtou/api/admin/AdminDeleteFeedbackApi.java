package com.scchuangtou.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.FeedBackDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.DeleteFeedbackReqEntity;
import com.scchuangtou.utils.StringUtils;

public class AdminDeleteFeedbackApi extends BaseAdminApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (json == null) {
			return null;
		}
		if (StringUtils.emptyString(token)) {
			BaseResEntity res = new BaseResEntity();
			res.status = Config.STATUS_TOKEN_ERROR;
			return res;
		}
		DeleteFeedbackReqEntity req = JSON.parseObject(json, DeleteFeedbackReqEntity.class);
		if (req.feed_back_ids == null || req.feed_back_ids.isEmpty()) {
			return null;
		}
		return FeedBackDao.adminDeleteFeedBack(req, token);
	}

}

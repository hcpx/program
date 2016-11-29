package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.admin.BaseAdminApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.CircleUserDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.JoinCircleReqEntity;
import com.scchuangtou.utils.StringUtils;

public class JoinCircleApi extends BaseAdminApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		JoinCircleReqEntity req = JSON.parseObject(json, JoinCircleReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.circle_id)) {
			return null;
		}
		return CircleUserDao.joinCircle(req);
	}

}

package com.scchuangtou.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.CircleDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetCirclesReqEntity;
import com.scchuangtou.utils.StringUtils;

public class AdminGetCircles extends BaseAdminApi {

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
		GetCirclesReqEntity req = JSON.parseObject(json, GetCirclesReqEntity.class);
		if (req == null) {
			return null;
		}
		return CircleDao.adminGetCircles(req,request);
	}

}

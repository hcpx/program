package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.CircleUserDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.QuitCircleReqEntity;
import com.scchuangtou.utils.StringUtils;

public class QuitCircleApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		QuitCircleReqEntity req = JSON.parseObject(json, QuitCircleReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.circle_id)) {
			return null;
		}
		return CircleUserDao.quitCircle(req);
	}

}

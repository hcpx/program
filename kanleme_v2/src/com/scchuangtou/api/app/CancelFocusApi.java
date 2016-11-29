package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.FocusDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.FocusReqEntity;
import com.scchuangtou.utils.StringUtils;

public class CancelFocusApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		FocusReqEntity req = JSON.parseObject(json, FocusReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.focused_user_id)) {
			return null;
		}
		return FocusDao.cancelFocus(req.token, req.focused_user_id);
	}
	

}

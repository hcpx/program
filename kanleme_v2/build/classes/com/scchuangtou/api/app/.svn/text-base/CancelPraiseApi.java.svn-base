package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.PraiseDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.PraiseReqEntity;
import com.scchuangtou.utils.StringUtils;

public class CancelPraiseApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		PraiseReqEntity req = JSON.parseObject(json, PraiseReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.praise_object_id)) {
			return null;
		}
		return PraiseDao.cancelPraise(req.token, req.praise_object_id, req.praise_object_type);
	}

}

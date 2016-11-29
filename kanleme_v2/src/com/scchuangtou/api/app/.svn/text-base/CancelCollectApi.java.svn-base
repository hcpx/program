package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.CollectDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.CollectReqEntity;
import com.scchuangtou.utils.StringUtils;

public class CancelCollectApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		CollectReqEntity req = JSON.parseObject(json, CollectReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.collect_object_id)) {
			return null;
		}
		return CollectDao.cancelCollect(req.token, req.collect_object_id, req.collect_object_type);
	}

}

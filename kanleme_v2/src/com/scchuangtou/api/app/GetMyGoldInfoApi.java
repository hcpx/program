package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.GoldNotesDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetMyGoldInfoReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetMyGoldInfoApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetMyGoldInfoReqEntity req = JSON.parseObject(json, GetMyGoldInfoReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token)) {
			return null;
		}
		return GoldNotesDao.getMyInfo(req);
	}

}

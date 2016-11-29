package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.GoldNotesDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetGoldInfoReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetGoldInfoApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetGoldInfoReqEntity req = JSON.parseObject(json, GetGoldInfoReqEntity.class);
		if (req == null || StringUtils.emptyString(req.change_desc)) {
			return null;
		}
		return GoldNotesDao.getInfo(request, req);
	}

}

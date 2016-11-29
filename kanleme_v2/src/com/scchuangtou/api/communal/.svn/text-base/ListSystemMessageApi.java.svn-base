package com.scchuangtou.api.communal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.SystemMessageDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.ListSystemMsgReqEntity;

public class ListSystemMessageApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (json == null) {
			return null;
		}
		ListSystemMsgReqEntity req = JSON.parseObject(json, ListSystemMsgReqEntity.class);
		if (req == null) {
			return null;
		}
		return SystemMessageDao.listSystemMessages(req);
	}

}

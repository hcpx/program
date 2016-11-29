package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.RedPacketDetailDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.ReceiveRedPacketsReqEntity;
import com.scchuangtou.utils.StringUtils;

public class ReceiveRedPacketsApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		ReceiveRedPacketsReqEntity req = JSON.parseObject(json, ReceiveRedPacketsReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token)) {
			return null;
		}
		return RedPacketDetailDao.listReceiveRedPackets(req);
	}

}

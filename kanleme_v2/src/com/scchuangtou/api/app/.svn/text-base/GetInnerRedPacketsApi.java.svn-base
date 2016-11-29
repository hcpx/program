package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.RedPacketDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetInnerRedPacketsReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetInnerRedPacketsApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetInnerRedPacketsReqEntity req = JSON.parseObject(json, GetInnerRedPacketsReqEntity.class);
		if (req == null) {
			return null;
		}
		return RedPacketDao.listInnerRedPackets(req,request);
	}

}

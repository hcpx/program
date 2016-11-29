package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.RedPacketDetailDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.RankListPacketsReqEntity;
import com.scchuangtou.utils.StringUtils;

public class RankListPacketsApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		RankListPacketsReqEntity req = JSON.parseObject(json, RankListPacketsReqEntity.class);
		if (req == null || StringUtils.emptyString(req.red_packet_id)) {
			return null;
		}
		return RedPacketDetailDao.rankRedPacketDetail(req,request);
	}

}

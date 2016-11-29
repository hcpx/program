package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.RewardDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.RewardReqEntity;
import com.scchuangtou.utils.StringUtils;

public class RewardApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		RewardReqEntity req = JSON.parseObject(json, RewardReqEntity.class);
		req.reward_gold = Config.parseGold(req.reward_gold);
		if (StringUtils.emptyString(req.token) || StringUtils.emptyString(req.reward_object_id)
				|| req.reward_gold <= 0) {
			return null;
		}
		return RewardDao.reward(req);
	}

}

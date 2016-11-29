package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.CapitalSourceDao;
import com.scchuangtou.entity.AddCapitalSourceReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.utils.StringUtils;

public class AddCapitalSourceApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		AddCapitalSourceReqEntity req = JSON.parseObject(json, AddCapitalSourceReqEntity.class);

		if (req == null || StringUtils.emptyString(req.source_name) || StringUtils.emptyString(req.token)
				|| StringUtils.emptyString(req.source_num)) {
			return null;
		}
		return CapitalSourceDao.addCapitalSource(req);
	}

}

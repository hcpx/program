package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.ProveDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.ProveReqEntity;
import com.scchuangtou.utils.StringUtils;

public class ProveApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		ProveReqEntity req = JSON.parseObject(json, ProveReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.help_each_id)
				|| StringUtils.emptyString(req.relation) || StringUtils.emptyString(req.name)
				|| StringUtils.emptyString(req.id_num) || StringUtils.emptyString(req.detail)) {
			return null;
		}
		int relationlen = req.relation.trim().length();
		int namelen = req.name.trim().length();
		int detaillen = req.detail.trim().length();
		if (relationlen < 2 || relationlen > 10 || namelen < 2 || namelen > 10 || detaillen < 10 || detaillen > 200)
			return null;
		return ProveDao.Prove(req);
	}

}

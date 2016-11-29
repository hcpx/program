package com.scchuangtou.api.communal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.SubjectDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetSubjectsReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetSubjectsApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetSubjectsReqEntity req = JSON.parseObject(json, GetSubjectsReqEntity.class);
		if (req == null) {
			return null;
		}
		return SubjectDao.listSubjects(request,req);
	}

}

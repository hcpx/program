package com.scchuangtou.api.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.SubjectDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetSubjectInfoReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetSubjectInfoApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetSubjectInfoReqEntity req = JSON.parseObject(json, GetSubjectInfoReqEntity.class);
		if (req == null || StringUtils.emptyString(req.subject_id)) {
			return null;
		}
		return SubjectDao.getSubjectInfo(request,req);
	}

}

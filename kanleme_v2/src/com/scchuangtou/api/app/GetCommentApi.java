package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.CommentDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetCommentReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetCommentApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetCommentReqEntity req = JSON.parseObject(json, GetCommentReqEntity.class);
		if (req == null || StringUtils.emptyString(req.object_id)) {
			return null;
		}

		return CommentDao.getComments(request, req);
	}

}

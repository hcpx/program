package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.CommentDao;
import com.scchuangtou.entity.AddCommentReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.utils.StringUtils;

public class CommentApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		AddCommentReqEntity req = JSON.parseObject(json, AddCommentReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.comment_object_id)
				|| StringUtils.emptyString(req.comment_content)) {
			return null;
		}
		req.comment_content = req.comment_content.trim();
		if(req.comment_content.length() > 200){
			return null;
		}
		return CommentDao.addComment(req);
	}

}

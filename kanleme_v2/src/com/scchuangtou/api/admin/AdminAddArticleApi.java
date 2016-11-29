package com.scchuangtou.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.AdminDao;
import com.scchuangtou.entity.AdminAddArticleReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.utils.StringUtils;

public class AdminAddArticleApi extends BaseAdminApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (json == null) {
			return null;
		}
		if (StringUtils.emptyString(token)) {
			BaseResEntity res = new BaseResEntity();
			res.status = Config.STATUS_TOKEN_ERROR;
			return res;
		}

		AdminAddArticleReqEntity req = JSON.parseObject(json, AdminAddArticleReqEntity.class);
		if (StringUtils.emptyString(req.title) || StringUtils.emptyString(req.article_content)
				|| req.article_label == null || req.article_label.length == 0 || req.article_type == null
				|| req.article_type.length == 0) {
			return null;
		}
		return AdminDao.addArticle(req, request, token);
	}

}

package com.scchuangtou.api.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.ArticleDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetArticleInfoReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetArticleInfoApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetArticleInfoReqEntity req = JSON.parseObject(json, GetArticleInfoReqEntity.class);
		if (req == null || StringUtils.emptyString(req.article_id)) {
			return null;
		}
		return ArticleDao.getArticleInfo(req,request);
	}

}

package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.ArticleDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetNoCircleArticlesReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetNoCircleArticlesApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetNoCircleArticlesReqEntity req = JSON.parseObject(json, GetNoCircleArticlesReqEntity.class);
		if (req == null) {
			return null;
		}
		return ArticleDao.listNoCircleArticles(req,request);
	}

}

package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.ArticleDao;
import com.scchuangtou.entity.AddArticleReqEntity;
import com.scchuangtou.entity.BaseArticleReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.utils.StringUtils;

public class AddArticleApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		AddArticleReqEntity req = JSON.parseObject(json, AddArticleReqEntity.class);

		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.title)
				|| StringUtils.emptyString(req.article_content) || req.article_type == null
				|| req.article_label == null) {
			return null;
		}
		int typelen = req.article_type.length;
		int labellen = req.article_label.length;
		int titlelen = req.title.trim().length();
		int contentlen = req.article_content.trim().length();
		if (typelen < 1 || typelen > 3 || labellen < 1 || labellen > 5 || titlelen < 2 || titlelen > 50
				|| contentlen < 8 || contentlen > 1000)
			return null;
		if (req.article_pic_desc != null) {
			if (req.article_pic_desc.length > 8) {
				return null;
			}
			for (BaseArticleReqEntity.Pic pic:req.article_pic_desc) {
				if (!StringUtils.emptyString(pic.article_pic_des)
						&& pic.article_pic_des.trim().length() > 200) {
					return null;
				}
			}
		}
		return ArticleDao.addArticle(req, request);
	}

}

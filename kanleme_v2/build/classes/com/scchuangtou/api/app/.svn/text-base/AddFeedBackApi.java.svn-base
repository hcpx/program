package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.FeedBackDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.FeedBackReqEntity;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.utils.StringUtils;

public class AddFeedBackApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		FeedBackReqEntity req = JSON.parseObject(json, FeedBackReqEntity.class);

		if (StringUtils.emptyString(req.content)) {
			return null;
		}
		req.content = req.content.trim();
		int clen = req.content.length();
		if(clen<5 || clen > 200){
			return null;
		}
		Part pic_part = request.getPart("feedback_pic");
		MyMutiPart picPart = null;
		if (pic_part != null) {
			picPart = new MyMutiPart(pic_part);
		}
		return FeedBackDao.addFeedBack(req, picPart);
	}

}

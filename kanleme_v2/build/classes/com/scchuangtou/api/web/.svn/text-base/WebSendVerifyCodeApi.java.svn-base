package com.scchuangtou.api.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.VerifyCodeDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.SendVerifyCodeReqEntity;
import com.scchuangtou.entity.WebSendVerifyCodeReqEntity;
import com.scchuangtou.helper.ImageVerifyCodeHelper;
import com.scchuangtou.utils.StringUtils;

public class WebSendVerifyCodeApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (json == null) {
			return null;
		}

		WebSendVerifyCodeReqEntity req = JSON.parseObject(json, WebSendVerifyCodeReqEntity.class);
		if (req == null || StringUtils.emptyString(req.account) || StringUtils.emptyString(req.web_verify_code)) {
			return null;
		}
		if (!ImageVerifyCodeHelper.checkCode(request, req.web_verify_code)) {
			BaseResEntity res = new BaseResEntity();
			res.status = Config.STATUS_VERIFY_CODE_ERROR;
			return res;
		}
		String userIp = BaseApi.getClientIP(request);

		SendVerifyCodeReqEntity svreq = new SendVerifyCodeReqEntity();
		svreq.type = req.type;
		svreq.account = req.account;
		return VerifyCodeDao.sendVerifyCode(svreq, userIp);
	}

}

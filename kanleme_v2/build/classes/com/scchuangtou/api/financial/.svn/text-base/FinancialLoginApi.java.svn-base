package com.scchuangtou.api.financial;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.FinancialDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.FinancialLoginReqEntity;
import com.scchuangtou.helper.ImageVerifyCodeHelper;
import com.scchuangtou.utils.StringUtils;

public class FinancialLoginApi extends BaseFinancialApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (json == null) {
			return null;
		}
		FinancialLoginReqEntity req = JSON.parseObject(json, FinancialLoginReqEntity.class);
		if (req == null || StringUtils.emptyString(req.user_name) || StringUtils.emptyString(req.password)
				|| StringUtils.emptyString(req.image_verify_code)) {
			return null;
		}
		if (!ImageVerifyCodeHelper.checkCode(request, req.image_verify_code)) {
			BaseResEntity res = new BaseResEntity();
			res.status = Config.STATUS_VERIFY_CODE_ERROR;
			return res;
		}
		return FinancialDao.login(req);
	}

}

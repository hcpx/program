package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.UserDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.EditPasswordReqEntity;
import com.scchuangtou.utils.StringUtils;

public class EditPasswordApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		EditPasswordReqEntity req = JSON.parseObject(json, EditPasswordReqEntity.class);
		if (req == null || StringUtils.emptyString(req.user_name) || StringUtils.emptyString(req.new_passwrod)
				|| StringUtils.emptyString(req.old_passwrod)) {
			return null;
		}
		return UserDao.editPassword(req.user_name, req.old_passwrod, req.new_passwrod);
	}

}
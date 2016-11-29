package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.UserDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.LoginReqEntity;
import com.scchuangtou.entity.LoginResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.utils.StringUtils;

public class LoginApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		LoginReqEntity req = JSON.parseObject(json, LoginReqEntity.class);
		if (req == null || StringUtils.emptyString(req.user_name) || StringUtils.emptyString(req.login_password)) {
			return null;
		}

		LoginResEntity res =  UserDao.login(req.user_name, req.login_password,req.os);
		if(res!=null && res.isSuccess() && !StringUtils.emptyString(res.profile.head_pic)){
			res.profile.head_pic = ImageHelper.getImageUrl(request, res.profile.head_pic);
		}
		return res;
	}

}

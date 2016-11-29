package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.UserDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.EditUserReqEntity;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.utils.StringUtils;

public class EditUserApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		Part headPicPart = request.getPart("head_pic");
		if (StringUtils.emptyString(json)) {
			return null;
		}
		EditUserReqEntity req = JSON.parseObject(json, EditUserReqEntity.class);

		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.nickname)
				|| StringUtils.emptyString(req.signature)) {
			return null;
		}
		req.nickname = req.nickname.trim();
		if (req.nickname.length() > 10) {
			return null;
		}
		req.signature = req.signature.trim();
		if (req.signature.length() > 200) {
			return null;
		}
		MyMutiPart pic = null;
		if (headPicPart != null) {
			pic = new MyMutiPart(headPicPart);
		}
		return UserDao.editUser(request, req, pic);
	}
}
package com.scchuangtou.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.CircleDao;
import com.scchuangtou.entity.AdminAddCircleReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.utils.StringUtils;

public class EditCircle extends BaseAdminApi {

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
		MyMutiPart picPart = null;
		MyMutiPart backPicPart = null;
		try {
			picPart=request.getPart("circle_pic")==null?null:new MyMutiPart(request.getPart("circle_pic"));
		} catch (Exception e) {
		}
		try {
			backPicPart = request.getPart("circle_back_img")==null?null:new MyMutiPart(request.getPart("circle_back_img"));
		} catch (Exception e) {
		}
		AdminAddCircleReqEntity req = JSON.parseObject(json, AdminAddCircleReqEntity.class);
		return CircleDao.adminEditCircle(req, token, picPart, backPicPart);
	}

}

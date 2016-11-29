package com.scchuangtou.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.CircleDao;
import com.scchuangtou.entity.AdminAddCircleReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.utils.StringUtils;

public class AdminAddCircleApi extends BaseAdminApi {

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
		
		Part picPart = request.getPart("circle_pic");
		Part backPicPart = request.getPart("circle_back_img");

		AdminAddCircleReqEntity req = JSON.parseObject(json, AdminAddCircleReqEntity.class);
		if (StringUtils.emptyString(req.circle_name) || picPart == null || backPicPart == null
				|| StringUtils.emptyString(req.circle_sign)) {
			return null;
		}
		return CircleDao.adminAddCircle(token, req.circle_name, req.circle_sign, new MyMutiPart(picPart),new MyMutiPart(backPicPart),request);
	}

}

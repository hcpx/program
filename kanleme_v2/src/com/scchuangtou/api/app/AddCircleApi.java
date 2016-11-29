package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.CircleDao;
import com.scchuangtou.entity.AddCircleReqEntity;
import com.scchuangtou.entity.AddCircleResEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.utils.StringUtils;

public class AddCircleApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		Part circlepicPart = request.getPart("circle_pic");
		if (StringUtils.emptyString(json)) {
			return null;
		}
		AddCircleReqEntity req = JSON.parseObject(json, AddCircleReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.circle_name)
				|| StringUtils.emptyString(req.circle_sign)) {
			return null;
		}
		MyMutiPart pic = null;
		if (circlepicPart != null) {
			pic = new MyMutiPart(circlepicPart);
		}
		AddCircleResEntity res = CircleDao.addCircle(req, pic,request);
		return res;
	}

}

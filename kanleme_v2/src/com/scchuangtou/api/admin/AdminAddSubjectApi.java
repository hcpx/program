package com.scchuangtou.api.admin;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.SubjectDao;
import com.scchuangtou.entity.AddSubjectReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.utils.StringUtils;

public class AdminAddSubjectApi extends BaseAdminApi {

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
		Part iconPart = request.getPart("subject_icon");

		AddSubjectReqEntity req = JSON.parseObject(json, AddSubjectReqEntity.class);
		if (StringUtils.emptyString(req.subject_content) || iconPart == null
				|| StringUtils.emptyString(req.subject_title)) {
			return null;
		}
		ArrayList<MyMutiPart> imgs = new ArrayList<>();
		if (req.subject_img_des != null) {
			int len = req.subject_img_des.size();
			for (int i = 0; i < len; i++) {
				imgs.add(new MyMutiPart(request.getPart("subject_img" + i)));
			}
		}
		return SubjectDao.adminAddSubject(token, req, new MyMutiPart(iconPart), imgs);
	}
}

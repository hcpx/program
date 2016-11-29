package com.scchuangtou.api.app;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.IdentificationDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.EditIdentificationReqEntity;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.utils.StringUtils;

public class EditIdentificationApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		EditIdentificationReqEntity req = JSON.parseObject(json, EditIdentificationReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.name)
				|| StringUtils.emptyString(req.phone_num) || StringUtils.emptyString(req.verify_code)) {
			return null;
		}

		List<MyMutiPart> newPicParts = new ArrayList<MyMutiPart>();
		for (Part part : request.getParts()) {
			if (part.getName().contains("photo")) {
				MyMutiPart Mutipic = null;
				if (part != null) {
					Mutipic = new MyMutiPart(part);
					newPicParts.add(Mutipic);
				}
			}
		}
		return IdentificationDao.editIdentification(req, newPicParts,request);
	}

}

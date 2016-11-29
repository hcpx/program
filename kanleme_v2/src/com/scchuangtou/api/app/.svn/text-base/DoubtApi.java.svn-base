package com.scchuangtou.api.app;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.DoubtDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.DoubtReqEntity;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.utils.StringUtils;

public class DoubtApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		DoubtReqEntity req = JSON.parseObject(json, DoubtReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.help_each_id)
				|| StringUtils.emptyString(req.detail) || StringUtils.emptyString(req.phone)) {
			return null;
		}
		int detaillen = req.detail.trim().length();
		if (detaillen < 10 || detaillen > 200)
			return null;
		List<MyMutiPart> picParts = new ArrayList<MyMutiPart>();
		for (Part part : request.getParts()) {
			MyMutiPart Mutipic = null;
            if (part != null && part.getName().startsWith("doubt_img")) {
            	Mutipic = new MyMutiPart(part);
            	picParts.add(Mutipic);
            }
        }
		return DoubtDao.doubt(req,picParts,request);
	}

}

package com.scchuangtou.api.app;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.ReportDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.ReportReqEntity;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.utils.StringUtils;

public class ReportApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		ReportReqEntity req = JSON.parseObject(json, ReportReqEntity.class);

		if (req == null || StringUtils.emptyString(req.report_object_id) || StringUtils.emptyString(req.token)
				|| StringUtils.emptyString(req.report_content)) {
			return null;
		}
		
		List<MyMutiPart> picParts = new ArrayList<MyMutiPart>();
		for (Part part : request.getParts()) {
			MyMutiPart Mutipic = null;
            if (part != null && part.getName().startsWith("report_img")) {
            	Mutipic = new MyMutiPart(part);
            	picParts.add(Mutipic);
            }
        }
		return ReportDao.report(req, picParts);
	}

}

package com.scchuangtou.api.app;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.DynamicsDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.UpdateDynamicsReqEntity;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.utils.StringUtils;

public class UpdateDynamicsApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		UpdateDynamicsReqEntity req = JSON.parseObject(json, UpdateDynamicsReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.help_each_id)
				|| StringUtils.emptyString(req.description)) {
			return null;
		}
		List<MyMutiPart> picParts = new ArrayList<MyMutiPart>();
		for (Part part : request.getParts()) {
			MyMutiPart Mutipic = null;
            if (part != null && part.getName().startsWith("dynamics_img")) {
            	Mutipic = new MyMutiPart(part);
            	picParts.add(Mutipic);
            }
        }
		return DynamicsDao.dynamics(req,picParts,request);
	}

}

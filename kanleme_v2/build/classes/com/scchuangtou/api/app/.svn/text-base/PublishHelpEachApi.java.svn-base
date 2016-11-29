package com.scchuangtou.api.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.ProjectDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.PublishIllnessHelpReqEntity;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.utils.StringUtils;

public class PublishHelpEachApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		PublishIllnessHelpReqEntity req = JSON.parseObject(json, PublishIllnessHelpReqEntity.class);

		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.capital_purpose)
				|| StringUtils.emptyString(req.payee_name) || StringUtils.emptyString(req.payee_id_num)
				|| StringUtils.emptyString(req.payee_phone_num) || StringUtils.emptyString(req.capital_id)
				|| StringUtils.emptyString(req.aided_person_hospital)
				|| StringUtils.emptyString(req.aided_person_hospital_phone)) {
			return null;
		}
		
		String[] keys=new String[]{"project_img","hospital_doctor_prove_img","hospital_diagnosis_img","relation_img","aided_person_id_img","organize_img","payee_id_img"};
		List<MyMutiPart> partsList = null;
		Map<String,List<MyMutiPart>> partsMap=new HashMap<>();
		for(String key:keys){
			partsList = new ArrayList<MyMutiPart>();
			for (Part part : request.getParts()) {
				if (part.getName().contains(key)) {
					MyMutiPart Mutipic = null;
					if (part != null) {
						Mutipic = new MyMutiPart(part);
						partsList.add(Mutipic);
					}
				}
			}
			partsMap.put(key,partsList);
		}
		return ProjectDao.publishHelpEach(req, keys, partsMap, request);
	}

}

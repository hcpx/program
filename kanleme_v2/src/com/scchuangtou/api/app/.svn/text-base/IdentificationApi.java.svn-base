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
import com.scchuangtou.entity.IdentificationReqEntity;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.utils.IDCardUtil;
import com.scchuangtou.utils.StringUtils;

public class IdentificationApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		IdentificationReqEntity req = JSON.parseObject(json, IdentificationReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.name)
				|| StringUtils.emptyString(req.phone_num) || StringUtils.emptyString(req.verify_code)) {
			return null;
		}
		List<MyMutiPart> newPicPart = new ArrayList<MyMutiPart>();
		for(Part part:request.getParts()){
			if(part.getName().contains("photo")){
				MyMutiPart Mutipic = null;
				if (part != null) {
					Mutipic = new MyMutiPart(part);
					newPicPart.add(Mutipic);
				}
			}
		}
		//个人认证必须传三张图片
		if(req.type==Config.IdentificationStatusType.IDENTIFICATION_PERSON){
			if(newPicPart.size()!=3 || !IDCardUtil.validate(req.id_num)){
				return null;
			}
		}
		//组织认证不能超过八张图片
		if(req.type==Config.IdentificationStatusType.IDENTIFICATION_ORGANIZATION){
			if(newPicPart.size()>8 || StringUtils.emptyString(req.address) || newPicPart.size()==0){
				return null;
			}
		}
		return IdentificationDao.identification(req, newPicPart,request);
	}

}

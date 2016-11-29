package com.scchuangtou.api.app;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.ActivityDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.PublishActivityReqEntity;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.utils.StringUtils;

public class PublishActivityApi extends BaseApi{

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (json == null) {
			return null;
		}
		Part picPart = request.getPart("activity_pic");
		PublishActivityReqEntity req = JSON.parseObject(json, PublishActivityReqEntity.class);
		if (StringUtils.emptyString(req.token) || picPart == null || StringUtils.emptyString(req.activity_name) ||
				req.activity_lable==null || req.activity_type==null ||  StringUtils.emptyString(req.organizer_phone) ||
				StringUtils.emptyString(req.address) || StringUtils.emptyString(req.activity_content)) {
			return null;
		}
		int typelen = req.activity_type.length;
		int labellen = req.activity_lable.length;
		int titlelen = req.activity_name.trim().length();
		int contentlen = req.activity_content.trim().length();
		int address = req.address.trim().length();
		if (typelen < 1 || typelen > 3 || labellen < 1 || labellen > 5 || titlelen < 4 || titlelen > 62
				|| address < 10 || address > 120 || contentlen>5000 || req.limit_num>9999 || contentlen>5000)
			return null;
		List<MyMutiPart> activityPicPart = null;
		if (req.activity_pic_desc != null) {
			if (req.activity_pic_desc.length > 8) {
				return null;
			}
			activityPicPart = new ArrayList<MyMutiPart>();
			int size = req.activity_pic_desc.length;
			for (int i = 0; i < size; i++) {
				PublishActivityReqEntity.Pic pic = req.activity_pic_desc[i];
				if (!StringUtils.emptyString(req.activity_pic_desc[i].activity_pic_des)
						&& req.activity_pic_desc[i].activity_pic_des.trim().length() > 200) {
					return null;
				}
				Part pic_part = request.getPart(pic.activity_pic_partname);
				MyMutiPart Mutipic = null;
				if (pic_part != null) {
					Mutipic = new MyMutiPart(pic_part);
				}
				activityPicPart.add(Mutipic);
			}
		}
		return ActivityDao.addActivity(req, new MyMutiPart(picPart), request,activityPicPart);
	}

}

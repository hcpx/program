package com.scchuangtou.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.BannerDao;
import com.scchuangtou.entity.AddBannerReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.utils.DateUtil;
import com.scchuangtou.utils.StringUtils;

public class AdminAddBannerApi extends BaseAdminApi {

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
		Part pic_part = request.getPart("bannerImg");

		AddBannerReqEntity req = JSON.parseObject(json, AddBannerReqEntity.class);
		if (StringUtils.emptyString(req.source) || pic_part == null
				|| req.end_time <= DateUtil.getDayEndTime(System.currentTimeMillis())) {
			return null;
		}
		return BannerDao.addBanner(request, req, new MyMutiPart(pic_part),token);
	}

}

package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.BannerDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.GetBannersReqEntity;
import com.scchuangtou.utils.StringUtils;

public class GetBannersApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		GetBannersReqEntity req = JSON.parseObject(json, GetBannersReqEntity.class);
		if (req == null) {
			return null;
		}
		return BannerDao.getBanners(request,req);
	}

}
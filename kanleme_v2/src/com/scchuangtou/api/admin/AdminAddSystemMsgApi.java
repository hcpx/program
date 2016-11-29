package com.scchuangtou.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.SystemMessageDao;
import com.scchuangtou.entity.AddSystemMessageReqEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.utils.StringUtils;

public class AdminAddSystemMsgApi extends BaseAdminApi {

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
		AddSystemMessageReqEntity req = JSON.parseObject(json, AddSystemMessageReqEntity.class);
		if (StringUtils.emptyString(req.message_title)
				|| StringUtils.emptyString(req.message_content)) {
			return null;
		}
		return SystemMessageDao.addSystemMessage(req,token);
	}

}

package com.scchuangtou.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.SystemMessageDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.EditSystemMsgReqEntity;
import com.scchuangtou.utils.StringUtils;

public class AdminEditSystemMsgApi extends BaseAdminApi {

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
		EditSystemMsgReqEntity req = JSON.parseObject(json, EditSystemMsgReqEntity.class);
		if (StringUtils.emptyString(req.message_id) || StringUtils.emptyString(req.message_title)
				|| StringUtils.emptyString(req.message_content)) {
			return null;
		}
		return SystemMessageDao.editSystemMessage(req, token);
	}

}

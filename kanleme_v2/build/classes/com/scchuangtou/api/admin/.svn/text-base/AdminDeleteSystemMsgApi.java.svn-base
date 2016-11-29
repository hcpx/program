package com.scchuangtou.api.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.SystemMessageDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.DeleteSystemMsgReqEntity;
import com.scchuangtou.utils.StringUtils;

public class AdminDeleteSystemMsgApi extends BaseAdminApi {

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
		DeleteSystemMsgReqEntity req = JSON.parseObject(json, DeleteSystemMsgReqEntity.class);
		if (req.message_ids == null || req.message_ids.isEmpty()) {
			return null;
		}
		return SystemMessageDao.deleteSystemMessages(req, token);
	}

}

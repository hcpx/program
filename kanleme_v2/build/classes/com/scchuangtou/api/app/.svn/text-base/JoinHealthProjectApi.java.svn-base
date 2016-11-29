package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.HealthProjectMemberDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.JoinHealthProjectReqEntity;
import com.scchuangtou.utils.DateUtil;
import com.scchuangtou.utils.StringUtils;

public class JoinHealthProjectApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}
		JoinHealthProjectReqEntity req = JSON.parseObject(json, JoinHealthProjectReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token)) {
			return null;
		}
		for (JoinHealthProjectReqEntity.PersonInfo personinfo : req.join_person_list) {
			if (StringUtils.emptyString(personinfo.name) || StringUtils.emptyString(personinfo.phone_num)
					|| StringUtils.emptyString(personinfo.id_num)
					|| DateUtil.getAge(personinfo.id_num, DateUtil.DIFF_TYPE_YEAR) == -1)
				return null;
		}
		return HealthProjectMemberDao.joinHealthProject(req);
	}

}

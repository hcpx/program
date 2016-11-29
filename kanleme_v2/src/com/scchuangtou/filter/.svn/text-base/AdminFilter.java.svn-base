package com.scchuangtou.filter;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.api.admin.BaseAdminApi;
import com.scchuangtou.config.AdminConfig;

public class AdminFilter extends BaseCheckLoginFilter {

	@Override
	protected String getLoginUrl() {
		return AdminConfig.LOGIN_URL;
	}

	@Override
	protected String getIndexUrl() {
		return AdminConfig.INDEX_URL;
	}

	@Override
	protected boolean isLogin(HttpServletRequest hreq) {
		String token = BaseAdminApi.getToken(hreq);
		return token != null && token.length() > 0;
	}

}

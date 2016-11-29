package com.scchuangtou.filter;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.api.financial.BaseFinancialApi;
import com.scchuangtou.config.FinancialConfig;

public class FinancialFilter extends BaseCheckLoginFilter {

	@Override
	protected String getLoginUrl() {
		return FinancialConfig.LOGIN_URL;
	}

	@Override
	protected String getIndexUrl() {
		return FinancialConfig.INDEX_URL;
	}

	@Override
	protected boolean isLogin(HttpServletRequest hreq) {
		String token = BaseFinancialApi.getToken(hreq);
		return token != null && token.length() > 0;
	}
}

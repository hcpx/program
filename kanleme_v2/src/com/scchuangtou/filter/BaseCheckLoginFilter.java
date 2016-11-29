package com.scchuangtou.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public abstract class BaseCheckLoginFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public final void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) req;
		String url = hreq.getServletPath();
		String u = url.toLowerCase();
		if (!u.endsWith(".jsp")) {
			chain.doFilter(req, res);
			return;
		}
		boolean isLogin = isLogin(hreq);
		String loginUrl = getLoginUrl();
		String indexUrl = getIndexUrl();

		if (getLoginUrl().equals(url)) {
			if (isLogin) {
				req.getRequestDispatcher(indexUrl).forward(req, res);
				return;
			}
		} else {
			if (!isLogin) {
				req.getRequestDispatcher(loginUrl).forward(req, res);
				return;
			}
		}
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	protected abstract String getLoginUrl();

	protected abstract String getIndexUrl();

	protected abstract boolean isLogin(HttpServletRequest hreq);

}

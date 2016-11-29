package com.scchuangtou.api.admin;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scchuangtou.api.BaseApi;
import com.scchuangtou.entity.AdminLoginResEntity;
import com.scchuangtou.entity.BaseResEntity;

public abstract class BaseAdminApi extends BaseApi {
	private static final String TOKEN = "admin_token";

	public static String getToken(HttpServletRequest request) {
		Object token = request.getSession(true).getAttribute(TOKEN);
		if (token == null) {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (TOKEN.equals(cookie.getName())) {
						token = cookie.getValue();
					}
				}
			}
		}
		return token == null ? null : token.toString();
	}

	private void setToken(HttpServletRequest request, HttpServletResponse response, String token) {
		request.getSession(true).setAttribute(TOKEN, token);

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (TOKEN.equals(cookie.getName())) {
					cookie.setValue(token);
				}
			}
		} else if (token != null) {
			Cookie newCookie = new Cookie(TOKEN, token);
			newCookie.setPath("/");
			response.addCookie(newCookie);
		}
	}

	@Override
	protected final BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		BaseResEntity res = action(request, response, getToken(request));
		if (res == null) {
			return null;
		}
		if (res.isTokenError()) {
			setToken(request, response, null);
		}else if (res.isSuccess() && res instanceof AdminLoginResEntity) {
			String token = ((AdminLoginResEntity) res).admin_token;
			setToken(request, response, token);
		}
		return res;
	}

	protected abstract BaseResEntity action(HttpServletRequest request, HttpServletResponse response, String token)
			throws Exception;

}

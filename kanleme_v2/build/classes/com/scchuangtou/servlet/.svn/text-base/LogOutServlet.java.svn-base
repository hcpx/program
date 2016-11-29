package com.scchuangtou.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.scchuangtou.config.Config;

public class LogOutServlet  extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		Enumeration<String> names = session.getAttributeNames();
		if(names != null){
			while(names.hasMoreElements()){
				String name = names.nextElement();
				session.removeAttribute(name);
			}
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setMaxAge(0);
				cookie.setValue(null);
			}
		}
		byte[] data = "ok".getBytes(Config.CHARSET);
		OutputStream out = response.getOutputStream();
		out.write(data);
		out.flush();
	}
}

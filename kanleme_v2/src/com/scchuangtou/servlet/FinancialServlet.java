package com.scchuangtou.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.config.FinancialConfig;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.utils.LogUtils;

@MultipartConfig(maxRequestSize = 1024L * 1024L * 50L)
public class FinancialServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding(Config.CHARSET);
		response.setCharacterEncoding(Config.CHARSET);
		response.setContentType("text/json; charset=" + Config.CHARSET);

		BaseResEntity mResponse = null;
		try {
			mResponse = BaseApi.dispatcher(request, response);
		} catch (Exception e) {
			LogUtils.log(e);
			mResponse = new BaseResEntity();
			mResponse.status = Config.STATUS_SERVER_ERROR;
		}
		if (mResponse == null) {
			mResponse = new BaseResEntity();
			mResponse.status = Config.STATUS_PARAMETER_ERROR;
		}
		if (mResponse.isTokenError()) {
			request.getRequestDispatcher(FinancialConfig.LOGIN_URL).forward(request, response);
		} else {
			sendResponseMessage(response, JSON.toJSONString(mResponse));
		}
	}

	private static void sendResponseMessage(HttpServletResponse response, String res) throws IOException {
		byte[] data = res.getBytes(Config.CHARSET);
		OutputStream out = response.getOutputStream();
		out.write(data);
		out.flush();
	}
}

package com.scchuangtou.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.guoli.utils.QRCodeUtils;
import com.scchuangtou.model.QrCodeInfo;
import com.scchuangtou.utils.LogUtils;

public class QRCodeEncoder extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getServletContext().getRealPath("") + "/code.png";
		String url = request.getRequestURL().toString();
		url = url.substring(0, url.lastIndexOf("/"));
		String durl = url + "/wechatdown.jsp";
		File codeFile = new File(path);
		if (!codeFile.exists()) {
			try {
				QRCodeUtils.createQRCode(durl, 150, 150, codeFile);
			} catch (Exception e) {
				LogUtils.log(e);
				return;
			}
		}
		QrCodeInfo info = new QrCodeInfo();
		info.status = "ok";
		info.code_url = url + "/" + codeFile.getName();
		info.download_url = durl;

		response.setCharacterEncoding("utf-8");

		response.setContentType("text/json; charset=UTF-8");
		OutputStream os = response.getOutputStream();
		os.write(JSON.toJSONString(info).getBytes("UTF-8"));
	}
}
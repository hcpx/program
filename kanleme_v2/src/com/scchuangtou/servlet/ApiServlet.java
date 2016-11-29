package com.scchuangtou.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.utils.StreamUtils;

/**
 * Servlet implementation class UserServlet
 */
@MultipartConfig(
// maxFileSize = 1024L * 1024L*5L,//5Mb
maxRequestSize = 1024L * 1024L * 50L)
public class ApiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding(Config.CHARSET);
		response.setCharacterEncoding(Config.CHARSET);
		response.setContentType("text/json; charset=" + Config.CHARSET);

		BaseApi.Response mResponse = BaseApi.dispatcher2JSON(request, response);

		OutputStream out = null;
		if (mResponse.isGzip && mResponse.res.length() > 100) {
			response.setHeader("Accept-Encoding", "gzip");
			out = new GZIPOutputStream(response.getOutputStream());
		} else {
			out = response.getOutputStream();
		}
		try {
			byte[] res = mResponse.res.getBytes(Config.CHARSET);
			out.write(res, 0, res.length);
			out.flush();
		} finally {
			StreamUtils.closeIO(out);
		}
	}

}

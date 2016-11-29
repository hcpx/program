package com.scchuangtou.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.scchuangtou.config.AccountConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.utils.StreamUtils;

public class GetImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String key = request.getParameter(AccountConfig.ImageDirConfig.REQUEST_PARAMETER_KEY);
		if (key == null) {
			return;
		}
		key = URLDecoder.decode(key, Config.CHARSET);
		File file = ImageHelper.getImageFile(key);
		if (!file.exists()) {
			return;
		}
		response.setContentType("image/jpeg");

		OutputStream out = response.getOutputStream();
		InputStream is = null;
		byte[] buffer = new byte[10 * 1024];
		try {
			is = new FileInputStream(file);
			int rlen;
			while (true) {
				rlen = is.read(buffer, 0, buffer.length);
				if (rlen == -1) {
					break;
				}
				out.write(buffer, 0, rlen);
			}
			out.flush();
		} finally {
			StreamUtils.closeIO(is);
		}
	}
}

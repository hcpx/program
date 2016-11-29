package com.scchuangtou.servlet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.scchuangtou.config.AdminConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.config.FinancialConfig;
import com.scchuangtou.config.UserConfig;
import com.scchuangtou.dao.AdminDao;
import com.scchuangtou.dao.FinancialDao;
import com.scchuangtou.dao.UserDao;
import com.scchuangtou.model.AdminInfo;
import com.scchuangtou.model.FinancialInfo;
import com.scchuangtou.task.MessageTask;
import com.scchuangtou.task.SMSSendTask;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DESUtils;
import com.scchuangtou.utils.LogUtils;

public class MainServletContextListener implements ServletContextListener {
	public static String projectDir;
	public static BufferedImage markImg;

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		projectDir = arg0.getServletContext().getRealPath("");
		LogUtils.init(projectDir);
		init();
		try {
			markImg = ImageIO.read(new File(projectDir, "imc_watermark.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		MessageTask.start();
		SMSSendTask.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		MessageTask.stop();
		SMSSendTask.stop();
	}

	private static void init() {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();

			AdminInfo mAdminInfo = new AdminInfo();
			mAdminInfo.admin_user = AdminConfig.USER_NAME;
			mAdminInfo.admin_phone = AdminConfig.PHONE_NUMBER;
			mAdminInfo.admin_email = AdminConfig.EMAIL;
			mAdminInfo.admin_pass = AdminConfig.DEFAULT_PASSWORD;
			mAdminInfo.modes = new ArrayList<>();
			mAdminInfo.modes.addAll(AdminConfig.getAdminModes().keySet());
			AdminDao.addAdmin(conn, mAdminInfo);

			FinancialInfo financialInfo = new FinancialInfo();
			financialInfo.financial_user = FinancialConfig.USER_NAME;
			FinancialDao.addAdmin(conn, financialInfo, FinancialConfig.DEFAULT_PASSWORD);

			HashMap<String, Object> datas = new HashMap<String, Object>();
			datas.put("user_id", UserConfig.USER_ID);
			datas.put("user_name", UserConfig.PHONE_NUMBER);
			datas.put("login_password", UserConfig.LOGIN_PASSWORD);
			datas.put("nickname", UserConfig.NICKNAME);
			datas.put("phone_number", UserConfig.PHONE_NUMBER);
			datas.put("user_key", DESUtils.createKey());
			datas.put("gold", 0);
			datas.put("growth", 0);
			datas.put("registration_time", System.currentTimeMillis());
			datas.put("status", UserDao.USER_STATUS_NORMAL);
			datas.put("is_complete", false);
			datas.put("third_login", false);
			datas.put("certification", Config.CertificationStauts.CERTIFICATION_STATUS_PASS);
			datas.put("close_time", 0);
			DBUtils.insert(conn, "INSERT IGNORE INTO userinfo", datas);

			LogUtils.log("init success");
		} catch (Exception e) {
			LogUtils.log(e);
		} finally {
			DBUtils.close(conn);
		}
	}

}

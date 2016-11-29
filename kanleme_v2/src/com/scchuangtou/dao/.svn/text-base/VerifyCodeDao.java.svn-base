package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;

import com.scchuangtou.config.AccountConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.entity.SendVerifyCodeReqEntity;
import com.scchuangtou.entity.SendVerifyCodeResEntity;
import com.scchuangtou.helper.SMSVerifyCodeHelper;
import com.scchuangtou.model.User;
import com.scchuangtou.task.SMSSendTask;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.StringUtils;

public class VerifyCodeDao {
	private static final int CODE_TIME = 10 * 60 * 1000;
	private static final int REPEAT_TIME = 60 * 1000;

	public static boolean checkVerifyCode(DBUtils.ConnectionCache conn, String phone, String verify_code)
			throws SQLException {
		long t = System.currentTimeMillis() - CODE_TIME;
		String nvc = IdUtils.createId(phone);
		String sql = "UPDATE verify_code set verify_code=?,time=0 WHERE phone_number=? AND verify_code=? AND time>=?";
		int row = DBUtils.executeUpdate(conn, sql, new Object[] { nvc, phone, verify_code, t });
		return row > 0;
	}

	/**
	 * @param conn
	 * @param phone
	 * @return null:表示重复提交
	 * @throws SQLException
	 */
	private static String getVerifyCode(DBUtils.ConnectionCache conn, String phone) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT time,verify_code FROM verify_code WHERE phone_number=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, phone);
			rs = pstat.executeQuery();
			if (rs.next()) {
				String verify_code = rs.getString("verify_code");
				long time = rs.getLong("time");
				long t = System.currentTimeMillis() - time;
				if (t <= REPEAT_TIME) {
					return null;
				}
				if (t <= CODE_TIME) {
					return verify_code;
				}
			}
			return SMSVerifyCodeHelper.createVerifyCode();
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	private static boolean checkIpCanSend(DBUtils.ConnectionCache conn, String ip) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT time FROM verify_ip WHERE ip=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, ip);
			rs = pstat.executeQuery();
			if (rs.next()) {
				long t = System.currentTimeMillis() - rs.getLong("time");
				return t > REPEAT_TIME;
			} else {
				return true;
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	private static String sendVerifyCode(DBUtils.ConnectionCache conn, String phone, String userIp)
			throws SQLException {
		if (!checkIpCanSend(conn, userIp)) {
			return Config.STATUS_REPEAT_ERROR;
		}
		String code = getVerifyCode(conn, phone);
		if (code == null) {
			return Config.STATUS_REPEAT_ERROR;
		}
		long times = System.currentTimeMillis();
		int row = 0;
		
		DBUtils.beginTransaction(conn);
		try{
			HashMap<String, Object> datas = new HashMap<String, Object>();
			datas.put("phone_number", phone);
			datas.put("verify_code", code);
			datas.put("time", times);
			row = DBUtils.insert(conn, "REPLACE INTO verify_code", datas);

			// 添加到IP表中
			datas = new HashMap<String, Object>();
			datas.put("ip", userIp);
			datas.put("time", times);
			row = DBUtils.insert(conn, "REPLACE INTO verify_ip", datas);
			if(row > 0){
				DBUtils.commitTransaction(conn);
				SMSSendTask.addMessage(
						new SMSSendTask.SMSParam(phone, MessageFormat.format(AccountConfig.SMSConfig.MSG, code)));
			}
		}catch(Exception e){
			LogUtils.log(e);
		}
		if (row > 0) {
			return Config.STATUS_OK;
		} else {
			DBUtils.rollbackTransaction(conn);
			return Config.STATUS_SERVER_ERROR;
		}
	}

	public static SendVerifyCodeResEntity sendVerifyCode(SendVerifyCodeReqEntity req, String userIp)
			throws SQLException {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			SendVerifyCodeResEntity res = new SendVerifyCodeResEntity();
			String phoneNumber;
			if (req.type == Config.VerifyCodeType.FIND_PASSWORD) {
				if (!StringUtils.isPhoneNum(req.account)) {
					return null;
				}
				User user = UserDao.getUserByUserName(conn, req.account);
				if (user == null) {
					res.status = Config.STATUS_NOT_EXITS;
					return res;
				}
				if (user.third_login) {
					res.status = Config.STATUS_THIRD_LOGIN_ERROR;
					return res;
				}
				phoneNumber = user.phone_number;
				if (StringUtils.emptyString(phoneNumber)) {
					res.status = Config.STATUS_NOT_BIND_PHONE;
					return res;
				}
			} else if (req.type == Config.VerifyCodeType.VERIFY_USER) {
				User user = UserDao.getUserByToken(conn, req.account);
				if (user == null) {
					res.status = Config.STATUS_TOKEN_ERROR;
					return res;
				}
				phoneNumber = user.phone_number;
				if (StringUtils.emptyString(phoneNumber)) {
					res.status = Config.STATUS_NOT_BIND_PHONE;
					return res;
				}
			} else if (req.type == Config.VerifyCodeType.REGISTER) {
				if (!StringUtils.isPhoneNum(req.account)) {
					return null;
				}
				phoneNumber = req.account;
				if (UserDao.phoneNumberIsBind(conn, phoneNumber)) {
					res.status = Config.STATUS_PHONE_NUMBER_EXITS;
					return res;
				}
			} else if (req.type == Config.VerifyCodeType.VERIFY) {
				if (!StringUtils.isPhoneNum(req.account)) {
					return null;
				}
				phoneNumber = req.account;
			} else {
				return null;
			}
			res.status = sendVerifyCode(conn, phoneNumber, userIp);
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}
}
package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.ThirdAccountBindReqEntity;
import com.scchuangtou.entity.ThirdAccountBindResEntity;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserProfile;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.MD5Utils;
import com.scchuangtou.utils.StringUtils;

public class UserThirdAccountDao {
	private static String getThirdId(int thirdType, String thirdId) {
		if (thirdType == Config.ThirdLoginType.THIRD_LOGIN_TYPE_SINA) {
			thirdId = "sina_" + thirdId;
		} else if (thirdType == Config.ThirdLoginType.THIRD_LOGIN_TYPE_WECHAT) {
			thirdId = "wechat_" + thirdId;
		} else if (thirdType == Config.ThirdLoginType.THIRD_LOGIN_TYPE_QQ) {
			thirdId = "qq_" + thirdId;
		} else {
			return null;
		}
		return MD5Utils.md5(thirdId.getBytes(Charset.forName(Config.CHARSET)), MD5Utils.MD5Type.MD5_16);
	}

	public static ThirdAccountBindResEntity bind(ThirdAccountBindReqEntity req) throws Exception {
		String id = getThirdId(req.type, req.id);
		if (StringUtils.emptyString(id)) {
			return null;
		}

		ThirdAccountBindResEntity res = new ThirdAccountBindResEntity();

		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();

			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}

			HashMap<String, Object> thirdDatas = new HashMap<>();
			thirdDatas.put("third_id", id);
			thirdDatas.put("third_name", req.nickname);
			thirdDatas.put("third_type", req.type);
			thirdDatas.put("user_id", user.user_id);
			int row = DBUtils.insert(conn, "INSERT IGNORE INTO user_third_account", thirdDatas);
			if (row > 0) {
				res.status = Config.STATUS_OK;
			} else {
				res.status = Config.STATUS_REPEAT_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	public static String getUserId(DBUtils.ConnectionCache conn, String id, int type) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT user_id FROM user_third_account WHERE third_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, getThirdId(type, id));
			rs = pstat.executeQuery();
			if (rs.next()) {
				return rs.getString("user_id");
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
		return null;
	}

	public static int bind(DBUtils.ConnectionCache conn, int type, String thirdId, String nickname, String user_id)
			throws SQLException {
		String id = getThirdId(type, thirdId);
		if (StringUtils.emptyString(id)) {
			return 0;
		}
		HashMap<String, Object> thirdDatas = new HashMap<>();
		thirdDatas.put("third_id", id);
		thirdDatas.put("third_name", nickname);
		thirdDatas.put("third_type", type);
		thirdDatas.put("user_id", user_id);
		return DBUtils.insert(conn, "INSERT IGNORE INTO user_third_account", thirdDatas);
	}

	public static List<UserProfile.ThridAccountInfo> getUserAccounts(DBUtils.ConnectionCache conn, String user_id)
			throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT third_name,third_type FROM user_third_account WHERE user_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, user_id);
			rs = pstat.executeQuery();
			List<UserProfile.ThridAccountInfo> accounts = new ArrayList<>();
			while (rs.next()) {
				UserProfile.ThridAccountInfo account = new UserProfile.ThridAccountInfo();
				account.nickname = rs.getString("third_name");
				account.type = rs.getInt("third_type");
				accounts.add(account);
			}
			return accounts;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
}

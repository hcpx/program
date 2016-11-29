package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.GetSigninInfoReqEntity;
import com.scchuangtou.entity.GetSigninInfoResEntity;
import com.scchuangtou.entity.SignInReqEntity;
import com.scchuangtou.entity.SignInResEntity;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserValueInfo;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.DateUtil;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.MD5Utils;
import com.scchuangtou.utils.MathUtils;

public class SiginDao {

	public static SignInResEntity signin(SignInReqEntity req) throws SQLException {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();

			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				SignInResEntity res = new SignInResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			UserValueInfo value = new UserValueInfo();
			long sigin_time = DateUtil.getDayTime(System.currentTimeMillis());
			int row = 0;
			try {
				DBUtils.beginTransaction(conn);

				String id = MD5Utils.md5((user.user_id + sigin_time).getBytes(Charset.forName(Config.CHARSET)),
						MD5Utils.MD5Type.MD5_32);
				HashMap<String, Object> datas = new HashMap<>();
				datas.put("sigin_id", id);
				datas.put("sigin_time", sigin_time);
				datas.put("sigin_user", user.user_id);
				row = DBUtils.insert(conn, "INSERT IGNORE INTO user_sigin_info", datas);
				if (row > 0) {
					value.gold = MathUtils.getRandomGold(Config.SiginGold.SIGIN_GOLD_MIN, Config.SiginGold.SIGIN_GOLD_MAX);
					value.growth = Config.GrowthGiving.SIGIN;
					row = UserDao.updateUserValue(conn, user.user_id, value, null, null,Config.GoldChangeType.GOLD_CHANGE_TYPE_SIGIN, id);
				}
				if (row > 0) {
					DBUtils.commitTransaction(conn);
				}
			} catch (Exception e) {
				LogUtils.log(e);
				row = 0;
			}
			SignInResEntity res = new SignInResEntity();
			if (row > 0) {
				res.status = Config.STATUS_OK;
				res.giving_gold = value.gold;
				res.giving_growth = value.growth;
				res.gold = MathUtils.sum(user.gold, value.gold);
				res.growth = user.growth + value.growth;
				res.level = Config.getLevel(res.growth);
				res.sigin_time = sigin_time;
			} else {
				DBUtils.rollbackTransaction(conn);
				res.status = Config.STATUS_OK;
				res.gold = user.gold;
				res.growth = user.growth;
				res.level = Config.getLevel(res.growth);
				res.sigin_time = sigin_time;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	public static long todayIsSignin(DBUtils.ConnectionCache conn, String user_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			long t = DateUtil.getDayTime(System.currentTimeMillis());
			String sql = "SELECT sigin_id FROM user_sigin_info WHERE sigin_user=? and sigin_time = ?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, user_id);
			pstat.setObject(2, t);
			rs = pstat.executeQuery();
			if (rs.next()) {
				return t;
			} else {
				return 0;
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	private static int getSiginTotal(DBUtils.ConnectionCache conn, String user_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT count(sigin_id) FROM user_sigin_info WHERE sigin_user=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, user_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return 0;
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	public static GetSigninInfoResEntity getSignInfo(GetSigninInfoReqEntity req) throws SQLException {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();

			GetSigninInfoResEntity res = new GetSigninInfoResEntity();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			String sql = "SELECT sigin_time FROM user_sigin_info WHERE sigin_user=? and sigin_time>=? and sigin_time <=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, user.user_id);
			pstat.setObject(2, req.start_time);
			pstat.setObject(3, req.end_time <= 0 ? System.currentTimeMillis() : req.end_time);
			rs = pstat.executeQuery();

			res.status = Config.STATUS_OK;
			res.signin_total = getSiginTotal(conn, user.user_id);
			res.signins = new ArrayList<>();
			while (rs.next()) {
				res.signins.add(rs.getLong("sigin_time"));
			}
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}
}

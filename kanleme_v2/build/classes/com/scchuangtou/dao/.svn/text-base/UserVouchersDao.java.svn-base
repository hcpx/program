package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.scchuangtou.model.UserProfile;
import com.scchuangtou.model.UserVouchersInfo;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.IdUtils;

public class UserVouchersDao {
	public static int USER_VOUCHERS_STATUS_NORMAL = 0; // 正常状态
	public static int USER_VOUCHERS_STATUS_BARRED = 1; // 已经使用过
	public static long USER_VOUCHERS_INVALID_MAX_TIME = -1l; // 不会失效的卷

	public static String addVoucher(DBUtils.ConnectionCache conn, String user_id, float money, long start_time,
			long end_time) throws SQLException {
		String id = IdUtils.createId(user_id);
		HashMap<String, Object> datas = new HashMap<String, Object>();
		datas.put("user_vouchers_id", id);
		datas.put("user_id", user_id);
		datas.put("gold", money);
		datas.put("end_time", end_time);
		datas.put("start_time", start_time);
		datas.put("status", USER_VOUCHERS_STATUS_NORMAL);
		int row = DBUtils.insert(conn, "INSERT IGNORE INTO user_vouchers", datas);
		if(row > 0){
			return id;
		}else{
			return null;
		}
	}

	/**
	 * 互助项目抵用券详情
	 * 
	 * @param conn
	 * @param healthProjectMemberInfoID
	 * @return
	 * @throws SQLException
	 */
	public static UserVouchersInfo getHealthProjectUserVouchersInfo(DBUtils.ConnectionCache conn,
			String healthProjectMemberInfoID) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select user_vouchers_id,user_id,gold,start_time,status,end_time from user_vouchers where user_vouchers_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, healthProjectMemberInfoID);
			rs = pstat.executeQuery();
			if (rs.next()) {
				UserVouchersInfo healthProjectUserVouchersInfo = new UserVouchersInfo();
				healthProjectUserVouchersInfo.user_vouchers_id = rs.getString("user_vouchers_id");
				healthProjectUserVouchersInfo.user_id = rs.getString("user_id");
				healthProjectUserVouchersInfo.gold = rs.getFloat("gold");
				healthProjectUserVouchersInfo.start_time = rs.getLong("start_time");
				healthProjectUserVouchersInfo.end_time = rs.getLong("end_time");
				healthProjectUserVouchersInfo.status = rs.getInt("status");
				return healthProjectUserVouchersInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	/**
	 * 获取用户的抵用卷
	 * 
	 * @param conn
	 * @param userid
	 * @param status
	 *            -1查询所有优惠券,0为可用卷，1为已经使用过的抵用卷
	 * @param where
	 * @return
	 * @throws SQLException
	 */
	public static List<UserProfile.voucher> getHealthProjectUserVouchersInfoList(DBUtils.ConnectionCache conn,
			String userid, int status, boolean useful) throws SQLException {
		PreparedStatementCache pstat = null;
		List<UserProfile.voucher> list = null;
		ResultSet rs = null;
		long time = System.currentTimeMillis();
		try {
			StringBuffer sql = new StringBuffer(
					"select user_vouchers_id,user_id,gold,start_time,status,end_time from user_vouchers where user_id=?");
			if (status != -1)
				sql.append(" and status=?");
			if (useful)
				sql.append(" and( end_time=").append(USER_VOUCHERS_INVALID_MAX_TIME).append(" or end_time>=")
						.append(time).append(")");
			sql.append(" order by gold desc");
			pstat = conn.prepareStatement(sql.toString());
			pstat.setObject(1, userid);
			if (status != -1)
				pstat.setObject(2, status);
			rs = pstat.executeQuery();
			list = new ArrayList<>();
			while (rs.next()) {
				UserProfile.voucher healthProjectUserVouchersInfo = new UserProfile.voucher();
				healthProjectUserVouchersInfo.user_vouchers_id = rs.getString("user_vouchers_id");
				healthProjectUserVouchersInfo.gold = rs.getFloat("gold");
				healthProjectUserVouchersInfo.start_time = rs.getLong("start_time");
				healthProjectUserVouchersInfo.end_time = rs.getLong("end_time");
				healthProjectUserVouchersInfo.status = rs.getInt("status");
				list.add(healthProjectUserVouchersInfo);
			}
			return list;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	/**
	 * 更新抵扣卷信息
	 * 
	 * @param conn
	 * @param user_id
	 * @param ids
	 * @return
	 * @throws SQLException
	 */
	public static int updateVouchers(DBUtils.ConnectionCache conn, String user_id, List<String> ids)
			throws SQLException {
		int row = -1;
		StringBuffer sbsql = new StringBuffer("");
		for (int i = 0; i < ids.size(); i++) {
			if (i != ids.size() - 1)
				sbsql.append("'"+ids.get(i)+"'" + ",");
			else
				sbsql.append("'"+ids.get(i)+"'");
		}
		String sql = "UPDATE user_vouchers SET status=? WHERE user_vouchers_id in("+sbsql+") and user_id=? and status=?";
		row=DBUtils.executeUpdate(conn, sql, new Object[] {USER_VOUCHERS_STATUS_BARRED, user_id,USER_VOUCHERS_STATUS_NORMAL });
		return row == ids.size() ? row : -1;
	}
}

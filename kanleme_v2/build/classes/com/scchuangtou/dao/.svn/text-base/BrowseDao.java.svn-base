package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;

import com.scchuangtou.config.Config;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.MD5Utils;

public class BrowseDao {

//	public static int getBrowseCount(DBUtils.ConnectionCache conn, String browse_object_id, int browse_object_type)
//			throws SQLException {
//		PreparedStatement pstat = null;
//		ResultSet rs = null;
//		try {
//			String sql = "SELECT count(browse_id) FROM browse WHERE browse_object_id=? and browse_object_type=?";
//			pstat = conn.prepareStatement(sql);
//			pstat.setString(1, browse_object_id);
//			pstat.setObject(2, browse_object_type);
//			rs = pstat.executeQuery();
//			if (rs.next()) {
//				return rs.getInt(1);
//			} else {
//				return 0;
//			}
//		} finally {
//			DBUtils.close(rs);
//			DBUtils.close(pstat);
//		}
//	}

	/**
	 * 十分钟算一次
	 * 
	 * @param user_id
	 * @param object_id
	 * @param type
	 * @return
	 */
	private static String getBrowseId(String user_id, String object_id, int type) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) / 10 * 10);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		long t = calendar.getTimeInMillis();

		return MD5Utils.md5((user_id + object_id + t).getBytes(Charset.forName(Config.CHARSET)),
				MD5Utils.MD5Type.MD5_16);
	}

	public static int browse(DBUtils.ConnectionCache conn, String user_id, String object_id, int type) throws SQLException {
		String id = getBrowseId(user_id, object_id, type);
		HashMap<String, Object> datas = new HashMap<>();
		datas.put("browse_id", id);
		datas.put("browse_object_type", type);
		datas.put("browse_object_id", object_id);
		datas.put("user_id", user_id);
		datas.put("browse_time", System.currentTimeMillis());
		int row = DBUtils.insert(conn, "insert ignore into browse", datas);
		if(row > 0){
			if(type == Config.BrowseType.BROWSE_TYPE_ARTICLE){
				ArticleDao.updateCount(conn, object_id, 0, 1, 0);
			}else if(type == Config.BrowseType.BROWSE_TYPE_SUBJECT){
				SubjectDao.updateCount(conn, object_id, 0, 1,0);
			}else if(type == Config.BrowseType.BROWSE_TYPE_PROJECT){
				ProjectDao.updateCount(conn, object_id, 0, 1,0);
			}else if(type == Config.BrowseType.BROWSE_TYPE_ACTIVITY){
				ActivityDao.updateCount(conn, object_id, 0, 1,0);
			}
		}
		return row;
	}

	public static int deleteBrowse(DBUtils.ConnectionCache conn, String object_id, int object_type) throws SQLException {
		String sql = "delete from browse where browse_object_id=? and browse_object_type=?";
		return DBUtils.executeUpdate(conn, sql, new Object[] { object_id, object_type });
	}
}

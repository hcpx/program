package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.CollectResEntity;
import com.scchuangtou.model.Collect;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.MD5Utils;

public class CollectDao {
	
	private static boolean checkCollectObject(DBUtils.ConnectionCache conn, String object_id, int type) throws SQLException {
		if (type == Config.CollectObjectType.COLLECT_OBJECT_TYPE_ARTICLE) {
			return ArticleDao.getArticleInfo(conn, object_id) != null;
		} else if (type == Config.CollectObjectType.COLLECT_OBJECT_TYPE_SUBJECT) {
			return SubjectDao.getSubjcetInfo(conn, object_id) != null;
		} else if (type == Config.CollectObjectType.COLLECT_OBJECT_TYPE_PROJECT) {
			return ProjectDao.getProjectInfo(conn, object_id) != null;
		}else if (type == Config.CollectObjectType.COLLECT_OBJECT_TYPE_ACTIVITY) {
			return ActivityDao.getActivityInfo(conn, object_id) != null;
		}
		return false;
	}
	
	private static String createCollectId(String user_id, String object_id, int type) {
		return MD5Utils.md5((user_id + object_id + type).getBytes(Charset.forName(Config.CHARSET)),
				MD5Utils.MD5Type.MD5_16);
	}
	
	public static CollectResEntity collect(String token, String object_id, int type) throws SQLException {
		CollectResEntity res = new CollectResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			User user = UserDao.getUserByToken(conn, token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!checkCollectObject(conn, object_id, type)) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			String id = createCollectId(user.user_id, object_id, type);
			HashMap<String, Object> datas = new HashMap<>();
			datas.put("collect_id", id);
			datas.put("user_id", user.user_id);
			datas.put("collect_object_id", object_id);
			datas.put("collect_object_type", type);
			datas.put("collect_time", System.currentTimeMillis());
			int row = DBUtils.insert(conn, "insert ignore into collect", datas);
			if(row > 0){
				res.collect_id = id;
				res.status = Config.STATUS_OK;
			}else{
				res.collect_id = id;
				res.status = Config.STATUS_REPEAT_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static BaseResEntity cancelCollect(String token, String object_id, int type) throws SQLException {
		BaseResEntity res = new BaseResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			User user = UserDao.getUserByToken(conn, token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!checkCollectObject(conn, object_id, type)) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			String id = createCollectId(user.user_id, object_id, type);
			DBUtils.executeUpdate(conn,"delete from collect where collect_id=?", new Object[] { id });
			res.status = Config.STATUS_OK;
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static String getUserCollectId(DBUtils.ConnectionCache conn, String id, int type, String user_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT collect_id FROM collect WHERE collect_object_id=? and collect_object_type=? and user_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, id);
			pstat.setObject(2, type);
			pstat.setObject(3, user_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			} else {
				return null;
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	public static Collect getCollect(DBUtils.ConnectionCache conn,String collect_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select collect_id,user_id,collect_object_id,collect_object_type,collect_time from collect where collect_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, collect_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				Collect collect = new Collect();
				collect.collect_id= rs.getString("collect_id");
				collect.user_id= rs.getString("user_id");
				collect.collect_object_id= rs.getString("collect_object_id");
				collect.collect_object_type= rs.getInt("collect_object_type");
				collect.collect_time= rs.getLong("collect_time");
				return collect;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	public static int deleteCollect(DBUtils.ConnectionCache conn, String object_id, int object_type) throws SQLException {
		String sql = "delete from collect where collect_object_id=? and collect_object_type=?";
		return DBUtils.executeUpdate(conn, sql, new Object[] { object_id, object_type });
	}
}

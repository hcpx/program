package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.PraiseResEntity;
import com.scchuangtou.model.ArticleInfo;
import com.scchuangtou.model.CommentInfo;
import com.scchuangtou.model.ProjectInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserValueInfo;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.MD5Utils;
import com.scchuangtou.utils.StringUtils;

public class PraiseDao {

	private static boolean checkPraiseObject(DBUtils.ConnectionCache conn, String object_id, int type, StringBuffer objectUserId)
			throws SQLException {
		if (type == Config.PraiseType.PRAISE_TYPE_ARTICLE) {
			ArticleInfo articleInfo = ArticleDao.getArticleInfo(conn, object_id);
			if (articleInfo != null)
				objectUserId.append(articleInfo.user_id);
			return articleInfo != null;
		} else if (type == Config.PraiseType.PRAISE_TYPE_SUBJECT) {
			return SubjectDao.getSubjcetInfo(conn, object_id) != null;
		} else if (type == Config.PraiseType.PRAISE_TYPE_PROJECT) {
			ProjectInfo mProjectInfo = ProjectDao.getProjectInfo(conn, object_id);
			if (mProjectInfo != null) {
				objectUserId.append(mProjectInfo.user_id);
			}
			return mProjectInfo != null;
		} else if (type == Config.PraiseType.PRAISE_TYPE_COMMENT) {
			CommentInfo mCommentInfo = CommentDao.getCommentInfo(conn, object_id);
			if (mCommentInfo != null) {
				objectUserId.append(mCommentInfo.user_id);
			}
			return mCommentInfo != null;
		}
		return false;
	}

	private static void updatePraiseCount(DBUtils.ConnectionCache conn, int type, String object_id, int count) throws SQLException {
		if (type == Config.PraiseType.PRAISE_TYPE_ARTICLE) {
			ArticleDao.updateCount(conn, object_id, 0, 0, count);
		} else if (type == Config.PraiseType.PRAISE_TYPE_SUBJECT) {
			SubjectDao.updateCount(conn, object_id, 0, 0, count);
		} else if (type == Config.PraiseType.PRAISE_TYPE_PROJECT) {
			ProjectDao.updateCount(conn, object_id, 0, 0, count);
		} else if (type == Config.PraiseType.PRAISE_TYPE_COMMENT) {
			CommentDao.updateCount(conn, object_id, count);
		}
	}

	private static String createPraiseId(String user_id, String object_id, int type) {
		return MD5Utils.md5((user_id + object_id + type).getBytes(Charset.forName(Config.CHARSET)),
				MD5Utils.MD5Type.MD5_16);
	}

	private static int praiseGiving(DBUtils.ConnectionCache conn, String userid) throws Exception {
		UserValueInfo value = new UserValueInfo();
		value.growth = Config.GrowthGiving.RECEIVED_THUMB_UP;
		int row = UserDao.updateUserValue(conn, userid, value, null, null, 0, null);
		return row;
	}

	public static PraiseResEntity praise(String token, String object_id, int type) throws SQLException {
		PraiseResEntity res = new PraiseResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			int row = 0;
			User user = UserDao.getUserByToken(conn, token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer objectUserId = new StringBuffer();
			if (!checkPraiseObject(conn, object_id, type, objectUserId)) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			String objcetuserid = objectUserId.toString();
			if(user.user_id.equals(objcetuserid)){
				return null;
			}
			String id = createPraiseId(user.user_id, object_id, type);
			try {
				DBUtils.beginTransaction(conn);
				HashMap<String, Object> datas = new HashMap<>();
				datas.put("praise_id", id);
				datas.put("praise_object_type", type);
				datas.put("praise_object_id", object_id);
				datas.put("user_id", user.user_id);
				datas.put("praise_time", System.currentTimeMillis());
				row = DBUtils.insert(conn, "insert ignore into praise", datas);
				if (row > 0 && type == Config.PraiseType.PRAISE_TYPE_ARTICLE
						&& !StringUtils.emptyString(objcetuserid)) {
					row = praiseGiving(conn, objcetuserid);
				}
				if (row > 0) {
					updatePraiseCount(conn, type, object_id, 1);
				}
				if (row > 0)
					DBUtils.commitTransaction(conn);
			} catch (Exception e) {
				row = 0;
				LogUtils.log(e);
			}
			if (row > 0){
				res.praise_id = id;
				res.status = Config.STATUS_OK;
			}else{
				res.status = Config.STATUS_REPEAT_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	public static BaseResEntity cancelPraise(String token, String object_id, int type) throws SQLException {
		BaseResEntity res = new BaseResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			User user = UserDao.getUserByToken(conn, token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer objectUserId = new StringBuffer();
			if (!checkPraiseObject(conn, object_id, type, objectUserId)) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			String id = createPraiseId(user.user_id, object_id, type);
			int row = DBUtils.executeUpdate(conn,"delete from praise where praise_id=?", new Object[] { id });
			if (row > 0) {
				updatePraiseCount(conn, type, object_id, -1);
			}
			res.status = Config.STATUS_OK;
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	// public static int getPraiseCount(DBUtils.ConnectionCache conn, String id, int type)
	// throws SQLException {
	// PreparedStatementCache pstat = null;
	// ResultSet rs = null;
	// try {
	// String sql = "SELECT count(praise_id) FROM praise WHERE
	// praise_object_id=? and praise_object_type=?";
	// pstat = conn.prepareStatement(sql);
	// pstat.setString(1, id);
	// pstat.setObject(2, type);
	// rs = pstat.executeQuery();
	// if (rs.next()) {
	// return rs.getInt(1);
	// } else {
	// return 0;
	// }
	// } finally {
	// DBUtils.close(rs);
	// DBUtils.close(pstat);
	// }
	// }

	public static String getUserPraiseId(DBUtils.ConnectionCache conn, String id, int type, String user_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT praise_id FROM praise WHERE praise_object_id=? and praise_object_type=? and user_id=?";
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

	public static int deletePraise(DBUtils.ConnectionCache conn, String object_id, int object_type) throws SQLException {
		String sql = "delete from praise where praise_object_id=? and praise_object_type=?";
		return DBUtils.executeUpdate(conn, sql, new Object[] { object_id, object_type });
	}
	
}

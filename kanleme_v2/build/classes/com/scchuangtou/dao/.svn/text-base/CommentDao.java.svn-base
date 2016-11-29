package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.config.Config;
import com.scchuangtou.config.MessageConfig;
import com.scchuangtou.entity.AddCommentReqEntity;
import com.scchuangtou.entity.AddCommentResEntity;
import com.scchuangtou.entity.GetCommentReqEntity;
import com.scchuangtou.entity.GetCommentResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.model.CommentInfo;
import com.scchuangtou.model.CommentObjectInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserInfo;
import com.scchuangtou.model.UserMessageInfo;
import com.scchuangtou.model.UserValueInfo;
import com.scchuangtou.task.MessageTask;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.DateUtil;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.StringUtils;

public class CommentDao {

	private static GetCommentResEntity.ParentInfo getCommentParentInfo(HttpServletRequest request,
			DBUtils.ConnectionCache conn, String parent_id) throws Exception {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select comment.comment_id,comment.comment_content,comment.comment_time,comment.parent_id,userinfo.user_id,userinfo.nickname,userinfo.head_pic,userinfo.growth from comment INNER JOIN userinfo ON comment.user_id = userinfo.user_id WHERE comment.comment_id =?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, parent_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				GetCommentResEntity.ParentInfo comment = new GetCommentResEntity.ParentInfo();
				comment.comment_id = rs.getString("comment_id");
				comment.comment_time = rs.getLong("comment_time");
				comment.comment_content = rs.getString("comment_content");
				comment.user_info = new UserInfo();
				comment.user_info.user_id = rs.getString("user_id");
				comment.user_info.nickname = rs.getString("nickname");
				comment.user_info.head_pic = rs.getString("head_pic");
				if (!StringUtils.emptyString(comment.user_info.head_pic)) {
					comment.user_info.head_pic = ImageHelper.getImageUrl(request, comment.user_info.head_pic);
				}
				comment.user_info.level = Config.getLevel(rs.getLong("growth"));
				return comment;
			} else {
				return null;
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	public static GetCommentResEntity getComments(HttpServletRequest request, GetCommentReqEntity req)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		GetCommentResEntity res = new GetCommentResEntity();
		try {
			conn = DBUtils.getConnection();
			User user = null;
			if (!StringUtils.emptyString(req.token)) {
				user = UserDao.getUserByToken(conn, req.token);
				if (user == null) {
					res.status = Config.STATUS_TOKEN_ERROR;
					return res;
				}
			}
			StringBuffer sqlsb = new StringBuffer(
					"select comment.comment_id,comment.comment_content,comment.comment_time,comment.parent_id,comment.praise_count,userinfo.user_id,userinfo.nickname,userinfo.head_pic,userinfo.growth from comment INNER JOIN userinfo ON comment.user_id = userinfo.user_id");
			sqlsb.append(" WHERE comment.comment_object_id =? AND comment.comment_object_type =?");
			if (req.order_type == 1) {
				sqlsb.append(" ORDER BY comment.praise_count DESC,comment.comment_time ASC ");
			} else {
				sqlsb.append(" ORDER BY comment.comment_time ASC");
			}
			sqlsb.append(" LIMIT ?,?");
			pstat = conn.prepareStatement(sqlsb.toString());
			req.begin = req.begin < 0 ? 0 : req.begin;
			pstat.setObject(1, req.object_id);
			pstat.setObject(2, req.object_type);
			pstat.setObject(3, req.begin);
			if (req.count > 0) {
				pstat.setObject(4, req.count);
			} else {
				pstat.setObject(4, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			res.status = Config.STATUS_OK;
			GetCommentResEntity.Comment comment = null;
			while (rs.next()) {
				comment = new GetCommentResEntity.Comment();
				comment.comment_id = rs.getString("comment_id");
				comment.comment_time = rs.getLong("comment_time");
				comment.comment_content = rs.getString("comment_content");
				comment.user_info = new UserInfo();
				comment.user_info.user_id = rs.getString("user_id");
				comment.user_info.nickname = rs.getString("nickname");
				comment.user_info.head_pic = rs.getString("head_pic");
				if (!StringUtils.emptyString(comment.user_info.head_pic)) {
					comment.user_info.head_pic = ImageHelper.getImageUrl(request, comment.user_info.head_pic);
				}
				comment.user_info.level = Config.getLevel(rs.getLong("growth"));

				String parent_id = rs.getString("parent_id");
				if (!StringUtils.emptyString(parent_id)) {
					comment.parent_info = getCommentParentInfo(request, conn, parent_id);
				}
				comment.praise_count = rs.getInt("praise_count");
				if (user != null && !user.user_id.equals(comment.user_info.user_id)) {
					comment.praise_id = PraiseDao.getUserPraiseId(conn, comment.comment_id,
							Config.PraiseType.PRAISE_TYPE_COMMENT, user.user_id);
				}
				res.comments.add(comment);
			}
			res.has_more_data = res.comments.size() == Config.ONCE_QUERY_COUNT;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
		return res;
	}

	/**
	 * 获取评论对象信息
	 * 
	 * @param conn
	 * @param comment_object_id
	 * @param comment_object_type
	 * @throws SQLException
	 */
	private static CommentObjectInfo getCommentObjectInfo(DBUtils.ConnectionCache conn, String comment_object_id,
			int comment_object_type) throws SQLException {
		CommentObjectInfo info = null;
		String sql = null;
		if (comment_object_type == Config.CommentObjectType.COMMENT_OBJECT_TYPE_ARTICLE) {
			info = new CommentObjectInfo();
			info.isNotify = true;
			info.messageSourceType = Config.MessageSourceType.MESSAGE_SOURCE_TYPE_ARTICLE;
			sql = "SELECT article.user_id,article.article_title as content,userinfo.os FROM article INNER JOIN userinfo ON article.user_id = userinfo.user_id where article.article_id=? and article.delete_time=0";
		} else if (comment_object_type == Config.CommentObjectType.COMMENT_OBJECT_TYPE_SUBJECT) {
			info = new CommentObjectInfo();
			info.isNotify = false;
			info.messageSourceType = Config.MessageSourceType.MESSAGE_SOURCE_TYPE_SUBJECT;
			sql = "SELECT '' as user_id ,0 as os,subject.subject_content as content FROM `subject` where subject_id=?";
		} else if (comment_object_type == Config.CommentObjectType.COMMENT_OBJECT_TYPE_PROJECT) {
			info = new CommentObjectInfo();
			info.isNotify = true;
			info.messageSourceType = Config.MessageSourceType.MESSAGE_SOURCE_TYPE_PROJECT;
		} else if (comment_object_type == Config.CommentObjectType.COMMENT_OBJECT_TYPE_ACTIVITY) {
			info = new CommentObjectInfo();
			info.isNotify = true;
			info.messageSourceType = Config.MessageSourceType.MESSAGE_SOURCE_TYPE_ACTIVITY;
			sql = "SELECT activity.user_id,activity.activity_name as content,userinfo.os FROM activity INNER JOIN userinfo ON activity.user_id = userinfo.user_id where activity.activity_id=? and activity.delete_time=0";
		} else {
			return null;
		}
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, comment_object_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				info.user_id = rs.getString("user_id");
				info.content = rs.getString("content");
				info.os = rs.getInt("os");
				return info;
			} else {
				return null;
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	private static void updateCommentCount(DBUtils.ConnectionCache conn, int type, String object_id)
			throws SQLException {
		if (type == Config.CommentObjectType.COMMENT_OBJECT_TYPE_ARTICLE) {
			ArticleDao.updateCount(conn, object_id, 1, 0, 0);
		} else if (type == Config.CommentObjectType.COMMENT_OBJECT_TYPE_SUBJECT) {
			SubjectDao.updateCount(conn, object_id, 1, 0, 0);
		} else if (type == Config.CommentObjectType.COMMENT_OBJECT_TYPE_PROJECT) {
			ProjectDao.updateCount(conn, object_id, 1, 0, 0);
		}else if (type == Config.CommentObjectType.COMMENT_OBJECT_TYPE_ACTIVITY) {
			ActivityDao.updateCount(conn, object_id, 1, 0, 0);
		}
	}

	public static CommentInfo getCommentInfo(DBUtils.ConnectionCache conn, String comment_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT comment_id,comment_object_id,comment_object_type, comment_time, comment_content,userinfo.os, userinfo.user_id FROM comment INNER JOIN userinfo ON comment.user_id = userinfo.user_id WHERE comment_id = ?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, comment_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				CommentInfo info = new CommentInfo();
				info.comment_id = rs.getString("comment_id");
				info.comment_object_id = rs.getString("comment_object_id");
				info.comment_object_type = rs.getInt("comment_object_type");
				info.comment_time = rs.getLong("comment_time");
				info.comment_content = rs.getString("comment_content");
				info.user_id = rs.getString("user_id");
				info.os = rs.getInt("os");
				return info;
			} else {
				return null;
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	private static long getLastCommentTime(DBUtils.ConnectionCache conn, String user_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT comment_time FROM comment WHERE user_id = ? order by comment_time desc limit 0,1";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, user_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				return rs.getLong("comment_time");
			} else {
				return 0;
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	private static long addCommentGiving(DBUtils.ConnectionCache conn, AddCommentReqEntity req, User commentUser,
			CommentInfo parentInfo) throws Exception {
		long giving_growth = 0;

		long ctime = System.currentTimeMillis();
		if (parentInfo == null) {
			UserValueInfo value = new UserValueInfo();
			value.growth = Config.GrowthGiving.FIRST_COMMENT;
			StringBuffer where = new StringBuffer();
			where.append("(SELECT count(comment_id) from `comment` where comment_object_id='")
					.append(req.comment_object_id).append("' AND comment_object_type=").append(req.comment_object_type);
			where.append(")=1");
			int r = UserDao.updateUserValue(conn, commentUser.user_id, value, null, where.toString(), 0, null);// 沙发奖励
			if (r > 0) {
				giving_growth = value.growth;
			} else {// 评论奖励
				value = new UserValueInfo();
				value.growth = Config.GrowthGiving.ADD_COMMENT;

				long stime = DateUtil.getDayTime(ctime);
				long etime = DateUtil.getDayEndTime(ctime);
				where = new StringBuffer();
				where.append("(SELECT count(comment_id) FROM `comment` WHERE user_id='").append(commentUser.user_id)
						.append("'");
				where.append(" AND (parent_id ='' or parent_id is NULL)");
				where.append(" AND comment_time>=").append(stime);
				where.append(" AND comment_time<=").append(etime);
				where.append(")<").append(Config.GrowthGivingMax.ADD_COMMENT);
				r = UserDao.updateUserValue(conn, commentUser.user_id, value, null, where.toString(), 0, null);
				if (r > 0) {
					giving_growth = value.growth;
				}
			}
		} else {// 评论回复奖励
			UserValueInfo value = new UserValueInfo();
			value.growth = Config.GrowthGiving.ADD_COMMENT_REPLY;

			long stime = DateUtil.getDayTime(ctime);
			long etime = DateUtil.getDayEndTime(ctime);
			StringBuffer where = new StringBuffer();
			where.append("(SELECT count(comment_id) FROM `comment` WHERE user_id='").append(commentUser.user_id)
					.append("'");
			where.append(" AND parent_id <>''");
			where.append(" AND parent_id IS NOT NULL");
			where.append(" AND comment_time>=").append(stime);
			where.append(" AND comment_time<=").append(etime);
			where.append(")<").append(Config.GrowthGivingMax.ADD_COMMENT_REPLY);
			int r = UserDao.updateUserValue(conn, commentUser.user_id, value, null, where.toString(), 0, null);
			if (r > 0) {
				giving_growth = value.growth;
			}
		}
		return giving_growth;
	}

	private static MessageTask.MessageParam commentNotify(DBUtils.ConnectionCache conn, AddCommentReqEntity req,
			User commentUser, String commentId, CommentObjectInfo commentObjInfo, CommentInfo parentInfo)
					throws Exception {
		UserMessageInfo mUserMessageInfo = new UserMessageInfo();
		mUserMessageInfo.action_user = commentUser.user_id;
		mUserMessageInfo.action_id = commentId;
		mUserMessageInfo.message_content = req.comment_content;

		mUserMessageInfo.source_id = req.comment_object_id;
		mUserMessageInfo.source_type = commentObjInfo.messageSourceType;
		mUserMessageInfo.source_content = commentObjInfo.content;

		MessageTask.MessageOs os;
		if (parentInfo == null) {
			mUserMessageInfo.message_type = Config.MessageType.MESSAGE_TYPE_COMMENT;
			mUserMessageInfo.message_user = commentObjInfo.user_id;
			os = MessageTask.getOs(commentObjInfo.os);
		} else {
			DBUtils.executeUpdate(conn,
					"UPDATE user_message SET is_dispose=? WHERE message_user=? AND action_user=? AND source_id=? AND action_id=?",
					new Object[] { true, commentUser.user_id, parentInfo.user_id, parentInfo.comment_object_id,
							parentInfo.comment_id });

			mUserMessageInfo.message_type = Config.MessageType.MESSAGE_TYPE_COMMENT_REPLY;
			mUserMessageInfo.message_user = parentInfo.user_id;
			mUserMessageInfo.action_content = parentInfo.comment_content;
			os = MessageTask.getOs(parentInfo.os);
		}
		String messageId = UserMessageDao.addUserMessage(conn, mUserMessageInfo);
		if (!StringUtils.emptyString(messageId)) {
			MessageTask.MessageParam mMessageParam = new MessageTask.MessageParam(os, mUserMessageInfo.message_type,
					messageId);
			mMessageParam.addAlias(mUserMessageInfo.message_user);
			mMessageParam.title = MessageConfig.TITLE;
			mMessageParam.description = MessageConfig.getMessageDescription(mUserMessageInfo, commentUser.nickname);
			return mMessageParam;
		}
		return null;
	}

	public static AddCommentResEntity addComment(AddCommentReqEntity req) throws SQLException {

		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();

			User commentUser = UserDao.getUserByToken(conn, req.token);
			if (commentUser == null) {
				AddCommentResEntity res = new AddCommentResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			long ctime = System.currentTimeMillis();
			long t = (commentUser.banned_time - ctime) / 1000;
			if (t > 0) {
				AddCommentResEntity res = new AddCommentResEntity();
				res.status = Config.STATUS_BANNED;
				res.banned_time = t;
				return res;
			}

			long lastCommentTime = getLastCommentTime(conn, commentUser.user_id);
			t = (ctime - lastCommentTime) / 1000;
			if (t > 0 && t < Config.COMMENT_INTERVAL_TIME) {
				AddCommentResEntity res = new AddCommentResEntity();
				res.status = Config.STATUS_REPEAT_ERROR;
				res.comment_countdown = Config.COMMENT_INTERVAL_TIME - t;
				return res;
			}
			CommentObjectInfo commentObjInfo = getCommentObjectInfo(conn, req.comment_object_id,
					req.comment_object_type);
			if (commentObjInfo == null) {
				AddCommentResEntity res = new AddCommentResEntity();
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			CommentInfo parentInfo = null;
			if (!StringUtils.emptyString(req.parent_id)) {
				parentInfo = getCommentInfo(conn, req.parent_id);
				if (parentInfo == null) {
					AddCommentResEntity res = new AddCommentResEntity();
					res.status = Config.STATUS_NOT_EXITS;
					return res;
				}
				if (parentInfo.user_id.equals(commentUser.user_id)) {// 不能对自己的评论进行回复
					return null;
				}
				commentObjInfo.isNotify = true;
			} else {
				if (commentUser.user_id.equals(commentObjInfo.user_id)) {// 自己不能评论自己
					return null;
				}
			}
			String commentId = IdUtils.createId(commentUser.user_id);
			DBUtils.beginTransaction(conn);
			long giving_growth = 0;
			int row = 0;
			try {
				HashMap<String, Object> datas = new HashMap<String, Object>();
				datas.put("comment_id", commentId);
				datas.put("comment_object_id", req.comment_object_id);
				datas.put("comment_object_type", req.comment_object_type);
				datas.put("comment_content", req.comment_content);
				if (!StringUtils.emptyString(req.parent_id))
					datas.put("parent_id", req.parent_id);
				datas.put("user_id", commentUser.user_id);
				datas.put("comment_time", System.currentTimeMillis());

				row = DBUtils.insert(conn, "INSERT INTO comment", datas);
				if (row > 0) {// 评论奖励
					giving_growth = addCommentGiving(conn, req, commentUser, parentInfo);
				}
				if (row > 0) {
					updateCommentCount(conn, req.comment_object_type, req.comment_object_id);
				}
				if (row > 0) {
					if (commentObjInfo.isNotify) {
						MessageTask.MessageParam mMessageParam = commentNotify(conn, req, commentUser, commentId,
								commentObjInfo, parentInfo);
						if (mMessageParam != null) {
							DBUtils.commitTransaction(conn);
							MessageTask.addMessage(mMessageParam);
						} else {
							row = 0;
						}
					} else {
						DBUtils.commitTransaction(conn);
					}
				}
			} catch (Exception e) {
				LogUtils.log(e);
				row = 0;
			}
			AddCommentResEntity res = new AddCommentResEntity();
			if (row > 0) {
				res.status = Config.STATUS_OK;
				res.comment_id = commentId;
				res.giving_growth = giving_growth;
				res.growth = commentUser.growth + giving_growth;
			} else {
				DBUtils.rollbackTransaction(conn);
				res.status = Config.STATUS_SERVER_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	// public static int getCommentCount(DBUtils.ConnectionCache conn, String
	// object_id, int
	// object_type) throws SQLException {
	// PreparedStatementCache pstat = null;
	// ResultSet rs = null;
	// try {
	// String sql = "SELECT count(comment_id) FROM comment WHERE
	// comment_object_id=? and comment_object_type=?";
	// pstat = conn.prepareStatement(sql);
	// pstat.setString(1, object_id);
	// pstat.setObject(2, object_type);
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

	public static void deleteAllComment(DBUtils.ConnectionCache conn, String object_id, int object_type)
			throws SQLException {
		String sql = "DELETE FROM praise WHERE praise_object_type =? AND praise_object_id IN (SELECT comment_id FROM comment WHERE comment_object_id=? AND comment_object_type=?)";
		DBUtils.executeUpdate(conn, sql,
				new Object[] { Config.PraiseType.PRAISE_TYPE_COMMENT, object_id, object_type });// 删除评论的点赞信息

		sql = "delete from comment where comment_object_id=? and comment_object_type=?";
		DBUtils.executeUpdate(conn, sql, new Object[] { object_id, object_type });
	}

	public static int updateCount(DBUtils.ConnectionCache conn, String comment_id, int praiseCount)
			throws SQLException {
		String sql = "UPDATE comment set praise_count=praise_count+? WHERE comment_id=?";
		return DBUtils.executeUpdate(conn, sql, new Object[] { praiseCount, comment_id });
	}
}

package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.scchuangtou.config.Config;
import com.scchuangtou.config.MessageConfig;
import com.scchuangtou.entity.RewardReqEntity;
import com.scchuangtou.entity.RewardResEntity;
import com.scchuangtou.model.RewardObjectInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserMessageInfo;
import com.scchuangtou.model.UserValueInfo;
import com.scchuangtou.task.MessageTask;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.MathUtils;
import com.scchuangtou.utils.StringUtils;

public class RewardDao {
	private static RewardObjectInfo getRewardObjectInfo(DBUtils.ConnectionCache conn, String reward_object_id,
			int reward_object_type) throws SQLException {
		RewardObjectInfo info = null;
		String sql = null;
		if (reward_object_type == Config.RewardObjectType.Article) {
			info = new RewardObjectInfo();
			info.messageSourceType = Config.MessageSourceType.MESSAGE_SOURCE_TYPE_ARTICLE;
			sql = "SELECT article.user_id,article.article_title as content,userinfo.os FROM article INNER JOIN userinfo ON article.user_id = userinfo.user_id where article.article_id=? and article.delete_time=0";
		} else {
			return null;
		}
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, reward_object_id);
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

	private static MessageTask.MessageParam rewardNotify(DBUtils.ConnectionCache conn, RewardReqEntity req,
			RewardObjectInfo objectInfo, User user) throws SQLException {
		UserMessageInfo mUserMessageInfo = new UserMessageInfo();
		mUserMessageInfo.message_user = objectInfo.user_id;
		mUserMessageInfo.message_type = Config.MessageType.MESSAGE_TYPE_REWARD;
		mUserMessageInfo.action_user = user.user_id;
		mUserMessageInfo.message_content = String.valueOf(req.reward_gold);

		mUserMessageInfo.source_id = req.reward_object_id;
		mUserMessageInfo.source_type = objectInfo.messageSourceType;
		mUserMessageInfo.source_content = objectInfo.content;

		String messageId = UserMessageDao.addUserMessage(conn, mUserMessageInfo);
		if (!StringUtils.emptyString(messageId)) {
			MessageTask.MessageOs os = MessageTask.getOs(objectInfo.os);
			MessageTask.MessageParam mMessageParam = new MessageTask.MessageParam(os, mUserMessageInfo.message_type,
					messageId);
			mMessageParam.addAlias(mUserMessageInfo.message_user);
			mMessageParam.title = MessageConfig.TITLE;
			mMessageParam.description = MessageConfig.getMessageDescription(mUserMessageInfo, user.nickname);
			return mMessageParam;
		}
		return null;
	}

	public static RewardResEntity reward(RewardReqEntity req) throws Exception {
		RewardResEntity res = new RewardResEntity();
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!StringUtils.emptyString(user.trade_password)) {
				if (!user.trade_password.equals(req.traders_password)) {
					res.status = Config.STATUS_PASSWORD_ERROR;
					return res;
				}
			}
			if (user.gold < req.reward_gold) {
				res.status = Config.STATUS_GOLD_LACK;
				return res;
			}
			RewardObjectInfo objectInfo = getRewardObjectInfo(conn, req.reward_object_id, req.reward_object_type);
			if (objectInfo == null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			if (user.user_id.equals(objectInfo.user_id)) {
				return null;
			}
			DBUtils.beginTransaction(conn);
			int row = 0;
			try {
				String reward_id = IdUtils.createId(user.user_id);

				HashMap<String, Object> datas = new HashMap<>();
				datas.put("reward_id", reward_id);
				datas.put("reward_obj_user", objectInfo.user_id);
				datas.put("user_id", user.user_id);
				datas.put("time", System.currentTimeMillis());
				datas.put("object_id", req.reward_object_id);
				datas.put("object_type", req.reward_object_type);
				datas.put("gold", req.reward_gold);

				row = DBUtils.insert(conn, "INSERT INTO reward", datas);
				if (row > 0) {
					UserValueInfo value = new UserValueInfo();
					value.gold = -req.reward_gold;
					row = UserDao.updateUserValue(conn, user.user_id, value, null, null,
							Config.GoldChangeType.GOLD_CHANGE_TYPE_REWARD, reward_id);
					if (row <= 0) {
						res.status = Config.STATUS_GOLD_LACK;
					}
				}
				if (row > 0) {
					UserValueInfo value = new UserValueInfo();
					value.gold = req.reward_gold;
					row = UserDao.updateUserValue(conn, objectInfo.user_id, value, null, null,
							Config.GoldChangeType.GOLD_CHANGE_TYPE_REWARD, reward_id);
				}
				if (row > 0) {
					MessageTask.MessageParam mMessageParam = rewardNotify(conn, req, objectInfo, user);
					if (mMessageParam != null) {
						DBUtils.commitTransaction(conn);
						MessageTask.addMessage(mMessageParam);
					} else {
						row = 0;
					}
				}
			} catch (Exception e) {
				LogUtils.log(e);
				row = 0;
			}
			if (row > 0) {
				res.status = Config.STATUS_OK;
				res.gold = MathUtils.sub(user.gold, req.reward_gold);
			} else {
				DBUtils.rollbackTransaction(conn);
				if (StringUtils.emptyString(res.status))
					res.status = Config.STATUS_SERVER_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}
}

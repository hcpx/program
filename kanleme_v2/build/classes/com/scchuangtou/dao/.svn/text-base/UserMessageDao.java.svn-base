package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.DeleteUserMessageReqEntity;
import com.scchuangtou.entity.DeleteUserMessageResEntity;
import com.scchuangtou.entity.GetUserMessageReqEntity;
import com.scchuangtou.entity.GetUserMessageResEntity;
import com.scchuangtou.entity.ListUserMessageReqEntity;
import com.scchuangtou.entity.ListUserMessageResEntity;
import com.scchuangtou.entity.UserMessageEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserInfo;
import com.scchuangtou.model.UserMessageInfo;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.StringUtils;

public class UserMessageDao {
	public static String addUserMessage(DBUtils.ConnectionCache conn, UserMessageInfo info) throws SQLException {
		if (StringUtils.emptyString(info.message_user) || StringUtils.emptyString(info.message_content)) {
			throw new IllegalArgumentException("message user is not null");
		}
		String message_id = IdUtils.createId(info.message_user);
		long time = System.currentTimeMillis();

		HashMap<String, Object> datas = new HashMap<String, Object>();
		datas.put("message_id", message_id);
		datas.put("message_content", info.message_content);
		datas.put("message_user", info.message_user);
		datas.put("message_type", info.message_type);
		datas.put("message_time", time);
		datas.put("is_dispose", false);

		if (!StringUtils.emptyString(info.action_user))
			datas.put("action_user", info.action_user);
		if (!StringUtils.emptyString(info.action_id))
			datas.put("action_id", info.action_id);
		if (!StringUtils.emptyString(info.action_content))
			datas.put("action_content", info.action_content);

		datas.put("source_type", info.source_type);
		if (!StringUtils.emptyString(info.source_id))
			datas.put("source_id", info.source_id);
		if (!StringUtils.emptyString(info.source_content))
			datas.put("source_content", info.source_content);

		int row = DBUtils.insert(conn, "INSERT INTO user_message", datas);
		if (row > 0) {
			return message_id;
		} else {
			return null;
		}
	}

	public static DeleteUserMessageResEntity deleteUserMessage(DeleteUserMessageReqEntity reqEntity)
			throws SQLException {
		DeleteUserMessageResEntity res = new DeleteUserMessageResEntity();
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, reqEntity.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			DBUtils.executeUpdate(conn, "delete from user_message where message_user=? AND message_id=?",
					new Object[] { user.user_id, reqEntity.message_id });
			res.status = Config.STATUS_OK;
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	public static ListUserMessageResEntity listUserMessage(HttpServletRequest request,
			ListUserMessageReqEntity reqEntity) throws Exception {
		ListUserMessageResEntity res = new ListUserMessageResEntity();
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, reqEntity.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			reqEntity.begin = reqEntity.begin < 0 ? 0 : reqEntity.begin;
			res.status = Config.STATUS_OK;
			StringBuffer orderSB = new StringBuffer();
			orderSB.append("ORDER BY user_message.message_time DESC LIMIT ").append(reqEntity.begin).append(",")
					.append(Config.ONCE_QUERY_COUNT);
			String where;
			if (reqEntity.type == 0) {
				where = new StringBuffer().append("user_message.message_type <>")
						.append(Config.MessageType.MESSAGE_TYPE_COMMENT).append(" AND user_message.message_type <>")
						.append(Config.MessageType.MESSAGE_TYPE_COMMENT_REPLY).toString();
			} else {
				where = new StringBuffer().append("(user_message.message_type =")
						.append(Config.MessageType.MESSAGE_TYPE_COMMENT).append(" or user_message.message_type =")
						.append(Config.MessageType.MESSAGE_TYPE_COMMENT_REPLY).append(")").toString();
			}
			res.messages = getUserMessage(request, conn, user.user_id, where, null, orderSB.toString());
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	public static GetUserMessageResEntity getUserMessageByID(HttpServletRequest request,
			GetUserMessageReqEntity reqEntity) throws Exception {
		GetUserMessageResEntity res = new GetUserMessageResEntity();
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, reqEntity.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			String where = "user_message.message_id=?";
			List<UserMessageEntity> messages = getUserMessage(request, conn, user.user_id, where,
					new Object[] { reqEntity.message_id }, null);
			if (messages != null && !messages.isEmpty()) {
				res.status = Config.STATUS_OK;
				res.messages = messages.get(0);
			} else {
				res.status = Config.STATUS_NOT_EXITS;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	private static List<UserMessageEntity> getUserMessage(HttpServletRequest request, DBUtils.ConnectionCache conn,
			String userid, String where, Object[] whereVlaue, String oder) throws Exception {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(
					"select user_message.message_id,user_message.message_content,user_message.message_user,user_message.message_type,user_message.message_time,user_message.action_user,user_message.action_content,user_message.source_content,user_message.source_type,user_message.source_id,user_message.action_id,user_message.is_dispose from user_message where user_message.message_user=?");
			if (!StringUtils.emptyString(where)) {
				sb.append(" AND ").append(where);
			}
			if (!StringUtils.emptyString(oder)) {
				sb.append(" ").append(oder);
			}
			pstat = conn.prepareStatement(sb.toString());
			int index = 1;
			pstat.setObject(index, userid);
			index++;
			if (whereVlaue != null) {
				for (int i = 0; i < whereVlaue.length; i++) {
					pstat.setObject(index, whereVlaue[i]);
					index++;
				}
			}
			rs = pstat.executeQuery();

			List<UserMessageEntity> messages = new ArrayList<UserMessageEntity>();
			UserMessageEntity mUserMessage = null;
			while (rs.next()) {
				mUserMessage = new UserMessageEntity();
				String action_user = rs.getString("action_user");
				if (!StringUtils.emptyString(action_user)) {
					mUserMessage.action_user = new UserInfo();
					User user = UserDao.getUser(conn, action_user);
					if (user != null) {
						mUserMessage.action_user.user_id = user.user_id;
						mUserMessage.action_user.nickname = user.nickname;
						if (!StringUtils.emptyString(user.head_pic)) {
							mUserMessage.action_user.head_pic = ImageHelper.getImageUrl(request, user.head_pic);
						}
						mUserMessage.action_user.level = Config.getLevel(user.growth);
					}
				}
				mUserMessage.message_id = rs.getString("message_id");
				mUserMessage.message_content = rs.getString("message_content");
				mUserMessage.message_type = rs.getInt("message_type");
				mUserMessage.message_time = rs.getLong("message_time");
				mUserMessage.source_content = rs.getString("source_content");
				mUserMessage.source_type = rs.getInt("source_type");
				mUserMessage.source_id = rs.getString("source_id");
				mUserMessage.action_id = rs.getString("action_id");
				mUserMessage.action_content = rs.getString("action_content");
				mUserMessage.is_dispose = rs.getBoolean("is_dispose");

				messages.add(mUserMessage);
			}
			return messages;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
}

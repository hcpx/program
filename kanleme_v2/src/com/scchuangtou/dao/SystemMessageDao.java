package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.scchuangtou.config.AdminConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.config.MessageConfig;
import com.scchuangtou.entity.AddSystemMessageReqEntity;
import com.scchuangtou.entity.AddSystemMessageResEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.DeleteSystemMsgReqEntity;
import com.scchuangtou.entity.EditSystemMsgReqEntity;
import com.scchuangtou.entity.ListSystemMsgReqEntity;
import com.scchuangtou.entity.ListSystemMsgResEntity;
import com.scchuangtou.entity.SystemMessageEntity;
import com.scchuangtou.model.AdminInfo;
import com.scchuangtou.task.MessageTask;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.StringUtils;

public class SystemMessageDao {
	public static AddSystemMessageResEntity addSystemMessage(AddSystemMessageReqEntity req, String admin_token)
			throws SQLException {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			AdminInfo mAdminInfo = AdminDao.getAdminByToken(conn, admin_token);
			if (mAdminInfo == null) {
				AddSystemMessageResEntity res = new AddSystemMessageResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!mAdminInfo.hasMode(AdminConfig.MODE_MANAGER_MESSAGE)) {
				AddSystemMessageResEntity res = new AddSystemMessageResEntity();
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}

			HashMap<String, Object> datas = new HashMap<>();
			String id = IdUtils.createId(mAdminInfo.admin_user);
			long time = System.currentTimeMillis();
			datas.put("message_id", id);
			datas.put("message_title", req.message_title);
			datas.put("message_content", req.message_content);
			datas.put("message_time", time);

			int row = DBUtils.insert(conn, "INSERT INTO system_message", datas);

			AddSystemMessageResEntity res = new AddSystemMessageResEntity();
			if (row > 0) {
				res.status = Config.STATUS_OK;

				MessageTask.MessageParam mMessageParam = new MessageTask.MessageParam(MessageTask.MessageOs.ALL,
						Config.MessageType.MESSAGE_TYPE_SYSTEM, id);
				mMessageParam.title = MessageConfig.TITLE;
				mMessageParam.description = req.message_title;
				MessageTask.addMessage(mMessageParam);
			} else {
				res.status = Config.STATUS_SERVER_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	public static BaseResEntity editSystemMessage(EditSystemMsgReqEntity req, String admin_token) throws SQLException {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			AdminInfo mAdminInfo = AdminDao.getAdminByToken(conn, admin_token);
			if (mAdminInfo == null) {
				AddSystemMessageResEntity res = new AddSystemMessageResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!mAdminInfo.hasMode(AdminConfig.MODE_MANAGER_MESSAGE)) {
				AddSystemMessageResEntity res = new AddSystemMessageResEntity();
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}

			int row = DBUtils.executeUpdate(conn,
					"UPDATE system_message SET message_title=?,message_content=? WHERE message_id=?",
					new Object[] { req.message_title, req.message_content, req.message_id });

			BaseResEntity res = new BaseResEntity();
			if (row > 0) {
				res.status = Config.STATUS_OK;
			} else {
				res.status = Config.STATUS_NOT_EXITS;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	public static ListSystemMsgResEntity listSystemMessages(ListSystemMsgReqEntity req) throws SQLException {
		ListSystemMsgResEntity res = new ListSystemMsgResEntity();
		res.status = Config.STATUS_OK;

		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			StringBuffer sb = new StringBuffer(
					"SELECT message_id,message_title,message_content,message_time FROM system_message");
			sb.append(" WHERE 1=1 ");
			if (!StringUtils.emptyString(req.keyword)) {
				sb.append(" AND message_title like '%").append(req.keyword).append("%'");
			}
			sb.append(" ORDER BY message_time DESC LIMIT ?,?");
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.begin);
			pstat.setObject(2, Config.ONCE_QUERY_COUNT);
			rs = pstat.executeQuery();
			res.messages = new ArrayList<>();
			while (rs.next()) {
				SystemMessageEntity info = new SystemMessageEntity();
				info.message_id = rs.getString("message_id");
				info.message_title = rs.getString("message_title");
				info.message_content = rs.getString("message_content");
				info.message_time = rs.getLong("message_time");
				res.messages.add(info);
			}
			res.has_more_data = res.messages.size() >= Config.ONCE_QUERY_COUNT;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
		return res;
	}

	public static BaseResEntity deleteSystemMessages(DeleteSystemMsgReqEntity req, String admin_token)
			throws SQLException {
		BaseResEntity res = new BaseResEntity();

		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();

			AdminInfo mAdminInfo = AdminDao.getAdminByToken(conn, admin_token);
			if (mAdminInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!mAdminInfo.hasMode(AdminConfig.MODE_MANAGER_MESSAGE)) {
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			DBUtils.beginTransaction(conn);
			int row = 0;
			try {
				String sql = "DELETE FROM system_message WHERE message_id=?";
				for (String message_id : req.message_ids) {
					row = DBUtils.executeUpdate(conn,sql, new Object[] { message_id });
					if (row <= 0) {
						break;
					}
				}
				if (row > 0) {
					DBUtils.commitTransaction(conn);
				}
			} catch (Exception e) {
				LogUtils.log(e);
			}
			if (row > 0) {
				res.status = Config.STATUS_OK;
			} else {
				res.status = Config.STATUS_SERVER_ERROR;
				DBUtils.rollbackTransaction(conn);
			}
		} finally {
			DBUtils.close(conn);
		}
		return res;
	}
}

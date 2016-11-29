package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.FocusResEntity;
import com.scchuangtou.model.FocusInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.MD5Utils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;

public class FocusDao {
	
	/**
	 * 关注
	 * @param req
	 * @return
	 * @throws SQLException
	 */
	public static FocusResEntity focus(String token,String focused_user_id) throws SQLException {
		FocusResEntity res = new FocusResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			User user = UserDao.getUserByToken(conn, token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if(user.user_id.equals(focused_user_id))
				return null;
			User focused_user = UserDao.getUser(conn, focused_user_id);
			if (focused_user == null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			String id = createFocusId(user.user_id, focused_user_id);
			HashMap<String, Object> datas = new HashMap<>();
			datas.put("focus_id", id);
			datas.put("focus_user_id", user.user_id);
			datas.put("focused_user_id", focused_user_id);
			datas.put("focus_time", System.currentTimeMillis());
			int row = DBUtils.insert(conn, "insert ignore into focus", datas);
			if(row > 0){
				res.focus_id = id;
				res.status = Config.STATUS_OK;
			}else{
				res.focus_id = id;
				res.status = Config.STATUS_REPEAT_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}
	
	private static String createFocusId(String user_id, String focused_user_id) {
		return MD5Utils.md5((user_id + focused_user_id).getBytes(Charset.forName(Config.CHARSET)),
				MD5Utils.MD5Type.MD5_16);
	}
	
	public static BaseResEntity cancelFocus(String token,String focused_user_id) throws SQLException {
		BaseResEntity res = new BaseResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		int row=0;
		try {
			User user = UserDao.getUserByToken(conn, token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			User focused_user = UserDao.getUserByToken(conn, focused_user_id);
			if (focused_user == null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			String id = createFocusId(user.user_id, focused_user_id);
			row=DBUtils.executeUpdate(conn,"delete from focus where focus_id=?", new Object[] { id });
			if(row>0)
				res.status = Config.STATUS_OK;
			else
				res.status = Config.STATUS_NOT_EXITS;
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static FocusInfo getFocusInfo(DBUtils.ConnectionCache conn, String focusInfoID) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select focus_id,focus_user_id,focused_user_id,focus_time from focus where focus_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, focusInfoID);
			rs = pstat.executeQuery();
			if (rs.next()) {
				FocusInfo articleInfo = new FocusInfo();
				articleInfo.focus_id = rs.getString("focus_id");
				articleInfo.focus_user_id = rs.getString("focus_user_id");
				articleInfo.focused_user_id = rs.getString("focused_user_id");
				articleInfo.focus_time = rs.getLong("focus_time");
				return articleInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	public static FocusInfo getFocusInfo(DBUtils.ConnectionCache conn, String user_id, String focused_user_id ) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		String focusInfoID=createFocusId(user_id, focused_user_id);
		try {
			String sql = "select focus_id,focus_user_id,focused_user_id,focus_time from focus where focus_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, focusInfoID);
			rs = pstat.executeQuery();
			if (rs.next()) {
				FocusInfo articleInfo = new FocusInfo();
				articleInfo.focus_id = rs.getString("focus_id");
				articleInfo.focus_user_id = rs.getString("focus_user_id");
				articleInfo.focused_user_id = rs.getString("focused_user_id");
				articleInfo.focus_time = rs.getLong("focus_time");
				return articleInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
}

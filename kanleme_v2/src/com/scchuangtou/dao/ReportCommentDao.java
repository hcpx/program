package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.ReportCommentReqEntity;
import com.scchuangtou.entity.ReportCommentResEntity;
import com.scchuangtou.model.CommentInfo;
import com.scchuangtou.model.ReportCommentInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.MD5Utils;

public class ReportCommentDao {

	private static String createReportCommenId(String user_id, String comment_id) {
		return MD5Utils.md5((user_id + comment_id).getBytes(Charset.forName(Config.CHARSET)), MD5Utils.MD5Type.MD5_16);
	}

	public static ReportCommentResEntity report(ReportCommentReqEntity req) throws Exception {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			// 判断token过期user为空，设置TOKEN_ERROR直接返回
			if (user == null) {
				ReportCommentResEntity res = new ReportCommentResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			CommentInfo comment = CommentDao.getCommentInfo(conn, req.comment_id);
			if (comment == null) {
				ReportCommentResEntity res = new ReportCommentResEntity();
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			ReportCommentResEntity reportRes = new ReportCommentResEntity();
			String report_comment_id = createReportCommenId(user.user_id, req.comment_id);
			int row = 0;
			HashMap<String, Object> datas = new HashMap<String, Object>();
			datas.put("report_comment_id", report_comment_id);
			datas.put("comment_id", req.comment_id);
			datas.put("user_id", user.user_id);
			datas.put("report_time", System.currentTimeMillis());
			row = DBUtils.insert(conn, "INSERT ignore INTO report_comment", datas);
			if (row > 0) {
				reportRes.status = Config.STATUS_OK;
			} else {
				reportRes.status = Config.STATUS_REPEAT_ERROR;
			}
			return reportRes;
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static ReportCommentInfo getReportCommentInfo(DBUtils.ConnectionCache conn, String report_comment_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT report_comment_id,comment_id,user_id, report_time FROM report_comment WHERE report_comment_id = ?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, report_comment_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				ReportCommentInfo info = new ReportCommentInfo();
				info.comment_id = rs.getString("comment_id");
				info.report_comment_id = rs.getString("report_comment_id");
				info.user_id = rs.getString("user_id");
				info.report_time = rs.getLong("report_time");
				return info;
			} else {
				return null;
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
}

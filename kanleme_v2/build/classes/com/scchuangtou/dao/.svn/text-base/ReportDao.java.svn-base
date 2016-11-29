package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.entity.ReportReqEntity;
import com.scchuangtou.entity.ReportResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.model.ActivityInfo;
import com.scchuangtou.model.ArticleInfo;
import com.scchuangtou.model.ProjectInfo;
import com.scchuangtou.model.SubjectInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.MD5Utils;
import com.scchuangtou.utils.StringUtils;

public class ReportDao {

	private static String createReportId(String user_id, String object_id, int type) {
		return MD5Utils.md5((user_id + object_id + type).getBytes(Charset.forName(Config.CHARSET)),
				MD5Utils.MD5Type.MD5_16);
	}

	private static String getImageDir(int object_type, String object_id) {
		return new StringBuffer().append("report/").append(object_type).append(object_id).toString();
	}

	private static String getImageName(int object_type, String object_id) {
		return new StringBuffer(getImageDir(object_type, object_id)).append("/").append(IdUtils.createId(object_id))
				.toString();
	}

	private static String getReportObjectId(DBUtils.ConnectionCache conn, int type, String report_object_id)
			throws Exception {
		if (type == Config.ReportObjectType.REPORT_OBJECT_TYPE_ARTICLE) {
			ArticleInfo info = ArticleDao.getArticleInfo(conn, report_object_id);
			return info != null ? info.article_id : null;
		} else if (type == Config.ReportObjectType.REPORT_OBJECT_TYPE_SUBJECT) {
			SubjectInfo info = SubjectDao.getSubjcetInfo(conn, report_object_id);
			return info != null ? info.subject_id : null;
		} else if (type == Config.ReportObjectType.REPORT_OBJECT_TYPE_PROJECT) {
			ProjectInfo info = ProjectDao.getProjectInfo(conn, report_object_id);
			return info != null ? info.help_each_id : null;
		} else if (type == Config.ReportObjectType.REPORT_OBJECT_TYPE_ACTIVITY) {
			ActivityInfo info = ActivityDao.getActivityInfo(conn, report_object_id);
			return info != null ? info.activity_id : null;
		} else {
			return null;
		}
	}

	public static ReportResEntity report(ReportReqEntity req, List<MyMutiPart> picParts) throws Exception {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			ReportResEntity reportRes = new ReportResEntity();
			User user = UserDao.getUserByToken(conn, req.token);
			// 判断token过期user为空，设置TOKEN_ERROR直接返回
			if (user == null) {
				ReportResEntity res = new ReportResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			String object_id = getReportObjectId(conn, req.report_object_type, req.report_object_id);
			if (StringUtils.emptyString(object_id)) {
				ReportResEntity res = new ReportResEntity();
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			String report_id = createReportId(user.user_id, req.report_object_id, req.report_object_type);
			Map<String, MyMutiPart> parts = new HashMap<>();
			if (picParts != null) {
				for (int i = 0; i < picParts.size(); i++) {
					String imageName = getImageName(req.report_object_type, object_id);
					parts.put(imageName, picParts.get(i));
				}
			}
			int row = 0;
			try {
				DBUtils.beginTransaction(conn);
				HashMap<String, Object> datas = new HashMap<String, Object>();
				datas.put("report_id", report_id);
				datas.put("user_id", user.user_id);
				datas.put("report_object_id", req.report_object_id);
				datas.put("report_object_type", req.report_object_type);
				datas.put("report_content", req.report_content);
				datas.put("report_time", System.currentTimeMillis());
				if (parts.size() > 0) {
					datas.put("report_imgs", JSON.toJSONString(parts.keySet()));
				}
				row = DBUtils.insert(conn, "INSERT ignore INTO report", datas);
				if (row > 0) {
					if (parts.size() > 0 && ImageHelper.upload(parts) == null) {
						row = 0;
					}
				} else {
					reportRes.status = Config.STATUS_REPEAT_ERROR;
					return reportRes;
				}
				if (row > 0)
					DBUtils.commitTransaction(conn);
			} catch (Exception e) {
				row = 0;
				LogUtils.log(e);
			}
			if (row > 0) {
				reportRes.status = Config.STATUS_OK;
			} else {
				DBUtils.rollbackTransaction(conn);
				if (StringUtils.emptyString(reportRes.status))
					reportRes.status = Config.STATUS_SERVER_ERROR;
			}
			return reportRes;
		} finally {
			DBUtils.close(conn);
		}
	}

	public static int deleteObjectReport(DBUtils.ConnectionCache conn, String object_id, int object_type)
			throws SQLException {
		String sql = "delete from report where report_object_id=? and report_object_type=?";
		return DBUtils.executeUpdate(conn, sql, new Object[] { object_id, object_type });
	}

	public static void deleteObjectReportImages(String object_id, int object_type) {
		ImageHelper.deleteByDir(getImageDir(object_type, object_id));
	}
}

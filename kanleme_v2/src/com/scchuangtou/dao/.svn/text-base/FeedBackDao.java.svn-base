package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.config.AdminConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.entity.DeleteFeedbackReqEntity;
import com.scchuangtou.entity.DeleteFeedbackResEntity;
import com.scchuangtou.entity.FeedBackReqEntity;
import com.scchuangtou.entity.FeedBackResEntity;
import com.scchuangtou.entity.ListFeedbackReqEntity;
import com.scchuangtou.entity.ListFeedbackResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.model.AdminInfo;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.StringUtils;

public class FeedBackDao {

	private static String getImageName(String feed_back_id) {
		return new StringBuffer().append("feedback/").append(feed_back_id).toString();
	}

	public static FeedBackResEntity addFeedBack(FeedBackReqEntity req, MyMutiPart picPart) throws SQLException {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();

			String feed_back_id = IdUtils.createId("feed_back");
			String picName = null;

			HashMap<String, Object> datas = new HashMap<String, Object>();
			datas.put("feed_back_id", feed_back_id);
			datas.put("feed_back_content", req.content);
			datas.put("contact", req.contact);
			datas.put("os", req.os);
			datas.put("os_version", req.os_version);
			datas.put("phone_model", req.phone_model);
			datas.put("time", System.currentTimeMillis());
			if (picPart != null) {
				picName = getImageName(feed_back_id);
				datas.put("img", picName);
			}
			int row = 0;
			try {
				DBUtils.beginTransaction(conn);
				row = DBUtils.insert(conn, "INSERT INTO feed_back", datas);
				if (row > 0 && picPart != null && ImageHelper.upload(picName, picPart, true) == null) {
					row = 0;
				}
				if (row > 0) {
					DBUtils.commitTransaction(conn);
				}
			} catch (Exception e) {
				LogUtils.log(e);
				row = 0;
			}
			FeedBackResEntity res = new FeedBackResEntity();
			if (row > 0) {
				res.status = Config.STATUS_OK;
			} else {
				DBUtils.rollbackTransaction(conn);
				res.status = Config.STATUS_SERVER_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	/****
	 * 查询功能
	 * 
	 * @throws SQLException
	 ***/
	public static ListFeedbackResEntity adminListFeedBack(HttpServletRequest request, ListFeedbackReqEntity req)
			throws Exception {
		ListFeedbackResEntity res = new ListFeedbackResEntity();
		List<ListFeedbackResEntity.Data> feedList = new ArrayList<ListFeedbackResEntity.Data>();
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			AdminInfo mAdminInfo = AdminDao.getAdminByToken(conn, req.admin_token);
			if (mAdminInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!mAdminInfo.hasMode(AdminConfig.MODE_MANAGER_FEEDBACK)) {
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}

			req.begin = req.begin < 0 ? 0 : req.begin;
			String sql = "select feed_back_id,feed_back_content,contact,os,os_version,phone_model,time,img from feed_back order by time desc limit ?,?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, req.begin);
			pstat.setObject(2, Config.ONCE_QUERY_COUNT);
			rs = pstat.executeQuery();
			while (rs.next()) {
				ListFeedbackResEntity.Data feedbackInfo = new ListFeedbackResEntity.Data();
				feedbackInfo.feed_back_id = rs.getString("feed_back_id");
				feedbackInfo.feed_back_content = rs.getString("feed_back_content");
				feedbackInfo.contact = rs.getString("contact");
				feedbackInfo.os = rs.getString("os");
				feedbackInfo.os_version = rs.getString("os_version");
				feedbackInfo.phone_model = rs.getString("phone_model");
				feedbackInfo.time = rs.getLong("time");
				feedbackInfo.img = rs.getString("img");
				if (!StringUtils.emptyString(feedbackInfo.img)) {
					feedbackInfo.img = ImageHelper.getImageUrl(request, feedbackInfo.img);
				}
				feedList.add(feedbackInfo);
			}
			res.datas = feedList;
			res.has_more_data = feedList.size() == Config.ONCE_QUERY_COUNT;
			res.status = Config.STATUS_OK;
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	/****
	 * 删除功能
	 * 
	 * @throws SQLException
	 ****/
	public static DeleteFeedbackResEntity adminDeleteFeedBack(DeleteFeedbackReqEntity req,String admin_token) throws SQLException {
		DeleteFeedbackResEntity res = new DeleteFeedbackResEntity();
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			AdminInfo adminInfo = AdminDao.getAdminByToken(conn, admin_token);
			if (adminInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!adminInfo.hasMode(AdminConfig.MODE_MANAGER_FEEDBACK)) {
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			DBUtils.beginTransaction(conn);
			int row = 0;
			try {
				String sql = "DELETE FROM feed_back WHERE feed_back_id =?";
				for (String feed_back_id : req.feed_back_ids) {
					row = DBUtils.executeUpdate(conn,sql, new Object[] { feed_back_id });
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
				for (String feed_back_id : req.feed_back_ids) {
					ImageHelper.deleteByName(getImageName(feed_back_id));
				}
				res.status = Config.STATUS_OK;
			} else {
				res.status = Config.STATUS_SERVER_ERROR;
				DBUtils.rollbackTransaction(conn);
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}
}
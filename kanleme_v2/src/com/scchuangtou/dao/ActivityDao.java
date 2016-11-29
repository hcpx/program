package com.scchuangtou.dao;

import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.entity.DeleteActivityReqEntity;
import com.scchuangtou.entity.DeleteActivityResEntity;
import com.scchuangtou.entity.GetActivityDetailReqEntity;
import com.scchuangtou.entity.GetActivityDetailResEntity;
import com.scchuangtou.entity.GetActivitysReqEntity;
import com.scchuangtou.entity.GetActivitysResEntity;
import com.scchuangtou.entity.GetCollectActivitysReqEntity;
import com.scchuangtou.entity.GetCollectActivitysResEntity;
import com.scchuangtou.entity.GetMyActivitysReqEntity;
import com.scchuangtou.entity.GetMyActivitysResEntity;
import com.scchuangtou.entity.GetParticipationActivitysReqEntity;
import com.scchuangtou.entity.GetParticipationActivitysResEntity;
import com.scchuangtou.entity.PublishActivityReqEntity;
import com.scchuangtou.entity.PublishActivityResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.model.ActivityInfo;
import com.scchuangtou.model.ArticleImageInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.StringUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;

public class ActivityDao {
	public static int ACTIVITY_STATUS_NORMAL = 0; // 正常状态
	public static int ACTIVITY_STATUS_BARRED = -1; // 禁止访问状态
	public static int ACTIVITY_DELTE_TIME_NORMAL = 0; // 没有被删除

	/**
	 * app发布活动
	 * 
	 * @param req
	 * @param picPart
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static PublishActivityResEntity addActivity(PublishActivityReqEntity req, MyMutiPart picPart,
			HttpServletRequest request, List<MyMutiPart> activityPicPart) throws Exception {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				PublishActivityResEntity res = new PublishActivityResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			long t = (user.banned_time - System.currentTimeMillis()) / 1000;
			if (t > 0) {
				PublishActivityResEntity res = new PublishActivityResEntity();
				res.status = Config.STATUS_BANNED;
				res.banned_time = t;
				return res;
			}
			if (StringUtils.emptyString(user.phone_number)) {
				PublishActivityResEntity res = new PublishActivityResEntity();
				res.status = Config.STATUS_NOT_BIND_PHONE;
				return res;
			}
			if (user.certification != Config.CertificationStauts.CERTIFICATION_STATUS_PASS) {
				PublishActivityResEntity res = new PublishActivityResEntity();
				res.status = Config.STATUS_NOT_IDENTIFICATION;
				return res;
			}
			return addActivity(conn, picPart, user, req.activity_name, req.address, req.activity_content,
					req.organizer_phone, req.begin_time, req.end_time, req.limit_num, req.activity_type,
					req.activity_lable, request, activityPicPart, req.activity_pic_desc);
		} finally {
			DBUtils.close(conn);
		}
	}

	/**
	 * 后台发布活动
	 * 
	 * @param adminToken
	 * @param picPart
	 * @param user_id
	 * @param activity_name
	 * @param address
	 * @param organizer_phone
	 * @param begin_time
	 * @param end_time
	 * @param limit_num
	 * @param activity_type
	 * @param activity_lable
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static PublishActivityResEntity adminaddActivity(String adminToken, MyMutiPart picPart, String user_id,
			String activity_name, String address, String activity_content, String organizer_phone, long begin_time,
			long end_time, int limit_num, String[] activity_type, String[] activity_lable, HttpServletRequest request,
			List<MyMutiPart> activityPicPart, PublishActivityReqEntity.Pic[] activity_pic_desc) throws Exception {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			if (AdminDao.getAdminByToken(conn, adminToken) == null) {
				PublishActivityResEntity res = new PublishActivityResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			return addActivity(conn, picPart, null, activity_name, address, activity_content, organizer_phone,
					begin_time, end_time, limit_num, activity_type, activity_lable, request, activityPicPart,
					activity_pic_desc);
		} finally {
			DBUtils.close(conn);
		}
	}

	/**
	 * 发布活动方法
	 * 
	 * @param conn
	 * @param picPart
	 * @param user_id
	 * @param activity_name
	 * @param address
	 * @param organizer_phone
	 * @param begin_time
	 * @param end_time
	 * @param limit_num
	 * @param activity_type
	 * @param activity_lable
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private static PublishActivityResEntity addActivity(DBUtils.ConnectionCache conn, MyMutiPart picPart, User user,
			String activity_name, String address, String activity_content, String organizer_phone, long begin_time,
			long end_time, int limit_num, String[] activity_type, String[] activity_lable, HttpServletRequest request,
			List<MyMutiPart> activityPicPart, PublishActivityReqEntity.Pic[] activity_pic_desc) throws Exception {
		String id = IdUtils.createId(user.user_id);
		int row = 0;
		String imageName = null;
		List<PublishActivityResEntity.img> activityPics = null;
		try {
			DBUtils.beginTransaction(conn);
			HashMap<String, Object> datas = new HashMap<>();
			datas.put("activity_id", id);
			datas.put("user_id", user.user_id);
			datas.put("activity_name", activity_name);
			datas.put("begin_time", begin_time);
			datas.put("end_time", end_time);
			datas.put("activity_content", activity_content);
			if (activity_type != null && activity_type.length != 0)
				datas.put("activity_type", JSON.toJSONString(activity_type));
			if (activity_type != null && activity_type.length != 0)
				datas.put("activity_lable", JSON.toJSONString(activity_lable));
			datas.put("address", address);
			datas.put("organizer_phone", organizer_phone);
			if (picPart != null) {
				imageName = getImageName(user.user_id);
				datas.put("poster_img", imageName);
			}
			datas.put("limit_num", limit_num);
			datas.put("sign_up_num", 0);
			datas.put("status", ACTIVITY_STATUS_NORMAL);
			datas.put("time", System.currentTimeMillis());
			row = DBUtils.insert(conn, "INSERT INTO activity", datas);
			if (imageName != null) {
				Map<String, MyMutiPart> parts = new HashMap<>();
				parts.put(imageName, picPart);
				if (row > 0 && ImageHelper.upload(parts) == null) {
					row = 0;
				}
			}
			long time = System.currentTimeMillis();
			if (row > 0 && activityPicPart != null) {
				activityPics = new ArrayList<PublishActivityResEntity.img>();
				for (int i = 0; i < activityPicPart.size(); i++) {
					// 图片信息
					imageName = getImageName(id);
					Dimension mDimension = ImageHelper.upload(imageName, activityPicPart.get(i), true);
					if (mDimension == null) {
						row = 0;
						break;
					}
					PublishActivityResEntity.img articleImageInfo = new PublishActivityResEntity.img();
					articleImageInfo.activity_image_description = activity_pic_desc[i].activity_pic_des;
					articleImageInfo.activity_image_url = ImageHelper.getImageUrl(request, imageName);
					activityPics.add(articleImageInfo);

					datas = new HashMap<String, Object>();
					datas.put("activity_image_id", IdUtils.createId("activity_image_id"));
					datas.put("activity_image_url", imageName);
					datas.put("activity_image_description", articleImageInfo.activity_image_description);
					datas.put("activity_time", time++);
					datas.put("width", mDimension.width);
					datas.put("height", mDimension.height);
					datas.put("activity_id", id);
					row = DBUtils.insert(conn, "INSERT INTO activity_image", datas);
					if (row <= 0) {
						break;
					}
				}
			}
			if (row > 0) {
				DBUtils.commitTransaction(conn);
			}
		} catch (Exception e) {
			LogUtils.log(e);
			row = 0;
		}
		PublishActivityResEntity res = new PublishActivityResEntity();
		if (row > 0) {
			res.status = Config.STATUS_OK;
			res.activity_id = id;
			res.poster_img = ImageHelper.getImageUrl(request, imageName);
			res.activity_pics = activityPics;
		} else {
			DBUtils.rollbackTransaction(conn);
			res.status = Config.STATUS_SERVER_ERROR;
		}
		return res;
	}

	/**
	 * 获取图片名称
	 * 
	 * @param user_id
	 * @return
	 */
	private static String getImageName(String user_id) {
		StringBuffer sb = new StringBuffer("activity/");
		if (!StringUtils.emptyString(user_id)) {
			sb.append("/").append(user_id);
		}
		sb.append("/").append(IdUtils.createId("activity"));
		return sb.toString();
	}

	/**
	 * 活动详情
	 * 
	 * @param conn
	 * @param activityID
	 * @return
	 * @throws SQLException
	 */
	public static ActivityInfo getActivityInfo(DBUtils.ConnectionCache conn, String activityID) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select activity_id,user_id,activity_name,begin_time,end_time,activity_type,activity_lable,address,organizer_phone,poster_img,limit_num,sign_up_num,status,time,activity_praise_num,activity_browse_num,activity_comment_num,delete_time,activity_content from activity where activity_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, activityID);
			rs = pstat.executeQuery();
			if (rs.next()) {
				ActivityInfo articleInfo = new ActivityInfo();
				articleInfo.activity_id = rs.getString("activity_id");
				articleInfo.activity_name = rs.getString("activity_name");
				articleInfo.user_id = rs.getString("user_id");
				articleInfo.activity_type = rs.getString("activity_type");
				articleInfo.activity_lable = rs.getString("activity_lable");
				articleInfo.address = rs.getString("address");
				articleInfo.organizer_phone = rs.getString("organizer_phone");
				articleInfo.poster_img = rs.getString("poster_img");
				articleInfo.time = rs.getLong("time");
				articleInfo.status = rs.getInt("status");
				articleInfo.limit_num = rs.getInt("limit_num");
				articleInfo.sign_up_num = rs.getInt("sign_up_num");
				articleInfo.begin_time = rs.getLong("begin_time");
				articleInfo.end_time = rs.getLong("end_time");
				articleInfo.delete_time = rs.getLong("delete_time");
				articleInfo.activity_content = rs.getString("activity_content");
				articleInfo.activity_comment_num = rs.getLong("activity_comment_num");
				articleInfo.activity_browse_num = rs.getLong("activity_browse_num");
				articleInfo.activity_praise_num = rs.getLong("activity_praise_num");
				return articleInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	/**
	 * 获取所有活动
	 * 
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetActivitysResEntity listActivitys(GetActivitysReqEntity req, HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = null;
			if (!StringUtils.emptyString(req.token)) {
				user = UserDao.getUserByToken(conn, req.token);
				if (user == null) {
					GetActivitysResEntity res = new GetActivitysResEntity();
					res.status = Config.STATUS_TOKEN_ERROR;
					return res;
				}
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					"select activity.activity_id,activity.user_id,activity.activity_name,activity.begin_time,activity.end_time,activity.activity_type,activity.activity_lable,activity.address,activity.organizer_phone,activity.poster_img,activity.limit_num,activity.sign_up_num,activity.status,activity.time,activity.activity_praise_num,activity.activity_browse_num,activity.activity_comment_num,userinfo.user_id,userinfo.nickname,userinfo.head_pic,userinfo.growth from activity ");
			sb.append(" INNER JOIN userinfo ON activity.user_id = userinfo.user_id ");
			sb.append(" WHERE activity.status =").append(ACTIVITY_STATUS_NORMAL);
			sb.append(" AND delete_time =").append(ACTIVITY_DELTE_TIME_NORMAL);
			sb.append(" ORDER BY activity.time  DESC");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			rs = pstat.executeQuery();
			GetActivitysResEntity res = new GetActivitysResEntity();
			res.activitys = new ArrayList<>();
			GetActivitysResEntity.Info info;
			GetActivitysResEntity.UserInfo userInfo;
			while (rs.next()) {
				info = new GetActivitysResEntity.Info();
				info.activity_id = rs.getString("activity_id");
				info.activity_name = rs.getString("activity_name");
				info.time = rs.getLong("time");
				if (rs.getString("poster_img") != null)
					info.poster_img = ImageHelper.getImageUrl(request, rs.getString("poster_img"));
				info.activity_comment_num = rs.getLong("activity_comment_num");
				info.activity_browse_num = rs.getLong("activity_browse_num");
				String activity_type = rs.getString("activity_type");
				if (!StringUtils.emptyString(activity_type))
					info.activity_type = JSON.parseArray(activity_type, String.class);
				String activity_lable = rs.getString("activity_lable");
				if (!StringUtils.emptyString(activity_lable))
					info.activity_lable = JSON.parseArray(activity_lable, String.class);
				// // 获取创建人信息
				userInfo = new GetActivitysResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				info.user_info = userInfo;
				if (user != null) {
					info.focus = FocusDao.getFocusInfo(conn, user.user_id, userInfo.user_id) == null ? null
							: FocusDao.getFocusInfo(conn, user.user_id, userInfo.user_id).focus_id;
				}
				res.activitys.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.activitys.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	/**
	 * 我的活动
	 * 
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetMyActivitysResEntity listMyActivitys(GetMyActivitysReqEntity req, HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				GetMyActivitysResEntity res = new GetMyActivitysResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					"select activity.activity_id,activity.user_id,activity.activity_name,activity.begin_time,activity.end_time,activity.activity_type,activity.activity_lable,activity.address,activity.organizer_phone,activity.poster_img,activity.limit_num,activity.sign_up_num,activity.status,activity.time,activity.activity_praise_num,activity.activity_browse_num,activity.activity_comment_num,userinfo.user_id,userinfo.nickname,userinfo.head_pic,userinfo.growth from activity ");
			sb.append(" INNER JOIN userinfo ON activity.user_id = userinfo.user_id ");
			sb.append(" WHERE activity.user_id =?");
			sb.append(" and activity.status =").append(ACTIVITY_STATUS_NORMAL);
			sb.append(" ORDER BY activity.time  DESC");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, user.user_id);
			rs = pstat.executeQuery();
			GetMyActivitysResEntity res = new GetMyActivitysResEntity();
			res.activitys = new ArrayList<>();
			GetMyActivitysResEntity.Info info;
			while (rs.next()) {
				info = new GetMyActivitysResEntity.Info();
				info.activity_id = rs.getString("activity_id");
				info.activity_name = rs.getString("activity_name");
				info.time = rs.getLong("time");
				if (rs.getString("poster_img") != null)
					info.poster_img = ImageHelper.getImageUrl(request, rs.getString("poster_img"));
				info.activity_comment_num = rs.getLong("activity_comment_num");
				info.activity_browse_num = rs.getLong("activity_browse_num");
				String activity_type = rs.getString("activity_type");
				if (!StringUtils.emptyString(activity_type))
					info.activity_type = JSON.parseArray(activity_type, String.class);
				String activity_lable = rs.getString("activity_lable");
				if (!StringUtils.emptyString(activity_lable))
					info.activity_lable = JSON.parseArray(activity_lable, String.class);
				info.status = rs.getInt("status");
				res.activitys.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.activitys.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	/**
	 * 我收藏的活动
	 * 
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetCollectActivitysResEntity listCollectActivitys(GetCollectActivitysReqEntity req,
			HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				GetCollectActivitysResEntity res = new GetCollectActivitysResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					"select activity.activity_id,activity.user_id,activity.activity_name,activity.begin_time,activity.end_time,activity.activity_type,activity.activity_lable,activity.address,activity.organizer_phone,activity.poster_img,activity.limit_num,activity.sign_up_num,activity.status,activity.time,activity.activity_praise_num,activity.activity_browse_num,activity.activity_comment_num,userinfo.user_id,userinfo.nickname,userinfo.head_pic,userinfo.growth from activity ");
			sb.append(" INNER JOIN collect c ON c.collect_object_id = activity.activity_id");
			sb.append(" INNER JOIN userinfo ON activity.user_id = userinfo.user_id ");
			sb.append(" WHERE activity.status =").append(ACTIVITY_STATUS_NORMAL);
			sb.append(" AND activity.delete_time =").append(ACTIVITY_DELTE_TIME_NORMAL);
			sb.append(" and c.collect_object_type =").append(Config.CollectObjectType.COLLECT_OBJECT_TYPE_ACTIVITY);
			sb.append(" and c.user_id =?");
			sb.append(" AND delete_time =").append(ACTIVITY_DELTE_TIME_NORMAL);
			sb.append(" ORDER BY activity.time  DESC");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, user.user_id);
			rs = pstat.executeQuery();
			GetCollectActivitysResEntity res = new GetCollectActivitysResEntity();
			res.activitys = new ArrayList<>();
			GetCollectActivitysResEntity.Info info;
			GetCollectActivitysResEntity.UserInfo userInfo;
			while (rs.next()) {
				info = new GetCollectActivitysResEntity.Info();
				info.activity_id = rs.getString("activity_id");
				info.activity_name = rs.getString("activity_name");
				info.time = rs.getLong("time");
				if (rs.getString("poster_img") != null)
					info.poster_img = ImageHelper.getImageUrl(request, rs.getString("poster_img"));
				info.activity_comment_num = rs.getLong("activity_comment_num");
				info.activity_browse_num = rs.getLong("activity_browse_num");
				String activity_type = rs.getString("activity_type");
				if (!StringUtils.emptyString(activity_type))
					info.activity_type = JSON.parseArray(activity_type, String.class);
				String activity_lable = rs.getString("activity_lable");
				if (!StringUtils.emptyString(activity_lable))
					info.activity_lable = JSON.parseArray(activity_lable, String.class);
				// // 获取创建人信息
				userInfo = new GetCollectActivitysResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				info.user_info = userInfo;
				if (user != null) {
					info.focus = FocusDao.getFocusInfo(conn, user.user_id, userInfo.user_id) == null ? null
							: FocusDao.getFocusInfo(conn, user.user_id, userInfo.user_id).focus_id;
				}
				res.activitys.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.activitys.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	/**
	 * 我参与过的活动其中包括我评论过的活动和我报名过的活动
	 * 
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetParticipationActivitysResEntity listParticipationActivitys(GetParticipationActivitysReqEntity req,
			HttpServletRequest request) throws Exception {
		GetParticipationActivitysResEntity res = new GetParticipationActivitysResEntity();
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			//评论过的
			res.activitys=GetCommentActivitys(conn,user,request, req.begin, Config.ONCE_QUERY_COUNT);
			//TODO 报名过的暂时没做
			res.status = Config.STATUS_OK;
			res.has_more_data = res.activitys.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	private static List<GetParticipationActivitysResEntity.Info> GetCommentActivitys(DBUtils.ConnectionCache conn, User user,
			HttpServletRequest request, int begin, int count) throws Exception {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		List<GetParticipationActivitysResEntity.Info> activitys = new ArrayList<>();
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(
					"select distinct activity.activity_id,activity.user_id,activity.activity_name,activity.begin_time,activity.end_time,activity.activity_type,activity.activity_lable,activity.address,activity.organizer_phone,activity.poster_img,activity.limit_num,activity.sign_up_num,activity.status,activity.time,activity.activity_praise_num,activity.activity_browse_num,activity.activity_comment_num,userinfo.user_id,userinfo.nickname,userinfo.head_pic,userinfo.growth from activity ");
			sb.append(
					" INNER JOIN userinfo ON activity.user_id = userinfo.user_id INNER JOIN comment ON comment.comment_object_id = activity.activity_id ");
			sb.append(" WHERE activity.status =").append(ACTIVITY_STATUS_NORMAL);
			sb.append(" AND delete_time =").append(ACTIVITY_DELTE_TIME_NORMAL);
			sb.append(" AND comment.user_id=? ").append(" AND comment.comment_object_type =")
					.append(Config.CommentObjectType.COMMENT_OBJECT_TYPE_ACTIVITY);
			sb.append(" AND activity.user_id <> ?");
			sb.append(" ORDER BY activity.time  DESC");
			sb.append(" LIMIT ").append(begin).append(",").append(count);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, user.user_id);
			pstat.setObject(2, user.user_id);
			rs = pstat.executeQuery();
			GetParticipationActivitysResEntity.Info info;
			GetParticipationActivitysResEntity.UserInfo userInfo;
			while (rs.next()) {
				info = new GetParticipationActivitysResEntity.Info();
				info.activity_id = rs.getString("activity_id");
				info.activity_name = rs.getString("activity_name");
				info.time = rs.getLong("time");
				if (rs.getString("poster_img") != null)
					info.poster_img = ImageHelper.getImageUrl(request, rs.getString("poster_img"));
				info.activity_comment_num = rs.getLong("activity_comment_num");
				info.activity_browse_num = rs.getLong("activity_browse_num");
				String activity_type = rs.getString("activity_type");
				if (!StringUtils.emptyString(activity_type))
					info.activity_type = JSON.parseArray(activity_type, String.class);
				String activity_lable = rs.getString("activity_lable");
				if (!StringUtils.emptyString(activity_lable))
					info.activity_lable = JSON.parseArray(activity_lable, String.class);
				// // 获取创建人信息
				userInfo = new GetParticipationActivitysResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				info.user_info = userInfo;
				if (user != null) {
					info.focus = FocusDao.getFocusInfo(conn, user.user_id, userInfo.user_id) == null ? null
							: FocusDao.getFocusInfo(conn, user.user_id, userInfo.user_id).focus_id;
				}
				info.type=0;
				activitys.add(info);
			}
			return activitys;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	/**
	 * 获取活动详情
	 * 
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetActivityDetailResEntity getActivityDetail(GetActivityDetailReqEntity req,
			HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = null;
			if (!StringUtils.emptyString(req.token)) {
				user = UserDao.getUserByToken(conn, req.token);
				if (user == null) {
					GetActivityDetailResEntity res = new GetActivityDetailResEntity();
					res.status = Config.STATUS_TOKEN_ERROR;
					return res;
				}
			}
			ActivityInfo activityInfo = getActivityInfo(conn, req.activity_id);
			if (activityInfo == null) {
				GetActivityDetailResEntity res = new GetActivityDetailResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					"select activity.activity_content,activity.activity_id,activity.user_id,activity.activity_name,activity.begin_time,activity.end_time,activity.activity_type,activity.activity_lable,activity.address,activity.organizer_phone,activity.poster_img,activity.limit_num,activity.sign_up_num,activity.status,activity.time,activity.activity_praise_num,activity.activity_browse_num,activity.activity_comment_num,userinfo.user_id,userinfo.nickname,userinfo.head_pic,userinfo.growth,userinfo.signature from activity ");
			sb.append(" INNER JOIN userinfo ON activity.user_id = userinfo.user_id where 1=1");
			sb.append(" AND activity_id =?");
			if (user == null) {
				sb.append(" AND activity.status =").append(ACTIVITY_STATUS_NORMAL);
			} else {
				if (!user.user_id.equals(activityInfo.user_id))
					sb.append(" AND activity.status =").append(ACTIVITY_STATUS_NORMAL);
			}
			sb.append(" AND delete_time =").append(ACTIVITY_DELTE_TIME_NORMAL);
			sb.append(" ORDER BY activity.time  DESC");
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.activity_id);
			rs = pstat.executeQuery();
			GetActivityDetailResEntity res = new GetActivityDetailResEntity();
			GetActivityDetailResEntity.Info info;
			GetActivityDetailResEntity.UserInfo userInfo;
			if (rs.next()) {
				// 获取基本信息
				info = new GetActivityDetailResEntity.Info();
				String activity_id = rs.getString("activity_id");
				info.activity_id = activity_id;
				info.activity_content = rs.getString("activity_content");
				info.activity_name = rs.getString("activity_name");
				info.address = rs.getString("address");
				info.organizer_phone = rs.getString("organizer_phone");
				if (!StringUtils.emptyString(rs.getString("poster_img")))
					info.poster_img = ImageHelper.getImageUrl(request, rs.getString("poster_img"));
				info.begin_time = rs.getLong("begin_time");
				info.end_time = rs.getLong("end_time");
				info.time = rs.getLong("time");
				info.limit_num = rs.getInt("limit_num");
				info.sign_up_num = rs.getInt("sign_up_num");
				info.activity_comment_num = rs.getLong("activity_comment_num");
				info.activity_browse_num = rs.getLong("activity_browse_num");
				String activity_type = rs.getString("activity_type");
				if (!StringUtils.emptyString(activity_type))
					info.activity_type = JSON.parseArray(activity_type, String.class);
				String activity_lable = rs.getString("activity_lable");
				if (!StringUtils.emptyString(activity_lable))
					info.activity_lable = JSON.parseArray(activity_lable, String.class);
				if (user != null) {
					info.activity_collecteid = CollectDao.getUserCollectId(conn, activity_id,
							Config.CollectObjectType.COLLECT_OBJECT_TYPE_ACTIVITY, user.user_id);
					BrowseDao.browse(conn, user.user_id, req.activity_id, Config.BrowseType.BROWSE_TYPE_ACTIVITY);
				}

				// 获取创建人信息
				userInfo = new GetActivityDetailResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.signature = rs.getString("signature");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				info.user_info = userInfo;

				// 获取图片信息
				List<ArticleImageInfo> imglist = ActivityImageDao.getActivityImage(conn, info.activity_id, request, 0);
				List<GetActivityDetailResEntity.img> article_pic = null;
				if (imglist != null && imglist.size() != 0) {
					article_pic = new ArrayList<>();
					for (ArticleImageInfo imginfo : imglist) {
						GetActivityDetailResEntity.img img = new GetActivityDetailResEntity.img();
						img.activity_image_description = imginfo.article_image_description;
						img.activity_image_url = imginfo.article_image_url;
						article_pic.add(img);
					}
				}
				info.activity_pic = article_pic;
				res.activity = info;
				res.status = Config.STATUS_OK;
			} else {
				res.status = Config.STATUS_NOT_EXITS;
			}
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	/**
	 * 更新评论数，点赞数，浏览数
	 * 
	 * @param conn
	 * @param project_id
	 * @param commentCount
	 * @param browseCount
	 * @param praiseCount
	 * @return
	 * @throws SQLException
	 */
	public static int updateCount(DBUtils.ConnectionCache conn, String project_id, int commentCount, int browseCount,
			int praiseCount) throws SQLException {
		String sql = "UPDATE activity SET activity_comment_num=activity_comment_num+?,activity_browse_num=activity_browse_num+?,activity_praise_num=activity_praise_num+? WHERE activity_id=?";
		return DBUtils.executeUpdate(conn, sql, new Object[] { commentCount, browseCount, praiseCount, project_id });
	}

	/**
	 * 删除活动
	 * 
	 * @param req
	 * @return
	 * @throws SQLException
	 */
	public static DeleteActivityResEntity deleteActivity(DeleteActivityReqEntity req) throws SQLException {
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				DeleteActivityResEntity res = new DeleteActivityResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			ActivityInfo activityInfo = getActivityInfo(conn, req.activity_id);
			if (activityInfo == null) {
				DeleteActivityResEntity res = new DeleteActivityResEntity();
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			String auser = activityInfo.user_id;
			if (!user.user_id.equals(auser)) {
				DeleteActivityResEntity res = new DeleteActivityResEntity();
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			int row = 0;
			try {
				DBUtils.beginTransaction(conn);
				// 删除举报信息
				ReportDao.deleteObjectReport(conn, req.activity_id,
						Config.ReportObjectType.REPORT_OBJECT_TYPE_ACTIVITY);
				// 删除评论信息
				CommentDao.deleteAllComment(conn, req.activity_id,
						Config.CommentObjectType.COMMENT_OBJECT_TYPE_ACTIVITY);
				// 删除浏览历史
				BrowseDao.deleteBrowse(conn, req.activity_id, Config.BrowseType.BROWSE_TYPE_ACTIVITY);
				// 删除收藏信息
				CollectDao.deleteCollect(conn, req.activity_id, Config.CommentObjectType.COMMENT_OBJECT_TYPE_ACTIVITY);
				// 规定60日后进行删除
				row = DBUtils.executeUpdate(conn,
						"UPDATE activity SET delete_time=?,activity_comment_num=0,activity_browse_num=0,activity_praise_num=0 WHERE activity_id=? AND user_id=?",
						new Object[] { System.currentTimeMillis(), req.activity_id, user.user_id });
				if (row > 0) {
					DBUtils.commitTransaction(conn);
					ReportDao.deleteObjectReportImages(req.activity_id,
							Config.ReportObjectType.REPORT_OBJECT_TYPE_ACTIVITY);
				}
			} catch (Exception e) {
				e.printStackTrace();
				row = 0;
			}
			if (row > 0) {
				DeleteActivityResEntity res = new DeleteActivityResEntity();
				res.status = Config.STATUS_OK;
				return res;
			} else {
				DeleteActivityResEntity res = new DeleteActivityResEntity();
				res.status = Config.STATUS_SERVER_ERROR;
				return res;
			}
		} finally {
			DBUtils.close(conn);
		}
	}

}

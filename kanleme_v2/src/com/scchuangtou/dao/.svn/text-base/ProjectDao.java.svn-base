package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.entity.DeleteHelpEachReqEntity;
import com.scchuangtou.entity.DeleteHelpEachResEntity;
import com.scchuangtou.entity.GetCollectHelpEachsReqEntity;
import com.scchuangtou.entity.GetCollectHelpEachsResEntity;
import com.scchuangtou.entity.GetHelpEachInfoReqEntity;
import com.scchuangtou.entity.GetHelpEachInfoResEntity;
import com.scchuangtou.entity.GetHelpEachsReqEntity;
import com.scchuangtou.entity.GetHelpEachsResEntity;
import com.scchuangtou.entity.GetMyHelpEachsReqEntity;
import com.scchuangtou.entity.GetMyHelpEachsResEntity;
import com.scchuangtou.entity.GetSuportedHelpEachsReqEntity;
import com.scchuangtou.entity.GetSuportedHelpEachsResEntity;
import com.scchuangtou.entity.PublishIllnessHelpReqEntity;
import com.scchuangtou.entity.PublishIllnessHelpResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.model.CapitalSourceInfo;
import com.scchuangtou.model.ProjectInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.StringUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;

public class ProjectDao {

	public static PublishIllnessHelpResEntity publishHelpEach(PublishIllnessHelpReqEntity req, String[] keys,
			Map<String, List<MyMutiPart>> partsMap, HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			PublishIllnessHelpResEntity publishIllnessHelpResEntity = new PublishIllnessHelpResEntity();
			User user = UserDao.getUserByToken(conn, req.token);
			// 判断token过期user为空，设置TOKEN_ERROR直接返回
			if (user == null) {
				PublishIllnessHelpResEntity res = new PublishIllnessHelpResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}

			CapitalSourceInfo capitalSourceInfo = CapitalSourceDao.getCapitalSourcesInfo(conn, req.capital_id);
			if (capitalSourceInfo == null) {
				PublishIllnessHelpResEntity res = new PublishIllnessHelpResEntity();
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}

			String help_each_id = IdUtils.createId("help_each");
			Map<String, MyMutiPart> parts = new HashMap<>();
			int row = 0;
			HashMap<String, Object> datas = new HashMap<String, Object>();
			datas.put("help_each_id", help_each_id);
			datas.put("user_id", user.user_id);
			datas.put("project_type", req.project_type);
			datas.put("help_type", req.help_type);
			datas.put("capital_goal", req.capital_goal);
			datas.put("capital_purpose", req.capital_purpose);
			datas.put("days", req.days);
			datas.put("title", req.title);
			datas.put("detail_description", req.detail_description);
			datas.put("status", Config.HelpEachType.HELP_EACH_TYPE_APPLYING);
			datas.put("payee_name", req.payee_name);
			datas.put("payee_id_num", req.payee_id_num);
			datas.put("payee_phone_num", req.payee_phone_num);
			datas.put("capital_id", req.capital_id);
			datas.put("aided_person_name", req.aided_person_name);
			datas.put("aided_person_id", req.aided_person_id);
			datas.put("aided_person_phone", req.aided_person_phone);
			datas.put("aided_person_name", req.aided_person_name);
			datas.put("aided_person_hospital", req.aided_person_hospital);
			datas.put("aided_person_hospital_phone", req.aided_person_hospital_phone);
			datas.put("organize_name", req.organize_name);
			datas.put("organize_phone", req.organize_phone);
			datas.put("project_suport_num", 0);
			datas.put("project_suport_gold", 0);
			datas.put("publish_time", System.currentTimeMillis());
			setDatas(partsMap, datas, parts, help_each_id);
			row = DBUtils.insert(conn, "INSERT ignore INTO help_each", datas);
			if (row > 0) {
				if (parts.size() > 0 && ImageHelper.upload(parts) == null) {
					row = 0;
				}
			}
			if (row > 0) {
				publishIllnessHelpResEntity.status = Config.STATUS_OK;
				publishIllnessHelpResEntity.project_img = getResElement("project_img", datas.get("project_img"),
						request);
				publishIllnessHelpResEntity.hospital_doctor_prove_img = getResElement("hospital_doctor_prove_img",
						datas.get("hospital_doctor_prove_img"), request);
				publishIllnessHelpResEntity.hospital_diagnosis_img = getResElement("hospital_diagnosis_img",
						datas.get("hospital_diagnosis_img"), request);
				publishIllnessHelpResEntity.relation_img = getResElement("relation_img", datas.get("relation_img"),
						request);
				publishIllnessHelpResEntity.aided_person_id_img = getResElement("aided_person_id_img",
						datas.get("aided_person_id_img"), request);
				publishIllnessHelpResEntity.organize_img = getResElement("organize_img", datas.get("organize_img"),
						request);
				publishIllnessHelpResEntity.payee_id_img = getResElement("payee_id_img", datas.get("payee_id_img"),
						request);
			} else {
				DBUtils.rollbackTransaction(conn);
				if (StringUtils.emptyString(publishIllnessHelpResEntity.status))
					publishIllnessHelpResEntity.status = Config.STATUS_SERVER_ERROR;
			}
			return publishIllnessHelpResEntity;
		} finally {
			DBUtils.close(conn);
		}
	}

	/**
	 * 创建图片id
	 * 
	 * @param id
	 * @return
	 */
	private static String getImageName(String id) {
		return new StringBuffer().append("helpEach/").append(id).append("/").append(IdUtils.createId("helpEach"))
				.toString();
	}

	/**
	 * 设置数据库字段以及返回值
	 * 
	 * @param parts
	 * @param keys
	 * @param datas
	 */
	private static void setDatas(Map<String, List<MyMutiPart>> parts, HashMap<String, Object> datas,
			Map<String, MyMutiPart> MutiParts, String illness_help_id) {
		for (String key : parts.keySet()) {
			List<MyMutiPart> partsList = parts.get(key);
			List<String> picNames = null;
			String jsonStr = null;
			String picName = null;
			if (partsList != null && 0 != partsList.size()) {
				picNames = new ArrayList<>();
				for (MyMutiPart part : partsList) {
					picName = getImageName(illness_help_id);
					picNames.add(picName);
					MutiParts.put(picName, part);
				}
				if (0 != picNames.size()) {
					jsonStr = JSON.toJSONString(picNames);
					datas.put(key, jsonStr);
				}
			}
		}
	}

	/**
	 * 图片上传成功后返回
	 * 
	 * @param key
	 * @param object
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private static List<String> getResElement(String key, Object object, HttpServletRequest request) throws Exception {
		List<String> reslutList = null;
		if (object != null) {
			reslutList = new ArrayList<>();
			reslutList = JSON.parseArray((String) object, String.class);
		}
		if (reslutList != null && reslutList.size() != 0) {
			for (int i = 0; i < reslutList.size(); i++) {
				reslutList.set(i, ImageHelper.getImageUrl(request, reslutList.get(i)));
			}
		}
		return reslutList;
	}

	public static ProjectInfo getProjectInfo(DBUtils.ConnectionCache conn, String help_each_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select help_each.days,help_each.publish_time,help_each.help_each_id,help_each.user_id,help_each.project_type,help_each.help_type,help_each.capital_goal,help_each.capital_purpose,help_each.begin_time,help_each.end_time,help_each.title,help_each.detail_description,help_each.project_img,help_each.status,help_each.payee_name,help_each.payee_id_num,help_each.payee_id_img,help_each.relation_img,help_each.payee_phone_num,help_each.capital_id,help_each.aided_person_name,help_each.aided_person_id,help_each.aided_person_phone,help_each.aided_person_id_img,help_each.aided_person_hospital,help_each.aided_person_hospital_phone,help_each.hospital_diagnosis_img,help_each.hospital_doctor_prove_img,help_each.organize_name,help_each.organize_phone,help_each.organize_img,help_each.project_suport_num from help_each where help_each_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, help_each_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				ProjectInfo projectInfo = new ProjectInfo();
				projectInfo.help_each_id = rs.getString("help_each_id");
				projectInfo.user_id = rs.getString("user_id");
				projectInfo.project_type = rs.getInt("project_type");
				projectInfo.help_type = rs.getInt("help_type");
				projectInfo.capital_goal = rs.getDouble("capital_goal");
				projectInfo.capital_purpose = rs.getString("capital_purpose");
				projectInfo.begin_time = rs.getLong("begin_time");
				projectInfo.end_time = rs.getLong("end_time");
				projectInfo.title = rs.getString("title");
				projectInfo.detail_description = rs.getString("detail_description");
				projectInfo.project_img = rs.getString("project_img");
				projectInfo.status = rs.getInt("status");
				projectInfo.payee_name = rs.getString("payee_name");
				projectInfo.payee_id_num = rs.getString("payee_id_num");
				projectInfo.payee_id_img = rs.getString("payee_id_img");
				projectInfo.relation_img = rs.getString("relation_img");
				projectInfo.payee_phone_num = rs.getString("payee_phone_num");
				projectInfo.capital_id = rs.getString("capital_id");
				projectInfo.aided_person_name = rs.getString("aided_person_name");
				projectInfo.aided_person_id = rs.getString("aided_person_id");
				projectInfo.aided_person_phone = rs.getString("aided_person_phone");
				projectInfo.aided_person_id_img = rs.getString("aided_person_id_img");
				projectInfo.aided_person_hospital = rs.getString("aided_person_hospital");
				projectInfo.aided_person_hospital_phone = rs.getString("aided_person_hospital_phone");
				projectInfo.hospital_diagnosis_img = rs.getString("hospital_diagnosis_img");
				projectInfo.hospital_doctor_prove_img = rs.getString("hospital_doctor_prove_img");
				projectInfo.organize_name = rs.getString("organize_name");
				projectInfo.organize_phone = rs.getString("organize_phone");
				projectInfo.organize_img = rs.getString("organize_img");
				projectInfo.project_suport_num = rs.getLong("project_suport_num");
				projectInfo.publish_time = rs.getLong("publish_time");
				projectInfo.days = rs.getInt("days");
				return projectInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	/**
	 * 项目捐款次数
	 * 
	 * @param conn
	 * @param object_id
	 * @param commentCount
	 * @param browseCount
	 * @param praiseCount
	 * @return
	 * @throws SQLException
	 */
	public static int updateCount(DBUtils.ConnectionCache conn, String object_id, int suportnum) throws SQLException {
		String sql = "update help_each set project_suport_num=project_suport_num+? where help_each_id=?";
		return DBUtils.executeUpdate(conn, sql, new Object[] { suportnum, object_id });
	}

	/**
	 * 互助项目详情
	 * 
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetHelpEachInfoResEntity getHelpEachInfo(GetHelpEachInfoReqEntity req, HttpServletRequest request)
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
					GetHelpEachInfoResEntity res = new GetHelpEachInfoResEntity();
					res.status = Config.STATUS_TOKEN_ERROR;
					return res;
				}
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					" SELECT help_each.status,help_each.help_each_id,help_each.title,help_each.detail_description,help_each.project_img,help_each.project_suport_num,help_each.begin_time,help_each.end_time,help_each.capital_goal,help_each.project_suport_num,help_each.project_suport_gold,help_each.aided_person_name,help_each.payee_name,p.num FROM");
			sb.append(
					" help_each,(select count(prove_id) num from prove where prove.help_each_id = ?) p WHERE help_each.help_each_id = ?");
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.help_each_id);
			pstat.setObject(2, req.help_each_id);
			rs = pstat.executeQuery();
			GetHelpEachInfoResEntity res = new GetHelpEachInfoResEntity();
			if (rs.next()) {
				// 获取基本信息
				String help_each_id = rs.getString("help_each_id");
				res.help_each_id = help_each_id;
				res.begin_time = rs.getLong("begin_time");
				res.end_time = rs.getLong("end_time");
				res.detail_description = rs.getString("detail_description");
				res.title = rs.getString("title");
				res.project_suport_num = rs.getInt("project_suport_num");
				res.gold_count = rs.getDouble("project_suport_gold");
				res.payee_name = rs.getString("payee_name");
				res.aided_person_name = rs.getString("aided_person_name");
				res.project_status = rs.getInt("status");
				res.capital_goal = rs.getDouble("capital_goal");
				res.prove_num = rs.getInt("num");
				// 是否被收藏
				if (user != null) {
					res.project_collected = CollectDao.getUserCollectId(conn, help_each_id,
							Config.CollectObjectType.COLLECT_OBJECT_TYPE_PROJECT, user.user_id);
					res.project_praiseid = PraiseDao.getUserPraiseId(conn, help_each_id,
							Config.PraiseType.PRAISE_TYPE_PROJECT, user.user_id);
					BrowseDao.browse(conn, user.user_id, help_each_id, Config.BrowseType.BROWSE_TYPE_PROJECT);
				}
				// 图片信息
				String jsonStr = rs.getString("project_img");
				List<String> imgList = null;
				if (!StringUtils.emptyString(jsonStr)) {
					imgList = JSON.parseArray(jsonStr, String.class);
					for (int i = 0; i < imgList.size(); i++) {
						imgList.set(i, ImageHelper.getImageUrl(request, imgList.get(i)));
					}
				}
				res.imgList = imgList;
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
		String sql = "UPDATE help_each SET comment_count=comment_count+?,browse_count=browse_count+?,praise_count=praise_count+? WHERE help_each_id=?";
		return DBUtils.executeUpdate(conn, sql, new Object[] { commentCount, browseCount, praiseCount, project_id });
	}

	/**
	 * 更新项目捐助金额以及支持次数
	 * 
	 * @param conn
	 * @param project_id
	 * @param gold
	 * @param suportCount
	 * @return
	 * @throws SQLException
	 */
	public static int updateGlodOrSuportNum(DBUtils.ConnectionCache conn, String project_id, float gold,
			int suportCount) throws SQLException {
		StringBuffer sbsql = new StringBuffer(
				"UPDATE help_each SET project_suport_gold=project_suport_gold+?,project_suport_num=project_suport_num+? WHERE help_each_id=?");
		if (gold < 0) {
			sbsql.append(" AND project_suport_gold>=").append(Math.abs(gold));
		}
		return DBUtils.executeUpdate(conn, sbsql.toString(), new Object[] { gold, suportCount, project_id });
	}

	/**
	 * 获取我的项目
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static GetMyHelpEachsResEntity listMyProjects(GetMyHelpEachsReqEntity req, HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				GetMyHelpEachsResEntity res = new GetMyHelpEachsResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					" SELECT help_each.status,help_each.publish_time,help_each.help_each_id,help_each.title,help_each.detail_description,help_each.project_img,help_each.project_suport_num,help_each.begin_time,help_each.end_time,help_each.end_time,help_each.capital_goal,help_each.project_suport_num,help_each.project_suport_gold,help_each.browse_count,help_each.comment_count,u.user_id,u.nickname,u.head_pic,u.growth FROM");
			sb.append(" help_each INNER JOIN userinfo u ON u.user_id = help_each.user_id and help_each.user_id=?");
			if (req.searchType == Config.HelpEachTSearch.PROJECT_SEARCH_PROCEED) {
				// 通过
				sb.append(" WHERE help_each.status = ").append(Config.HelpEachType.HELP_EACH_TYPE_PASSED);
			} else if (req.searchType == Config.HelpEachTSearch.PROJECT_SEARCH_SOLDOUT) {
				// 已结束
				sb.append(" WHERE help_each.status = ").append(Config.HelpEachType.HELP_EACH_TYPE_END);
			} else if (req.searchType == Config.HelpEachTSearch.PROJECT_SEARCH_DRAFT) {
				// 未通过
				sb.append(" WHERE help_each.status = ").append(Config.HelpEachType.HELP_EACH_TYPE_NO_PASSED);
				// 待审核
				sb.append(" or help_each.status = ").append(Config.HelpEachType.HELP_EACH_TYPE_APPLYING);
			}
			sb.append(" order by begin_time desc");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, user.user_id);
			rs = pstat.executeQuery();
			GetMyHelpEachsResEntity res = new GetMyHelpEachsResEntity();
			res.projects = new ArrayList<>();
			GetMyHelpEachsResEntity.Info info;
			GetMyHelpEachsResEntity.UserInfo userInfo;
			while (rs.next()) {
				info = new GetMyHelpEachsResEntity.Info();
				// 获取基本信息
				String help_each_id = rs.getString("help_each_id");
				info.help_each_id = help_each_id;
				info.begin_time = rs.getLong("begin_time");
				info.end_time = rs.getLong("end_time");
				info.publish_time = rs.getLong("publish_time");
				info.title = rs.getString("title");
				info.project_suport_num = rs.getInt("project_suport_num");
				info.gold_count = rs.getDouble("project_suport_gold");
				info.project_status = rs.getInt("status");
				info.capital_goal = rs.getDouble("capital_goal");
				info.comment_num = rs.getLong("comment_count");
				info.view_count = rs.getLong("browse_count");

				// 获取创建人信息
				userInfo = new GetMyHelpEachsResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				info.user_info = userInfo;
				res.projects.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.projects.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}
	
	/**
	 * 我关注的互助项目
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetCollectHelpEachsResEntity listCollectProjects(GetCollectHelpEachsReqEntity req, HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				GetCollectHelpEachsResEntity res = new GetCollectHelpEachsResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					" SELECT help_each.publish_time,help_each.help_each_id,help_each.title,help_each.detail_description,help_each.project_img,help_each.project_suport_num,help_each.begin_time,help_each.end_time,help_each.end_time,help_each.capital_goal,help_each.project_suport_num,help_each.project_suport_gold,help_each.browse_count,help_each.comment_count,u.user_id,u.nickname,u.head_pic,u.growth FROM");
			sb.append(" help_each INNER JOIN collect c ON c.collect_object_id = help_each.help_each_id INNER JOIN userinfo u ON u.user_id = help_each.user_id and help_each.user_id=?");
			sb.append(" where  c.collect_object_type =").append(Config.CollectObjectType.COLLECT_OBJECT_TYPE_PROJECT);
			sb.append(" and c.user_id =?");
			if (req.searchType == Config.HelpEachTSearch.PROJECT_SEARCH_PROCEED) {
				// 通过
				sb.append(" and help_each.status = ").append(Config.HelpEachType.HELP_EACH_TYPE_PASSED);
			} else if (req.searchType == Config.HelpEachTSearch.PROJECT_SEARCH_SOLDOUT) {
				// 已结束
				sb.append(" and help_each.status = ").append(Config.HelpEachType.HELP_EACH_TYPE_END);
			}
			sb.append(" order by begin_time desc");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, user.user_id);
			pstat.setObject(2, user.user_id);
			rs = pstat.executeQuery();
			GetCollectHelpEachsResEntity res = new GetCollectHelpEachsResEntity();
			res.projects = new ArrayList<>();
			GetCollectHelpEachsResEntity.Info info;
			GetCollectHelpEachsResEntity.UserInfo userInfo;
			while (rs.next()) {
				info = new GetCollectHelpEachsResEntity.Info();
				// 获取基本信息
				String help_each_id = rs.getString("help_each_id");
				info.help_each_id = help_each_id;
				info.begin_time = rs.getLong("begin_time");
				info.end_time = rs.getLong("end_time");
				info.publish_time = rs.getLong("publish_time");
				info.title = rs.getString("title");
				info.project_suport_num = rs.getInt("project_suport_num");
				info.gold_count = rs.getDouble("project_suport_gold");
				info.capital_goal = rs.getDouble("capital_goal");
				info.comment_num = rs.getLong("comment_count");
				info.view_count = rs.getLong("browse_count");

				// 获取创建人信息
				userInfo = new GetCollectHelpEachsResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				info.user_info = userInfo;
				res.projects.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.projects.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}
	
	
	public static GetSuportedHelpEachsResEntity listSuportedProjects(GetSuportedHelpEachsReqEntity req, HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				GetSuportedHelpEachsResEntity res = new GetSuportedHelpEachsResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					" SELECT DISTINCT help_each.publish_time,help_each.help_each_id,help_each.title,help_each.detail_description,help_each.project_img,help_each.project_suport_num,help_each.begin_time,help_each.end_time,help_each.end_time,help_each.capital_goal,help_each.project_suport_num,help_each.project_suport_gold,help_each.browse_count,help_each.comment_count,u.user_id,u.nickname,u.head_pic,u.growth FROM");
			sb.append(" help_each INNER JOIN gold_change g ON g.change_desc = help_each.help_each_id and g.userid=? ");
			sb.append(" and g.change_type=").append(Config.GoldChangeType.GOLD_CHANGE_TYPE_PROJECT_DONATIONS);
			sb.append(" INNER JOIN userinfo u ON u.user_id = help_each.user_id and help_each.user_id=? where  1=1");
			if (req.searchType == Config.HelpEachTSearch.PROJECT_SEARCH_PROCEED) {
				// 通过
				sb.append(" and help_each.status = ").append(Config.HelpEachType.HELP_EACH_TYPE_PASSED);
			} else if (req.searchType == Config.HelpEachTSearch.PROJECT_SEARCH_SOLDOUT) {
				// 已结束
				sb.append(" and help_each.status = ").append(Config.HelpEachType.HELP_EACH_TYPE_END);
			}
			sb.append(" order by begin_time desc");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, user.user_id);
			pstat.setObject(2, user.user_id);
			rs = pstat.executeQuery();
			GetSuportedHelpEachsResEntity res = new GetSuportedHelpEachsResEntity();
			res.projects = new ArrayList<>();
			GetSuportedHelpEachsResEntity.Info info;
			GetSuportedHelpEachsResEntity.UserInfo userInfo;
			while (rs.next()) {
				info = new GetSuportedHelpEachsResEntity.Info();
				// 获取基本信息
				String help_each_id = rs.getString("help_each_id");
				info.help_each_id = help_each_id;
				info.begin_time = rs.getLong("begin_time");
				info.end_time = rs.getLong("end_time");
				info.publish_time = rs.getLong("publish_time");
				info.title = rs.getString("title");
				info.project_suport_num = rs.getInt("project_suport_num");
				info.gold_count = rs.getDouble("project_suport_gold");
				info.capital_goal = rs.getDouble("capital_goal");
				info.comment_num = rs.getLong("comment_count");
				info.view_count = rs.getLong("browse_count");

				// 获取创建人信息
				userInfo = new GetSuportedHelpEachsResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				info.user_info = userInfo;
				res.projects.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.projects.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}
	
	

	/**
	 * 获取所有互助项目
	 * 
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetHelpEachsResEntity listProjects(GetHelpEachsReqEntity req, HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			StringBuffer sb = new StringBuffer();
			sb.append(
					" SELECT help_each.publish_time,help_each.help_each_id,help_each.title,help_each.detail_description,help_each.project_img,help_each.project_suport_num,help_each.begin_time,help_each.end_time,help_each.end_time,help_each.capital_goal,help_each.project_suport_num,help_each.project_suport_gold,help_each.browse_count,help_each.comment_count,u.user_id,u.nickname,u.head_pic,u.growth FROM");
			sb.append(" help_each INNER JOIN userinfo u ON u.user_id = help_each.user_id");
			sb.append(" WHERE help_each.status = ").append(Config.HelpEachType.HELP_EACH_TYPE_PASSED);
			sb.append(" order by begin_time desc");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			rs = pstat.executeQuery();
			GetHelpEachsResEntity res = new GetHelpEachsResEntity();
			res.projects = new ArrayList<>();
			GetHelpEachsResEntity.Info info;
			GetHelpEachsResEntity.UserInfo userInfo;
			while (rs.next()) {
				info = new GetHelpEachsResEntity.Info();
				// 获取基本信息
				String help_each_id = rs.getString("help_each_id");
				info.help_each_id = help_each_id;
				info.begin_time = rs.getLong("begin_time");
				info.end_time = rs.getLong("end_time");
				info.publish_time = rs.getLong("publish_time");
				info.title = rs.getString("title");
				info.project_suport_num = rs.getInt("project_suport_num");
				info.gold_count = rs.getDouble("project_suport_gold");
				info.capital_goal = rs.getDouble("capital_goal");
				info.comment_num = rs.getLong("comment_count");
				info.view_count = rs.getLong("browse_count");

				// 获取创建人信息
				userInfo = new GetHelpEachsResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				info.user_info = userInfo;
				res.projects.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.projects.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	public static DeleteHelpEachResEntity deleteHelpEach(DeleteHelpEachReqEntity req) throws SQLException {
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				DeleteHelpEachResEntity res = new DeleteHelpEachResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			ProjectInfo projectInfo = getProjectInfo(conn, req.help_each_id);
			if (projectInfo == null) {
				DeleteHelpEachResEntity res = new DeleteHelpEachResEntity();
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			if (!user.user_id.equals(projectInfo.user_id)) {
				DeleteHelpEachResEntity res = new DeleteHelpEachResEntity();
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			if (projectInfo.status != Config.HelpEachType.HELP_EACH_TYPE_PASSED) {
				return null;
			}
			int row = DBUtils.executeUpdate(conn, "delete from help_each where help_each_id=?",
					new Object[] { req.help_each_id });
			if (row > 0) {
				DeleteHelpEachResEntity res = new DeleteHelpEachResEntity();
				res.status = Config.STATUS_OK;
				return res;
			} else {
				DeleteHelpEachResEntity res = new DeleteHelpEachResEntity();
				res.status = Config.STATUS_SERVER_ERROR;
				return res;
			}
		} finally {
			DBUtils.close(conn);
		}
	}
}

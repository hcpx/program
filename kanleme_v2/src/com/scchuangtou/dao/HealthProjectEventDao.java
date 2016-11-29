package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.config.AdminConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.entity.AdminGetMyHealthProjectEventsReqEntity;
import com.scchuangtou.entity.AdminGetMyHealthProjectEventsResEntity;
import com.scchuangtou.entity.ApplyForSalveReqEntity;
import com.scchuangtou.entity.ApplyForSalveResEntity;
import com.scchuangtou.entity.GetHealthProjectEventDetailReqEntity;
import com.scchuangtou.entity.GetHealthProjectEventDetailResEntity;
import com.scchuangtou.entity.GetHealthProjectEventsReqEntity;
import com.scchuangtou.entity.GetHealthProjectEventsResEntity;
import com.scchuangtou.entity.ParticipateHealthProjectEventReqEntity;
import com.scchuangtou.entity.ParticipateHealthProjectEventResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.model.AdminInfo;
import com.scchuangtou.model.HealthProjectMemberInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;

public class HealthProjectEventDao {
	public static int HEALTH_PROJECT_EVENT_APPLICATION=1;  //申请状态
	public static int HEALTH_PROJECT_EVENT_NORMAL = 0; // 正常状态
	public static int HEALTH_PROJECT_EVENT_BARRED = -1; // 禁止访问状态

	/**
	 * 健康互助申请
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static ApplyForSalveResEntity addHealthProjectEvent(ApplyForSalveReqEntity req) throws Exception {
		ApplyForSalveResEntity res = new ApplyForSalveResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			int row = 0;
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}

			HealthProjectMemberInfo healthProjectMemberInfo = HealthProjectMemberDao.getHealthProjectMemberInfo(conn,
					req.health_project_member_id);
			if (healthProjectMemberInfo == null
					|| !healthProjectMemberInfo.health_project_id.equals(req.health_project_id)) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			// 正式会员
			if (healthProjectMemberInfo.status == HealthProjectMemberDao.HEALTH_PROJECT_OVER
					|| (healthProjectMemberInfo.status == HealthProjectMemberDao.HEALTH_PROJECT_STATUS_WATCH
							&& System.currentTimeMillis() < healthProjectMemberInfo.effect_time)) {
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			String health_project_event_id = IdUtils.createId("health_project_event");
			HashMap<String, Object> datas = new HashMap<>();
			datas.put("health_project_event_id", health_project_event_id);
			datas.put("health_project_id", req.health_project_id);
			datas.put("health_project_member_id", req.health_project_member_id);
			datas.put("user_id", user.user_id);
			datas.put("id_num", req.id_num);
			datas.put("contact_name", req.contact_name);
			datas.put("contact_phone", req.contact_phone);
			datas.put("event_infomation", req.event_infomation);
			datas.put("publish_time", req.publish_time);
			datas.put("status", HEALTH_PROJECT_EVENT_APPLICATION);
			row = DBUtils.insert(conn, "insert into health_project_event", datas);
			if (row > 0) {
				res.health_project_event_id = health_project_event_id;
				res.status = Config.STATUS_OK;
			} else
				res.status = Config.STATUS_SERVER_ERROR;
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	/**
	 * 互助事件公示列表
	 * 
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetHealthProjectEventsResEntity listHealthProjectEvents(GetHealthProjectEventsReqEntity req,HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		GetHealthProjectEventsResEntity res = new GetHealthProjectEventsResEntity();
		try {
			conn = DBUtils.getConnection();
			StringBuffer sql = new StringBuffer(
					"select health_project_event_id,publish_time,event_title,event_img,event_infomation from health_project_event where health_project_id=?")
							.append(" and health_project_event.status=").append(HEALTH_PROJECT_EVENT_NORMAL).append(" ORDER BY publish_time DESC LIMIT ?,?");
			pstat = conn.prepareStatement(sql.toString());
			req.begin = req.begin < 0 ? 0 : req.begin;
			pstat.setObject(1, req.health_project_id);
			pstat.setObject(2, req.begin);
			if (req.count > 0) {
				pstat.setObject(3, req.count);
			} else {
				pstat.setObject(3, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			List<GetHealthProjectEventsResEntity.Event> events = new ArrayList<>();
			GetHealthProjectEventsResEntity.Event event = null;
			while (rs.next()) {
				event = new GetHealthProjectEventsResEntity.Event();
				event.publish_time = rs.getLong("publish_time");
				event.health_project_event_id = rs.getString("health_project_event_id");
				event.event_img = ImageHelper.getImageUrl(request, rs.getString("event_img"));
				event.event_title=rs.getString("event_title");
				event.event_infomation=rs.getString("event_infomation");
				events.add(event);
			}
			res.events = events;
			boolean has_more_data = true;
			if (req.count > 0) {
				has_more_data = res.events.size() == req.count;
			} else {
				has_more_data = res.events.size() == Config.ONCE_QUERY_COUNT;
			}
			res.has_more_data = has_more_data;
			res.status = Config.STATUS_OK;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
		return res;
	}

	/**
	 * 获取互助事件详情
	 * 
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetHealthProjectEventDetailResEntity getHealthProjectEventDetail(
			GetHealthProjectEventDetailReqEntity req) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			StringBuffer sb = new StringBuffer();
			sb.append(
					" SELECT health_project_event.health_project_event_id,health_project.health_project_name,health_project_member.age,health_project_member.family_register,health_project_member.join_time,health_project_member.effect_time,health_project_event.total_money,health_project_event.notice_time,health_project_event.cut_payment_time,health_project_event.event_infomation,health_project_event.survey_infomation,health_project_event.help_each_detail,health_project_event.material_infomation,health_project_event.participate_member,health_project_member.name,health_project_member.sex FROM");
			sb.append(
					" (health_project_event INNER JOIN health_project_member ON health_project_event.health_project_member_id = health_project_member.health_project_member_id) INNER JOIN health_project ON health_project.health_project_id = health_project_event.health_project_id WHERE health_project_event.health_project_event_id = ?");
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.health_project_event_id);
			rs = pstat.executeQuery();
			GetHealthProjectEventDetailResEntity res = new GetHealthProjectEventDetailResEntity();
			if (rs.next()) {
				res.health_project_event_id = rs.getString("health_project_event_id");
				res.health_project_name = rs.getString("health_project_name");
				res.name = rs.getString("name");
				res.sex = rs.getInt("sex");
				res.age = rs.getInt("age");
				res.family_register = rs.getString("family_register");
				res.join_time = rs.getLong("join_time");
				res.total_money = rs.getFloat("total_money");
				res.notice_time = rs.getLong("notice_time");
				res.cut_payment_time = rs.getLong("cut_payment_time");
				res.event_infomation = rs.getString("event_infomation");
				res.survey_infomation = rs.getString("survey_infomation");
				res.help_each_detail = rs.getString("help_each_detail");
				res.material_infomation = rs.getString("material_infomation");
				res.participate_member = rs.getInt("participate_member");
				res.effect_time = rs.getLong("effect_time");
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

	public static ParticipateHealthProjectEventResEntity participateHealthProjectEvents(
			ParticipateHealthProjectEventReqEntity req,HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		ParticipateHealthProjectEventResEntity res = new ParticipateHealthProjectEventResEntity();
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			HealthProjectMemberInfo healthProjectMemberInfo = HealthProjectMemberDao.getHealthProjectMemberInfo(conn,
					req.health_project_member_id);
			if (healthProjectMemberInfo == null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			if (!user.user_id.equals(healthProjectMemberInfo.user_id))
				return null;
			StringBuffer sql = new StringBuffer(
					"select health_project_event.event_title,health_project_event.event_img,health_project_event.event_infomation,health_project_event.publish_time,health_project_event.health_project_event_id,health_project_gold_change.gold from health_project_event inner JOIN health_project_gold_change where health_project_gold_change.health_project_event_id=health_project_event.health_project_event_id")
							.append(" and health_project_gold_change.health_project_member_id=? and health_project_gold_change.type=")
							.append(Config.HealthProjectPayType.HEALTH_PROJECT_PAY_TYPE_PAID)
							.append(" ORDER BY health_project_gold_change.time DESC LIMIT ?,?");
			pstat = conn.prepareStatement(sql.toString());
			req.begin = req.begin < 0 ? 0 : req.begin;
			pstat.setObject(1, req.health_project_member_id);
			pstat.setObject(2, req.begin);
			if (req.count > 0) {
				pstat.setObject(3, req.count);
			} else {
				pstat.setObject(3, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			List<ParticipateHealthProjectEventResEntity.Event> events = new ArrayList<>();
			ParticipateHealthProjectEventResEntity.Event event = null;
			while (rs.next()) {
				event = new ParticipateHealthProjectEventResEntity.Event();
				event.publish_time = rs.getLong("publish_time");
				event.health_project_event_id = rs.getString("health_project_event_id");
				event.event_title = rs.getString("event_title");
				event.gold = rs.getFloat("gold");
				event.event_img = ImageHelper.getImageUrl(request, rs.getString("event_img"));
				event.event_infomation =rs.getString("event_infomation");
				events.add(event);
			}
			res.events = events;
			boolean has_more_data = true;
			if (req.count > 0) {
				has_more_data = res.events.size() == req.count;
			} else {
				has_more_data = res.events.size() == Config.ONCE_QUERY_COUNT;
			}
			res.has_more_data = has_more_data;
			res.status = Config.STATUS_OK;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
		return res;
	}
	
	/**
	 * 互助事件个数
	 * @param conn
	 * @param health_project_id
	 * @return
	 * @throws SQLException
	 */
	public static int getEventCount(DBUtils.ConnectionCache conn, String health_project_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			StringBuffer sb = new StringBuffer(
					"select count(health_project_event.health_project_event_id) from health_project_event where health_project_event.health_project_id=? and health_project_event.`status`=");
			sb.append(HEALTH_PROJECT_EVENT_NORMAL);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, health_project_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return 0;
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	public static AdminGetMyHealthProjectEventsResEntity adminlistMyHealthProjectEvents(AdminGetMyHealthProjectEventsReqEntity req,String token)
			throws Exception {
		AdminGetMyHealthProjectEventsResEntity res=new AdminGetMyHealthProjectEventsResEntity();
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			AdminInfo mAdminInfo = AdminDao.getAdminByToken(conn, token);
			if (mAdminInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!mAdminInfo.hasMode(AdminConfig.MODE_MANAGER_USER)) {
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			User user=UserDao.getUser(conn, req.user_id);
			if (user==null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			StringBuffer sql = new StringBuffer(
					"SELECT health_project_event.health_project_event_id,health_project_event.publish_time,health_project_event.event_title FROM health_project_event INNER JOIN health_project_member on health_project_event.health_project_member_id=health_project_member.health_project_member_id INNER JOIN userinfo on userinfo.user_id=health_project_member.user_id WHERE health_project_member.user_id=?")
			.append(" ORDER BY health_project_event.publish_time DESC LIMIT ?,?");
			pstat = conn.prepareStatement(sql.toString());
			pstat.setObject(1, user.user_id);
			pstat.setObject(2, req.begin);
			if (req.count > 0) {
				pstat.setObject(3, req.count);
			} else {
				pstat.setObject(3, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			res.infos = new ArrayList<>();
			AdminGetMyHealthProjectEventsResEntity.Info info;
			while (rs.next()) {
				info = new AdminGetMyHealthProjectEventsResEntity.Info();
				String health_project_event_id = rs.getString("health_project_event_id");
				info.health_project_event_id = health_project_event_id;
				info.event_title = rs.getString("event_title");
				info.publish_time = rs.getLong("publish_time");
				res.infos.add(info);
			}
			res.status = Config.STATUS_OK;
			boolean has_more_data=true;
			if (req.count > 0) {
				has_more_data=res.infos.size()==req.count;
			} else {
				has_more_data=res.infos.size()==Config.ONCE_QUERY_COUNT;
			}
			res.has_more_data = has_more_data;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}
}

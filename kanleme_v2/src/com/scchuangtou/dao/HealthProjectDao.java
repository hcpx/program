package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.AdminHomepageDataResEntity;
import com.scchuangtou.entity.GetCommonDataResEntity;
import com.scchuangtou.entity.GetHealthProjectDetailReqEntity;
import com.scchuangtou.entity.GetHealthProjectDetailResEntity;
import com.scchuangtou.entity.GetHealthProjectsReqEntity;
import com.scchuangtou.entity.GetHealthProjectsResEntity;
import com.scchuangtou.entity.SearchHealthProjectDetailsReqEntity;
import com.scchuangtou.entity.SearchHealthProjectDetailsResEntity;
import com.scchuangtou.entity.GetHealthProjectsResEntity.Detail;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.model.FinancialInfo;
import com.scchuangtou.model.HealthProjectInfo;
import com.scchuangtou.model.HealthProjectValuInfo;
import com.scchuangtou.model.HomepageGraphInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.StringUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.MathUtils;

public class HealthProjectDao {
	public static int HEALTH_PROJECT_STATUS_NORMAL = 0; // 正常状态
	public static int HEALTH_PROJECT_BARRED = -1; // 禁止访问状态

	/**
	 * 健康互助列表
	 * 
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetHealthProjectsResEntity listHealthProjects(GetHealthProjectsReqEntity req,
			HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		GetHealthProjectsResEntity res = new GetHealthProjectsResEntity();
		try {
			conn = DBUtils.getConnection();
			StringBuffer sql = new StringBuffer(
					"SELECT health_project.health_project_min,health_project.health_project_min_type,health_project.health_project_max,health_project.health_project_max_type,health_project.scope,health_project.health_project_default_gold,health_project.desc,health_project.health_project_paid_max_gold,health_project.detail_url,health_project.health_project_person_num,health_project.health_project_paid_max_gold,health_project.health_project_id,health_project.health_project_name,health_project.has_amount,health_project.all_member,health_project.index_img,health_project.serious_disease_url,health_project.clause_url,health_project.share_url FROM health_project WHERE STATUS = ");
			sql.append(HEALTH_PROJECT_STATUS_NORMAL).append(" ORDER BY health_project.all_member DESC LIMIT ?,?");
			pstat = conn.prepareStatement(sql.toString());
			pstat.setObject(1, req.begin);
			if (req.count > 0) {
				pstat.setObject(2, req.count);
			} else {
				pstat.setObject(2, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			List<Detail> detail_list = new ArrayList<>();
			Detail detail = null;
			while (rs.next()) {
				detail = new GetHealthProjectsResEntity.Detail();
				detail.health_project_id = rs.getString("health_project_id");
				if(detail.health_project_id.equals(Config.HealthProjectType.HEALTH_PROJECT_TYPE_CHILDREN_ID))
					detail.type=Config.HealthProjectType.HEALTH_PROJECT_TYPE_CHILDREN;
				else if(detail.health_project_id.equals(Config.HealthProjectType.HEALTH_PROJECT_TYPE_YOUTH_ID))
					detail.type=Config.HealthProjectType.HEALTH_PROJECT_TYPE_YOUTH;
				detail.health_project_name = rs.getString("health_project_name");
//				detail.all_member = rs.getInt("all_member");
				detail.all_member=HealthProjectMemberDao.getAllMemberCount(conn, rs.getString("health_project_id"));
				int officelpersonnum=HealthProjectMemberDao.getAllMemberCount(conn,rs.getString("health_project_id"));
				if(officelpersonnum<=rs.getInt("health_project_person_num")){					
					detail.share_amount = 3;
				}else{
					detail.share_amount = MathUtils.divide(rs.getFloat("health_project_paid_max_gold"), officelpersonnum);
				}
				detail.has_amount = rs.getFloat("has_amount");
				detail.detail_url = rs.getString("detail_url");
				detail.clause_url=rs.getString("clause_url");
				detail.serious_disease_url=rs.getString("serious_disease_url");
				String index_img = rs.getString("index_img");
				if (!StringUtils.emptyString(index_img)) {
					detail.index_img = ImageHelper.getImageUrl(request, index_img);
				}
				detail.join_money= rs.getFloat("health_project_default_gold");
				detail.desc= rs.getString("desc");
				detail.max_security= rs.getFloat("health_project_paid_max_gold");
				detail.scope=rs.getString("scope");
				detail.min_age=rs.getInt("health_project_min");
				detail.min_type=rs.getInt("health_project_min_type");
				detail.max_age=rs.getInt("health_project_max");
				detail.max_type=rs.getInt("health_project_max_type");
				detail.share_url=rs.getString("share_url");
				detail_list.add(detail);
			}
			res.detail_list = detail_list;
			boolean has_more_data = true;
			if (req.count > 0) {
				has_more_data = res.detail_list.size() == req.count;
			} else {
				has_more_data = res.detail_list.size() == Config.ONCE_QUERY_COUNT;
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
	 * 获取健康互助详情
	 * 
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetHealthProjectDetailResEntity getHealthProjectDetail(GetHealthProjectDetailReqEntity req,
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
					GetHealthProjectDetailResEntity res = new GetHealthProjectDetailResEntity();
					res.status = Config.STATUS_TOKEN_ERROR;
					return res;
				}
			}
			StringBuffer sb = new StringBuffer();
			sb.append(" select health_project.health_project_person_num,health_project.health_project_paid_max_gold,health_project.health_project_id,health_project.health_project_name,health_project.has_amount,health_project.all_member,health_project.index_img,health_project.property_img,health_project.time,health_project.status,health_project.scope from health_project");
			sb.append(" WHERE health_project.health_project_id =?");
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.health_project_id);
			rs = pstat.executeQuery();
			GetHealthProjectDetailResEntity res = new GetHealthProjectDetailResEntity();
			int officelpersonnum=HealthProjectMemberDao.getAllMemberCount(conn,req.health_project_id);
			if (rs.next()) {
				res.health_project_id=rs.getString("health_project_id");
				res.health_project_name=rs.getString("health_project_name");
				res.property_img=rs.getString("property_img");
				int join_today_num=HealthProjectMemberDao.getTodayMemberCountByHealthProjectId(conn, req.health_project_id);
				res.join_today_num=join_today_num;
				res.all_member=HealthProjectMemberDao.getAllMemberCount(conn, req.health_project_id);
				if(officelpersonnum<=rs.getInt("health_project_person_num")){					
					res.share_amount = 3;
				}else{
					res.share_amount = MathUtils.divide(rs.getFloat("health_project_paid_max_gold"), officelpersonnum);
				}
				if(user!=null){
					res.isjoin=HealthProjectMemberDao.isJoin(conn,req.health_project_id, user.user_id);
				}
				res.health_event_num=HealthProjectEventDao.getEventCount(conn, res.health_project_id);
				res.has_amount=rs.getFloat("has_amount");
				res.scope=rs.getString("scope");
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
	
	public static int updateHealthProject(DBUtils.ConnectionCache conn,String health_project_id, HealthProjectValuInfo value,String set,
			String where) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("update health_project set has_amount=has_amount+?,all_member=all_member+?");
		if (!StringUtils.emptyString(set)) {
			sql.append(" ,").append(set);
		}
		sql.append(" WHERE health_project_id=?");
		if (!StringUtils.emptyString(where)) {
			sql.append(" AND ").append(where);
		}
		if (value.has_amount < 0) {
			sql.append(" AND has_amount>=").append(Math.abs(value.has_amount));
		}
		int row = DBUtils.executeUpdate(conn, sql.toString(), new Object[] { value.has_amount, value.all_member, health_project_id });
		return row;
	}
	
	public static HealthProjectInfo getHealthProjectInfo(DBUtils.ConnectionCache conn, String healthProjectID) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select health_project_id,health_project_name,has_amount,all_member,index_img,property_img,time,status,health_project_min,health_project_min_type,health_project_max,health_project_max_type,health_project_default_gold,detail_url,health_project_paid_max_gold,health_project_min_gold,health_project_person_num,health_project_member_conver,effect_time_limit from health_project where health_project_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, healthProjectID);
			rs = pstat.executeQuery();
			if (rs.next()) {
				HealthProjectInfo healthProjectInfo = new HealthProjectInfo();
				healthProjectInfo.health_project_id = rs.getString("health_project_id");
				healthProjectInfo.health_project_name = rs.getString("health_project_name");
				healthProjectInfo.has_amount = rs.getFloat("has_amount");
				healthProjectInfo.all_member = rs.getInt("all_member");
				healthProjectInfo.index_img = rs.getString("index_img");
				healthProjectInfo.property_img = rs.getString("property_img");
				healthProjectInfo.status = rs.getInt("status");
				healthProjectInfo.time = rs.getLong("time");
				
				healthProjectInfo.health_project_min = rs.getInt("health_project_min");
				healthProjectInfo.health_project_min_type = rs.getInt("health_project_min_type");
				healthProjectInfo.health_project_max = rs.getInt("health_project_max");
				healthProjectInfo.health_project_max_type = rs.getInt("health_project_max_type");
				healthProjectInfo.health_project_default_gold = rs.getFloat("health_project_default_gold");
				healthProjectInfo.detail_url = rs.getString("detail_url");
				healthProjectInfo.health_project_paid_max_gold = rs.getFloat("health_project_paid_max_gold");
				healthProjectInfo.health_project_min_gold = rs.getFloat("health_project_min_gold");
				healthProjectInfo.health_project_person_num = rs.getInt("health_project_person_num");
				healthProjectInfo.health_project_member_conver = rs.getLong("health_project_member_conver");
				healthProjectInfo.effect_time_limit = rs.getLong("effect_time_limit");
				return healthProjectInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	public static List<HealthProjectInfo> getHealthProjects(DBUtils.ConnectionCache conn) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		List<HealthProjectInfo> healthProjects=null;
		try {
			String sql = "select health_project_id,health_project_name,has_amount,all_member,index_img,property_img,time,status,health_project_min,health_project_min_type,health_project_max,health_project_max_type,health_project_default_gold,detail_url,health_project_paid_max_gold,health_project_min_gold,health_project_person_num,health_project_member_conver from health_project";
			pstat = conn.prepareStatement(sql);
			rs = pstat.executeQuery();
			healthProjects=new ArrayList<HealthProjectInfo>();
			while (rs.next()) {
				HealthProjectInfo healthProjectInfo = new HealthProjectInfo();
				healthProjectInfo.health_project_id = rs.getString("health_project_id");
				healthProjectInfo.health_project_name = rs.getString("health_project_name");
				healthProjectInfo.has_amount = rs.getFloat("has_amount");
				healthProjectInfo.all_member = rs.getInt("all_member");
				healthProjectInfo.index_img = rs.getString("index_img");
				healthProjectInfo.property_img = rs.getString("property_img");
				healthProjectInfo.status = rs.getInt("status");
				healthProjectInfo.time = rs.getLong("time");
				
				healthProjectInfo.health_project_min = rs.getInt("health_project_min");
				healthProjectInfo.health_project_min_type = rs.getInt("health_project_min_type");
				healthProjectInfo.health_project_max = rs.getInt("health_project_max");
				healthProjectInfo.health_project_max_type = rs.getInt("health_project_max_type");
				healthProjectInfo.health_project_default_gold = rs.getFloat("health_project_default_gold");
				healthProjectInfo.detail_url = rs.getString("detail_url");
				healthProjectInfo.health_project_paid_max_gold = rs.getFloat("health_project_paid_max_gold");
				healthProjectInfo.health_project_min_gold = rs.getFloat("health_project_min_gold");
				healthProjectInfo.health_project_person_num = rs.getInt("health_project_person_num");
				healthProjectInfo.health_project_member_conver = rs.getLong("health_project_member_conver");
				healthProjects.add(healthProjectInfo);
			}
			return healthProjects;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	/**
	 * 健康互助个数
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static int getHealthProjectNum(DBUtils.ConnectionCache conn)
			throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		int num=0;
		try {
			StringBuffer sql = new StringBuffer("select count(health_project_id) num from health_project");
			pstat = conn.prepareStatement(sql.toString());
			rs = pstat.executeQuery();
			if (rs.next()) {
				num= rs.getInt("num");
			}
			return num;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	public static int getHealthProjectTotalGold(DBUtils.ConnectionCache conn,GetCommonDataResEntity res)
			throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		int num=0;
		try {
			StringBuffer sql = new StringBuffer("select health_project_id,has_amount from health_project");
			pstat = conn.prepareStatement(sql.toString());
			rs = pstat.executeQuery();
			while (rs.next()) {
				if(rs.getString("health_project_id").equals("0")){
					res.children_amount=rs.getFloat("has_amount");
				}else if(rs.getString("health_project_id").equals("1")){
					res.youth_amount=rs.getFloat("has_amount");
				}
			}
			res.health_amount=MathUtils.sum(res.children_amount, res.youth_amount);
			return num;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	public static void getHealthProjectTotalGold(DBUtils.ConnectionCache conn,AdminHomepageDataResEntity res)
			throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			StringBuffer sql = new StringBuffer("select health_project_id,has_amount from health_project");
			pstat = conn.prepareStatement(sql.toString());
			rs = pstat.executeQuery();
			while (rs.next()) {
				if(rs.getString("health_project_id").equals("0")){
					res.children_amount=rs.getFloat("has_amount");
				}else if(rs.getString("health_project_id").equals("1")){
					res.youth_amount=rs.getFloat("has_amount");
				}
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	

	/**
	 * 获取后台财务数据
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static List<HomepageGraphInfo> getHealthinfo(DBUtils.ConnectionCache conn)throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		HomepageGraphInfo info=null;
		List<HomepageGraphInfo> infolist=null;
		try {
			StringBuffer sb=new StringBuffer("select health_project_id,has_amount from health_project");
			pstat = conn.prepareStatement(sb.toString());
			rs = pstat.executeQuery();
			infolist=new ArrayList<HomepageGraphInfo>();
			while (rs.next()) {
				info=new HomepageGraphInfo();
				info.date_str=rs.getString("health_project_id");
				info.gold=rs.getFloat("has_amount");
				infolist.add(info);
			}
			return infolist;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	/**
	 * 获取财务后台数据
	 * @param req
	 * @return
	 * @throws SQLException
	 */
	public static SearchHealthProjectDetailsResEntity getHealthProjectDetails(SearchHealthProjectDetailsReqEntity req) throws SQLException {
		SearchHealthProjectDetailsResEntity res = new SearchHealthProjectDetailsResEntity();
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		List<SearchHealthProjectDetailsResEntity.SearchHealthProjectDetail> healthprojectdetails=null;
		SearchHealthProjectDetailsResEntity.SearchHealthProjectDetail detail=null;
		try {
			conn = DBUtils.getConnection();
			FinancialInfo finanInfo = FinancialDao.getFinancialByToken(conn, req.financial_token);
			if (finanInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sb=new StringBuffer("SELECT hpm.`name`,hpm.health_project_member_id,hpgc.time,hpgc.type,hpgc.health_project_gold_change_id,hpgc.gold,hpm.health_project_id,hpm.balance,hpm.status");
			sb.append(" FROM health_project_gold_change hpgc INNER JOIN health_project_member hpm ON hpgc.health_project_member_id = hpm.health_project_member_id");
			sb.append(" where 1=1");
			if(req.begin_time!=0)
				sb.append(" and hpgc.time >=").append(req.begin_time);
			if(req.end_time!=0)
				sb.append(" and hpgc.time <=").append(req.end_time);
			sb.append(" order by hpgc.time desc LIMIT ?,?");
			pstat = conn.prepareStatement(sb.toString());
			req.begin = req.begin < 0 ? 0 : req.begin;
			pstat.setObject(1, req.begin);
			if (req.count > 0) {
				pstat.setObject(2, req.count);
			} else {
				pstat.setObject(2, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			healthprojectdetails=new ArrayList<SearchHealthProjectDetailsResEntity.SearchHealthProjectDetail>();
			while (rs.next()) {
				detail=new SearchHealthProjectDetailsResEntity.SearchHealthProjectDetail();
				detail.name=rs.getString("name");
				detail.health_project_member_id=rs.getString("health_project_member_id");
				detail.time=rs.getLong("time");
				detail.type=rs.getInt("type");
				detail.health_project_gold_change_id=rs.getString("health_project_gold_change_id");
				detail.gold=rs.getFloat("gold");
				detail.health_project_id=rs.getString("health_project_id");
				detail.balance=rs.getFloat("balance");
				detail.status=rs.getInt("status");
				healthprojectdetails.add(detail);
			}
			res.healthprojectdetails=healthprojectdetails;
			boolean has_more_data = true;
			if (req.count > 0) {
				has_more_data = res.healthprojectdetails.size() == req.count;
			} else {
				has_more_data = res.healthprojectdetails.size() == Config.ONCE_QUERY_COUNT;
			}
			res.has_more_data = has_more_data;
			res.status = Config.STATUS_OK;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
		return res;
	}
}

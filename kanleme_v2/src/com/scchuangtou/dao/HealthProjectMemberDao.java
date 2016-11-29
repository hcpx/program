package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.config.AdminConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.config.MessageConfig;
import com.scchuangtou.entity.AdminGetHealthProjectMembersReqEntity;
import com.scchuangtou.entity.AdminGetHealthProjectMembersResEntity;
import com.scchuangtou.entity.GetHealthProjectMembersReqEntity;
import com.scchuangtou.entity.GetHealthProjectMembersResEntity;
import com.scchuangtou.entity.HealthProjectDetailManageReqEntity;
import com.scchuangtou.entity.HealthProjectDetailManageResEntity;
import com.scchuangtou.entity.JoinHealthProjectReqEntity;
import com.scchuangtou.entity.JoinHealthProjectResEntity;
import com.scchuangtou.entity.VerifyIdcarIsjoinReqEntity;
import com.scchuangtou.entity.VerifyIdcarIsjoinResEntity;
import com.scchuangtou.entity.JoinHealthProjectReqEntity.PersonInfo;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.model.AdminInfo;
import com.scchuangtou.model.FinancialInfo;
import com.scchuangtou.model.HealthProjectGoldChangeInfo;
import com.scchuangtou.model.HealthProjectInfo;
import com.scchuangtou.model.HealthProjectMemberInfo;
import com.scchuangtou.model.HealthProjectValuInfo;
import com.scchuangtou.model.TopUpOrderInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserMessageInfo;
import com.scchuangtou.model.UserVouchersInfo;
import com.scchuangtou.task.MessageTask;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.DateUtil;
import com.scchuangtou.utils.IDCardUtil;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.MD5Utils;
import com.scchuangtou.utils.MathUtils;
import com.scchuangtou.utils.StringUtils;

public class HealthProjectMemberDao {
	public static int HEALTH_PROJECT_STATUS_WATCH = 0; // 观察期
	public static int HEALTH_PROJECT_OVER = 1; // 失效会员
	public static int HEALTH_PROJECT_OFFICIAL = 2; // 正式会员
	public static int HEALTH_PROJECT_INVALID  = 3; // 超出保障范围
	public static int HEALTH_PROJECT_ID_TYPE_DAY = 0;  // 根据天数来限定
	public static int HEALTH_PROJECT_ID_TYPE_YEAR = 1; // 根据年数来限定

	/**
	 * 获取今天加入计划的人员数
	 * 
	 * @param conn
	 * @param health_project_id
	 * @return
	 * @throws SQLException
	 */
	public static int getTodayMemberCountByHealthProjectId(DBUtils.ConnectionCache conn, String health_project_id)
			throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			long time = System.currentTimeMillis();
			long endtime = DateUtil.getDayEndTime(time);
			long begintime = DateUtil.getDayTime(time);
			String sql = "SELECT count(health_project_member_id) FROM health_project_member WHERE health_project_id=? and join_time>? and join_time<?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, health_project_id);
			pstat.setObject(2, begintime);
			pstat.setObject(3, endtime);
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

	/**
	 * 加入健康互助项目
	 * 
	 * @param req
	 * @return
	 * @throws SQLException
	 */
	public static JoinHealthProjectResEntity joinHealthProject(JoinHealthProjectReqEntity req) throws SQLException {
		JoinHealthProjectResEntity res = new JoinHealthProjectResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			int row = 0;
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			User inviteUser = null;
			if (StringUtils.emptyString(user.invite_user)) {
				if (!StringUtils.emptyString(req.invite_code)) {
					String inviteUserID = UserDao.getUserIdByInviteCode(conn, req.invite_code);
					if (StringUtils.emptyString(inviteUserID)) {
						res.status = Config.STATUS_INVITE_CODE_NOT_EXITS;
						return res;
					}
					inviteUser = UserDao.getUser(conn, inviteUserID);
				}
			} else {
				req.invite_code = null;
				inviteUser = UserDao.getUser(conn, user.invite_user);
			}
			float voucher_value=0;
			if(req.vouchers_id!=null && req.vouchers_id.size()!=0){
				for(String id:req.vouchers_id){
					UserVouchersInfo userVouchersInfo=UserVouchersDao.getHealthProjectUserVouchersInfo(conn, id);
					if (userVouchersInfo==null) {
						res.status = Config.STATUS_NOT_EXITS;
						return res;
					}
					if(userVouchersInfo.end_time==UserVouchersDao.USER_VOUCHERS_INVALID_MAX_TIME){
						if(userVouchersInfo.status==UserVouchersDao.USER_VOUCHERS_STATUS_BARRED){
							return null;
						}
					}else if(userVouchersInfo.status==UserVouchersDao.USER_VOUCHERS_STATUS_BARRED || userVouchersInfo.end_time<System.currentTimeMillis()){
						return null;
					}
					voucher_value=MathUtils.sum(voucher_value, Math.abs(userVouchersInfo.gold));
				}
			}
			HealthProjectInfo healthProjectInfo = HealthProjectDao.getHealthProjectInfo(conn, req.health_project_id);
			if (healthProjectInfo == null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			// 根据身份证来判断，此人属于哪个互助项目
			int flage=getHealthProjectByID(conn,req.join_person_list,healthProjectInfo);
			if(flage==-2){
				res.status = Config.STATUS_ID_CARD_ERROR;
				return res;
			}
			float total_amount = 0;
			for(PersonInfo personInfo:req.join_person_list){
				total_amount=MathUtils.sum(total_amount,personInfo.healthProjectInfo.health_project_default_gold);
			}
			int type=Config.HealthProjectPayType.HEALTH_PROJECT_PAY_TYPE_ALIPAY;
			if (StringUtils.emptyString(req.order_num)) {
				if (!StringUtils.emptyString(user.trade_password)) {
					if (!user.trade_password.equals(req.traders_password)) {
						res.status = Config.STATUS_PASSWORD_ERROR;
						return res;
					}
				}
			} else {
				TopUpOrderInfo topUpOrderInfo = TopUpOrderDao.getTopUpMoneyByOrderNo(conn, user.user_id, req.order_num);
				if (topUpOrderInfo==null || topUpOrderInfo.topup_money != total_amount) {
					return null;
				}else{
					if(topUpOrderInfo.type==0)
						type=Config.HealthProjectPayType.HEALTH_PROJECT_PAY_TYPE_ALIPAY;
					else if(topUpOrderInfo.type==1)
						type=Config.HealthProjectPayType.HEALTH_PROJECT_PAY_TYPE_WECHAT;
				}
			}
			List<String> health_project_member_id = new ArrayList<>();
			float sub_gold = 0;
			try {
				DBUtils.beginTransaction(conn);
				// 对用户金币进行扣除
				if(voucher_value<total_amount){
					sub_gold=MathUtils.sub(voucher_value, total_amount);
				}
				String gold_note_id = UserDao.updateUserValue(conn, user.user_id, sub_gold, req.vouchers_id, null, null, Config.GoldChangeType.GOLD_CHANGE_TYPE_HEALTH_PAY, null);
				if(StringUtils.emptyString(gold_note_id)){
					res.status = Config.STATUS_GOLD_LACK;
					row = 0;
				}else{
					// 插入成员表
					row = joinHealthProjectBatch(conn, req.join_person_list, user, req, health_project_member_id);
					// 改变金额总数及其成员数
					if (row > 0) {
						HealthProjectValuInfo value = new HealthProjectValuInfo();
						value.all_member = row;
						value.has_amount = total_amount;
						row = HealthProjectDao.updateHealthProject(conn, req.health_project_id, value, null, null);
					} else if (row == -1) {
						res.status = Config.STATUS_NOT_EXITS;
					} else {
						res.status = Config.STATUS_REPEAT_ERROR;
					}
					if(row>0 && inviteUser!=null){
						row = UserDao.updateRedPacketCount(conn, inviteUser.user_id, req.join_person_list.size());
					}
				}
				// 对金币变动表进行更新
				if (row > 0) {
					List<HealthProjectGoldChangeInfo> healthProjectGoldChangeInfoList = new ArrayList<>();
					for (int i = 0; i < health_project_member_id.size(); i++) {
						HealthProjectGoldChangeInfo info = new HealthProjectGoldChangeInfo();
						info.gold = req.join_person_list.get(i).healthProjectInfo.health_project_default_gold;
						req.join_person_list.get(i).healthProjectInfo=null;
						info.user_id = user.user_id;
						info.health_project_member_id = health_project_member_id.get(i);
						info.type = type;
						healthProjectGoldChangeInfoList.add(info);
					}
					row = HealthProjectGoldChangeDao.insertHealthProjectGoldChangeBatch(conn,
							healthProjectGoldChangeInfoList, gold_note_id);
				}
				if(row > 0 && voucher_value>0){
					//更新抵扣卷的状态
					row = UserVouchersDao.updateVouchers(conn, user.user_id, req.vouchers_id);
				}
				if(row > 0){
					// 对用户邀请人进行更新
					if (!StringUtils.emptyString(req.invite_code)) {
						row = UserDao.updateInviteUser(conn, user.user_id, req.invite_code);
					}
				}
				if (row > 0) {
					if (inviteUser != null) {
						MessageTask.MessageParam mMessageParam = joinHealthProjectNotify(conn, req, inviteUser, user,
								health_project_member_id.size());
						if (mMessageParam != null) {
							DBUtils.commitTransaction(conn);
							MessageTask.addMessage(mMessageParam);
						} else {
							row = 0;
						}
					} else {
						DBUtils.commitTransaction(conn);
					}
				}
			} catch (Exception e) {
				row = 0;
				LogUtils.log(e);
			}
			if (row > 0) {
				res.person_info_list = req.join_person_list;
				res.health_project_member_id = health_project_member_id;
				res.gold = MathUtils.sum(user.gold, sub_gold);
				res.status = Config.STATUS_OK;
			} else {
				DBUtils.rollbackTransaction(conn);
				if (res.status == null)
					res.status = Config.STATUS_SERVER_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	private static MessageTask.MessageParam joinHealthProjectNotify(DBUtils.ConnectionCache conn,
			JoinHealthProjectReqEntity req, User inviteUser, User user, int num) throws SQLException {
		UserMessageInfo mUserMessageInfo = new UserMessageInfo();
		mUserMessageInfo.message_user = inviteUser.user_id;
		mUserMessageInfo.message_type = Config.MessageType.MESSAGE_TYPE_JOIN_HEALTH_PROJECT;
		mUserMessageInfo.action_user = user.user_id;
		mUserMessageInfo.message_content = String.valueOf(user.nickname);

		mUserMessageInfo.action_content = String.valueOf(num);
		String messageId = UserMessageDao.addUserMessage(conn, mUserMessageInfo);
		if (!StringUtils.emptyString(messageId)) {
			MessageTask.MessageOs os = MessageTask.getOs(inviteUser.os);
			MessageTask.MessageParam mMessageParam = new MessageTask.MessageParam(os, mUserMessageInfo.message_type,
					messageId);
			mMessageParam.addAlias(mUserMessageInfo.message_user);
			mMessageParam.title = MessageConfig.TITLE;
			mMessageParam.description = MessageConfig.getMessageDescription(mUserMessageInfo, user.nickname);
			return mMessageParam;
		}
		return null;
	}

	public static String createHealthProhcetMemberId(String object_id, String id_num) {
		return MD5Utils.md5((object_id + id_num).getBytes(Charset.forName(Config.CHARSET)), MD5Utils.MD5Type.MD5_16);
	}

	/**
	 * 批量插入需要加入的人员
	 * 
	 * @param conn
	 * @param list
	 * @param red_packet_id
	 * @return
	 * @throws Exception
	 */
	public static int joinHealthProjectBatch(DBUtils.ConnectionCache conn,
			List<JoinHealthProjectReqEntity.PersonInfo> list, User user, JoinHealthProjectReqEntity req,
			List<String> health_project_member_id) throws Exception {
		int row = 0;
		List<Map<String, Object>> dataLists = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			HealthProjectInfo healthProjectInfo = list.get(i).healthProjectInfo;
			if (healthProjectInfo == null)
				return -1;
			String id = createHealthProhcetMemberId(healthProjectInfo.health_project_id, list.get(i).id_num);
			HashMap<String, Object> datas = new HashMap<>();
			datas.put("health_project_member_id", id);
			datas.put("health_project_id", healthProjectInfo.health_project_id);
			datas.put("user_id", user.user_id);
			datas.put("name", list.get(i).name);
			// 身份证数据库做了唯一标示
			datas.put("id_num", list.get(i).id_num);
			datas.put("phone_num", list.get(i).phone_num);
			datas.put("balance", healthProjectInfo.health_project_default_gold);
			datas.put("status", HEALTH_PROJECT_STATUS_WATCH);
			long time = System.currentTimeMillis();
			datas.put("join_time", time);
			datas.put("effect_time", time + healthProjectInfo.health_project_member_conver);
			getAgeTimeInMillis(list.get(i).id_num, healthProjectInfo.health_project_max, datas,healthProjectInfo.health_project_max_type);
			health_project_member_id.add(id);
			dataLists.add(datas);
		}
		row = DBUtils.insertBatch(conn, "insert ignore into health_project_member", dataLists);
		return row;
	}

	/**
	 * 获取正式会员
	 * 
	 * @param conn
	 * @param health_project_id
	 * @return
	 * @throws SQLException
	 */
	public static int getOfficialCount(DBUtils.ConnectionCache conn, String health_project_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			long time = System.currentTimeMillis();
			StringBuffer sb = new StringBuffer(
					"SELECT count(health_project_member_id) FROM health_project_member WHERE health_project_id=? and status=");
			sb.append(HEALTH_PROJECT_STATUS_WATCH).append(" and ").append(time)
					.append(">=health_project_member.effect_time");
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

	/**
	 * 获取总会员数不包含失效用户
	 * 总人数为观察期人数加上余额足够已经没有过保障访问的会员
	 * @param conn
	 * @param health_project_id
	 * @return
	 * @throws SQLException
	 */
	public static int getAllMemberCount(DBUtils.ConnectionCache conn, String health_project_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		long time = System.currentTimeMillis();
		try {
			StringBuffer sb = new StringBuffer(
					"SELECT count(health_project_member_id) FROM health_project_member WHERE health_project_id=? and status=");
			sb.append(HEALTH_PROJECT_STATUS_WATCH).append(" and ").append(time).append("<health_project_member.invalid_time");
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

	/**
	 * 判断用户是否加入这个互助项目
	 * 
	 * @param health_project_id
	 * @param user_id
	 * @return
	 * @throws SQLException
	 */
	public static boolean isJoin(DBUtils.ConnectionCache conn,String health_project_id, String user_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT count(health_project_member_id) FROM health_project_member WHERE health_project_id=? and user_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, health_project_id);
			pstat.setObject(2, user_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			} else {
				return false;
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	public static GetHealthProjectMembersResEntity getHealthProjectMembers(GetHealthProjectMembersReqEntity req,HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		GetHealthProjectMembersResEntity res = new GetHealthProjectMembersResEntity();
		try {
			conn = DBUtils.getConnection();
			User user = null;
			user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sql = new StringBuffer(
					"select health_project.health_project_min,health_project.health_project_min_type,health_project.health_project_max,health_project.health_project_max_type,health_project.scope,health_project.health_project_default_gold,health_project.desc,health_project.health_project_paid_max_gold,health_project.detail_url,health_project.health_project_person_num,health_project.health_project_paid_max_gold,health_project.health_project_id,health_project.health_project_name,health_project.has_amount,health_project.all_member,health_project.index_img,health_project_member.health_project_member_id,health_project_member.health_project_id,health_project_member.user_id,health_project_member.name,health_project_member.id_num,health_project_member.phone_num,health_project_member.join_time,health_project_member.effect_time,health_project_member.balance,health_project_member.over_time,health_project_member.status,health_project_member.sex,health_project_member.age,health_project_member.invalid_time from health_project_member inner join health_project on health_project.health_project_id=health_project_member.health_project_id WHERE 1=1");
			if(!StringUtils.emptyString(req.health_project_id))
				sql.append(" and health_project.health_project_id='").append(req.health_project_id).append("'");
			sql.append(" and user_id=? ORDER BY join_time desc LIMIT ?,?");
			pstat = conn.prepareStatement(sql.toString());
			req.begin = req.begin < 0 ? 0 : req.begin;
			pstat.setObject(1, user.user_id);
			pstat.setObject(2, req.begin);
			if (req.count > 0) {
				pstat.setObject(3, req.count);
			} else {
				pstat.setObject(3, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			GetHealthProjectMembersResEntity.ProjectMembers projectMembers = null;
			GetHealthProjectMembersResEntity.Detail detail=null;
			List<GetHealthProjectMembersResEntity.ProjectMembers> projectMembersList = new ArrayList<GetHealthProjectMembersResEntity.ProjectMembers>();
			while (rs.next()) {
				projectMembers = new GetHealthProjectMembersResEntity.ProjectMembers();
				detail =new GetHealthProjectMembersResEntity.Detail();
				projectMembers.no = rs.getString("health_project_member_id");
				projectMembers.balance = rs.getFloat("balance");
				projectMembers.name = rs.getString("name");
				projectMembers.id_num = rs.getString("id_num");
				projectMembers.phone_num = rs.getString("phone_num");
				projectMembers.join_time = rs.getLong("join_time");
				projectMembers.effect_time = rs.getLong("effect_time");
				int status = rs.getInt("status");
				// 这个值没有存储在数据库
				if (status == HEALTH_PROJECT_STATUS_WATCH && System.currentTimeMillis() >= rs.getLong("effect_time")) {
					status = HEALTH_PROJECT_OFFICIAL;
				}
				if(System.currentTimeMillis()>rs.getLong("invalid_time")){
					status = HEALTH_PROJECT_INVALID;
				}
				projectMembers.status = status;
				detail.health_project_id = rs.getString("health_project_id");
				if(detail.health_project_id.equals(Config.HealthProjectType.HEALTH_PROJECT_TYPE_CHILDREN_ID))
					detail.type=Config.HealthProjectType.HEALTH_PROJECT_TYPE_CHILDREN;
				else if(detail.health_project_id.equals(Config.HealthProjectType.HEALTH_PROJECT_TYPE_YOUTH_ID))
					detail.type=Config.HealthProjectType.HEALTH_PROJECT_TYPE_YOUTH;
				detail.health_project_name = rs.getString("health_project_name");
				detail.all_member = rs.getInt("all_member");
				int officelpersonnum=HealthProjectMemberDao.getAllMemberCount(conn,rs.getString("health_project_id"));
				if(officelpersonnum<=rs.getInt("health_project_person_num")){				
					detail.share_amount = 3;
				}else{
					detail.share_amount = MathUtils.divide(rs.getFloat("health_project_paid_max_gold"), officelpersonnum);
				}
				detail.has_amount = rs.getFloat("has_amount");
				detail.detail_url = rs.getString("detail_url");
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
				projectMembers.project=detail;
				projectMembersList.add(projectMembers);
			}
			res.projectMembersList = projectMembersList;
			boolean has_more_data = true;
			if (req.count > 0) {
				has_more_data = res.projectMembersList.size() == req.count;
			} else {
				has_more_data = res.projectMembersList.size() == Config.ONCE_QUERY_COUNT;
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

	public static HealthProjectMemberInfo getHealthProjectMemberInfo(DBUtils.ConnectionCache conn,
			String healthProjectMemberInfoID) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select health_project_member_id,health_project_id,user_id,name,id_num,phone_num,join_time,effect_time,balance,over_time,status,sex,age,invalid_time from health_project_member where health_project_member_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, healthProjectMemberInfoID);
			rs = pstat.executeQuery();
			if (rs.next()) {
				HealthProjectMemberInfo healthProjectMemberInfo = new HealthProjectMemberInfo();
				healthProjectMemberInfo.health_project_member_id = rs.getString("health_project_member_id");
				healthProjectMemberInfo.health_project_id = rs.getString("health_project_id");
				healthProjectMemberInfo.user_id = rs.getString("user_id");
				healthProjectMemberInfo.name = rs.getString("name");
				healthProjectMemberInfo.id_num = rs.getString("id_num");
				healthProjectMemberInfo.phone_num = rs.getString("phone_num");
				healthProjectMemberInfo.sex = rs.getInt("sex");
				healthProjectMemberInfo.age = rs.getInt("age");
				healthProjectMemberInfo.status = rs.getInt("status");
				healthProjectMemberInfo.balance = rs.getFloat("balance");
				healthProjectMemberInfo.join_time = rs.getLong("join_time");
				healthProjectMemberInfo.effect_time = rs.getLong("effect_time");
				healthProjectMemberInfo.over_time = rs.getLong("over_time");
				healthProjectMemberInfo.invalid_time = rs.getLong("invalid_time");
				return healthProjectMemberInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	/**
	 * 对成员状态进行改变
	 * 
	 * @param conn
	 * @param object_id
	 * @param commentCount
	 * @param browseCount
	 * @param praiseCount
	 * @return
	 * @throws SQLException
	 */
	public static int updateHealthProjectMember(DBUtils.ConnectionCache conn, String healthProjectMemberID, String set,
			String where, float amout) throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("update health_project_member set balance=balance+?");
		if (!StringUtils.emptyString(set)) {
			sql.append(" ,").append(set);
		}
		sql.append(" WHERE health_project_member_id=?");
		if (!StringUtils.emptyString(where)) {
			sql.append(" AND ").append(where);
		}
		if (amout < 0) {
			sql.append(" AND balance>=").append(Math.abs(amout));
		}
		int row = DBUtils.executeUpdate(conn, sql.toString(), new Object[] { amout, healthProjectMemberID });
		return row;
	}
	
	private static int getHealthProjectByID(DBUtils.ConnectionCache conn,List<PersonInfo> join_person_list,HealthProjectInfo healthProjectInfo) throws SQLException{
//		List<HealthProjectInfo> healthProjectInfos= HealthProjectDao.getHealthProjects(conn);
		int flage=0;
		for (int i = 0; i < join_person_list.size(); i++) {
			String id_num=join_person_list.get(i).id_num;
			int minage=0;
			if(healthProjectInfo.health_project_min_type==HEALTH_PROJECT_ID_TYPE_DAY){
				minage = DateUtil.getAge(id_num, DateUtil.DIFF_TYPE_DAY);
			}else
				minage = DateUtil.getAge(id_num, DateUtil.DIFF_TYPE_YEAR);
			int maxage=0;
			if(healthProjectInfo.health_project_max_type==HEALTH_PROJECT_ID_TYPE_DAY)
				maxage = DateUtil.getAge(id_num, DateUtil.DIFF_TYPE_DAY);
			else
				maxage = DateUtil.getAge(id_num, DateUtil.DIFF_TYPE_YEAR);
			if(minage>=healthProjectInfo.health_project_min && maxage<=healthProjectInfo.health_project_max){
				join_person_list.get(i).healthProjectInfo = healthProjectInfo;
				join_person_list.get(i).type = healthProjectInfo.health_project_name;
			}else{
//				for(HealthProjectInfo info:healthProjectInfos){
//					if(info.health_project_id.equals(healthProjectInfo.health_project_id))
//						continue;
//					if(info.health_project_min_type==HEALTH_PROJECT_ID_TYPE_DAY)
//						minage = DateUtil.getAge(id_num, DateUtil.DIFF_TYPE_DAY);
//					else
//						minage = DateUtil.getAge(id_num, DateUtil.DIFF_TYPE_YEAR);
//					if(info.health_project_max_type==HEALTH_PROJECT_ID_TYPE_DAY)
//						maxage = DateUtil.getAge(id_num, DateUtil.DIFF_TYPE_DAY);
//					else
//						maxage = DateUtil.getAge(id_num, DateUtil.DIFF_TYPE_YEAR);
//					if(minage>=info.health_project_min && maxage<=info.health_project_max){
//						join_person_list.get(i).healthProjectInfo = info;
//						join_person_list.get(i).type = info.health_project_name;
//						break;
//					}
//				}
				flage=-2;
				break;
			}
		}
		return flage;
	}
	
	/**
	 * 根据身份证获取失效时间和出生时间
	 * @param IDCardNum
	 * @param addYear
	 * @param datas
	 * @param max_type
	 */
	public static void getAgeTimeInMillis(String IDCardNum, int addYear,HashMap<String, Object> datas,int max_type) {
		int year = 0, month = 0, day = 0, idLength = IDCardNum.length();
		Calendar cal1 = Calendar.getInstance();
		if (idLength == 18) {
			year = Integer.parseInt(IDCardNum.substring(6, 10));
			month = Integer.parseInt(IDCardNum.substring(10, 12));
			day = Integer.parseInt(IDCardNum.substring(12, 14));
		} else if (idLength == 15) {
			year = Integer.parseInt(IDCardNum.substring(6, 8)) + 1900;
			month = Integer.parseInt(IDCardNum.substring(8, 10));
			day = Integer.parseInt(IDCardNum.substring(10, 12));
		} 
		cal1.set(year, month, day);
		long begin=cal1.getTime().getTime();
		if(max_type==HEALTH_PROJECT_ID_TYPE_YEAR)
			cal1.set(year+addYear, month, day);
		if(max_type==HEALTH_PROJECT_ID_TYPE_DAY)
			cal1.set(year, month, day+addYear);
		long end=cal1.getTime().getTime();
		datas.put("birthday", begin);
		datas.put("invalid_time", end);
		datas.put("sex", IDCardUtil.getSexFromId(IDCardNum));
		datas.put("age", DateUtil.getAge(IDCardNum, DateUtil.DIFF_TYPE_YEAR));
	}
	//TODO 下次更新正式服务器时候运行增加对加入健康互助用户性别的年龄的数据记录
	public static void main(String[] args) {
		DBUtils.ConnectionCache conn =null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		int row = 0;
		try {
			try {
				conn= DBUtils.getConnection();
				DBUtils.beginTransaction(conn);
				StringBuffer sb = new StringBuffer(
						"SELECT health_project_member_id,id_num FROM health_project_member where (sex is null or sex ='') and (age is null or age ='')");
				pstat = conn.prepareStatement(sb.toString());
				rs = pstat.executeQuery();
				while (rs.next()) {
					StringBuffer sql = new StringBuffer();
					String health_project_member_id=rs.getString("health_project_member_id");
					String id_num=rs.getString("id_num");
					sql.append("update health_project_member set age=?,sex=? where health_project_member_id=?");
					row = DBUtils.executeUpdate(conn, sql.toString(), new Object[] { DateUtil.getAge(id_num, DateUtil.DIFF_TYPE_YEAR), IDCardUtil.getSexFromId(id_num),health_project_member_id });
					if(row<=0)
						break;
				} 
				if(row>0)
					DBUtils.commitTransaction(conn);
			} catch (Exception e) {
				row = 0;
				LogUtils.log(e);
			}
			if (row <= 0) {
				DBUtils.rollbackTransaction(conn);
			}
		}  finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}
	
	/**
	 * 判断身份证合法性和是否加入健康互助
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static VerifyIdcarIsjoinResEntity verifyIdcarIsjoin(VerifyIdcarIsjoinReqEntity req)
			throws Exception {
		VerifyIdcarIsjoinResEntity res = new VerifyIdcarIsjoinResEntity();
		if(DateUtil.getAge(req.id_num, DateUtil.DIFF_TYPE_YEAR)==-1){
			res.status=Config.STATUS_ID_CARD_ERROR;
			return res;
		}
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			HealthProjectInfo healthProjectInfo=HealthProjectDao.getHealthProjectInfo(conn, req.health_project_id);
			if(healthProjectInfo==null){
				res.status=Config.STATUS_NOT_EXITS;
				return res;
			}
			//判断这个身份证是否能加入这个项目
			int minage=0;
			if(healthProjectInfo.health_project_min_type==HEALTH_PROJECT_ID_TYPE_DAY){
				minage = DateUtil.getAge(req.id_num, DateUtil.DIFF_TYPE_DAY);
			}else
				minage = DateUtil.getAge(req.id_num, DateUtil.DIFF_TYPE_YEAR);
			int maxage=0;
			if(healthProjectInfo.health_project_max_type==HEALTH_PROJECT_ID_TYPE_DAY)
				maxage = DateUtil.getAge(req.id_num, DateUtil.DIFF_TYPE_DAY);
			else
				maxage = DateUtil.getAge(req.id_num, DateUtil.DIFF_TYPE_YEAR);
			if(!(minage>=healthProjectInfo.health_project_min && maxage<=healthProjectInfo.health_project_max)){
				res.status=Config.STATUS_ID_CARD_ERROR;
				return res;
			}
			String memberid=createHealthProhcetMemberId(req.health_project_id, req.id_num);
			HealthProjectMemberInfo healthProjectMemberInfo=getHealthProjectMemberInfo(conn, memberid);
			if (healthProjectMemberInfo==null)
				res.status=Config.STATUS_OK;
			else
				res.status=Config.STATUS_REPEAT_ERROR;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
		return res;
	}
	
	/**
	 * 改变互助成员状态
	 * @param req
	 * @return
	 * @throws SQLException
	 */
	public static HealthProjectDetailManageResEntity updateHealthProjectMemberStatus(HealthProjectDetailManageReqEntity req,String token) throws SQLException {
		HealthProjectDetailManageResEntity res = new HealthProjectDetailManageResEntity();
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
			if (!mAdminInfo.hasMode(AdminConfig.MODE_MANAGER_HEALTH_PROJECT_ADMIN)) {
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			HealthProjectMemberInfo healthProjectMemberInfo=getHealthProjectMemberInfo(conn, req.health_project_member_id);
			if (healthProjectMemberInfo == null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			int status=HEALTH_PROJECT_STATUS_WATCH;
			if(healthProjectMemberInfo.status==HEALTH_PROJECT_STATUS_WATCH)
				status=HEALTH_PROJECT_OVER;
			StringBuffer sb=new StringBuffer("status=").append(status);
			updateHealthProjectMember(conn, req.health_project_member_id, sb.toString(), null, 0);
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
	
	public static HealthProjectDetailManageResEntity updateHealthProjectMemberStatus(HealthProjectDetailManageReqEntity req) throws SQLException {
		HealthProjectDetailManageResEntity res = new HealthProjectDetailManageResEntity();
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			FinancialInfo finanInfo = FinancialDao.getFinancialByToken(conn, req.financial_token);
			if (finanInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			HealthProjectMemberInfo healthProjectMemberInfo=getHealthProjectMemberInfo(conn, req.health_project_member_id);
			if (healthProjectMemberInfo == null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			int status=HEALTH_PROJECT_STATUS_WATCH;
			if(healthProjectMemberInfo.status==HEALTH_PROJECT_STATUS_WATCH)
				status=HEALTH_PROJECT_OVER;
			StringBuffer sb=new StringBuffer("status=").append(status);
			updateHealthProjectMember(conn, req.health_project_member_id, sb.toString(), null, 0);
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
	
	public static AdminGetHealthProjectMembersResEntity adminGetHealthProjectMembers(AdminGetHealthProjectMembersReqEntity req, HttpServletRequest request,
			String token)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		AdminGetHealthProjectMembersResEntity res = new AdminGetHealthProjectMembersResEntity();
		try {
			conn = DBUtils.getConnection();
			AdminInfo mAdminInfo = AdminDao.getAdminByToken(conn, token);
			if (mAdminInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!mAdminInfo.hasMode(AdminConfig.MODE_MANAGER_HEALTH_PROJECT_ADMIN)) {
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			StringBuffer sql = new StringBuffer(
					"select h.invalid_time,h.health_project_member_id,u.nickname,h.name,h.health_project_id,u1.nickname invite_name,h.status,h.effect_time,h.balance,h.join_time from health_project_member h INNER JOIN userinfo u on u.user_id=h.user_id");
			sql.append(" LEFT JOIN userinfo u1 on u.invite_user=u1.user_id");
			if(!StringUtils.emptyString(req.key_word)){
				sql.append(" where u.nickname like ? ");
				sql.append(" or u1.nickname like ? ");
				sql.append(" or h.name like ? ");
			}
			sql.append(" order by h.join_time desc limit ?,?");
			pstat = conn.prepareStatement(sql.toString());
			req.begin = req.begin < 0 ? 0 : req.begin;
			if(!StringUtils.emptyString(req.key_word)){
				pstat.setObject(1, "%"+req.key_word+"%");
				pstat.setObject(2, "%"+req.key_word+"%");
				pstat.setObject(3, "%"+req.key_word+"%");
				pstat.setObject(4, req.begin);
				if (req.count > 0) {
					pstat.setObject(5, req.count);
				} else {
					pstat.setObject(5, Config.ONCE_QUERY_COUNT);
				}
			}else{
				pstat.setObject(1, req.begin);
				if (req.count > 0) {
					pstat.setObject(2, req.count);
				} else {
					pstat.setObject(2, Config.ONCE_QUERY_COUNT);
				}
			}
			rs = pstat.executeQuery();
			AdminGetHealthProjectMembersResEntity.ProjectMembers projectMembers = null;
			List<AdminGetHealthProjectMembersResEntity.ProjectMembers> projectMembersList = new ArrayList<AdminGetHealthProjectMembersResEntity.ProjectMembers>();
			while (rs.next()) {
				projectMembers = new AdminGetHealthProjectMembersResEntity.ProjectMembers();
				projectMembers.health_project_member_id = rs.getString("health_project_member_id");
				projectMembers.nickname = rs.getString("nickname");
				projectMembers.name = rs.getString("name");
				projectMembers.invite_name=rs.getString("invite_name");
				projectMembers.health_project_id = rs.getString("health_project_id");
				int status = rs.getInt("status");
				// 这个值没有存储在数据库
				if (status == HEALTH_PROJECT_STATUS_WATCH && System.currentTimeMillis() >= rs.getLong("effect_time")) {
					status = HEALTH_PROJECT_OFFICIAL;
				}
				if(System.currentTimeMillis()>rs.getLong("invalid_time")){
					status = HEALTH_PROJECT_INVALID;
				}
				projectMembers.status = status;
				projectMembers.balance = rs.getFloat("balance");
				projectMembers.join_time = rs.getLong("join_time");
				projectMembers.effect_time = rs.getLong("effect_time");
				projectMembersList.add(projectMembers);
			}
			res.projectMembersList = projectMembersList;
			boolean has_more_data = true;
			if (req.count > 0) {
				has_more_data = res.projectMembersList.size() == req.count;
			} else {
				has_more_data = res.projectMembersList.size() == Config.ONCE_QUERY_COUNT;
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
}

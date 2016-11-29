package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.HealthProjectMemberTopUpReqEntity;
import com.scchuangtou.entity.HealthProjectMemberTopUpResEntity;
import com.scchuangtou.entity.HealthRedPacketsRecordReqEntity;
import com.scchuangtou.entity.HealthRedPacketsRecordResEntity;
import com.scchuangtou.entity.PayRecordReqEntity;
import com.scchuangtou.entity.PayRecordResEntity;
import com.scchuangtou.model.HealthProjectGoldChangeInfo;
import com.scchuangtou.model.HealthProjectInfo;
import com.scchuangtou.model.HealthProjectMemberInfo;
import com.scchuangtou.model.HealthProjectValuInfo;
import com.scchuangtou.model.TopUpOrderInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserValueInfo;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.MD5Utils;
import com.scchuangtou.utils.MathUtils;
import com.scchuangtou.utils.StringUtils;

import com.scchuangtou.utils.DBUtils.PreparedStatementCache;

public class HealthProjectGoldChangeDao {

	public static int insertHealthProjectGoldChangeBatch(DBUtils.ConnectionCache conn,
			List<HealthProjectGoldChangeInfo> list,String gold_note_id) throws Exception {
		int row = 0;
		List<Map<String, Object>> dataLists = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			HealthProjectGoldChangeInfo healthProjectGoldChangeInfo = list.get(i);
			String id = "";
			if (list.get(i).type != Config.HealthProjectPayType.HEALTH_PROJECT_PAY_TYPE_PAID)
				id = IdUtils.createId("health_project_gold_change");
			else
				id = createHealthProhcetId(healthProjectGoldChangeInfo.health_project_member_id,
						healthProjectGoldChangeInfo.health_project_event_id);
			HashMap<String, Object> datas = new HashMap<>();
			datas.put("health_project_gold_change_id", id);
			datas.put("type", healthProjectGoldChangeInfo.type);
			datas.put("health_project_event_id", healthProjectGoldChangeInfo.health_project_event_id);
			datas.put("health_project_member_id", healthProjectGoldChangeInfo.health_project_member_id);
			datas.put("user_id", healthProjectGoldChangeInfo.user_id);
			datas.put("gold", healthProjectGoldChangeInfo.gold);
			datas.put("time", System.currentTimeMillis());
			datas.put("health_project_gold_change.desc", gold_note_id);
			dataLists.add(datas);
		}
		row = DBUtils.insertBatch(conn, "insert ignore into health_project_gold_change", dataLists);
		return row;
	}

	/**
	 * 扣款的时候产生id
	 * 
	 * @param member_id
	 * @param event_id
	 * @return
	 */
	public static String createHealthProhcetId(String member_id, String event_id) {
		return MD5Utils.md5((member_id + event_id).getBytes(Charset.forName(Config.CHARSET)), MD5Utils.MD5Type.MD5_16);
	}

	/**
	 * 获取金币变成详情
	 * 
	 * @param conn
	 * @param healthProjectGoldChangeInfoID
	 * @return
	 * @throws SQLException
	 */
	public static HealthProjectGoldChangeInfo getHealthProjectGoldChangeInfo(DBUtils.ConnectionCache conn,
			String healthProjectGoldChangeInfoID, int type) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select health_project_gold_change_id,health_project_event_id,health_project_member_id,user_id,gold,time from health_project_gold_change where health_project_gold_change_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, healthProjectGoldChangeInfoID);
			rs = pstat.executeQuery();
			if (rs.next()) {
				HealthProjectGoldChangeInfo healthProjectGoldChangeInfo = new HealthProjectGoldChangeInfo();
				healthProjectGoldChangeInfo.health_project_gold_change_id = rs
						.getString("health_project_gold_change_id");
				healthProjectGoldChangeInfo.health_project_event_id = rs.getString("health_project_event_id");
				healthProjectGoldChangeInfo.health_project_member_id = rs.getString("health_project_member_id");
				healthProjectGoldChangeInfo.user_id = rs.getString("user_id");
				healthProjectGoldChangeInfo.gold = rs.getFloat("gold");
				healthProjectGoldChangeInfo.time = rs.getLong("time");
				return healthProjectGoldChangeInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	/**
	 * 充值记录
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static PayRecordResEntity listPayRecord(PayRecordReqEntity req) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		PayRecordResEntity res = new PayRecordResEntity();
		try {
			conn = DBUtils.getConnection();
			User user = null;
			user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			HealthProjectMemberInfo healthProjectMemberInfo = HealthProjectMemberDao.getHealthProjectMemberInfo(conn, req.health_project_member_id);
			if (healthProjectMemberInfo == null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			StringBuffer sql = new StringBuffer(
					"SELECT gold,time,type FROM health_project_gold_change INNER JOIN health_project_member ON health_project_member.health_project_member_id = health_project_gold_change.health_project_member_id WHERE")
							.append(" health_project_gold_change.health_project_member_id=? and health_project_gold_change.type!=").append(Config.HealthProjectPayType.HEALTH_PROJECT_PAY_TYPE_PAID).append(" ORDER BY health_project_gold_change.time DESC LIMIT ?,?");
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
			PayRecordResEntity.userinfo userinfo = null;
			List<PayRecordResEntity.userinfo> users = new ArrayList<PayRecordResEntity.userinfo>();
			while (rs.next()) {
				userinfo = new PayRecordResEntity.userinfo();
				userinfo.money = rs.getFloat("gold");
				userinfo.time = rs.getLong("time");
				userinfo.type = rs.getInt("type");
				users.add(userinfo);
			}
			res.users = users;
			boolean has_more_data = true;
			if (req.count > 0) {
				has_more_data = res.users.size() == req.count;
			} else {
				has_more_data = res.users.size() == Config.ONCE_QUERY_COUNT;
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
	 * 用户充值
	 * 
	 * @param req
	 * @return
	 * @throws SQLException
	 */
	public static HealthProjectMemberTopUpResEntity memberTopUp(HealthProjectMemberTopUpReqEntity req)
			throws SQLException {
		HealthProjectMemberTopUpResEntity res = new HealthProjectMemberTopUpResEntity();
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
			if (healthProjectMemberInfo == null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			//校验不符合要求的充值者，不允许充值
			if(System.currentTimeMillis()>healthProjectMemberInfo.invalid_time){
				return null;
			}
			int type=Config.HealthProjectPayType.HEALTH_PROJECT_PAY_TYPE_KANBI;
			if (StringUtils.emptyString(req.order_num)) {
				if (!StringUtils.emptyString(user.trade_password)) {
					if (!user.trade_password.equals(req.traders_password)) {
						res.status = Config.STATUS_PASSWORD_ERROR;
						return res;
					}
				}
			} else {
				TopUpOrderInfo topUpOrderInfo = TopUpOrderDao.getTopUpMoneyByOrderNo(conn, user.user_id, req.order_num);
				if (topUpOrderInfo==null || topUpOrderInfo.topup_money != req.amount) {
					return null;
				}else{
					if(topUpOrderInfo.type==0)
						type=Config.HealthProjectPayType.HEALTH_PROJECT_PAY_TYPE_ALIPAY;
					else if(topUpOrderInfo.type==1)
						type=Config.HealthProjectPayType.HEALTH_PROJECT_PAY_TYPE_WECHAT;
				}
			}
			HealthProjectInfo healthProjectInfo=HealthProjectDao.getHealthProjectInfo(conn, healthProjectMemberInfo.health_project_id);
			try {
				DBUtils.beginTransaction(conn);
				String health_project_gold_change_id = IdUtils.createId("health_project_gold_change");
				HashMap<String, Object> datas = new HashMap<>();
				datas.put("health_project_gold_change_id", health_project_gold_change_id);
				datas.put("health_project_member_id", healthProjectMemberInfo.health_project_member_id);
				datas.put("user_id", user.user_id);
				datas.put("gold", req.amount);
				datas.put("type", type);
				datas.put("time", System.currentTimeMillis());
				row = DBUtils.insert(conn,"insert ignore into health_project_gold_change", datas);
				// 对互助项目总金额进行增加
				if (row > 0) {
					HealthProjectValuInfo value = new HealthProjectValuInfo();
					value.has_amount = req.amount;
					row = HealthProjectDao.updateHealthProject(conn, healthProjectMemberInfo.health_project_id, value,
							null, null);
				}
				if (row > 0) {
					long now_time=System.currentTimeMillis();
					//现在时间加上需要多长时间才能够转为正式会员的时间，用来更新生效时间
					long time = now_time + healthProjectInfo.health_project_member_conver;
					//失效时间加上多久之前充值可以不重新进行入观察期的时间
					long limit= healthProjectMemberInfo.invalid_time + healthProjectInfo.effect_time_limit;
					StringBuffer setSb = new StringBuffer("`status` = ( CASE when balance >=")
							.append(healthProjectInfo.health_project_min_gold + " then "
									+ HealthProjectMemberDao.HEALTH_PROJECT_STATUS_WATCH + " else "
									+ HealthProjectMemberDao.HEALTH_PROJECT_OVER
									+ " end),health_project_member.effect_time=( CASE when (balance >=")
							.append(healthProjectInfo.health_project_min_gold + " and "+now_time+">"+limit+") then " + time
									+ " else effect_time end)");
					row = HealthProjectMemberDao.updateHealthProjectMember(conn,
							healthProjectMemberInfo.health_project_member_id, setSb.toString(), null, req.amount);
				}
				// 对用户金币进行扣除
				if (row > 0) {
					UserValueInfo userValueInfo = new UserValueInfo();
					userValueInfo.gold = 0-req.amount;
					row = UserDao.updateUserValue(conn, user.user_id, userValueInfo, null, null,
							Config.GoldChangeType.GOLD_CHANGE_TYPE_HEALTH_PAY, health_project_gold_change_id);
					if (row <= 0) {
						res.status = Config.STATUS_GOLD_LACK;
					}
				}
				if (row > 0)
					DBUtils.commitTransaction(conn);
			} catch (Exception e) {
				row = 0;
				LogUtils.log(e);
			}
			if (row > 0) {
				res.amount = req.amount;
				res.gold=MathUtils.sum(healthProjectMemberInfo.balance, req.amount);
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
	
	/**
	 * 获取已领取奖励红包的信息
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static HealthRedPacketsRecordResEntity healthRedPacketsRecord(HealthRedPacketsRecordReqEntity req) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		HealthRedPacketsRecordResEntity res = new HealthRedPacketsRecordResEntity();
		try {
			conn = DBUtils.getConnection();
			User user = null;
			user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sql = new StringBuffer(
					"SELECT red_packet_detail.money,red_packet_detail.time FROM red_packet INNER JOIN red_packet_detail ON red_packet_detail.red_packet_id = red_packet.red_packet_id where packet_type =");
			sql.append(Config.PacketType.PACKET_TYPE_HEALTH).append(" AND red_packet_detail.user_id = ? order by time desc LIMIT ?,?");
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
			HealthRedPacketsRecordResEntity.Info record = null;
			List<HealthRedPacketsRecordResEntity.Info> records = new ArrayList<HealthRedPacketsRecordResEntity.Info>();
			while (rs.next()) {
				record = new HealthRedPacketsRecordResEntity.Info();
				record.time=rs.getLong("time");
				record.gold=rs.getFloat("money");
				records.add(record);
			}
			res.records = records;
			if(records.size()>0){
				HealthRedPacketsRecordSum(conn,res,user);
			}
			boolean has_more_data = true;
			if (req.count > 0) {
				has_more_data = res.records.size() == req.count;
			} else {
				has_more_data = res.records.size() == Config.ONCE_QUERY_COUNT;
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
	
	private static void HealthRedPacketsRecordSum(DBUtils.ConnectionCache conn,HealthRedPacketsRecordResEntity res,User user) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			StringBuffer sumsql = new StringBuffer(
					"SELECT sum(red_packet_detail.money) total_amount FROM red_packet INNER JOIN red_packet_detail ON red_packet_detail.red_packet_id = red_packet.red_packet_id where packet_type =");
			sumsql.append(Config.PacketType.PACKET_TYPE_HEALTH).append(" AND red_packet_detail.user_id = ?");
			pstat = conn.prepareStatement(sumsql.toString());
			pstat.setObject(1, user.user_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				res.total_amount=rs.getFloat("total_amount");
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
}

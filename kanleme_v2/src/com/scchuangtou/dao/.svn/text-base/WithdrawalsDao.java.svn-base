package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.config.Config;
import com.scchuangtou.config.MessageConfig;
import com.scchuangtou.entity.AuditUserWithdrawalsReqEntity;
import com.scchuangtou.entity.AuditUserWithdrawalsResEntity;
import com.scchuangtou.entity.GetGraphDataResEntity;
import com.scchuangtou.entity.GetUserWithdrawalsReqEntity;
import com.scchuangtou.entity.GetUserWithdrawalsResEntity;
import com.scchuangtou.entity.GetwithdrawalsReqEntity;
import com.scchuangtou.entity.GetwithdrawalsResEntity;
import com.scchuangtou.entity.GetwithdrawalsResEntity.Withdrawals;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.entity.WithdrawalsReqEntity;
import com.scchuangtou.entity.WithdrawalsResEntity;
import com.scchuangtou.model.FinancialInfo;
import com.scchuangtou.model.HomepageGraphInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserMessageInfo;
import com.scchuangtou.model.UserValueInfo;
import com.scchuangtou.model.WithdrawalsInfo;
import com.scchuangtou.task.MessageTask;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.DateUtil;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.MathUtils;
import com.scchuangtou.utils.StringUtils;

public class WithdrawalsDao {
	// private static int getWithdrawalsCount(DBUtils.ConnectionCache conn,
	// String user_id, long stime)
	// throws SQLException {
	// PreparedStatementCache pstat = null;
	// ResultSet rs = null;
	// try {
	// String sql = "SELECT count(withdrawals.withdrawals_id) count FROM
	// `withdrawals` WHERE withdrawals.withdrawals_user=? and
	// withdrawals.withdrawals_status<>? and withdrawals.withdrawals_time>=?";
	// pstat = conn.prepareStatement(sql);
	// pstat.setObject(1, user_id);
	// pstat.setObject(2, Config.WithdrawalsType.WITHDRAWALS_NO_PASSED);
	// pstat.setObject(3, stime);
	// rs = pstat.executeQuery();
	// if (rs.next()) {
	// return rs.getInt("count");
	// } else {
	// return 0;
	// }
	// } finally {
	// DBUtils.close(rs);
	// DBUtils.close(pstat);
	// }
	// }

	public static WithdrawalsResEntity userWithdrawals(WithdrawalsReqEntity req) throws SQLException {
		req.withdrawals_gold = Config.parseGold(req.withdrawals_gold);
		if (req.withdrawals_gold % Config.Withrawals.WITHRAWALS_MIN_GOLD != 0) {
			return null;
		}
		WithdrawalsResEntity res = new WithdrawalsResEntity();
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			long t = user.ban_withdrawals_time - System.currentTimeMillis();
			if (t > 0) {
				res.status = Config.STATUS_BAN_WITHDRAWALS;
				res.ban_withdrawals_time = t;
				return res;
			}
			if (MathUtils.sub(user.gold, req.withdrawals_gold) < 0) {
				res.status = Config.STATUS_GOLD_LACK;
				return res;
			}
			if (StringUtils.emptyString(user.phone_number)) {
				res.status = Config.STATUS_NOT_BIND_PHONE;
				return res;
			}
			if (!req.trade_password.equals(user.trade_password)) {
				res.status = Config.STATUS_PASSWORD_ERROR;
				return res;
			}
			// int count = getWithdrawalsCount(conn, user.user_id,
			// DateUtil.getFirstMonthDayTime(System.currentTimeMillis()));
			// if(count > Config.Withrawals.WITHRAWALS_MONTH_COUNT){
			// res.status = Config.STATUS_REPEAT_ERROR;
			// return res;
			// }
			String withdrawals_id = IdUtils.createId(user.user_id);
			long withdrawalsTime = 0;

			DBUtils.beginTransaction(conn);
			int row = 0;
			try {
				UserValueInfo value = new UserValueInfo();
				value.gold = -req.withdrawals_gold;
				row = UserDao.updateUserValue(conn, user.user_id, value, "", "",
						Config.GoldChangeType.GOLD_CHANGE_TYPE_WITHDRAWALS, user.user_id);
				if (row > 0) {
					withdrawalsTime = System.currentTimeMillis();
					float poundage = MathUtils.multiply(req.withdrawals_gold, Config.Withrawals.WITHRAWALS_POUNDAGE);
					float withdrawals_gold = MathUtils.sub(req.withdrawals_gold, poundage);
					HashMap<String, Object> datas = new HashMap<>();
					datas.put("withdrawals_id", withdrawals_id);
					datas.put("withdrawals_account", req.withdrawals_account);
					datas.put("withdrawals_account_type", req.withdrawals_account_type);
					datas.put("withdrawals_gold", withdrawals_gold);
					datas.put("poundage", poundage);// 手续费
					datas.put("withdrawals_user", user.user_id);
					datas.put("withdrawals_time", withdrawalsTime);
					datas.put("withdrawals_status", Config.WithdrawalsType.WITHDRAWALS_APPLYING);// 提现申请中0
					datas.put("withdrawals_is_share", false);// 是否分享
					datas.put("withdrawals_account_extra", req.withdrawals_account_extra);
					row = DBUtils.insert(conn, "INSERT INTO withdrawals", datas);
				} else {
					res.status = Config.STATUS_GOLD_LACK;
				}
				if (row > 0) {
					DBUtils.commitTransaction(conn);
				}
			} catch (Exception e) {
				LogUtils.log(e);
				row = 0;
			}
			if (row > 0) {
				WithdrawalsResEntity.Data withInfo = new WithdrawalsResEntity.Data();
				withInfo.withdrawals_id = withdrawals_id;
				withInfo.withdrawals_time = withdrawalsTime;
				withInfo.gold = MathUtils.sub(user.gold, req.withdrawals_gold);
				res.data = withInfo;
				res.status = Config.STATUS_OK;
			} else {
				DBUtils.rollbackTransaction(conn);
				if (StringUtils.emptyString(res.status)) {
					res.status = Config.STATUS_SERVER_ERROR;
				}
			}
		} finally {
			DBUtils.close(conn);
		}
		return res;
	}

	/******
	 * 提现记录查询
	 *
	 * @throws SQLException
	 *****/

	public static GetwithdrawalsResEntity getWithdrawals(GetwithdrawalsReqEntity req) throws SQLException {
		GetwithdrawalsResEntity res = new GetwithdrawalsResEntity();
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
			String sql = "SELECT withdrawals_id,withdrawals_user,withdrawals_account,withdrawals_account_type,withdrawals_account_extra,withdrawals_gold,withdrawals_time,withdrawals_status,withdrawals_is_share,withdrawals_faild_desc,withdrawals_audit_user,withdrawals_audit_time,withdrawals_audit_ip,poundage FROM withdrawals"
					+ " WHERE withdrawals_user=? ORDER BY withdrawals_time DESC LIMIT ?,?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, user.user_id);
			pstat.setObject(2, req.begin);
			pstat.setObject(3, Config.ONCE_QUERY_COUNT);
			rs = pstat.executeQuery();
			res.datas = new ArrayList<Withdrawals>();
			while (rs.next()) {
				GetwithdrawalsResEntity.Withdrawals withdrawalsInfo = new GetwithdrawalsResEntity.Withdrawals();
				withdrawalsInfo.withdrawals_id = rs.getString("withdrawals_id");
				withdrawalsInfo.withdrawals_user = rs.getString("withdrawals_user");
				withdrawalsInfo.withdrawals_account = rs.getString("withdrawals_account");
				withdrawalsInfo.withdrawals_account_type = rs.getInt("withdrawals_account_type");
				withdrawalsInfo.withdrawals_account_extra = rs.getString("withdrawals_account_extra");
				withdrawalsInfo.withdrawals_gold = rs.getFloat("withdrawals_gold");
				withdrawalsInfo.poundage = rs.getFloat("poundage");
				withdrawalsInfo.withdrawals_time = rs.getLong("withdrawals_time");
				withdrawalsInfo.withdrawals_status = rs.getInt("withdrawals_status");
				withdrawalsInfo.withdrawals_is_share = rs.getBoolean("withdrawals_is_share");
				withdrawalsInfo.withdrawals_faild_desc = rs.getString("withdrawals_faild_desc");
				withdrawalsInfo.withdrawals_audit_time = rs.getLong("withdrawals_audit_time");
				res.datas.add(withdrawalsInfo);
			}
			res.has_more_data = res.datas.size() == Config.ONCE_QUERY_COUNT;
			res.status = Config.STATUS_OK;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
		return res;
	}

	/***
	 * 管理员查看所有用户的提现记录
	 * @throws Exception 
	 *****/
	public static GetUserWithdrawalsResEntity adminGetUserWithdrawals(GetUserWithdrawalsReqEntity req,HttpServletRequest request)
			throws Exception {
		GetUserWithdrawalsResEntity res = new GetUserWithdrawalsResEntity();
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			req.begin = req.begin < 0 ? 0 : req.begin;

			FinancialInfo finanInfo = FinancialDao.getFinancialByToken(conn, req.financial_token);
			if (finanInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sb = new StringBuffer(
					"SELECT userinfo.nickname,userinfo.head_pic,withdrawals_id,withdrawals_user,withdrawals_account,withdrawals_account_type,withdrawals_account_extra,withdrawals_gold,withdrawals_time,withdrawals_status,withdrawals_is_share,withdrawals_faild_desc,withdrawals_audit_user,withdrawals_audit_time,withdrawals_audit_ip,poundage FROM withdrawals inner join userinfo on userinfo.user_id=withdrawals.withdrawals_user");
			sb.append(" WHERE 1=1");
			if (req.statu == 0) {
				sb.append(" AND withdrawals_status=").append(Config.WithdrawalsType.WITHDRAWALS_APPLYING);
			} else if (req.statu == 1) {
				sb.append(" AND withdrawals_status=").append(Config.WithdrawalsType.WITHDRAWALS_PASSED);
			} else if (req.statu == 2) {
				sb.append(" AND withdrawals_status=").append(Config.WithdrawalsType.WITHDRAWALS_NO_PASSED);
			}
			if (req.start_time > 0) {
				req.start_time = DateUtil.getDayTime(req.start_time);
			}
			if (req.end_time <= 0) {
				req.end_time = System.currentTimeMillis();
			} else {
				req.end_time = DateUtil.getDayEndTime(req.end_time);
			}
			if(req.start_time != 0)
				sb.append(" AND withdrawals_time >= ").append(req.start_time);
			if(req.end_time != 0)
				sb.append(" AND withdrawals_time <= ").append(req.end_time);
			sb.append(" ORDER BY withdrawals_time DESC LIMIT ?,?");
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.begin);
			pstat.setObject(2, Config.ONCE_QUERY_COUNT);
			rs = pstat.executeQuery();
			res.datas = new ArrayList<>();
			while (rs.next()) {
				GetUserWithdrawalsResEntity.Withdrawals userWithdrawalsInfo = new GetUserWithdrawalsResEntity.Withdrawals();
				userWithdrawalsInfo.withdrawals_id = rs.getString("withdrawals_id");
				userWithdrawalsInfo.withdrawals_account = rs.getString("withdrawals_account");
				userWithdrawalsInfo.withdrawals_account_type = rs.getInt("withdrawals_account_type");
				userWithdrawalsInfo.withdrawals_account_extra = rs.getString("withdrawals_account_extra");
				userWithdrawalsInfo.withdrawals_gold = rs.getFloat("withdrawals_gold");
				userWithdrawalsInfo.poundage = rs.getFloat("poundage");
				userWithdrawalsInfo.withdrawals_total = MathUtils.sum(userWithdrawalsInfo.withdrawals_gold,
						userWithdrawalsInfo.poundage);
				userWithdrawalsInfo.withdrawals_user = rs.getString("withdrawals_user");
				userWithdrawalsInfo.withdrawals_time = rs.getLong("withdrawals_time");
				userWithdrawalsInfo.withdrawals_status = rs.getInt("withdrawals_status");
				if(rs.getString("head_pic")!=null)
					userWithdrawalsInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				userWithdrawalsInfo.nickname=rs.getString("nickname");
				res.datas.add(userWithdrawalsInfo);
			}
			res.has_more_data = res.datas.size() == Config.ONCE_QUERY_COUNT;
			res.status = Config.STATUS_OK;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
		return res;
	}

	private static WithdrawalsInfo getNormalWithdrawalsInfo(DBUtils.ConnectionCache conn, String withdrawalsId)
			throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select withdrawals_id,withdrawals_gold,withdrawals_user,withdrawals_account,poundage from withdrawals where withdrawals_id=? and withdrawals_status=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, withdrawalsId);
			pstat.setObject(2, Config.WithdrawalsType.WITHDRAWALS_APPLYING);
			rs = pstat.executeQuery();
			WithdrawalsInfo info = null;
			if (rs.next()) {
				info = new WithdrawalsInfo();
				info.withdrawals_id = rs.getString("withdrawals_id");
				float withdrawals_gold = rs.getFloat("withdrawals_gold");
				info.poundage = rs.getFloat("poundage");
				info.gold = MathUtils.sum(withdrawals_gold, info.poundage);
				info.withdrawals_user = rs.getString("withdrawals_user");
				info.withdrawals_account = rs.getString("withdrawals_account");
			}
			return info;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	private static MessageTask.MessageParam withrawalsNotify(DBUtils.ConnectionCache conn,
			AuditUserWithdrawalsReqEntity req, WithdrawalsInfo info, int status) throws SQLException {
		UserMessageInfo messageInfo = new UserMessageInfo();
		messageInfo.message_user = info.withdrawals_user;

		messageInfo.source_type = Config.MessageSourceType.MESSAGE_SOURCE_TYPE_WITHDRAWALS;
		messageInfo.source_id = req.withdrawals_id;
		messageInfo.source_content = info.withdrawals_account;

		if (status == Config.WithdrawalsType.WITHDRAWALS_NO_PASSED) {
			messageInfo.message_type = Config.MessageType.MESSAGE_TYPE_WITHDRAWALS_FAILD;
			messageInfo.message_content = req.faild_desc;
			messageInfo.action_content = String.valueOf(info.gold);
		} else {
			messageInfo.message_type = Config.MessageType.MESSAGE_TYPE_WITHDRAWALS_SUCCESS;
			messageInfo.message_content = String.valueOf(info.gold);
			messageInfo.action_content = String.valueOf(info.poundage);
		}
		User wuser = UserDao.getUser(conn, messageInfo.message_user);

		String msgId = UserMessageDao.addUserMessage(conn, messageInfo);
		if (!StringUtils.emptyString(msgId)) {
			MessageTask.MessageParam mMessageParam = new MessageTask.MessageParam(MessageTask.getOs(wuser.os),
					messageInfo.message_type, msgId);
			mMessageParam.addAlias(messageInfo.message_user);
			mMessageParam.title = MessageConfig.TITLE;
			mMessageParam.description = MessageConfig.getMessageDescription(messageInfo, wuser.nickname);
			return mMessageParam;
		} else {
			return null;
		}
	}

	/***
	 * 管理员审核
	 *
	 * @throws SQLException
	 *****/
	public static AuditUserWithdrawalsResEntity auditUserWithdrawals(AuditUserWithdrawalsReqEntity req, String userIp)
			throws SQLException {
		AuditUserWithdrawalsResEntity res = new AuditUserWithdrawalsResEntity();
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			FinancialInfo finanInfo = FinancialDao.getFinancialByToken(conn, req.financial_token);
			if (finanInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			WithdrawalsInfo info = getNormalWithdrawalsInfo(conn, req.withdrawals_id);
			if (info == null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			DBUtils.beginTransaction(conn);
			int row = 0;
			try {
				String sql = " UPDATE withdrawals SET withdrawals_status=?,withdrawals_faild_desc=?,withdrawals_audit_user=?,withdrawals_audit_time=?,withdrawals_audit_ip=?,is_return_gold=? WHERE withdrawals_id=? AND withdrawals_status=?";
				int status;
				if (StringUtils.emptyString(req.faild_desc)) {
					status = Config.WithdrawalsType.WITHDRAWALS_PASSED;// 通过
				} else {
					status = Config.WithdrawalsType.WITHDRAWALS_NO_PASSED;
				}
				row = DBUtils.executeUpdate(conn, sql,
						new Object[] { status, req.faild_desc, finanInfo.financial_user, System.currentTimeMillis(),
								userIp, req.faild_is_return_money, req.withdrawals_id,
								Config.WithdrawalsType.WITHDRAWALS_APPLYING });
				if (row > 0 && status == Config.WithdrawalsType.WITHDRAWALS_NO_PASSED
						&& req.faild_is_return_money == Config.WithdrawalsCashBackOrNot.WITHDRAWALS_CASH_BACK) {
					// 将钱更新到userinfo表中gold字段
					UserValueInfo value = new UserValueInfo();
					value.gold = info.gold;
					row = UserDao.updateUserValue(conn, info.withdrawals_user, value, "", "",
							Config.GoldChangeType.GOLD_CHANGE_TYPE_WITHDRAWALS_FAILD, req.withdrawals_id);
				}
				if (row > 0 && status == Config.WithdrawalsType.WITHDRAWALS_NO_PASSED && req.ban_withdrawals_time > 0) {
					row = DBUtils.executeUpdate(conn, "UPDATE userinfo set ban_withdrawals_time=? WHERE user_id=?",
							new Object[] { System.currentTimeMillis() + req.ban_withdrawals_time,
									info.withdrawals_user });
				}
				if (row > 0) {
					MessageTask.MessageParam param = withrawalsNotify(conn, req, info, status);
					if (param != null) {
						DBUtils.commitTransaction(conn);
						MessageTask.addMessage(param);
					}
				}
			} catch (Exception e) {
				LogUtils.log(e);
				row = 0;
			}
			if (row > 0) {
				res.status = Config.STATUS_OK;
			} else {
				res.status = Config.STATUS_NOT_EXITS;
				DBUtils.rollbackTransaction(conn);
			}
		} finally {
			DBUtils.close(conn);
		}
		return res;
	}

	/**
	 * 获取待审核的数据
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public static int getApplyingNum(DBUtils.ConnectionCache conn) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		int num = 0;
		try {
			StringBuffer sql = new StringBuffer(
					"select count(withdrawals_id) num from withdrawals inner join userinfo on userinfo.user_id=withdrawals.withdrawals_user where withdrawals_status=?");
			pstat = conn.prepareStatement(sql.toString());
			pstat.setObject(1, Config.WithdrawalsType.WITHDRAWALS_APPLYING);
			rs = pstat.executeQuery();
			if (rs.next()) {
				num = rs.getInt("num");
			}
			return num;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	/**
	 * 提现记录
	 * @param conn
	 * @param begin_time
	 * @param end_time
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public static List<HomepageGraphInfo> getWithdrawalsRecord(DBUtils.ConnectionCache conn,long begin_time,long end_time)
			throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		HomepageGraphInfo info=null;
		List<HomepageGraphInfo> infolist=null;
		try {
			StringBuffer sb=new StringBuffer("SELECT sum(withdrawals_gold) sum,FROM_UNIXTIME(withdrawals_time/1000, '%Y-%m-%d') time FROM withdrawals");
			sb.append(" WHERE withdrawals_status=").append(Config.WithdrawalsType.WITHDRAWALS_PASSED);
			if(begin_time!=0)
				sb.append(" and withdrawals_time >=").append(begin_time);
			if(end_time!=0)
				sb.append(" and withdrawals_time <=").append(end_time);
			sb.append(" GROUP BY FROM_UNIXTIME(withdrawals_time/1000, '%Y-%m-%d') order by time desc");
			pstat = conn.prepareStatement(sb.toString());
			rs = pstat.executeQuery();
			infolist=new ArrayList<HomepageGraphInfo>();
			while (rs.next()) {
				info=new HomepageGraphInfo();
				info.date_str=rs.getString("time");
				info.gold=rs.getFloat("sum");
				infolist.add(info);
			}
			return infolist;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	/**
	 * 获取提现金额
	 * @param conn
	 * @param res
	 * @throws SQLException
	 */
	public static void getWithdrawSum(DBUtils.ConnectionCache conn,GetGraphDataResEntity res)
			throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			StringBuffer sb=new StringBuffer("select sum(withdrawals_gold) sum from withdrawals_time");
			sb.append(" WHERE withdrawals_status=").append(Config.WithdrawalsType.WITHDRAWALS_PASSED);
			pstat = conn.prepareStatement(sb.toString());
			rs = pstat.executeQuery();
			if (rs.next()) {
				res.withdraw_gold=rs.getFloat("sum");
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

}
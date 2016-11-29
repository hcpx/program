package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.FinancialGetGoldInfoReqEntity;
import com.scchuangtou.entity.FinancialGetGoldInfoResEntity;
import com.scchuangtou.entity.FinancialLoginReqEntity;
import com.scchuangtou.entity.FinancialLoginResEntity;
import com.scchuangtou.entity.GetCommonDataReqEntity;
import com.scchuangtou.entity.GetCommonDataResEntity;
import com.scchuangtou.entity.GetGraphDataReqEntity;
import com.scchuangtou.entity.GetGraphDataResEntity;
import com.scchuangtou.entity.GetTopUpInfoReqEntity;
import com.scchuangtou.entity.GetTopUpInfoResEntity;
import com.scchuangtou.entity.UpdateFinancialPasswordReqEntity;
import com.scchuangtou.entity.UpdateFinancialPasswordResEntity;
import com.scchuangtou.model.FinancialInfo;
import com.scchuangtou.model.HomepageGraphInfo;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.DateUtil;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.MD5Utils;
import com.scchuangtou.utils.MathUtils;

public class FinancialDao {
	public static int addAdmin(DBUtils.ConnectionCache conn, FinancialInfo financialInfo, String password)
			throws SQLException {
		HashMap<String, Object> datas = new HashMap<>();
		datas.put("financial_user", financialInfo.financial_user);
		datas.put("financial_pass",
				MD5Utils.md5(password.getBytes(Charset.forName(Config.CHARSET)), MD5Utils.MD5Type.MD5_32));
		return DBUtils.insert(conn, "INSERT IGNORE INTO financialinfo", datas);
	}

	public static FinancialLoginResEntity login(FinancialLoginReqEntity req) throws SQLException {
		String password = MD5Utils.md5(req.password.getBytes(Charset.forName(Config.CHARSET)), MD5Utils.MD5Type.MD5_32);
		String token;
		if (Config.BACKGROUND_IS_SINGLE_LOGIN) {
			token = IdUtils.createId(req.user_name);
		} else {
			token = req.user_name + "4ab69ba28616e75b";
		}

		FinancialLoginResEntity res = null;
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			int row = DBUtils.executeUpdate(conn,
					"UPDATE financialinfo SET token=? WHERE financial_user=? AND financial_pass=?",
					new Object[] { token, req.user_name, password });

			if (row > 0) {
				String sql = "SELECT financial_user,financial_pass,token FROM financialinfo WHERE financial_user=? AND financial_pass=?";
				pstat = conn.prepareStatement(sql);
				pstat.setObject(1, req.user_name);
				pstat.setObject(2, password);
				rs = pstat.executeQuery();
				if (rs.next()) {
					res = new FinancialLoginResEntity();
					res.status = Config.STATUS_OK;
					res.token = rs.getString("token");
				}
			} else {
				res = new FinancialLoginResEntity();
				res.status = Config.STATUS_PASSWORD_ERROR;
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
		return res;
	}

	public static UpdateFinancialPasswordResEntity updateFinancialPassword(UpdateFinancialPasswordReqEntity req)
			throws SQLException {
		UpdateFinancialPasswordResEntity res = new UpdateFinancialPasswordResEntity();
		DBUtils.ConnectionCache conn = null;
		int row = 0;
		try {
			conn = DBUtils.getConnection();
			FinancialInfo finanInfo = getFinancialByToken(conn, req.financial_token);
			if (finanInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			String encryptionPassword = MD5Utils.md5(req.password.getBytes(Charset.forName(Config.CHARSET)),
					MD5Utils.MD5Type.MD5_32);
			if (!encryptionPassword.equals(finanInfo.financial_pass)) {
				res.status = Config.STATUS_PASSWORD_ERROR;
				return res;
			}
			String password = MD5Utils.md5(req.new_password.getBytes(Charset.forName(Config.CHARSET)),
					MD5Utils.MD5Type.MD5_32);
			String sql = "update financialinfo set financial_pass=? ";
			row = DBUtils.executeUpdate(conn, sql, new Object[] { password });
			if (row > 0) {
				res.status = Config.STATUS_OK;
				return res;
			} else {
				res.status = Config.STATUS_SERVER_ERROR;
				return res;
			}
		} finally {
			DBUtils.close(conn);
		}
	}

	public static FinancialInfo getFinancialByToken(DBUtils.ConnectionCache conn, String token) throws SQLException {
		FinancialInfo finaInfo = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT financial_user,financial_pass FROM financialinfo WHERE token=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, token);
			rs = pstat.executeQuery();

			if (rs.next()) {
				finaInfo = new FinancialInfo();
				finaInfo.financial_user = rs.getString("financial_user");
				finaInfo.financial_pass = rs.getString("financial_pass");
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
		return finaInfo;
	}

	public static FinancialGetGoldInfoResEntity getGoldInfo(FinancialGetGoldInfoReqEntity req) throws SQLException {
		FinancialGetGoldInfoResEntity res = new FinancialGetGoldInfoResEntity();
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			FinancialInfo finanInfo = getFinancialByToken(conn, req.financial_token);
			if (finanInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			req.begin = req.begin < 0 ? 0 : req.begin;
			req.time = req.time <= 0 ? System.currentTimeMillis() : req.time;

			String sql = "SELECT change_type, change_value, change_time,gold_type,change_desc FROM gold_change"
					+ " WHERE gold_type=? and userid=? and change_time<=?  ORDER BY change_time DESC LIMIT " + req.begin
					+ "," + Config.ONCE_QUERY_COUNT;
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, req.gold_type);
			pstat.setObject(2, req.user_id);
			pstat.setObject(3, req.time);
			rs = pstat.executeQuery();
			res.datas = new ArrayList<>();
			FinancialGetGoldInfoResEntity.Data info = null;
			while (rs.next()) {
				info = new FinancialGetGoldInfoResEntity.Data();
				info.change_type = rs.getInt("change_type");
				info.change_time = rs.getLong("change_time");
				info.change_value = Config.parseGold(rs.getFloat("change_value"));
				info.change_desc = rs.getString("change_desc");
				res.datas.add(info);
			}
			res.has_more_data = res.datas.size() >= Config.ONCE_QUERY_COUNT;
			res.status = Config.STATUS_OK;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	public static GetTopUpInfoResEntity getTopUpInfo(GetTopUpInfoReqEntity req) throws SQLException {
		GetTopUpInfoResEntity res = new GetTopUpInfoResEntity();

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

			req.begin = req.begin < 0 ? 0 : req.begin;

			String sql = " select  `type`, order_no,topup_money,end_time  from  `topup_order`  where 1 = 1 ";

			List<Object> values = new ArrayList<Object>();

			if (req.time != -1) {
				sql += " and start_time >= ? ";
				values.add(DateUtil.getDayTime(req.time));
				sql += " and start_time <= ? ";
				values.add(DateUtil.getDayEndTime(req.time));
			}
			if (req.type != -1) {
				sql += " and `type` = ? ";
				values.add(req.type);
			}
			sql += " and status =? ";
			values.add(Config.TopUpOrderStatusType.TRADE_FINISHED);

			sql += " order by start_time desc";
			sql += " limit " + req.begin + "," + Config.ONCE_QUERY_COUNT;

			pstat = conn.prepareStatement(sql);

			int size = values.size();

			for (int i = 0; i < size; i++) {
				pstat.setObject(i + 1, values.get(i));
			}

			rs = pstat.executeQuery();

			res.datas = new ArrayList<>();

			while (rs.next()) {
				GetTopUpInfoResEntity.Data data = new GetTopUpInfoResEntity.Data();
				data.price = rs.getFloat("topup_money");
				data.top_up_time = rs.getLong("end_time");
				data.type = rs.getInt("type");
				data.order_no = rs.getString("order_no");

				res.datas.add(data);
			}

			res.has_more_data = res.datas.size() == Config.ONCE_QUERY_COUNT;

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

	/**
	 * 财务公共数据
	 * 
	 * @param req
	 * @return
	 * @throws SQLException
	 */
	public static GetCommonDataResEntity getCommonData(GetCommonDataReqEntity req) throws SQLException {
		GetCommonDataResEntity res = new GetCommonDataResEntity();
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
			// 待办提现申请
			res.message_num = WithdrawalsDao.getApplyingNum(conn);
			// 互助项目总个数
			res.health_project_num = HealthProjectDao.getHealthProjectNum(conn);
			// 分别获取少年儿童健康互助和青年健康互助的资金池
			HealthProjectDao.getHealthProjectTotalGold(conn, res);
			//获取用户总数和用户看币数
			HomepageGraphInfo info=UserDao.getUsersInfomation(conn);
			res.all_amount=info.gold;
			res.all_user=info.date_str;
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

	/**
	 * 主页数据
	 * 
	 * @param req
	 * @return
	 * @throws SQLException
	 */
	public static GetGraphDataResEntity getGraphData(GetGraphDataReqEntity req) throws SQLException {
		GetGraphDataResEntity res = new GetGraphDataResEntity();
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
			List<String> dates=new ArrayList<>();
			if (req.begin_time == 0 && req.end_time == 0) {
				List<String> dateStr = DateUtil.getLastWeekDays(-1);
				dates=DateUtil.findDates(dateStr.get(0), dateStr.get(1));
				req.begin_time = DateUtil.formatDate(dateStr.get(0), "yyyy-MM-dd");
				req.end_time = DateUtil.getDayEndTime(DateUtil.formatDate(dateStr.get(1), "yyyy-MM-dd"));
			}else{
				String begin=DateUtil.formatDate(req.begin_time, "yyyy-MM-dd");
				String end=DateUtil.formatDate(req.end_time, "yyyy-MM-dd");
				dates=DateUtil.findDates(begin,end);
			}
			// 充值
			List<HomepageGraphInfo> payList = TopUpOrderDao.getPayRecord(conn, req.begin_time, req.end_time);
			// 提现
			List<HomepageGraphInfo> withdrawList = WithdrawalsDao.getWithdrawalsRecord(conn, req.begin_time, req.end_time);
			// 互助项目信息
			List<HomepageGraphInfo> healthList = HealthProjectDao.getHealthinfo(conn);
			// 用户总数和账户总余额
//			HomepageGraphInfo info=UserDao.getUsersInfomation(conn);
//			res.all_gold=info.gold;
//			res.all_user=info.date_str;
			// 充值总额和提现总额
			if (req.begin_time == 0 && req.end_time == 0) {
				TopUpOrderDao.getPaySum(conn, res);
				WithdrawalsDao.getWithdrawSum(conn, res);
			}else{
				for(HomepageGraphInfo payinfo:payList){
				    res.pay_gold=MathUtils.sum(res.pay_gold, payinfo.gold);
				}
				for(HomepageGraphInfo withdrawinfo:withdrawList){
					res.withdraw_gold=MathUtils.sum(res.pay_gold, withdrawinfo.gold);
				}
			}
			res.dates=dates;
			res.payList = fillData(payList,dates);
			res.withdrawList = fillData(withdrawList,dates);
			res.healthList = healthList;
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
	
	/**
	 * 将没有的日期值填为空值
	 * @param list
	 * @param dates
	 * @return
	 */
	private static List<String> fillData(List<HomepageGraphInfo> list,List<String> dates){
		List<String> homepageGraphInfolist =new ArrayList<>();
		if(list==null || list.size()==0){
			for(int i=0;i<dates.size();i++){
				homepageGraphInfolist.add("0");
			}
			return homepageGraphInfolist;
		}
		for(int i=0;i<dates.size();i++){
			HomepageGraphInfo info=new HomepageGraphInfo();
			info.date_str=dates.get(i);
			boolean flage=true;
			for(int j=0;j<list.size();j++){
				if(dates.get(i).equals(list.get(j).date_str)){
					homepageGraphInfolist.add(String.valueOf(list.get(j).gold));
					flage=false;
					break;
				}
			}
			if(flage){
				homepageGraphInfolist.add("0");
			}
		}
		return homepageGraphInfolist;
	}
}

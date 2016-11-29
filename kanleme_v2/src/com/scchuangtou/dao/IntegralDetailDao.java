package com.scchuangtou.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.ExchangeIntegralReqEntity;
import com.scchuangtou.entity.ExchangeIntegralResEntity;
import com.scchuangtou.model.IntegralWall;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserValueInfo;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.DateUtil;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;

public class IntegralDetailDao {
	public static int INTEGRAL_QUERY_ID = 0; // 按id查询
	public static int INTEGRAL_QUERY_ORDERID = 1; // 按订单号
	public static String INTEGRAL_DETAIL_PRICE = "points"; // 获取积分key

	/**
	 * 这种情况为第三方服务器直接增加积分的情况
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static boolean addIntegralWallByCallBack(HashMap<String, Object> datas, int integral_type,StringBuffer logStr) {
		DBUtils.ConnectionCache conn = null;
		int row = 0;
		try {
			conn = DBUtils.getConnection();
			DBUtils.beginTransaction(conn);
			int points = Integer.parseInt(String.valueOf(datas.get(INTEGRAL_DETAIL_PRICE)));
			String userid = (String) datas.get("user_id");
			User user=UserDao.getUser(conn, userid);
			if(user==null){
				logStr.append("\r\n").append("系统中没有此user_id。");
				return false;
			}
			boolean flage = true;
			// 没有关于这个用户的积分记录
			if (!IntegralDao.checkExist(userid)) {
				flage = IntegralDao.insertIntegral(userid);
			}
			if (flage) {
				String integral_wall_id = IdUtils.createId("integral_detail");
				datas.put("integral_wall_id", integral_wall_id);
				datas.put("status", Config.IntegralWallStatusType.INTEGRAL_WALL_COMPLETE);
				datas.put("time", System.currentTimeMillis());
				row = DBUtils.insert(conn, "INSERT INTO integral_detail", datas);
				if (row > 0) {
					row = IntegralDao.updateIntegral(conn, points, userid);
				}
			}
			if (row > 0) {
				DBUtils.commitTransaction(conn);
			}
		} catch (Exception e) {
			LogUtils.log(e);
			row = 0;
		} finally {
			DBUtils.close(conn);
		}
		if (row > 0) {
			return true;
		} else {
			DBUtils.rollbackTransaction(conn);
			return false;
		}
	}

	public static IntegralWall getIntegralWall(DBUtils.ConnectionCache conn, String id, int type) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			StringBuffer sbsql = new StringBuffer(
					"select integral_wall_id,orderid,user_id,device,price,points,time,status from integral_detail where");
			if (type == INTEGRAL_QUERY_ID)
				sbsql.append(" integral_wall_id=?");
			else if (type == INTEGRAL_QUERY_ORDERID)
				sbsql.append(" orderid=?");
			pstat = conn.prepareStatement(sbsql.toString());
			pstat.setObject(1, id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				IntegralWall integralWall = new IntegralWall();
				integralWall.integral_wall_id = rs.getString("integral_wall_id");
				integralWall.orderid = rs.getString("orderid");
				integralWall.user_id = rs.getString("user_id");
				integralWall.device = rs.getString("device");
				integralWall.price = rs.getFloat("price");
				integralWall.status = rs.getInt("status");
				integralWall.points = rs.getInt("points");
				integralWall.time = rs.getLong("time");
				return integralWall;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	/**
	 * 是否已经存在这个订单号
	 * 
	 * @param orderID
	 * @return
	 */
	public static boolean checkExist(String orderID) {
		orderID = orderID == null ? "" : orderID;
		DBUtils.ConnectionCache conn = null;
		boolean flage = false;
		try {
			conn = DBUtils.getConnection();
			IntegralWall integralWall = getIntegralWall(conn, orderID, INTEGRAL_QUERY_ORDERID);
			flage = integralWall == null ? false : true;
		} catch (Exception e) {
			LogUtils.log(e);
		} finally {
			DBUtils.close(conn);
		}
		return flage;
	}

	/**
	 * 更新积分墙信息记录
	 * 
	 * @param conn
	 * @param points
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public static int updateIntegralWall(DBUtils.ConnectionCache conn, String userid, HashMap<String, Object> datas,
			int integral_type) throws SQLException {
		Object orderid = datas.get("orderid");
		Object device = datas.get("device");
		Object price = datas.get("price");
		Object points = datas.get("points");
		StringBuffer sql = new StringBuffer("update integral_detail set status=")
				.append(Config.IntegralWallStatusType.INTEGRAL_WALL_COMPLETE);
		sql.append(",orderid=?,device=?,price=?,points=?");
		sql.append(" where user_id=? and status=").append(Config.IntegralWallStatusType.INTEGRAL_WALL_WAIT);
		sql.append(" and type=").append(integral_type);
		int row = DBUtils.executeUpdate(conn, sql.toString(), new Object[] { userid, orderid, device, price, points });
		return row;
	}

	/**
	 * 是否超过每日最大限额
	 * 
	 * @param conn
	 * @param user
	 * @param changePoints
	 * @return
	 * @throws SQLException
	 */
	public static boolean isSurpassMax(DBUtils.ConnectionCache conn, String userid, int changePoints) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		boolean flage = true;
		try {
			long ctime = System.currentTimeMillis();
			long stime = DateUtil.getDayTime(ctime);
			long etime = DateUtil.getDayEndTime(ctime);

			StringBuffer sql = new StringBuffer("select sum(points)").append(changePoints).append("<=-")
					.append(Config.GrowthGivingMax.INTEGRALWALL_CHANGE_MAX + " num")
					.append(" from integral_detail where ");
			sql.append(" user_id=?").append(" and points<0");
			sql.append(" AND time>=").append(stime);
			sql.append(" AND time<=").append(etime);
			pstat = conn.prepareStatement(sql.toString());
			pstat.setObject(1, userid);
			rs = pstat.executeQuery();
			if (rs.next()) {
				if(rs.getInt("num")!=1)
					flage = false;
			}
			return flage;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	/**
	 * 这种方法为直接扣除积分，不需要和第三方进行回调
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static ExchangeIntegralResEntity exchangeIntegral(ExchangeIntegralReqEntity req) throws Exception {
		DBUtils.ConnectionCache conn = null;
		ExchangeIntegralResEntity res = new ExchangeIntegralResEntity();
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (req.points > -Config.GrowthGivingMax.INTEGRALWALL_CHANGE_MIN) {
				res.status = Config.STATUS_INTEGRAL_WALL_LESSMIN;
				return res;
			}
			if (isSurpassMax(conn, user.user_id, req.points)) {
				res.status = Config.STATUS_INTEGRAL_WALL_MOREMAX;
				return res;
			}
			int row = 0;
			float giving_gold = 0;
			try {
				DBUtils.beginTransaction(conn);
				boolean flage = true;
				// 没有关于这个用户的积分记录
				if (!IntegralDao.checkExist(user.user_id)) {
					flage = IntegralDao.insertIntegral(user.user_id);
				}
				if (flage) {
					HashMap<String, Object> datas = new HashMap<>();
					String integral_wall_id = IdUtils.createId("integral_detail");
					datas.put("integral_wall_id", integral_wall_id);
					datas.put("status", Config.IntegralWallStatusType.INTEGRAL_WALL_COMPLETE);
					datas.put("time", System.currentTimeMillis());
					datas.put("points", req.points);
					datas.put("user_id", user.user_id);
					datas.put("type", -1);
					datas.put("os", req.os);
					row = DBUtils.insert(conn, "INSERT INTO integral_detail", datas);
					if (row > 0)
						row = IntegralDao.updateIntegral(conn, req.points, user.user_id);
					if (row > 0) 
						giving_gold = exchangeIntegralGiving(conn, user, req.points, Config.GoldChangeType.GOLD_CHANGE_TYPE_INTEGRAL,integral_wall_id);
					else{
						res.status = Config.STATUS_INTEGRAL_WALL_NOTENOUGH;
						return res;
					}	
				}
				if (row > 0)
					DBUtils.commitTransaction(conn);
			} catch (Exception e) {
				row = 0;
				LogUtils.log(e);
			}
			if (row > 0) {
				res.points = req.points;
				res.gold = giving_gold;
				res.allgolds = user.gold + giving_gold;
				res.status = Config.STATUS_OK;
			} else
				res.status = Config.STATUS_SERVER_ERROR;
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	private static float exchangeIntegralGiving(DBUtils.ConnectionCache conn, User user, int changePoints, int integral_type,String integral_wall_id)
			throws Exception {
		UserValueInfo value = new UserValueInfo();
		int min = Config.GrowthGivingMax.INTEGRALWALL_CHANGE_MIN;
		BigDecimal bd = new BigDecimal((float)Math.abs(changePoints) / min);
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		float changResult = bd.floatValue();
		value.gold=changResult;
		int r = UserDao.updateUserValue(conn, user.user_id, value, null, null, integral_type, integral_wall_id);
		return r > 0 ? value.gold : 0;
	}
	
}

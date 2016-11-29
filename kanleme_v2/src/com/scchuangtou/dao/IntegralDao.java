package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.GetTotalIntegralReqEntity;
import com.scchuangtou.entity.GetTotalIntegralResEntity;
import com.scchuangtou.model.Integral;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.MD5Utils;

public class IntegralDao {
	
	private static String createIntegralId(String user_id) {
		return MD5Utils.md5((user_id).getBytes(Charset.forName(Config.CHARSET)), MD5Utils.MD5Type.MD5_16);
	}
	
	/**
	 * 判断是否存在此人这个类型的积分信息
	 * @param userID
	 * @return
	 */
	public static boolean checkExist(String userID){
		DBUtils.ConnectionCache conn = null;
		boolean flage = false;
		try {
			conn = DBUtils.getConnection();
			Integral integral=getIntegral(conn,userID);
			flage=integral==null?false:true;
		} catch (Exception e) {
			LogUtils.log(e);
		} finally {
			DBUtils.close(conn);
		}
		return flage;
	}
	
	/**
	 * 获取积分信息
	 * @param conn
	 * @param user_id
	 * @return
	 * @throws SQLException
	 */
	public static Integral getIntegral(DBUtils.ConnectionCache conn, String userid) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			StringBuffer sbsql=new StringBuffer("select integral_id,user_id,integral from integral where user_id=?");
			pstat = conn.prepareStatement(sbsql.toString());
			pstat.setObject(1, userid);
			rs = pstat.executeQuery();
			if (rs.next()) {
				Integral integral = new Integral();
				integral.integral_id = rs.getString("integral_id");
				integral.user_id = rs.getString("user_id");
				integral.integral = rs.getLong("integral");
				return integral;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	/**
	 * 新增用户积分信息
	 * @param conn
	 * @param userid
	 * @return
	 * @throws SQLException
	 */
	public static boolean insertIntegral(String userid) throws SQLException {
		DBUtils.ConnectionCache conn = null;
		int row=0;
		try {
			conn = DBUtils.getConnection();
			String id = createIntegralId(userid);
			HashMap<String, Object> datas = new HashMap<>();
			datas.put("integral_id", id);
			datas.put("user_id", userid);
			row = DBUtils.insert(conn, "insert ignore into integral", datas);
		} finally {
			DBUtils.close(conn);
		}
		return row>0;
	}
	
	/**
	 * 更新用户积分
	 * @param conn
	 * @param points
	 * @return
	 * @throws SQLException 
	 */
	public static int updateIntegral(DBUtils.ConnectionCache conn,int points,String userid) throws SQLException{
		StringBuffer sql=new StringBuffer("update integral set integral=integral+? where user_id=?");
		if (points < 0) {
			sql.append(" AND integral>=").append(Math.abs(points));
		}
		int row = DBUtils.executeUpdate(conn, sql.toString(), new Object[] { points, userid });
		return row;
	}
	
	/**
	 * 获取总积分
	 * @param req
	 * @return
	 * @throws SQLException
	 */
	public static GetTotalIntegralResEntity getTotalIntegral(GetTotalIntegralReqEntity req) throws SQLException {
		GetTotalIntegralResEntity res = new GetTotalIntegralResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			Integral integral=getIntegral(conn,user.user_id);
			if (integral == null) {
				res.integral=0;
			}else{
				res.integral=integral.integral;
			}
			res.status = Config.STATUS_OK;
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}
	
}

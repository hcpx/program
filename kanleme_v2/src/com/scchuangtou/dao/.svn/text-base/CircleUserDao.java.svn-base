package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.GetCircleMembersReqEntity;
import com.scchuangtou.entity.GetCircleMembersResEntity;
import com.scchuangtou.entity.JoinCircleReqEntity;
import com.scchuangtou.entity.JoinCircleResEntity;
import com.scchuangtou.entity.QuitCircleReqEntity;
import com.scchuangtou.entity.QuitCircleResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.model.CircleUserInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserValueInfo;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.MD5Utils;
import com.scchuangtou.utils.StringUtils;

public class CircleUserDao {

	public static int getUserCount(DBUtils.ConnectionCache conn, String circle_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT count(user_id) FROM circle_user WHERE circle_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, circle_id);
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

	private static long addCircleGiving(DBUtils.ConnectionCache conn, User user) throws Exception {
		UserValueInfo value = new UserValueInfo();
		value.growth = Config.GrowthGiving.JOIN_CIRCLE;
		StringBuffer set = new StringBuffer();
		set.append("add_circle_num=add_circle_num+1");
		int r = UserDao.updateUserValue(conn, user.user_id, value, set.toString(), null, 0, null);
		return r>0?value.growth:0;
	}

	public static JoinCircleResEntity joinCircle(JoinCircleReqEntity req) throws SQLException {
		JoinCircleResEntity res = new JoinCircleResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			int row = 0;
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			String id = createCircleUserId(user.user_id, req.circle_id);
			long giving_growth = 0;
			try {
				DBUtils.beginTransaction(conn);
				HashMap<String, Object> datas = new HashMap<>();
				datas.put("circle_user_id", id);
				datas.put("circle_id", req.circle_id);
				datas.put("user_id", user.user_id);
				datas.put("circle_user_join_time", System.currentTimeMillis());
				row = DBUtils.insert(conn, "insert ignore into circle_user", datas);
				if (row > 0) {
					if (user.add_circle_num < Config.GrowthGivingMax.JOIN_CIRCLE) {
						giving_growth = addCircleGiving(conn, user);
					}
				}else{
					res.status = Config.STATUS_REPEAT_ERROR;
					return res;
				}
				if (row > 0)
					DBUtils.commitTransaction(conn);
			} catch (Exception e) {
				row = 0;
				LogUtils.log(e);
			}
			if (row > 0){
				res.giving_growth = giving_growth;
				res.growth = user.growth + giving_growth;
				res.status = Config.STATUS_OK;
			}else
				res.status = Config.STATUS_SERVER_ERROR;
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	private static String createCircleUserId(String user_id, String object_id) {
		return MD5Utils.md5((user_id + object_id).getBytes(Charset.forName(Config.CHARSET)), MD5Utils.MD5Type.MD5_16);
	}

	public static boolean isJoin(DBUtils.ConnectionCache conn, String circle_id, String user_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT circle_user_id FROM circle_user WHERE circle_id=? and user_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, circle_id);
			pstat.setObject(2, user_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	/**
	 * 获取圈子下成员个数
	 * 
	 * @param conn
	 * @param circle_id
	 * @return
	 * @throws SQLException
	 */
	public static int getMemberCount(DBUtils.ConnectionCache conn, String circle_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT count(user_id) FROM circle_user WHERE circle_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, circle_id);
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
	 * 获取圈子成员
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetCircleMembersResEntity getCircleMembers(GetCircleMembersReqEntity req,HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		GetCircleMembersResEntity res = new GetCircleMembersResEntity();
		try {
			conn = DBUtils.getConnection();
			User user = null;
			if (!StringUtils.emptyString(req.token)) {
				user = UserDao.getUserByToken(conn, req.token);
				if (user == null) {
					res.status = Config.STATUS_TOKEN_ERROR;
					return res;
				}
			}
			StringBuffer sql=new StringBuffer("select userinfo.user_id,userinfo.nickname,userinfo.head_pic,userinfo.growth from circle_user INNER JOIN userinfo ON circle_user.user_id = userinfo.user_id ");
			sql.append(" WHERE circle_id=? ORDER BY circle_user.circle_user_join_time LIMIT ?,?");
			pstat = conn.prepareStatement(sql.toString());
			req.begin = req.begin < 0 ? 0 : req.begin;
			pstat.setObject(1, req.circle_id);
			pstat.setObject(2, req.begin);
			if (req.count > 0) {
				pstat.setObject(3, req.count);
			} else {
				pstat.setObject(3, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			GetCircleMembersResEntity.UserInfo userInfo = null;
			List<GetCircleMembersResEntity.UserInfo> users=new ArrayList<GetCircleMembersResEntity.UserInfo>();
			while (rs.next()) {
				userInfo = new GetCircleMembersResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.head_pic = rs.getString("head_pic");
				if (!StringUtils.emptyString(userInfo.head_pic)) {
					userInfo.head_pic = ImageHelper.getImageUrl(request, userInfo.head_pic);
				}
				userInfo.level = Config.getLevel(rs.getLong("growth"));
				users.add(userInfo);
			}
			res.users=users;
			boolean has_more_data=true;
			if (req.count > 0) {
				has_more_data=res.users.size()==req.count;
			} else {
				has_more_data=res.users.size()==Config.ONCE_QUERY_COUNT;
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
	
	public static QuitCircleResEntity quitCircle(QuitCircleReqEntity req) throws SQLException {
		QuitCircleResEntity res = new QuitCircleResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		int row=0;
		try {
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			String id = createCircleUserId(user.user_id, req.circle_id);
			CircleUserInfo circleUserInfo = getCircleUserInfo(conn, id);
			if (circleUserInfo == null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			row=DBUtils.executeUpdate(conn,"delete from circle_user where circle_id=? and user_id=?", new Object[] { req.circle_id,user.user_id });
			if(row>0){
				res.status = Config.STATUS_OK;
			}else{
				res.status = Config.STATUS_NOT_EXITS;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}
	
	/**
	 * 获取圈子人员表信息
	 * @param conn
	 * @param circleUserID
	 * @return
	 * @throws SQLException
	 */
	public static CircleUserInfo getCircleUserInfo(DBUtils.ConnectionCache conn, String circleUserID) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select circle_user_id,circle_id,user_id,circle_user_join_time from circle_user where circle_user_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, circleUserID);
			rs = pstat.executeQuery();
			if (rs.next()) {
				CircleUserInfo circleUserInfo = new CircleUserInfo();
				circleUserInfo.circle_user_id = rs.getString("circle_user_id");
				circleUserInfo.circle_id = rs.getString("circle_id");
				circleUserInfo.user_id = rs.getString("user_id");
				circleUserInfo.circle_user_join_time = rs.getLong("circle_user_join_time");
				return circleUserInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	
}

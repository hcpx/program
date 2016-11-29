package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.config.AdminConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.entity.AddCircleReqEntity;
import com.scchuangtou.entity.AddCircleResEntity;
import com.scchuangtou.entity.AdminAddCircleReqEntity;
import com.scchuangtou.entity.GetCircleDetailReqEntity;
import com.scchuangtou.entity.GetCircleDetailResEntity;
import com.scchuangtou.entity.GetCirclesReqEntity;
import com.scchuangtou.entity.GetCirclesResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.model.AdminInfo;
import com.scchuangtou.model.Circle;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.StringUtils;

public class CircleDao {
	public static int CIRCLE_STATUS_NORMAL = 0; // 正常状态
	public static int CIRCLE_STATUS_BARRED = -1; // 禁止访问状态

	public static AddCircleResEntity addCircle(AddCircleReqEntity req, MyMutiPart picPart, HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				AddCircleResEntity res = new AddCircleResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			return addCircle(conn, picPart, null, user.user_id, req.circle_name, req.circle_sign, request);
		} finally {
			DBUtils.close(conn);
		}
	}

	public static AddCircleResEntity adminAddCircle(String adminToken, String title, String content, MyMutiPart picPart,
			MyMutiPart backPicPart, HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			AdminInfo mAdminInfo = AdminDao.getAdminByToken(conn, adminToken);
			if (mAdminInfo == null) {
				AddCircleResEntity res = new AddCircleResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!mAdminInfo.hasMode(AdminConfig.MODE_MANAGER_CIRCLE)) {
				AddCircleResEntity res = new AddCircleResEntity();
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			return addCircle(conn, picPart, backPicPart, mAdminInfo.admin_user, title, content, request);
		} finally {
			DBUtils.close(conn);
		}
	}

	private static AddCircleResEntity addCircle(DBUtils.ConnectionCache conn, MyMutiPart picPart,
			MyMutiPart backPicPart, String user_id, String title, String content, HttpServletRequest request)
					throws Exception {
		String imageName = getImageName(user_id);
		String backImageName = getImageName(user_id);
		String id = IdUtils.createId(imageName);
		int row = 0;
		try {
			DBUtils.beginTransaction(conn);

			HashMap<String, Object> datas = new HashMap<>();
			datas.put("circle_id", id);
			datas.put("circle_name", title);
			datas.put("circle_sign", content);
			datas.put("circle_created_time", System.currentTimeMillis());
			datas.put("circle_pic", imageName);
			datas.put("circle_back_img", backImageName);
			if (!StringUtils.emptyString(user_id))
				datas.put("user_id", user_id);
			datas.put("circle_status", CIRCLE_STATUS_NORMAL);
			row = DBUtils.insert(conn, "INSERT INTO circle", datas);
			Map<String, MyMutiPart> parts = new HashMap<>();
			parts.put(imageName, picPart);
			parts.put(backImageName, backPicPart);
			if (row > 0 && ImageHelper.upload(parts) == null) {
				row = 0;
			}
			if (row > 0) {
				DBUtils.commitTransaction(conn);
			}
		} catch (Exception e) {
			LogUtils.log(e);
			row = 0;
		}
		AddCircleResEntity res = new AddCircleResEntity();
		if (row > 0) {
			res.status = Config.STATUS_OK;
			res.circle_id = id;
			res.circle_pic = ImageHelper.getImageUrl(request, imageName);
			res.circle_back_img = ImageHelper.getImageUrl(request, backImageName);
		} else {
			DBUtils.rollbackTransaction(conn);
			res.status = Config.STATUS_SERVER_ERROR;
		}
		return res;
	}

	/**
	 * 获取所有圈子按时间排序
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static GetCirclesResEntity listCircles(GetCirclesReqEntity req, HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			StringBuffer sb = new StringBuffer();
			sb.append("select circle_id,circle_name,circle_sign,circle_created_time,circle_pic from circle");
			sb.append(" WHERE circle_status =").append(CIRCLE_STATUS_NORMAL);
			sb.append(" ORDER BY circle_created_time  asc");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			rs = pstat.executeQuery();
			GetCirclesResEntity res = new GetCirclesResEntity();
			GetCirclesResEntity.Circle circle = null;
			res.circleList = new ArrayList<>();
			while (rs.next()) {
				circle = new GetCirclesResEntity.Circle();
				String circle_id = rs.getString("circle_id");
				circle.circle_id = circle_id;
				circle.circle_name = rs.getString("circle_name");
				circle.circle_sign = rs.getString("circle_sign");
				circle.circle_created_time = rs.getLong("circle_created_time");
				circle.circle_pic = ImageHelper.getImageUrl(request, rs.getString("circle_pic"));
				circle.circle_user_count = CircleUserDao.getUserCount(conn, circle_id);
				circle.circle_articles = ArticleDao.getAticlesCountByCircleId(conn, circle_id);
				res.circleList.add(circle);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.circleList.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}
	
	/**
	 * 管理系统获取所有圈子信息
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetCirclesResEntity adminGetCircles(GetCirclesReqEntity req, HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			StringBuffer sb = new StringBuffer();
			sb.append("select circle_id,circle_name,circle_sign,circle_created_time,circle_pic,circle_status,user_id,circle_back_img from circle");
			sb.append(" WHERE 1 = 1");
			if(!StringUtils.emptyString(req.key_word)){
				sb.append(" and (circle_name like '%"+req.key_word+"%' or circle_sign like '%"+req.key_word+"%' or user_id like '%"+req.key_word+"%')");
			}
			sb.append(" ORDER BY circle_created_time  asc");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			rs = pstat.executeQuery();
			GetCirclesResEntity res = new GetCirclesResEntity();
			GetCirclesResEntity.Circle circle = null;
			res.circleList = new ArrayList<>();
			while (rs.next()) {
				circle = new GetCirclesResEntity.Circle();
				String circle_id = rs.getString("circle_id");
				circle.circle_id = circle_id;
				circle.circle_name = rs.getString("circle_name");
				circle.circle_sign = rs.getString("circle_sign");
				circle.circle_created_time = rs.getLong("circle_created_time");
				circle.circle_pic = ImageHelper.getImageUrl(request, rs.getString("circle_pic"));
				circle.circle_back_img = ImageHelper.getImageUrl(request, rs.getString("circle_back_img"));
				circle.circle_user_count = CircleUserDao.getUserCount(conn, circle_id);
				circle.circle_articles = ArticleDao.getAticlesCountByCircleId(conn, circle_id);
				circle.create_user=rs.getString("user_id");
				circle.status=rs.getInt("circle_status");
				res.circleList.add(circle);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.circleList.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	/**
	 * 获取我加入的圈子按时间排序
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static GetCirclesResEntity listMyCircles(GetCirclesReqEntity req, HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				GetCirclesResEntity res = new GetCirclesResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append("select c.circle_id,circle_name,circle_sign,circle_created_time,circle_pic from circle c");
			sb.append(" INNER JOIN circle_user cu on cu.circle_id =c.circle_id");
			sb.append(" WHERE cu.user_id=?");
			sb.append(" AND circle_status =").append(CIRCLE_STATUS_NORMAL);
			sb.append(" ORDER BY circle_created_time  DESC");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, user.user_id);
			rs = pstat.executeQuery();
			GetCirclesResEntity res = new GetCirclesResEntity();
			GetCirclesResEntity.Circle circle = null;
			res.circleList = new ArrayList<>();
			while (rs.next()) {
				circle = new GetCirclesResEntity.Circle();
				String circle_id = rs.getString("circle_id");
				circle.circle_id = circle_id;
				circle.circle_name = rs.getString("circle_name");
				circle.circle_sign = rs.getString("circle_sign");
				circle.circle_created_time = rs.getLong("circle_created_time");
				circle.circle_pic = ImageHelper.getImageUrl(request, rs.getString("circle_pic"));
				circle.circle_user_count = CircleUserDao.getUserCount(conn, circle_id);
				circle.circle_articles = ArticleDao.getAticlesCountByCircleId(conn, circle_id);
				res.circleList.add(circle);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.circleList.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	public static Circle getCircleInfo(DBUtils.ConnectionCache conn, String circleID) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select circle_id,user_id,circle_name,circle_sign,circle_created_time,circle_pic,circle_status,circle_back_img from circle where circle_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, circleID);
			rs = pstat.executeQuery();
			if (rs.next()) {
				Circle circle = new Circle();
				circle.circle_id = rs.getString("circle_id");
				circle.user_id = rs.getString("user_id");
				circle.circle_name = rs.getString("circle_name");
				circle.circle_sign = rs.getString("circle_sign");
				circle.circle_pic = rs.getString("circle_pic");
				circle.circle_back_img = rs.getString("circle_back_img");
				circle.circle_status = rs.getInt("circle_status");
				circle.circle_created_time = rs.getLong("circle_created_time");
				return circle;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	public static GetCircleDetailResEntity getCircleDetail(GetCircleDetailReqEntity req, HttpServletRequest request)
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
					GetCircleDetailResEntity res = new GetCircleDetailResEntity();
					res.status = Config.STATUS_TOKEN_ERROR;
					return res;
				}
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					" SELECT c.circle_name,c.circle_sign,c.user_id,u.nickname,u.head_pic,u.growth,c.circle_pic,c.circle_back_img FROM");
			sb.append(" circle c left JOIN userinfo u ON u.user_id = c.user_id WHERE c.circle_id = ?");
			sb.append(" AND c.circle_status =").append(CIRCLE_STATUS_NORMAL);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.circle_id);
			rs = pstat.executeQuery();
			GetCircleDetailResEntity res = new GetCircleDetailResEntity();
			GetCircleDetailResEntity.UserInfo userInfo;
			if (rs.next()) {
				res.circle_name = rs.getString("circle_name");
				res.circle_sign = rs.getString("circle_sign");
				// 创建人信息
				if (rs.getString("user_id") != null) {
					userInfo = new GetCircleDetailResEntity.UserInfo();
					userInfo.user_id = rs.getString("user_id");
					userInfo.nickname = rs.getString("nickname");
					userInfo.level = Config.getLevel(rs.getInt("growth"));
					userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
					res.user_info = userInfo;
				}
				if (user != null)
					res.isjoin = CircleUserDao.isJoin(conn, req.circle_id, user.user_id);
				res.article_num = ArticleDao.getAticlesCountByCircleId(conn, req.circle_id);
				res.member_num = CircleUserDao.getMemberCount(conn, req.circle_id);
				if (!StringUtils.emptyString(rs.getString("circle_pic")))
					res.circle_pic = ImageHelper.getImageUrl(request, rs.getString("circle_pic"));
				if (!StringUtils.emptyString(rs.getString("circle_back_img")))
					res.circle_back_img = ImageHelper.getImageUrl(request, rs.getString("circle_back_img"));
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

	private static String getImageName(String user_id) {
		StringBuffer sb = new StringBuffer("circle/");
		if (!StringUtils.emptyString(user_id)) {
			sb.append("/").append(user_id);
		}
		sb.append("/").append(IdUtils.createId("circle"));
		return sb.toString();
	}
	
	public static AddCircleResEntity adminEditCircle(AdminAddCircleReqEntity req,String token,MyMutiPart picPart,MyMutiPart backPicPart) throws SQLException {
		AddCircleResEntity res = new AddCircleResEntity();
		DBUtils.ConnectionCache conn = null;
		int row = 0;
		try {
			conn = DBUtils.getConnection();
			DBUtils.beginTransaction(conn);
			AdminInfo adminInfo = AdminDao.getAdminByToken(conn, token);
			if (adminInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!adminInfo.hasMode(AdminConfig.MODE_MANAGER_CIRCLE)) {
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			Circle circle=CircleDao.getCircleInfo(conn, req.circle_id);
			if (circle == null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			String old_circle_back_img=circle.circle_back_img;
			String old_circle_pic=circle.circle_pic;
			String imageName = null;
			String backImageName = null;
			Map<String, MyMutiPart> parts = new HashMap<>();
			if(picPart!=null && picPart.getSize()!=0){
				imageName=getImageName(token);
				parts.put(imageName, picPart);
			}
			if(picPart!=null && backPicPart.getSize()!=0){
				backImageName=getImageName(token);
				parts.put(backImageName, backPicPart);
			}
			if(parts.isEmpty()){
				row=1;
			}else{
				if (ImageHelper.upload(parts) == null) {
					row = 0;
				}else{
					row = 1;
				}
			}
			if(row>0){
				StringBuffer sb = new StringBuffer("update circle set circle_id=?");
				if (!StringUtils.emptyString(req.circle_name)) {
					sb.append(",circle_name='").append(req.circle_name).append("'");
				}
				if (!StringUtils.emptyString(req.circle_sign)) {
					sb.append(",circle_sign='").append(req.circle_sign).append("'");
				}
				if(imageName!=null)
					sb.append(",circle_pic='").append(imageName).append("'");
				if(backImageName!=null)
					sb.append(",circle_back_img='").append(backImageName).append("'");
				if(req.status==1){
					int status=CIRCLE_STATUS_NORMAL;
					if(circle.circle_status==CIRCLE_STATUS_NORMAL)
						status=CIRCLE_STATUS_BARRED;
					sb.append(",circle_status=").append(status);
				}
				sb.append(" where circle_id=?");
				row = DBUtils.executeUpdate(conn, sb.toString(), new Object[] { req.circle_id, req.circle_id });
			}
			if(row>0){
				res.status=Config.STATUS_OK;
				DBUtils.commitTransaction(conn);
				if(backImageName!=null && StringUtils.emptyString(old_circle_back_img))
					ImageHelper.deleteByName(old_circle_back_img);
				if(imageName!=null && StringUtils.emptyString(old_circle_pic))
					ImageHelper.deleteByName(old_circle_pic);
			}else{
				res.status=Config.STATUS_SERVER_ERROR;
				DBUtils.rollbackTransaction(conn);
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}
}

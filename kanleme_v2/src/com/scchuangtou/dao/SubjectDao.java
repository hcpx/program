package com.scchuangtou.dao;

import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.config.AdminConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.entity.AddSubjectReqEntity;
import com.scchuangtou.entity.AddSubjectResEntity;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.DeleteSubjectReqEntity;
import com.scchuangtou.entity.GetSubjectInfoReqEntity;
import com.scchuangtou.entity.GetSubjectInfoResEntity;
import com.scchuangtou.entity.GetSubjectsReqEntity;
import com.scchuangtou.entity.GetSubjectsResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.model.AdminInfo;
import com.scchuangtou.model.SubjectInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.StringUtils;

public class SubjectDao {
	private static String getImageDir(String subject_id) {
		return new StringBuffer().append("subject/").append(subject_id).append("/").toString();
	}

	private static String createImageName(String subject_id) {
		return new StringBuffer(getImageDir(subject_id)).append("/").append(IdUtils.createId(subject_id)).toString();
	}

	public static BaseResEntity deleteSubject(DeleteSubjectReqEntity req, String admin_token)
			throws SQLException {
		BaseResEntity res = new BaseResEntity();

		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();

			AdminInfo mAdminInfo = AdminDao.getAdminByToken(conn, admin_token);
			if (mAdminInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!mAdminInfo.hasMode(AdminConfig.MODE_MANAGER_SUBJECT)) {
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			DBUtils.beginTransaction(conn);
			int row = 0;
			try{
				String sql = "DELETE FROM subject WHERE subject_id=?";
				for(String subject_id:req.subject_ids){
					row = DBUtils.executeUpdate(conn,sql, new Object[]{subject_id});
					if(row <=0){
						break;
					}
				}
				if(row > 0){
					DBUtils.commitTransaction(conn);
				}
			}catch(Exception e){
				LogUtils.log(e);
			}
			if(row > 0){
				for(String subject_id:req.subject_ids){//删除专题图片
					ImageHelper.deleteByDir(getImageDir(subject_id));
				}
				res.status = Config.STATUS_OK;
			}else{
				res.status = Config.STATUS_SERVER_ERROR;
				DBUtils.rollbackTransaction(conn);
			}
		} finally {
			DBUtils.close(conn);
		}
		return res;
	}
	
	public static AddSubjectResEntity adminAddSubject(String admin_token,AddSubjectReqEntity req, MyMutiPart iconPart,
			List<MyMutiPart> imgs) throws SQLException {
		if (StringUtils.emptyString(admin_token)) {
			AddSubjectResEntity res = new AddSubjectResEntity();
			res.status = Config.STATUS_TOKEN_ERROR;
			return res;
		}
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			AdminInfo mAdminInfo = AdminDao.getAdminByToken(conn, admin_token);
			if (mAdminInfo == null) {
				AddSubjectResEntity res = new AddSubjectResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!mAdminInfo.hasMode(AdminConfig.MODE_MANAGER_SUBJECT)) {
				AddSubjectResEntity res = new AddSubjectResEntity();
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			Long time = System.currentTimeMillis();
			int row = 0;
			DBUtils.beginTransaction(conn);
			try {
				String subject_id = IdUtils.createId(mAdminInfo.admin_user);
				String iconImgName = createImageName(subject_id);
				HashMap<String, Object> datas = new HashMap<String, Object>();
				datas.put("subject_id", subject_id);
				datas.put("title", req.subject_title);
				datas.put("subject_content", req.subject_content);
				datas.put("subject_icon", iconImgName);
				datas.put("subject_create_time", time);
				row = DBUtils.insert(conn, "INSERT INTO subject", datas);
				if (ImageHelper.upload(iconImgName, iconPart, false) == null) {
					row = 0;
				}
				if (row > 0 && imgs != null && imgs.size() > 0) {
					int size = imgs.size();
					String imgName;
					for (int i = 0; i < size; i++) {
						imgName = createImageName(subject_id);
						Dimension mDimension = ImageHelper.upload(imgName, imgs.get(i), true);
						if (mDimension == null) {
							row = 0;
							break;
						}
						datas = new HashMap<String, Object>();
						datas.put("subject_id", subject_id);
						datas.put("subject_img", imgName);
						datas.put("width", mDimension.width);
						datas.put("height", mDimension.height);
						datas.put("subject_img_desc", req.subject_img_des.get(i));
						datas.put("subject_img_time", time++);
						row = DBUtils.insert(conn, "INSERT INTO subject_img", datas);
						if (row <= 0) {
							break;
						}
					}
				}
				if (row > 0) {
					DBUtils.commitTransaction(conn);
				}
			} catch (Exception e) {
				LogUtils.log(e);
				row = 0;
			}
			AddSubjectResEntity res = new AddSubjectResEntity();
			if (row > 0) {
				res.status = Config.STATUS_OK;
			} else {
				DBUtils.rollbackTransaction(conn);
				res.status = Config.STATUS_SERVER_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	/**
	 * 获取所有专题
	 * 
	 * @param req
	 * @return
	 * @throws SQLException
	 */
	public static GetSubjectsResEntity listSubjects(HttpServletRequest request, GetSubjectsReqEntity req)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			StringBuffer sb = new StringBuffer();
			sb.append(
					"SELECT subject_id,title,subject_content,subject_icon,subject_create_time,comment_count,browse_count,praise_count");
			sb.append(" FROM subject WHERE 1=1 ");
			if (!StringUtils.emptyString(req.keyword)) {
				sb.append(" AND title like '%").append(req.keyword).append("%'");
			}
			sb.append(" ORDER BY subject_create_time  DESC");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			rs = pstat.executeQuery();
			GetSubjectsResEntity res = new GetSubjectsResEntity();
			res.subjects = new ArrayList<>();
			GetSubjectsResEntity.Info info;
			while (rs.next()) {
				info = new GetSubjectsResEntity.Info();
				info.title = rs.getString("title");
				info.subject_id = rs.getString("subject_id");
				info.subject_content = rs.getString("subject_content");
				info.create_time = rs.getLong("subject_create_time");
				info.comment_count = rs.getInt("comment_count");
				info.browse_count = rs.getInt("browse_count");
				info.praise_count = rs.getInt("praise_count");
				String subject_icon = rs.getString("subject_icon");
				if (!StringUtils.emptyString(subject_icon))
					info.subject_icon = ImageHelper.getImageUrl(request, subject_icon);
				res.subjects.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.subjects.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	private static List<GetSubjectInfoResEntity.img> getSubjectImages(HttpServletRequest request,
			DBUtils.ConnectionCache conn, String subjectID) throws Exception {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		String sql = "select subject_img_time,subject_id,subject_img,subject_img_desc from subject_img where subject_id=? order by subject_img_time asc";
		List<GetSubjectInfoResEntity.img> imglist = null;
		try {
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, subjectID);
			rs = pstat.executeQuery();
			imglist = new ArrayList<>();
			while (rs.next()) {
				GetSubjectInfoResEntity.img imginfo = new GetSubjectInfoResEntity.img();
				imginfo.subject_image_url = ImageHelper.getImageUrl(request, rs.getString("subject_img"));
				imginfo.subject_image_description = rs.getString("subject_img_desc");
				imglist.add(imginfo);
			}
			return imglist;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	/**
	 * 获取专题详情
	 * 
	 * @param req
	 * @return
	 * @throws SQLException
	 */
	public static GetSubjectInfoResEntity getSubjectInfo(HttpServletRequest request, GetSubjectInfoReqEntity req)
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
					GetSubjectInfoResEntity res = new GetSubjectInfoResEntity();
					res.status = Config.STATUS_TOKEN_ERROR;
					return res;
				}
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					"SELECT title,subject_id,subject_icon,subject_content,subject_icon,subject_create_time,comment_count,browse_count,praise_count");
			sb.append(" FROM subject WHERE subject_id=?");
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.subject_id);
			rs = pstat.executeQuery();
			GetSubjectInfoResEntity res = new GetSubjectInfoResEntity();
			GetSubjectInfoResEntity.Info info;
			if (rs.next()) {
				// 获取基本信息
				info = new GetSubjectInfoResEntity.Info();
				String subject_id = rs.getString("subject_id");
				info.title = rs.getString("title");
				info.subject_id = subject_id;
				info.subject_content = rs.getString("subject_content");
				info.subject_time = rs.getLong("subject_create_time");
				info.comment_count = rs.getInt("comment_count");
				info.browse_count = rs.getInt("browse_count");
				info.praise_count = rs.getInt("praise_count");
				String subject_icon = rs.getString("subject_icon");
				if (!StringUtils.emptyString(subject_icon))
					info.subject_icon = ImageHelper.getImageUrl(request, subject_icon);
				// 是否被收藏
				if (user != null) {
					info.subject_collecteid = CollectDao.getUserCollectId(conn, subject_id,
							Config.CollectObjectType.COLLECT_OBJECT_TYPE_SUBJECT, user.user_id);
					info.subject_praiseid = PraiseDao.getUserPraiseId(conn, subject_id,
							Config.PraiseType.PRAISE_TYPE_SUBJECT, user.user_id);
					BrowseDao.browse(conn, user.user_id, req.subject_id, Config.BrowseType.BROWSE_TYPE_SUBJECT);
				}
				info.subject_pic = getSubjectImages(request, conn, subject_id);
				res.subject = info;
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

	public static SubjectInfo getSubjcetInfo(DBUtils.ConnectionCache conn, String subjcetID) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select subject_id,subject_content,subject_icon,subject_create_time from subject where subject_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, subjcetID);
			rs = pstat.executeQuery();
			if (rs.next()) {
				SubjectInfo subjectInfo = new SubjectInfo();
				subjectInfo.subject_id = rs.getString("subject_id");
				subjectInfo.subject_content = rs.getString("subject_content");
				subjectInfo.subject_icon = rs.getString("subject_icon");
				subjectInfo.subject_create_time = rs.getLong("subject_create_time");
				return subjectInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	public static int updateCount(DBUtils.ConnectionCache conn, String subject_id, int commentCount, int browseCount,
			int praiseCount) throws SQLException {
		String sql = "UPDATE subject set comment_count=comment_count+?,browse_count=browse_count+?,praise_count=praise_count+? WHERE subject_id=?";
		return DBUtils.executeUpdate(conn, sql, new Object[] { commentCount, browseCount, praiseCount, subject_id });
	}
}

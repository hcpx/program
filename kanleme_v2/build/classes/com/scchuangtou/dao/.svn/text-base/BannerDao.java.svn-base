package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.config.AdminConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.entity.AddBannerReqEntity;
import com.scchuangtou.entity.AddBannerResEntity;
import com.scchuangtou.entity.AdminGetBannersReqEntity;
import com.scchuangtou.entity.AdminGetBannersResEntity;
import com.scchuangtou.entity.BannerEntity;
import com.scchuangtou.entity.DeleteBannerReqEntity;
import com.scchuangtou.entity.DeleteBannerResEntity;
import com.scchuangtou.entity.GetBannersReqEntity;
import com.scchuangtou.entity.GetBannersResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.model.AdminInfo;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.DateUtil;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;

public class BannerDao {
	/**
	 * 图片名为banner_id
	 * 
	 * @param banner_id
	 * @return
	 */
	private static String getImageName(String banner_id) {
		return new StringBuffer().append("banner/").append(banner_id).toString();
	}

	private static boolean checkSource(DBUtils.ConnectionCache conn, String source, int source_type)
			throws SQLException {
		if (source_type == Config.BannerSourceType.BANNER_SOURCE_TYPE_WEB) {
			source = source.toLowerCase();
			return source.startsWith("http://") || source.startsWith("https://");
		} else if (source_type == Config.BannerSourceType.BANNER_SOURCE_TYPE_ARTICLE) {
			return ArticleDao.getArticleInfo(conn, source) != null;
		} else if (source_type == Config.BannerSourceType.BANNER_SOURCE_TYPE_SUBJECT) {
			return SubjectDao.getSubjcetInfo(conn, source) != null;
		} else if (source_type == Config.BannerSourceType.BANNER_source_TYPE_PROJECT) {
			return ProjectDao.getProjectInfo(conn, source) != null;
		}
		return false;
	}

	public static AddBannerResEntity addBanner(HttpServletRequest request, AddBannerReqEntity req, MyMutiPart bannerImg,
			String admin_token) throws Exception {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			AdminInfo mAdminInfo = AdminDao.getAdminByToken(conn, admin_token);
			if (mAdminInfo == null) {
				AddBannerResEntity res = new AddBannerResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!mAdminInfo.hasMode(AdminConfig.MODE_MANAGER_BANNER)) {
				AddBannerResEntity res = new AddBannerResEntity();
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			if (checkSource(conn, req.source, req.source_type)) {
				AddBannerResEntity res = new AddBannerResEntity();
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			req.start_time = req.start_time <= 0 ? System.currentTimeMillis() : req.start_time;
			int row = 0;

			String banner_id = IdUtils.createId(mAdminInfo.admin_user);
			String banner_name = getImageName(banner_id);//必须使用banner_id，否则删除banner时图片不能被删除
			try {
				DBUtils.beginTransaction(conn);

				HashMap<String, Object> datas = new HashMap<>();
				datas.put("banner_id", banner_id);
				datas.put("source_type", req.source_type);
				datas.put("banner_type", req.banner_type);
				datas.put("source", req.source);
				datas.put("start_time", DateUtil.getDayTime(req.start_time));
				datas.put("end_time", DateUtil.getDayEndTime(req.end_time));
				datas.put("img_url", banner_name);
				row = DBUtils.insert(conn, "INSERT INTO banner_info", datas);

				// ImageHelper.ImageClipInfo clipInfo = null;
				// if (req.banner_img_clip_info != null) {
				// clipInfo = new ImageHelper.ImageClipInfo();
				// clipInfo.x = req.banner_img_clip_info.x;
				// clipInfo.y = req.banner_img_clip_info.y;
				// clipInfo.width = req.banner_img_clip_info.width;
				// clipInfo.height = req.banner_img_clip_info.height;
				// }
				if (row > 0 && ImageHelper.upload(banner_name, bannerImg, false) == null) {
					row = 0;
				}
				if (row > 0) {
					DBUtils.commitTransaction(conn);
				}
			} catch (Exception e) {
				LogUtils.log(e);
				row = 0;
			}
			AddBannerResEntity res = new AddBannerResEntity();
			if (row > 0) {
				res.status = Config.STATUS_OK;
				res.banner_id = banner_id;
				res.img_url = ImageHelper.getImageUrl(request, banner_name);
			} else {
				DBUtils.rollbackTransaction(conn);
				res.status = Config.STATUS_SERVER_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	public static GetBannersResEntity getBanners(HttpServletRequest request, GetBannersReqEntity req) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			long time = System.currentTimeMillis();
			conn = DBUtils.getConnection();
			String sql = "SELECT banner_id,source_type,source,img_url FROM banner_info WHERE banner_type=? and start_time>=? and end_time<=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, req.type);
			pstat.setObject(2, time);
			pstat.setObject(3, time);
			rs = pstat.executeQuery();

			GetBannersResEntity res = new GetBannersResEntity();
			res.status = Config.STATUS_OK;
			res.banners = new ArrayList<>();
			while (rs.next()) {
				BannerEntity info = new BannerEntity();
				info.banner_id = rs.getString("banner_id");
				info.source_type = rs.getInt("source_type");
				info.source = rs.getString("source");
				info.img_url = ImageHelper.getImageUrl(request, rs.getString("img_url"));
				res.banners.add(info);
			}
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	public static DeleteBannerResEntity deleteBanner(DeleteBannerReqEntity req, String admin_token)
			throws SQLException {
		DeleteBannerResEntity res = new DeleteBannerResEntity();

		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();

			AdminInfo mAdminInfo = AdminDao.getAdminByToken(conn, admin_token);
			if (mAdminInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!mAdminInfo.hasMode(AdminConfig.MODE_MANAGER_BANNER)) {
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			DBUtils.beginTransaction(conn);
			int row = 0;
			try {
				String sql = "DELETE FROM banner_info WHERE banner_id=?";
				for (String banner_id : req.banner_ids) {
					row = DBUtils.executeUpdate(conn,sql, new Object[] { banner_id });
					if (row <= 0) {
						break;
					}
				}
				if (row > 0) {
					DBUtils.commitTransaction(conn);
				}
			} catch (Exception e) {
				LogUtils.log(e);
			}
			if (row > 0) {
				for (String banner_id : req.banner_ids) {
					ImageHelper.deleteByName(getImageName(banner_id));
				}
				res.status = Config.STATUS_OK;
			} else {
				res.status = Config.STATUS_SERVER_ERROR;
				DBUtils.rollbackTransaction(conn);
			}
		} finally {
			DBUtils.close(conn);
		}
		return res;
	}

	public static AdminGetBannersResEntity adminGetBanners(HttpServletRequest request, AdminGetBannersReqEntity req,
			String token) throws Exception {
		DBUtils.ConnectionCache conn = null;
		AdminGetBannersResEntity res = new AdminGetBannersResEntity();
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			AdminInfo mAdminInfo = AdminDao.getAdminByToken(conn, token);
			if (mAdminInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!mAdminInfo.hasMode(AdminConfig.MODE_MANAGER_BANNER)) {
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}

			StringBuffer sql = new StringBuffer(
					"SELECT banner_id,source_type,banner_type,source,img_url,start_time,end_time FROM banner_info order by end_time desc");
			sql.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sql.toString());
			rs = pstat.executeQuery();
			res.data = new ArrayList<>();
			while (rs.next()) {
				BannerEntity info = new BannerEntity();
				info.banner_id = rs.getString("banner_id");
				info.source_type = rs.getInt("source_type");
				info.banner_type = rs.getInt("banner_type");
				info.source = rs.getString("source");
				info.img_url = ImageHelper.getImageUrl(request, rs.getString("img_url"));
				info.start_time = rs.getLong("start_time");
				info.end_time = rs.getLong("end_time");
				res.data.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.data.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}
}
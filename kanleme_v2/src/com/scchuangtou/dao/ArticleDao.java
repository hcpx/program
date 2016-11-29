package com.scchuangtou.dao;

import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.AdminConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.entity.AddArticleReqEntity;
import com.scchuangtou.entity.AddArticleResEntity;
import com.scchuangtou.entity.AdminGetMyArticlesReqEntity;
import com.scchuangtou.entity.AdminGetMyArticlesResEntity;
import com.scchuangtou.entity.BaseArticleReqEntity;
import com.scchuangtou.entity.BaseArticleResEntity;
import com.scchuangtou.entity.DeleteArticleReqEntity;
import com.scchuangtou.entity.DeleteArticleResEntity;
import com.scchuangtou.entity.GetArticleInfoReqEntity;
import com.scchuangtou.entity.GetArticleInfoResEntity;
import com.scchuangtou.entity.GetCircleArticlesReqEntity;
import com.scchuangtou.entity.GetCircleArticlesResEntity;
import com.scchuangtou.entity.GetCollectArticlesReqEntity;
import com.scchuangtou.entity.GetCollectArticlesResEntity;
import com.scchuangtou.entity.GetMyArticlesReqEntity;
import com.scchuangtou.entity.GetMyArticlesResEntity;
import com.scchuangtou.entity.GetNoCircleArticlesReqEntity;
import com.scchuangtou.entity.GetNoCircleArticlesResEntity;
import com.scchuangtou.entity.GetParticipationArticlesReqEntity;
import com.scchuangtou.entity.GetParticipationArticlesResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.model.AdminInfo;
import com.scchuangtou.model.ArticleImageInfo;
import com.scchuangtou.model.ArticleInfo;
import com.scchuangtou.model.Circle;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserValueInfo;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.DateUtil;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.StringUtils;

public class ArticleDao {
	public static int ARTICLE_STATUS_NORMAL = 0; // 正常状态
	public static int ARTICLE_STATUS_BARRED = -1; // 禁止访问状态
	public static int ARTICLE_DELTE_TIME_NORMAL = 0; // 没有被删除

	private static String getImageDir(String id) {
		return new StringBuffer().append("article/").append(id).append("/").toString();
	}

	private static String createImageName(String article_id) {
		return new StringBuffer(getImageDir(article_id)).append("/").append(IdUtils.createId(article_id)).toString();
	}

	/**
	 * 成长值增加
	 * 
	 * @param conn
	 * @param user
	 * @return
	 * @throws Exception
	 */
	private static long addArticleGiving(DBUtils.ConnectionCache conn, String user_id) throws Exception {
		long ctime = System.currentTimeMillis();
		long giving_growth = 0;

		UserValueInfo value = new UserValueInfo();
		value.growth = Config.GrowthGiving.FIRST_ARTICLE;
		StringBuffer where = new StringBuffer();
		where.append("(SELECT count(`article`.article_id) from `article` where `article`.user_id='").append(user_id)
				.append("')=0");
		int r = UserDao.updateUserValue(conn, user_id, value, null, where.toString(), 0, null);
		if (r > 0) {
			giving_growth = value.growth;
		} else {
			value = new UserValueInfo();
			value.growth = Config.GrowthGiving.ADD_ARTICLE;

			long stime = DateUtil.getDayTime(ctime);
			long etime = DateUtil.getDayEndTime(ctime);
			where = new StringBuffer();
			where.append("(SELECT count(article_id) FROM `article` WHERE user_id='").append(user_id).append("'");
			where.append(" AND article_publish_time>=").append(stime);
			where.append(" AND article_publish_time<=").append(etime);
			where.append(")<").append(Config.GrowthGivingMax.ADD_ARTICLE);
			r = UserDao.updateUserValue(conn, user_id, value, null, where.toString(), 0, null);
			if (r > 0) {
				giving_growth = value.growth;
			}
		}
		return giving_growth;
	}

	public static BaseArticleResEntity addArticle(DBUtils.ConnectionCache conn, String user_id, String add_user_name,
			BaseArticleReqEntity req, HttpServletRequest request) throws Exception {
		if (!StringUtils.emptyString(req.circle_id)) {
			Circle circle = CircleDao.getCircleInfo(conn, req.circle_id);
			if (circle == null || circle.circle_status != CircleDao.CIRCLE_STATUS_NORMAL) {
				AddArticleResEntity res = new AddArticleResEntity();
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
		}
		String article_id = IdUtils.createId(user_id);
		long giving_growth = 0;
		int row = 0;
		long ctime = System.currentTimeMillis();
		List<AddArticleResEntity.img> articlePics = null;
		try {
			DBUtils.beginTransaction(conn);
			HashMap<String, Object> datas = new HashMap<String, Object>();
			datas.put("article_id", article_id);
			datas.put("circle_id", req.circle_id);
			datas.put("user_id", user_id);
			datas.put("article_title", req.title);
			datas.put("article_content", req.article_content);
			datas.put("article_type", JSON.toJSONString(req.article_type));
			datas.put("article_label", JSON.toJSONString(req.article_label));
			datas.put("article_publish_time", ctime);
			datas.put("article_status", ARTICLE_STATUS_NORMAL);
			datas.put("add_user_name", add_user_name);
			row = DBUtils.insert(conn, "INSERT INTO article", datas);
			if (row > 0 && req.article_pic_desc != null) {
				articlePics = new ArrayList<>();
				for (int i = 0, len = req.article_pic_desc.length; i < len; i++) {
					MyMutiPart pic_part = new MyMutiPart(request.getPart(req.article_pic_desc[i].article_pic_partname));
					// 图片信息
					String imageName = createImageName(article_id);
					Dimension mDimension = ImageHelper.upload(imageName, pic_part, true);
					if (mDimension == null) {
						row = 0;
						break;
					}
					AddArticleResEntity.img articleImageInfo = new AddArticleResEntity.img();
					articleImageInfo.article_image_description = req.article_pic_desc[i].article_pic_des;
					articleImageInfo.article_image_url = ImageHelper.getImageUrl(request, imageName);
					articlePics.add(articleImageInfo);

					datas.clear();
					datas.put("article_image_id", IdUtils.createId(article_id));
					datas.put("article_id", article_id);
					datas.put("article_image_url", imageName);
					datas.put("article_image_description", articleImageInfo.article_image_description);
					datas.put("article_time", ctime++);
					datas.put("width", mDimension.width);
					datas.put("height", mDimension.height);
					row = DBUtils.insert(conn, "INSERT INTO article_image", datas);
					if (row <= 0) {
						break;
					}
				}
			}
			if (row > 0) {
				giving_growth = addArticleGiving(conn, user_id);
			}
			if (row > 0)
				DBUtils.commitTransaction(conn);

		} catch (Exception e) {
			row = 0;
			LogUtils.log(e);
		}
		BaseArticleResEntity res = new BaseArticleResEntity();
		if (row > 0) {
			res.status = Config.STATUS_OK;
			res.article_id = article_id;
			res.giving_growth = giving_growth;
			if (articlePics != null) {
				res.article_pics = articlePics;
			}
		} else {
			// 图片上传不完全或者失败的情况下，进行已成功图片的删除。
			DBUtils.rollbackTransaction(conn);
			res.status = Config.STATUS_SERVER_ERROR;
		}
		return res;
	}

	/**
	 * 增加帖子
	 * 
	 * @param req
	 * @param articlePicPart
	 * @return
	 * @throws Exception
	 */
	public static AddArticleResEntity addArticle(AddArticleReqEntity req, HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			// 判断token过期user为空，设置TOKEN_ERROR直接返回
			if (user == null) {
				AddArticleResEntity res = new AddArticleResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			long ctime = System.currentTimeMillis();
			long t = (user.banned_time - ctime) / 1000;
			if (t > 0) {
				AddArticleResEntity res = new AddArticleResEntity();
				res.status = Config.STATUS_BANNED;
				res.banned_time = t;
				return res;
			}
			if (StringUtils.emptyString(user.phone_number)) {
				AddArticleResEntity res = new AddArticleResEntity();
				res.status = Config.STATUS_NOT_BIND_PHONE;
				return res;
			}
			if (!StringUtils.emptyString(req.circle_id)) {
				if (!CircleUserDao.isJoin(conn, req.circle_id, user.user_id)) {
					AddArticleResEntity res = new AddArticleResEntity();
					res.status = Config.STATUS_NOT_EXITS;
					return res;
				}
			}
			// TODO 这里必须认证为下个版本先注释
			// if(user.certification !=
			// Config.CertificationStauts.CERTIFICATION_STATUS_PASS){
			// AddArticleResEntity res = new AddArticleResEntity();
			// res.status = Config.STATUS_NOT_IDENTIFICATION;
			// return res;
			// }

			BaseArticleResEntity res = addArticle(conn, user.user_id, user.user_name, req, request);
			AddArticleResEntity addarticleRes = new AddArticleResEntity();
			addarticleRes.status = res.status;
			addarticleRes.article_id = res.article_id;
			addarticleRes.giving_growth = res.giving_growth;
			addarticleRes.growth = user.growth + res.giving_growth;
			addarticleRes.article_pics = res.article_pics;
			return addarticleRes;
		} finally {
			DBUtils.close(conn);
		}
	}

	/**
	 * 删除帖子，保留60天
	 * 
	 * @param req
	 * @return
	 * @throws SQLException
	 */
	public static DeleteArticleResEntity deleteArticle(DeleteArticleReqEntity req) throws SQLException {
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				DeleteArticleResEntity res = new DeleteArticleResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			String auser = getArticleInfo(conn, req.article_id).user_id;
			if (!user.user_id.equals(auser)) {
				DeleteArticleResEntity res = new DeleteArticleResEntity();
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			int row = 0;
			try {
				DBUtils.beginTransaction(conn);
				// 删除举报信息
				ReportDao.deleteObjectReport(conn, req.article_id, Config.ReportObjectType.REPORT_OBJECT_TYPE_ARTICLE);
				// 删除评论信息
				CommentDao.deleteAllComment(conn, req.article_id, Config.CommentObjectType.COMMENT_OBJECT_TYPE_ARTICLE);
				// 删除浏览历史
				BrowseDao.deleteBrowse(conn, req.article_id, Config.BrowseType.BROWSE_TYPE_ARTICLE);
				// 删除点赞信息
				PraiseDao.deletePraise(conn, req.article_id, Config.PraiseType.PRAISE_TYPE_ARTICLE);
				// 删除收藏信息
				CollectDao.deleteCollect(conn, req.article_id, Config.CommentObjectType.COMMENT_OBJECT_TYPE_ARTICLE);
				// 规定60日后进行删除
				row = DBUtils.executeUpdate(conn,
						"UPDATE article SET delete_time=?,article_comment_num=0,article_browse_num=0,article_praise_num=0 WHERE article_id=? AND user_id=?",
						new Object[] { System.currentTimeMillis(), req.article_id, user.user_id });
				if (row > 0) {
					DBUtils.commitTransaction(conn);
					ReportDao.deleteObjectReportImages(req.article_id,
							Config.ReportObjectType.REPORT_OBJECT_TYPE_ARTICLE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				row = 0;
			}
			if (row > 0) {
				DeleteArticleResEntity res = new DeleteArticleResEntity();
				res.status = Config.STATUS_OK;
				return res;
			} else {
				DeleteArticleResEntity res = new DeleteArticleResEntity();
				res.status = Config.STATUS_SERVER_ERROR;
				return res;
			}
		} finally {
			DBUtils.close(conn);
		}
	}

	/**
	 * 获取所有帖子按时间排序
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static GetNoCircleArticlesResEntity listNoCircleArticles(GetNoCircleArticlesReqEntity req,
			HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			StringBuffer sb = new StringBuffer();
			sb.append(
					"SELECT article.article_title,article.article_id,article.article_content,article.article_publish_time,article.article_type,article.article_label,userinfo.user_id,userinfo.nickname,userinfo.head_pic,userinfo.growth,article.article_comment_num,article.article_browse_num,article.article_praise_num ");
			sb.append(" FROM `article` INNER JOIN userinfo ON article.user_id = userinfo.user_id ");
			sb.append(" WHERE article_status =").append(ARTICLE_STATUS_NORMAL);
			sb.append(" and circle_id is null");
			sb.append(" AND delete_time =").append(ARTICLE_DELTE_TIME_NORMAL);
			sb.append(" ORDER BY article_publish_time  DESC");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			rs = pstat.executeQuery();
			GetNoCircleArticlesResEntity res = new GetNoCircleArticlesResEntity();
			res.article = new ArrayList<>();
			GetNoCircleArticlesResEntity.Info info;
			GetNoCircleArticlesResEntity.UserInfo userInfo;
			while (rs.next()) {
				// 获取基本信息
				info = new GetNoCircleArticlesResEntity.Info();
				String article_id = rs.getString("article_id");
				info.article_id = article_id;
				info.article_title = rs.getString("article_title");
				info.article_content = rs.getString("article_content");
				info.article_time = rs.getLong("article_publish_time");
				String article_type = rs.getString("article_type");
				if (!StringUtils.emptyString(article_type))
					info.article_type = JSON.parseArray(article_type, String.class);
				String article_label = rs.getString("article_label");
				if (!StringUtils.emptyString(article_label))
					info.article_label = JSON.parseArray(article_label, String.class);
				info.comment_num = rs.getLong("article_comment_num");
				info.view_count = rs.getLong("article_browse_num");
				info.article_praise_num = rs.getLong("article_praise_num");
				// 获取创建人信息
				userInfo = new GetNoCircleArticlesResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				info.user_info = userInfo;

				// 获取图片信息
				List<ArticleImageInfo> imglist = ArticleImageDao.getArticleImage(conn, info.article_id, request, 3);
				List<GetNoCircleArticlesResEntity.img> article_pic = null;
				if (imglist != null && imglist.size() != 0) {
					article_pic = new ArrayList<>();
					for (ArticleImageInfo imginfo : imglist) {
						GetNoCircleArticlesResEntity.img img = new GetNoCircleArticlesResEntity.img();
						img.article_image_url = imginfo.article_image_url;
						article_pic.add(img);
					}
				}
				info.article_pic = article_pic;
				res.article.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.article.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	/**
	 * 获取本人所有帖子按时间排序
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static GetMyArticlesResEntity listMyArticles(GetMyArticlesReqEntity req, HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				GetMyArticlesResEntity res = new GetMyArticlesResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					"select a.article_title,a.article_id,a.article_content,a.article_publish_time,a.article_type,a.article_label,a.user_id,a.article_status,article_comment_num,article_browse_num,a.article_praise_num");
			sb.append(" FROM article a where a.user_id=?");
			sb.append(" AND a.delete_time =").append(ARTICLE_DELTE_TIME_NORMAL);
			sb.append(" ORDER BY a.article_publish_time  DESC");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, user.user_id);
			rs = pstat.executeQuery();
			GetMyArticlesResEntity res = new GetMyArticlesResEntity();
			res.article = new ArrayList<>();
			GetMyArticlesResEntity.Info info;
			while (rs.next()) {
				info = new GetMyArticlesResEntity.Info();
				String article_id = rs.getString("article_id");
				info.article_id = article_id;
				info.article_title = rs.getString("article_title");
				info.article_content = rs.getString("article_content");
				info.article_time = rs.getLong("article_publish_time");
				String article_type = rs.getString("article_type");
				if (!StringUtils.emptyString(article_type))
					info.article_type = JSON.parseArray(article_type, String.class);
				String article_label = rs.getString("article_label");
				if (!StringUtils.emptyString(article_label))
					info.article_label = JSON.parseArray(article_label, String.class);
				info.comment_num = rs.getLong("article_comment_num");
				info.view_count = rs.getLong("article_browse_num");
				info.article_praise_num = rs.getLong("article_praise_num");
				// 获取图片信息
				List<ArticleImageInfo> imglist = ArticleImageDao.getArticleImage(conn, info.article_id, request, 3);
				List<GetMyArticlesResEntity.img> article_pic = null;
				if (imglist != null && imglist.size() != 0) {
					article_pic = new ArrayList<>();
					for (ArticleImageInfo imginfo : imglist) {
						GetMyArticlesResEntity.img img = new GetMyArticlesResEntity.img();
						// img.article_image_description =
						// imginfo.article_image_description;
						img.article_image_url = imginfo.article_image_url;
						article_pic.add(img);
					}
				}
				info.article_pic = article_pic;
				res.article.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.article.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	/**
	 * 获取收藏的帖子
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static GetCollectArticlesResEntity listCollectArticles(GetCollectArticlesReqEntity req,
			HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				GetCollectArticlesResEntity res = new GetCollectArticlesResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					"select a.article_title,a.article_id,a.article_content,a.article_publish_time,a.article_type,a.article_label,a.user_id,a.article_status,a.user_id,a.article_comment_num,a.article_browse_num,a.article_praise_num,");
			sb.append(" u.nickname,u.head_pic,u.growth");
			sb.append(" FROM article a INNER JOIN collect c ON c.collect_object_id = a.article_id");
			sb.append(" INNER JOIN userinfo u ON u.user_id = a.user_id where ");
			sb.append(" c.collect_object_type =").append(Config.CollectObjectType.COLLECT_OBJECT_TYPE_ARTICLE);
			sb.append(" and c.user_id =?");
			sb.append(" AND a.article_status =").append(ARTICLE_STATUS_NORMAL);
			sb.append(" AND a.delete_time =").append(ARTICLE_DELTE_TIME_NORMAL);
			sb.append(" ORDER BY a.article_publish_time  DESC");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, user.user_id);
			rs = pstat.executeQuery();
			GetCollectArticlesResEntity res = new GetCollectArticlesResEntity();
			res.article = new ArrayList<>();
			GetCollectArticlesResEntity.Info info;
			GetCollectArticlesResEntity.UserInfo userInfo;
			while (rs.next()) {
				// 获取基本信息
				info = new GetCollectArticlesResEntity.Info();
				String article_id = rs.getString("article_id");
				info.article_id = article_id;
				info.article_title = rs.getString("article_title");
				info.article_content = rs.getString("article_content");
				info.article_time = rs.getLong("article_publish_time");
				String article_type = rs.getString("article_type");
				if (!StringUtils.emptyString(article_type))
					info.article_type = JSON.parseArray(article_type, String.class);
				String article_label = rs.getString("article_label");
				if (!StringUtils.emptyString(article_label))
					info.article_label = JSON.parseArray(article_label, String.class);
				info.comment_num = rs.getLong("article_comment_num");
				info.view_count = rs.getLong("article_browse_num");
				info.article_praise_num = rs.getLong("article_praise_num");
				// 获取创建人信息
				userInfo = new GetCollectArticlesResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				info.user_info = userInfo;

				// 获取图片信息
				List<ArticleImageInfo> imglist = ArticleImageDao.getArticleImage(conn, info.article_id, request, 3);
				List<GetCollectArticlesResEntity.img> article_pic = null;
				if (imglist != null && imglist.size() != 0) {
					article_pic = new ArrayList<>();
					for (ArticleImageInfo imginfo : imglist) {
						GetCollectArticlesResEntity.img img = new GetCollectArticlesResEntity.img();
						img.article_image_url = imginfo.article_image_url;
						article_pic.add(img);
					}
				}
				info.article_pic = article_pic;
				res.article.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.article.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	/**
	 * 获取我参与的帖子
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static GetParticipationArticlesResEntity listParticipationArticles(GetParticipationArticlesReqEntity req,
			HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				GetParticipationArticlesResEntity res = new GetParticipationArticlesResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					"SELECT distinct article.article_id,article.circle_id,article.user_id,article.article_title,article.article_content,article.article_type,article.article_label,article.article_publish_time,");
			sb.append(
					" article.article_status,article.delete_time,article.article_comment_num,article.article_browse_num,article.article_praise_num,userinfo.user_id,userinfo.nickname,userinfo.head_pic,userinfo.growth");
			sb.append(
					" FROM comment INNER JOIN article ON comment.comment_object_id = article.article_id INNER JOIN userinfo ON article.user_id = userinfo.user_id");
			sb.append(" WHERE comment.user_id=? ").append(" AND comment.comment_object_type =")
					.append(Config.CommentObjectType.COMMENT_OBJECT_TYPE_ARTICLE);
			sb.append(" AND article.user_id <> ?");
			// sb.append(" AND (comment.parent_id ='' OR comment.parent_id is
			// NULL)");
			sb.append(" ORDER BY article.article_publish_time  DESC");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, user.user_id);
			pstat.setObject(2, user.user_id);
			rs = pstat.executeQuery();
			GetParticipationArticlesResEntity res = new GetParticipationArticlesResEntity();
			res.article = new ArrayList<>();
			GetParticipationArticlesResEntity.Info info;
			GetParticipationArticlesResEntity.UserInfo userInfo;
			while (rs.next()) {
				// 获取基本信息
				info = new GetParticipationArticlesResEntity.Info();
				String article_id = rs.getString("article_id");
				info.article_id = article_id;
				info.article_title = rs.getString("article_title");
				info.article_content = rs.getString("article_content");
				info.article_time = rs.getLong("article_publish_time");
				String article_type = rs.getString("article_type");
				if (!StringUtils.emptyString(article_type))
					info.article_type = JSON.parseArray(article_type, String.class);
				String article_label = rs.getString("article_label");
				if (!StringUtils.emptyString(article_label))
					info.article_label = JSON.parseArray(article_label, String.class);
				info.comment_num = rs.getLong("article_comment_num");
				info.view_count = rs.getLong("article_browse_num");
				info.article_praise_num = rs.getLong("article_praise_num");
				// 获取创建人信息
				userInfo = new GetParticipationArticlesResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				info.user_info = userInfo;

				// 获取图片信息
				List<ArticleImageInfo> imglist = ArticleImageDao.getArticleImage(conn, info.article_id, request, 3);
				List<GetParticipationArticlesResEntity.img> article_pic = null;
				if (imglist != null && imglist.size() != 0) {
					article_pic = new ArrayList<>();
					for (ArticleImageInfo imginfo : imglist) {
						GetParticipationArticlesResEntity.img img = new GetParticipationArticlesResEntity.img();
						img.article_image_url = imginfo.article_image_url;
						article_pic.add(img);
					}
				}
				info.article_pic = article_pic;
				res.article.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.article.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	public static GetCircleArticlesResEntity listCircleArticles(GetCircleArticlesReqEntity req,
			HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			Circle circle = CircleDao.getCircleInfo(conn, req.circle_id);
			if (circle == null) {
				GetCircleArticlesResEntity res = new GetCircleArticlesResEntity();
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					"SELECT article.article_title,article.article_id,article.article_content,article.article_publish_time,article.article_type,article.article_label,userinfo.user_id,userinfo.nickname,userinfo.head_pic,userinfo.growth,article.article_comment_num,article.article_browse_num,article.article_praise_num ");
			sb.append(" FROM `article` INNER JOIN userinfo ON article.user_id = userinfo.user_id ");
			sb.append(" WHERE article_status =").append(ARTICLE_STATUS_NORMAL);
			sb.append(" AND delete_time =").append(ARTICLE_DELTE_TIME_NORMAL);
			sb.append(" AND circle_id =?");
			if (req.type == Config.ArticleSearch.ARTICLE_SEARCH_HOT)
				sb.append(" order by article_comment_num desc");
			else
				sb.append(" ORDER BY article_publish_time  DESC");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.circle_id);
			rs = pstat.executeQuery();
			GetCircleArticlesResEntity res = new GetCircleArticlesResEntity();
			res.article = new ArrayList<>();
			GetCircleArticlesResEntity.Info info;
			GetCircleArticlesResEntity.UserInfo userInfo;
			while (rs.next()) {
				// 获取基本信息
				info = new GetCircleArticlesResEntity.Info();
				String article_id = rs.getString("article_id");
				info.article_id = article_id;
				info.article_title = rs.getString("article_title");
				info.article_content = rs.getString("article_content");
				info.article_time = rs.getLong("article_publish_time");
				String article_type = rs.getString("article_type");
				if (!StringUtils.emptyString(article_type))
					info.article_type = JSON.parseArray(article_type, String.class);
				String article_label = rs.getString("article_label");
				if (!StringUtils.emptyString(article_label))
					info.article_label = JSON.parseArray(article_label, String.class);
				info.comment_num = rs.getLong("article_comment_num");
				info.view_count = rs.getLong("article_browse_num");
				info.article_praise_num = rs.getLong("article_praise_num");
				// 获取创建人信息
				userInfo = new GetCircleArticlesResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				info.user_info = userInfo;

				// 获取图片信息
				List<ArticleImageInfo> imglist = ArticleImageDao.getArticleImage(conn, info.article_id, request, 3);
				List<GetCircleArticlesResEntity.img> article_pic = null;
				if (imglist != null && imglist.size() != 0) {
					article_pic = new ArrayList<>();
					for (ArticleImageInfo imginfo : imglist) {
						GetCircleArticlesResEntity.img img = new GetCircleArticlesResEntity.img();
						img.article_image_url = imginfo.article_image_url;
						article_pic.add(img);
					}
				}
				info.article_pic = article_pic;
				res.article.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.article.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	/**
	 * 获取帖子详情
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static GetArticleInfoResEntity getArticleInfo(GetArticleInfoReqEntity req, HttpServletRequest request)
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
					GetArticleInfoResEntity res = new GetArticleInfoResEntity();
					res.status = Config.STATUS_TOKEN_ERROR;
					return res;
				}
			}
			ArticleInfo articleInfo = getArticleInfo(conn, req.article_id);
			if (articleInfo == null) {
				GetArticleInfoResEntity res = new GetArticleInfoResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					" SELECT a.article_title,a.article_id,a.article_content,a.article_publish_time,a.article_type,a.article_label,a.article_comment_num,a.article_browse_num,a.article_praise_num,");
			sb.append(" a.user_id,a.article_status,a.delete_time,u.nickname,u.head_pic,u.growth,u.signature FROM ");
			sb.append(" article a INNER JOIN userinfo u ON u.user_id = a.user_id WHERE a.article_id = ?");
			if (user == null) {
				sb.append(" AND a.article_status =").append(ARTICLE_STATUS_NORMAL);
			} else {
				if (!user.user_id.equals(articleInfo.user_id))
					sb.append(" AND a.article_status =").append(ARTICLE_STATUS_NORMAL);
			}
			sb.append(" AND a.delete_time =").append(ARTICLE_DELTE_TIME_NORMAL);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.article_id);
			rs = pstat.executeQuery();
			GetArticleInfoResEntity res = new GetArticleInfoResEntity();
			GetArticleInfoResEntity.Info info;
			GetArticleInfoResEntity.UserInfo userInfo;
			if (rs.next()) {
				// 获取基本信息
				info = new GetArticleInfoResEntity.Info();
				String article_id = rs.getString("article_id");
				info.article_id = article_id;
				info.article_title = rs.getString("article_title");
				info.article_content = rs.getString("article_content");
				info.article_time = rs.getLong("article_publish_time");
				info.comment_num = rs.getLong("article_comment_num");
				info.view_count = rs.getLong("article_browse_num");
				info.article_praise_num = rs.getLong("article_praise_num");
				String article_type = rs.getString("article_type");
				if (!StringUtils.emptyString(article_type))
					info.article_type = JSON.parseArray(article_type, String.class);
				String article_label = rs.getString("article_label");
				if (!StringUtils.emptyString(article_label))
					info.article_label = JSON.parseArray(article_label, String.class);
				if (user != null) {
					info.article_collecteid = CollectDao.getUserCollectId(conn, article_id,
							Config.CollectObjectType.COLLECT_OBJECT_TYPE_ARTICLE, user.user_id);
					info.article_praiseid = PraiseDao.getUserPraiseId(conn, article_id,
							Config.PraiseType.PRAISE_TYPE_ARTICLE, user.user_id);
					BrowseDao.browse(conn, user.user_id, req.article_id, Config.BrowseType.BROWSE_TYPE_ARTICLE);
				}

				// 获取创建人信息
				userInfo = new GetArticleInfoResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.signature = rs.getString("signature");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				info.user_info = userInfo;

				// 获取图片信息
				List<ArticleImageInfo> imglist = ArticleImageDao.getArticleImage(conn, info.article_id, request, 0);
				List<GetArticleInfoResEntity.img> article_pic = null;
				if (imglist != null && imglist.size() != 0) {
					article_pic = new ArrayList<>();
					for (ArticleImageInfo imginfo : imglist) {
						GetArticleInfoResEntity.img img = new GetArticleInfoResEntity.img();
						img.article_image_description = imginfo.article_image_description;
						img.article_image_url = imginfo.article_image_url;
						article_pic.add(img);
					}
				}
				info.article_pic = article_pic;
				res.article = info;
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

	/**
	 * 根据帖子id获取创建用户articleInfo
	 * 
	 * @param conn
	 * @param articleID
	 * @return
	 * @throws SQLException
	 */
	public static ArticleInfo getArticleInfo(DBUtils.ConnectionCache conn, String articleID) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select article_id,circle_id,user_id,article_title,article_content,article_type,article_label,article_publish_time,article_status,delete_time,article_comment_num,article_browse_num,article.article_praise_num from article where article_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, articleID);
			rs = pstat.executeQuery();
			if (rs.next()) {
				ArticleInfo articleInfo = new ArticleInfo();
				articleInfo.article_id = rs.getString("article_id");
				articleInfo.circle_id = rs.getString("circle_id");
				articleInfo.user_id = rs.getString("user_id");
				articleInfo.article_title = rs.getString("article_title");
				articleInfo.article_content = rs.getString("article_content");
				articleInfo.article_type = rs.getString("article_type");
				articleInfo.article_label = rs.getString("article_label");
				articleInfo.article_publish_time = rs.getLong("article_publish_time");
				articleInfo.article_status = rs.getInt("article_status");
				articleInfo.delete_time = rs.getLong("delete_time");
				articleInfo.article_comment_num = rs.getLong("article_comment_num");
				articleInfo.article_browse_num = rs.getLong("article_browse_num");
				articleInfo.article_praise_num = rs.getLong("article_praise_num");
				return articleInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	/**
	 * 获取圈子下帖子数
	 * 
	 * @param conn
	 * @param circle_id
	 * @return
	 * @throws SQLException
	 */
	public static int getAticlesCountByCircleId(DBUtils.ConnectionCache conn, String circle_id) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT count(article_id) FROM article WHERE circle_id=? and article_status =? and delete_time =?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, circle_id);
			pstat.setObject(2, ARTICLE_STATUS_NORMAL);
			pstat.setObject(3, ARTICLE_DELTE_TIME_NORMAL);
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
	 * 增加浏览次数或者增加评论次数
	 * 
	 * @param conn
	 * @param objcet_id
	 * @param object_type
	 * @return
	 * @throws SQLException
	 */
	public static int updateCount(DBUtils.ConnectionCache conn, String object_id, int commentCount, int browseCount,
			int praiseCount) throws SQLException {
		String sql = "update article set article_comment_num=article_comment_num+?,article_browse_num=article_browse_num+?,article_praise_num=article_praise_num+? where article_id=?";
		return DBUtils.executeUpdate(conn, sql, new Object[] { commentCount, browseCount, praiseCount, object_id });
	}

	public static AdminGetMyArticlesResEntity adminlistMyArticles(AdminGetMyArticlesReqEntity req, String token)
			throws Exception {
		AdminGetMyArticlesResEntity res = new AdminGetMyArticlesResEntity();
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
			if (!mAdminInfo.hasMode(AdminConfig.MODE_MANAGER_USER)) {
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}
			User user = UserDao.getUser(conn, req.user_id);
			if (user == null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append("select a.article_title,a.article_id,a.article_publish_time");
			sb.append(" FROM article a where a.user_id=?");
			sb.append(" AND a.delete_time =").append(ARTICLE_DELTE_TIME_NORMAL);
			sb.append(" ORDER BY a.article_publish_time  DESC");
			sb.append(" LIMIT ?,?");
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, user.user_id);
			pstat.setObject(2, req.begin);
			if (req.count > 0) {
				pstat.setObject(3, req.count);
			} else {
				pstat.setObject(3, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			res.article = new ArrayList<>();
			AdminGetMyArticlesResEntity.Info info;
			while (rs.next()) {
				info = new AdminGetMyArticlesResEntity.Info();
				String article_id = rs.getString("article_id");
				info.article_id = article_id;
				info.article_title = rs.getString("article_title");
				info.article_time = rs.getLong("article_publish_time");
				res.article.add(info);
			}
			res.status = Config.STATUS_OK;
			boolean has_more_data = true;
			if (req.count > 0) {
				has_more_data = res.article.size() == req.count;
			} else {
				has_more_data = res.article.size() == Config.ONCE_QUERY_COUNT;
			}
			res.has_more_data = has_more_data;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

}

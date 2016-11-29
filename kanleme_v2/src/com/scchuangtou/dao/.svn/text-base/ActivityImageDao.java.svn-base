package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.model.ArticleImageInfo;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.StringUtils;

public class ActivityImageDao {
	
	public static List<ArticleImageInfo> getActivityImage(DBUtils.ConnectionCache conn, String activityID,HttpServletRequest request,int count) throws Exception {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer("select activity_image_url,activity_image_description from activity_image where activity_id=? order by activity_time asc");
		if(count!=0)
			sql.append(" LIMIT 0,").append(count);
		List<ArticleImageInfo> imglist = null;
		try {
			pstat = conn.prepareStatement(sql.toString());
			pstat.setObject(1, activityID);
			rs = pstat.executeQuery();
			imglist = new ArrayList<>();
			while (rs.next()) {
				ArticleImageInfo imginfo = new ArticleImageInfo();
				String article_image_url = rs.getString("activity_image_url");
				if (!StringUtils.emptyString(article_image_url))
					imginfo.article_image_url = ImageHelper.getImageUrl(request, article_image_url);
				imginfo.article_image_description = rs.getString("activity_image_description");
				imglist.add(imginfo);
			}
			return imglist;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	public static List<String> getArticleImagesStr(DBUtils.ConnectionCache conn, String activityID) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		String sql = "select activity_image_url,activity_image_description from activity_image where activity_id=?";
		List<String> imglist = null;
		try {
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, activityID);
			rs = pstat.executeQuery();
			imglist = new ArrayList<>();
			while (rs.next()) {
				imglist.add(rs.getString("activity_image_url"));
			}
			return imglist;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	public static int deleteArticleImage(DBUtils.ConnectionCache conn, String activityID) throws SQLException {
		String sql = "delete from activity_image where activity_id=?";
		return DBUtils.executeUpdate(conn, sql, new Object[] { activityID });
	}
}

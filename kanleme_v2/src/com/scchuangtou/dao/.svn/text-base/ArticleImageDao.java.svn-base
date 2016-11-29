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

public class ArticleImageDao {
	public static List<ArticleImageInfo> getArticleImage(DBUtils.ConnectionCache conn, String articleID,HttpServletRequest request,int count) throws Exception {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer("select article_image_url,article_image_description from article_image where article_id=? order by article_time asc");
		if(count!=0)
			sql.append(" LIMIT 0,").append(count);
		List<ArticleImageInfo> imglist = null;
		try {
			pstat = conn.prepareStatement(sql.toString());
			pstat.setObject(1, articleID);
			rs = pstat.executeQuery();
			imglist = new ArrayList<>();
			while (rs.next()) {
				ArticleImageInfo imginfo = new ArticleImageInfo();
				String article_image_url = rs.getString("article_image_url");
				if (!StringUtils.emptyString(article_image_url))
					imginfo.article_image_url = ImageHelper.getImageUrl(request, article_image_url);
				imginfo.article_image_description = rs.getString("article_image_description");
				imglist.add(imginfo);
			}
			return imglist;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	public static List<String> getArticleImagesStr(DBUtils.ConnectionCache conn, String articleID) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		String sql = "select article_image_url,article_image_description from article_image where article_id=?";
		List<String> imglist = null;
		try {
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, articleID);
			rs = pstat.executeQuery();
			imglist = new ArrayList<>();
			while (rs.next()) {
				imglist.add(rs.getString("article_image_url"));
			}
			return imglist;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	public static int deleteArticleImage(DBUtils.ConnectionCache conn, String articleID) throws SQLException {
		String sql = "delete from article_image where article_id=?";
		return DBUtils.executeUpdate(conn, sql, new Object[] { articleID });
	}
}

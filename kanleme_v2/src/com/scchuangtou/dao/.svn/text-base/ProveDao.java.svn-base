package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.GetProvesReqEntity;
import com.scchuangtou.entity.GetProvesResEntity;
import com.scchuangtou.entity.ProveReqEntity;
import com.scchuangtou.entity.ProveResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.model.ProjectInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.MD5Utils;
import com.scchuangtou.utils.StringUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;

public class ProveDao {

	public static ProveResEntity Prove(ProveReqEntity req) throws SQLException {
		ProveResEntity res = new ProveResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			int row = 0;
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}

			ProjectInfo projectInfo = ProjectDao.getProjectInfo(conn, req.help_each_id);
			if (projectInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}

			if (user.user_id.equals(projectInfo.user_id)) {
				return null;
			}
			String id = createProveId(user.user_id, req.help_each_id);
			HashMap<String, Object> datas = new HashMap<>();
			datas.put("prove_id", id);
			datas.put("help_each_id", req.help_each_id);
			datas.put("user_id", user.user_id);
			datas.put("relation", req.relation);
			datas.put("name", req.name);
			datas.put("id_num", req.id_num);
			datas.put("detail", req.detail);
			datas.put("time", System.currentTimeMillis());
			row = DBUtils.insert(conn, "insert ignore into prove", datas);
			if (row > 0) {
				res.prove_id = id;
				res.status = Config.STATUS_OK;
			} else {
				res.status = Config.STATUS_REPEAT_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	private static String createProveId(String user_id, String object_id) {
		return MD5Utils.md5((user_id + object_id).getBytes(Charset.forName(Config.CHARSET)), MD5Utils.MD5Type.MD5_16);
	}

	public static GetProvesResEntity listProves(GetProvesReqEntity req, HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();

			User user = null;
			if (!StringUtils.emptyString(req.token)) {
				user = UserDao.getUserByToken(conn, req.token);
				if (user == null) {
					GetProvesResEntity res = new GetProvesResEntity();
					res.status = Config.STATUS_TOKEN_ERROR;
					return res;
				}
			}

			ProjectInfo projectInfo = ProjectDao.getProjectInfo(conn, req.help_each_id);
			if (projectInfo == null) {
				GetProvesResEntity res = new GetProvesResEntity();
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}

			StringBuffer sb = new StringBuffer();
			sb.append(
					"select u.nickname,u.head_pic,u.growth,u.user_id,prove.detail from prove INNER JOIN userinfo u on prove.user_id=u.user_id and prove.help_each_id=? ");
			sb.append(" order by prove.time desc");
			sb.append(" LIMIT ").append(req.begin).append(",").append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.help_each_id);
			rs = pstat.executeQuery();
			GetProvesResEntity res = new GetProvesResEntity();
			res.proves = new ArrayList<>();
			GetProvesResEntity.UserInfo userInfo;
			while (rs.next()) {

				// 获取创建人信息
				userInfo = new GetProvesResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				userInfo.detail=rs.getString("detail");
				res.proves.add(userInfo);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.proves.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}
}

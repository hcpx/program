package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.entity.GetGoldInfoReqEntity;
import com.scchuangtou.entity.GetGoldInfoResEntity;
import com.scchuangtou.entity.GetMyGoldInfoReqEntity;
import com.scchuangtou.entity.GetMyGoldInfoResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserInfo;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.StringUtils;

public class GoldNotesDao {
	public static String addGoldNote(DBUtils.ConnectionCache conn, int change_type, String userid, float gold,List<String> vouchers,
			int gold_type, String desc) throws SQLException {
		String change_id = IdUtils.createId("gold_change");
		HashMap<String, Object> datas = new HashMap<String, Object>();
		datas.put("change_id", change_id);
		datas.put("change_type", change_type);
		datas.put("userid", userid);
		datas.put("change_value", gold);
		datas.put("gold_type", gold_type);
		datas.put("change_time", System.currentTimeMillis());
		if (!StringUtils.emptyString(desc))
			datas.put("change_desc", desc);
		if (vouchers != null && vouchers.size() > 0)
			datas.put("voucher_ids", JSON.toJSONString(vouchers));
		int row = DBUtils.insert(conn, "INSERT IGNORE INTO gold_change", datas);
		return row > 0 ? change_id : null;
	}

	public static GetMyGoldInfoResEntity getMyInfo(GetMyGoldInfoReqEntity req) throws SQLException {
		GetMyGoldInfoResEntity res = new GetMyGoldInfoResEntity();
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			long stime = req.start_time;
			if (stime <= 0) {
				stime = 0;
			}
			long endtime = req.end_time;
			if (stime <= stime) {
				endtime = System.currentTimeMillis();
			}
			req.begin = req.begin < 0 ? 0 : req.begin;

			StringBuffer sb = new StringBuffer();
			sb.append("SELECT change_type, change_value, change_time,gold_type,change_desc,gold_change.desc FROM gold_change");
			sb.append(" WHERE userid= ?");
			if (req.change_type != -1)
				sb.append(" AND change_type=").append(req.change_type);
			sb.append(" AND change_time >=").append(stime);
			sb.append(" AND change_time<=").append(endtime);

			sb.append(" ORDER BY change_time DESC LIMIT ?,?");
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, user.user_id);
			pstat.setObject(2, req.begin);
			pstat.setObject(3, Config.ONCE_QUERY_COUNT);
			rs = pstat.executeQuery();

			res.datas = new ArrayList<>();
			GetMyGoldInfoResEntity.Data info = null;
			while (rs.next()) {
				info = new GetMyGoldInfoResEntity.Data();
				info.change_type = rs.getInt("change_type");
				info.change_time = rs.getLong("change_time");
				info.change_value = Config.parseGold(rs.getFloat("change_value"));
				info.change_desc = rs.getString("change_desc");
				info.desc = rs.getString("desc");
				res.datas.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.datas.size() >= Config.ONCE_QUERY_COUNT;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
		return res;
	}

	public static GetGoldInfoResEntity getInfo(HttpServletRequest request, GetGoldInfoReqEntity req) throws Exception {
		GetGoldInfoResEntity res = new GetGoldInfoResEntity();
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			long stime = req.start_time;
			if (stime <= 0) {
				stime = 0;
			}
			long endtime = req.end_time;
			if (stime <= stime) {
				endtime = System.currentTimeMillis();
			}
			req.begin = req.begin < 0 ? 0 : req.begin;

			StringBuffer sb = new StringBuffer();
			sb.append(
					"SELECT gold_change.change_type, gold_change.change_value, gold_change.change_time,gold_change.gold_type,gold_change.change_desc,gold_change.desc,userinfo.user_id,userinfo.nickname,userinfo.head_pic,userinfo.growth");
			sb.append(" FROM gold_change INNER JOIN userinfo ON gold_change.user_id = userinfo.user_id");
			sb.append(" WHERE change_desc= ?");
			sb.append(" AND change_type=").append(req.change_type);
			sb.append(" AND change_time >=").append(stime);
			sb.append(" AND change_time<=").append(endtime);
			sb.append(" ORDER BY change_time DESC LIMIT ?,?");
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.change_desc);
			pstat.setObject(2, req.begin);
			pstat.setObject(3, Config.ONCE_QUERY_COUNT);
			rs = pstat.executeQuery();

			res.datas = new ArrayList<>();
			GetGoldInfoResEntity.Data info = null;
			UserInfo userInfo;
			while (rs.next()) {
				info = new GetGoldInfoResEntity.Data();
				info.change_type = rs.getInt("change_type");
				info.change_time = rs.getLong("change_time");
				info.change_value = Config.parseGold(rs.getFloat("change_value"));
				info.change_desc = rs.getString("change_desc");
				info.desc = rs.getString("desc");

				userInfo = new UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				info.user_info = userInfo;

				res.datas.add(info);
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.datas.size() >= Config.ONCE_QUERY_COUNT;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
		return res;
	}
	  
}

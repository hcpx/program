package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.AddCapitalSourceReqEntity;
import com.scchuangtou.entity.AddCapitalSourceResEntity;
import com.scchuangtou.model.CapitalSourceInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserProfile;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.IdUtils;

public class CapitalSourceDao {
	public static int CAPITALSOURCE_STATUS_NORMAL = 0; // 正常状态
	public static int CAPITALSOURCE_STATUS_BARRED = -1; // 禁止访问状态

	/**
	 * 增加资金来源
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static AddCapitalSourceResEntity addCapitalSource(AddCapitalSourceReqEntity req) throws Exception {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				AddCapitalSourceResEntity res = new AddCapitalSourceResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			String id = IdUtils.createId(req.token);
			int row = 0;
			HashMap<String, Object> datas = new HashMap<>();
			datas.put("capital_source_id", id);
			datas.put("user_id", user.user_id);
			datas.put("source_name", req.source_name);
			datas.put("source_num", req.source_num);
			datas.put("source_type", req.source_type);
			datas.put("public_private_type", req.public_private_type);
			datas.put("opening_bank_type", req.opening_bank_name);
			datas.put("create_time", System.currentTimeMillis());
			datas.put("capital_source_status", CAPITALSOURCE_STATUS_NORMAL);
			row = DBUtils.insert(conn, "INSERT INTO capital_source", datas);
			AddCapitalSourceResEntity res = new AddCapitalSourceResEntity();
			if (row > 0) {
				res.status = Config.STATUS_OK;
			} else {
				res.status = Config.STATUS_SERVER_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	/**
	 * 根据用户id获取资金来源信息
	 * 
	 * @param conn
	 * @param userID
	 * @return
	 * @throws SQLException
	 */
	public static List<UserProfile.CapitalSourceInfo> getCapitalSourcesByUserId(DBUtils.ConnectionCache conn,
			String userID) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select capital_source_id,user_id,source_name,source_num,source_type,opening_bank_type,public_private_type,create_time,capital_source_status from capital_source where user_id=? and capital_source_status=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, userID);
			pstat.setObject(2, CAPITALSOURCE_STATUS_NORMAL);
			rs = pstat.executeQuery();
			List<UserProfile.CapitalSourceInfo> capitalSources = new ArrayList<>();
			while (rs.next()) {
				UserProfile.CapitalSourceInfo capitalSourceInfo = new UserProfile.CapitalSourceInfo();
				capitalSourceInfo.source_name = rs.getString("source_name");
				capitalSourceInfo.source_num = rs.getString("source_num");
				capitalSourceInfo.source_type = rs.getInt("source_type");
				capitalSourceInfo.opening_bank_type = rs.getString("opening_bank_type");
				capitalSourceInfo.public_private_type = rs.getInt("public_private_type");
				capitalSources.add(capitalSourceInfo);
			}
			return capitalSources;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	/**
	 * 根据资金id获取资金来源详情
	 * 
	 * @param conn
	 * @param capitalSourceId
	 * @return
	 * @throws SQLException
	 */
	public static CapitalSourceInfo getCapitalSourcesInfo(DBUtils.ConnectionCache conn, String capitalSourceId)
			throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		CapitalSourceInfo capitalSourceInfo = null;
		try {
			String sql = "select capital_source_id,user_id,source_name,source_num,source_type,opening_bank_type,public_private_type,create_time,capital_source_status from capital_source where capital_source_id=? and capital_source_status=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, capitalSourceId);
			pstat.setObject(2, CAPITALSOURCE_STATUS_NORMAL);
			rs = pstat.executeQuery();
			if (rs.next()) {
				capitalSourceInfo = new CapitalSourceInfo();
				capitalSourceInfo.capital_source_id = rs.getString("capital_source_id");
				capitalSourceInfo.user_id = rs.getString("user_id");
				capitalSourceInfo.source_name = rs.getString("source_name");
				capitalSourceInfo.source_num = rs.getString("source_num");
				capitalSourceInfo.source_type = rs.getInt("source_type");
				capitalSourceInfo.opening_bank_type = rs.getString("opening_bank_type");
				capitalSourceInfo.public_private_type = rs.getInt("public_private_type");
				capitalSourceInfo.create_time = rs.getLong("create_time");
			}
			return capitalSourceInfo;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

}

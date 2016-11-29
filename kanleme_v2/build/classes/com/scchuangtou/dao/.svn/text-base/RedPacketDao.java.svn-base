package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.FinancialPublishRedPacketReqEntity;
import com.scchuangtou.entity.FinancialPublishRedPacketResEntity;
import com.scchuangtou.entity.FinancialSearchRedPacketsReqEntity;
import com.scchuangtou.entity.FinancialSearchRedPacketsResEntity;
import com.scchuangtou.entity.GetInnerRedPacketsReqEntity;
import com.scchuangtou.entity.GetInnerRedPacketsResEntity;
import com.scchuangtou.entity.GetRedPacketsReqEntity;
import com.scchuangtou.entity.GetRedPacketsResEntity;
import com.scchuangtou.entity.PublishRedPacketReqEntity;
import com.scchuangtou.entity.PublishRedPacketResEntity;
import com.scchuangtou.entity.RedPacketsManageReqEntity;
import com.scchuangtou.entity.RedPacketsManageResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.helper.RedPacketHelper;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.model.FinancialInfo;
import com.scchuangtou.model.RedPacket;
import com.scchuangtou.model.RedPacketDetailInfo;
import com.scchuangtou.model.RedPacketInfo;
import com.scchuangtou.model.TopUpOrderInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserValueInfo;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.MD5Utils;
import com.scchuangtou.utils.MathUtils;
import com.scchuangtou.utils.StringUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;

/**
 * @author glct
 *
 */
public class RedPacketDao {
	public static int RED_PACKET_STATUS_NORMAL = 0; // 红包可用
	public static int RED_PACKET_STATUS_BARRED = 1; // 红包不可用

	/**
	 * 发布红包
	 * 
	 * @param req
	 * @param picPart
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static PublishRedPacketResEntity addRedPacket(PublishRedPacketReqEntity req, MyMutiPart picPart,
			HttpServletRequest request) throws Exception {
		req.total_amount = Config.parseGold(req.total_amount);
		if(req.total_amount < 10 || req.count < 100){//最低10块，最低100个
			return null;
		}
		if(MathUtils.divide(req.total_amount, req.count) < 0.1f){//平均最低0.1元
			return null;
		}
		List<RedPacketHelper.RedPacket> redPacketList = RedPacketHelper.createRedPacket(req.total_amount,
				req.count);
		if (redPacketList == null || redPacketList.size() == 0) {
			return null;
		}
		DBUtils.ConnectionCache conn = null;
		PublishRedPacketResEntity res=null;
		try {
			conn = DBUtils.getConnection();
			res = new PublishRedPacketResEntity();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (StringUtils.emptyString(req.order_num)) {
				if (!StringUtils.emptyString(user.trade_password)) {
					if (!user.trade_password.equals(req.traders_password)) {
						res.status = Config.STATUS_PASSWORD_ERROR;
						return res;
					}
				}
			} else {
				TopUpOrderInfo topUpOrderInfo = TopUpOrderDao.getTopUpMoneyByOrderNo(conn, user.user_id, req.order_num);
				if (topUpOrderInfo==null || topUpOrderInfo.topup_money != req.total_amount) {
					return null;
				}
			}
			String imageName = null;
			String id = IdUtils.createId("red_packet");
			int row = 0;
			try {
				DBUtils.beginTransaction(conn);
				HashMap<String, Object> datas = new HashMap<>();
				datas.put("red_packet_id", id);
				datas.put("user_id", user.user_id);
				datas.put("company_name", req.company_name);
				datas.put("public_num", req.public_num);
				datas.put("count", req.count);
				datas.put("remain_count", req.count);
				datas.put("total_amount", req.total_amount);
				datas.put("warm_prompt", req.warm_prompt);
				datas.put("packet_type", Config.PacketType.PACKET_TYPE_COMPANY);
				datas.put("receive_type", req.receive_type);
				datas.put("begin_time", req.begin_time);
				if(!StringUtils.emptyString(req.command)){
					String command=MD5Utils.md5(req.command.getBytes(Charset.forName(Config.CHARSET)),
							MD5Utils.MD5Type.MD5_32);
					datas.put("command", command);
				}
				datas.put("type", req.type);
				datas.put("time", System.currentTimeMillis());
				datas.put("status", RED_PACKET_STATUS_NORMAL);
				datas.put("order_num", req.order_num);
				if(picPart != null){
					imageName = getImageName("company_head_img");
					datas.put("company_img", imageName);
				}
				row = DBUtils.insert(conn, "INSERT INTO red_packet", datas);
				// 红包批量插入子表
				if (row > 0)
					row = RedPacketDetailDao.addRedPacketDetail(conn, redPacketList, id);
				if (row > 0 && row == redPacketList.size()) {
					UserValueInfo userValueInfo = new UserValueInfo();
					userValueInfo.gold = 0 - req.total_amount;
					row = UserDao.updateUserValue(conn, user.user_id, userValueInfo, null, null,
							Config.GoldChangeType.GOLD_CHANGE_TYPE_PUBLISH_RED_PACKET, id);
					if (row <= 0) {
						res.status = Config.STATUS_GOLD_LACK;
					}
				} 
				if (row > 0 && imageName != null) {
					if (ImageHelper.upload(imageName, picPart,false)==null) {
						row = 0;
					}
				}
				if (row > 0) {
					DBUtils.commitTransaction(conn);
				}
			} catch (Exception e) {
				LogUtils.log(e);
				row = 0;
			}
			if (row > 0) {
				res.status = Config.STATUS_OK;
				res.red_packet_id = id;
				res.gold = MathUtils.sub(user.gold, req.total_amount);
				if (picPart != null)
					res.company_img = ImageHelper.getImageUrl(request, imageName);
			} else {
				DBUtils.rollbackTransaction(conn);
				if(res.status==null)
					res.status = Config.STATUS_SERVER_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	private static String getImageName(String id) {
		return new StringBuffer().append("companyhead/").append(id).append("/").append(IdUtils.createId("companyhead"))
				.toString();
	}

	/**
	 * 企业红包信息
	 * 
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetRedPacketsResEntity listRedPackets(GetRedPacketsReqEntity req, HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		GetRedPacketsResEntity res = new GetRedPacketsResEntity();
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
			StringBuffer sql = new StringBuffer(
					"select red_packet.red_packet_id,red_packet.user_id,red_packet.packet_name,red_packet.company_img,red_packet.company_name,red_packet.public_num,red_packet.count,red_packet.total_amount,red_packet.warm_prompt,red_packet.packet_type,red_packet.receive_type,red_packet.begin_time,red_packet.command,red_packet.type,red_packet.time,red_packet.status,red_packet.remain_count from red_packet ");
			sql.append(" WHERE red_packet.packet_type=").append(Config.PacketType.PACKET_TYPE_COMPANY).append(" and red_packet.status=").append(RED_PACKET_STATUS_NORMAL)
					.append(" ORDER BY time desc LIMIT ?,?");
			pstat = conn.prepareStatement(sql.toString());
			req.all_begin = req.all_begin < 0 ? 0 : req.all_begin;
			pstat.setObject(1, req.all_begin);
			if (req.all_count > 0) {
				pstat.setObject(2, req.all_count);
			} else {
				pstat.setObject(2, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			List<RedPacket> all_packets = new ArrayList<>();
			RedPacket redPacket = null;
			while (rs.next()) {
				redPacket = setRedPacket(conn,user, rs, request);
				all_packets.add(redPacket);
			}
			// 获取红包个数和剩余个数
			// packetsCount(conn, res, req);
			res.all_redPacket = all_packets;
			boolean has_more_data = true;
			if (req.all_count > 0) {
				has_more_data = res.all_redPacket.size() == req.all_count;
			} else {
				has_more_data = res.all_redPacket.size() == Config.ONCE_QUERY_COUNT;
			}
			res.all_has_more_data = has_more_data;
			res.status = Config.STATUS_OK;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
		return res;
	}

	/**
	 * 
	 * 系统红包
	 * 
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static GetInnerRedPacketsResEntity listInnerRedPackets(GetInnerRedPacketsReqEntity req,
			HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		GetInnerRedPacketsResEntity res = new GetInnerRedPacketsResEntity();
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
			// 获取时间最大的系统红包
			StringBuffer sql = new StringBuffer(
					"select red_packet.red_packet_id,red_packet.user_id,red_packet.packet_name,red_packet.company_img,red_packet.company_name,red_packet.public_num,red_packet.count,red_packet.total_amount,red_packet.warm_prompt,red_packet.packet_type,red_packet.receive_type,red_packet.begin_time,red_packet.command,red_packet.type,red_packet.time,red_packet.status,red_packet.remain_count from red_packet ");
			sql.append(" WHERE red_packet.packet_type=").append(Config.PacketType.PACKET_TYPE_INNER).append(" and red_packet.status=").append(RED_PACKET_STATUS_NORMAL)
					.append(" ORDER BY red_packet.time desc LIMIT ?,?");
			pstat = conn.prepareStatement(sql.toString());
			req.inner_begin = req.inner_begin < 0 ? 0 : req.inner_begin;
			pstat.setObject(1, req.inner_begin);
			if (req.inner_count > 0) {
				pstat.setObject(2, req.inner_count);
			} else {
				pstat.setObject(2, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			List<RedPacket> all_packets = new ArrayList<>();
			RedPacket redPacket = null;
			while (rs.next()) {
				redPacket = setRedPacket(conn,user, rs, request);
				all_packets.add(redPacket);
			}
			res.inner_redPacket = all_packets;
			boolean has_more_data = true;
			if (req.inner_count > 0) {
				has_more_data = res.inner_redPacket.size() == req.inner_count;
			} else {
				has_more_data = res.inner_redPacket.size() == Config.ONCE_QUERY_COUNT;
			}
			res.inner_has_more_data = has_more_data;
			res.status = Config.STATUS_OK;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
		return res;
	}

	/**
	 * 获取红包基本信息
	 * 
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	private static RedPacket setRedPacket(DBUtils.ConnectionCache conn,User user, ResultSet rs, HttpServletRequest request)
			throws Exception {
		RedPacket redPacket = null;
		redPacket = new RedPacket();
		String red_packet_id = rs.getString("red_packet_id");
		redPacket.red_packet_id = red_packet_id;
		if (user != null) {
			int receive_type = rs.getInt("receive_type");
			String get_id = RedPacketDetailDao.getGetId(user.user_id, red_packet_id, receive_type);
			RedPacketDetailInfo redPacketDetailInfo = RedPacketDetailDao.getRedPacketDetailInfo(conn,get_id);
			redPacket.grabed = redPacketDetailInfo == null ? false : true;
			redPacket.amount = redPacketDetailInfo == null ? 0 : redPacketDetailInfo.money;
		}
		redPacket.total_amount = rs.getFloat("total_amount");
		redPacket.count = rs.getInt("count");
		redPacket.remainSize = rs.getInt("remain_count");
		redPacket.receive_type = rs.getInt("receive_type");
		redPacket.begin_time = rs.getLong("begin_time");
		redPacket.red_packet_name = rs.getString("packet_name");
		redPacket.warm_prompt = rs.getString("warm_prompt");
		redPacket.type = rs.getInt("type");
		String company_img = rs.getString("company_img");
		if (!StringUtils.emptyString(company_img) && request!=null)
			redPacket.company_img = ImageHelper.getImageUrl(request, rs.getString("company_img"));
		redPacket.status = rs.getInt("status");
		redPacket.public_num = rs.getString("public_num");
		redPacket.company_name = rs.getString("company_name");
		return redPacket;
	}

	/**
	 * 红包总数和剩余红包数
	 * 
	 * @param conn
	 * @param res
	 * @param req
	 * @throws SQLException
	 */
	// public static void packetsCount(DBUtils.ConnectionCache conn,
	// GetRedPacketsResEntity res,
	// GetRedPacketsReqEntity req) throws SQLException {
	// PreparedStatementCache pstat = null;
	// ResultSet rs = null;
	// try {
	// StringBuffer sql = new StringBuffer(
	// "SELECT allPacket.all_allCount,notfinish.all_remainCount FROM ( SELECT
	// count(red_packet.red_packet_id) all_allCount FROM red_packet where
	// red_packet.packet_type!=")
	// .append(Config.PacketType.PACKET_TYPE_INNER);
	// sql.append(
	// " ) allPacket,(select count(red_packet_id) all_remainCount from
	// red_packet where red_packet.remain_count<>0 and red_packet.packet_type
	// !=")
	// .append(Config.PacketType.PACKET_TYPE_INNER).append(" ) notfinish");
	// pstat = conn.prepareStatement(sql.toString());
	// rs = pstat.executeQuery();
	// if (rs.next()) {
	// res.all_allCount = rs.getInt("all_allCount");
	// res.all_remainCount = rs.getInt("all_remainCount");
	// }
	// } finally {
	// DBUtils.close(rs);
	// DBUtils.close(pstat);
	// }
	// }

	/**
	 * 红包详情
	 * 
	 * @param conn
	 * @param redPacketID
	 * @return
	 * @throws SQLException
	 */
	public static RedPacketInfo getRedPacketInfo(DBUtils.ConnectionCache conn, String redPacketID) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select red_packet.red_packet_id,red_packet.user_id,red_packet.packet_name,red_packet.company_img,red_packet.company_name,red_packet.public_num,red_packet.count,red_packet.total_amount,red_packet.warm_prompt,red_packet.packet_type,red_packet.receive_type,red_packet.begin_time,red_packet.command,red_packet.type,red_packet.time,red_packet.status,red_packet.order_num,red_packet.remain_count from red_packet where red_packet_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, redPacketID);
			rs = pstat.executeQuery();
			if (rs.next()) {
				RedPacketInfo redPacketInfo = new RedPacketInfo();
				redPacketInfo.red_packet_id = rs.getString("red_packet_id");
				redPacketInfo.user_id = rs.getString("user_id");
				redPacketInfo.packet_name = rs.getString("packet_name");
				redPacketInfo.company_img = rs.getString("company_img");
				redPacketInfo.company_name = rs.getString("company_name");
				redPacketInfo.public_num = rs.getString("public_num");
				redPacketInfo.command = rs.getString("command");
				redPacketInfo.warm_prompt = rs.getString("warm_prompt");
				redPacketInfo.order_num = rs.getString("order_num");
				redPacketInfo.count = rs.getInt("count");
				redPacketInfo.packet_type = rs.getInt("packet_type");
				redPacketInfo.receive_type = rs.getInt("receive_type");
				redPacketInfo.type = rs.getInt("type");
				redPacketInfo.status = rs.getInt("status");
				redPacketInfo.total_amount = rs.getFloat("total_amount");
				redPacketInfo.begin_time = rs.getLong("begin_time");
				redPacketInfo.remain_count = rs.getInt("remain_count");
				redPacketInfo.time = rs.getLong("time");
				return redPacketInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}


	/**
	 * 更改剩余红包数
	 * 
	 * @param conn
	 * @param num
	 * @return
	 * @throws SQLException
	 */
	public static int updateCount(DBUtils.ConnectionCache conn, int num, String red_packet_id) throws SQLException {
		String sql = "UPDATE red_packet set remain_count=remain_count+? where remain_count>0 and red_packet.red_packet_id=?";
		return DBUtils.executeUpdate(conn, sql, new Object[] { num, red_packet_id });
	}
	
	/**
	 * 财务发布红包
	 * @param req
	 * @param picPart
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static FinancialPublishRedPacketResEntity financialAddRedPacket(FinancialPublishRedPacketReqEntity req) throws Exception {
		req.total_amount = Config.parseGold(req.total_amount);
		if(req.total_amount < 10 || req.count < 100){//最低10块，最低100个
			return null;
		}
		if(MathUtils.divide(req.total_amount, req.count) < 0.1f){//平均最低0.1元
			return null;
		}
		List<RedPacketHelper.RedPacket> redPacketList = RedPacketHelper.createRedPacket(req.total_amount,
				req.count);
		if (redPacketList == null || redPacketList.size() == 0) {
			return null;
		}
		String password = MD5Utils.md5(req.password.getBytes(Charset.forName(Config.CHARSET)), MD5Utils.MD5Type.MD5_32);
		DBUtils.ConnectionCache conn = null;
		FinancialPublishRedPacketResEntity res=null;
		try {
			conn = DBUtils.getConnection();
			res = new FinancialPublishRedPacketResEntity();
			FinancialInfo finanInfo = FinancialDao.getFinancialByToken(conn, req.financial_token);
			if (finanInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if(!finanInfo.financial_pass.equals(password)){
				res.status = Config.STATUS_PASSWORD_ERROR;
				return res;
			}
			String id = IdUtils.createId("red_packet");
			int row = 0;
			try {
				DBUtils.beginTransaction(conn);
				HashMap<String, Object> datas = new HashMap<>();
				datas.put("red_packet_id", id);
				datas.put("count", req.count);
				datas.put("remain_count", req.count);
				datas.put("total_amount", req.total_amount);
				datas.put("packet_type", req.packet_type);
				datas.put("begin_time", req.begin_time);
				datas.put("type", Config.PacketCommandType.PACKET_COMMAND_TYPE_NORMAL);
				datas.put("time", System.currentTimeMillis());
				datas.put("status", RED_PACKET_STATUS_NORMAL);
				row = DBUtils.insert(conn, "INSERT INTO red_packet", datas);
				// 红包批量插入子表
				if (row > 0)
					row = RedPacketDetailDao.addRedPacketDetail(conn, redPacketList, id);
				if (row > 0) {
					DBUtils.commitTransaction(conn);
				}
			} catch (Exception e) {
				LogUtils.log(e);
				row = 0;
			}
			if (row > 0) {
				res.status = Config.STATUS_OK;
				res.red_packet_id = id;
			} else {
				DBUtils.rollbackTransaction(conn);
				if(res.status==null)
					res.status = Config.STATUS_SERVER_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}
	
	/**
	 * 获取剩余红宝数最多的红包
	 * @param conn
	 * @return
	 * @throws SQLException 
	 */
	public static RedPacketInfo getMaxHealthRedPacket(DBUtils.ConnectionCache conn) throws SQLException{
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select red_packet.red_packet_id,red_packet.user_id,red_packet.packet_name,red_packet.company_img,red_packet.company_name,red_packet.public_num,red_packet.count,red_packet.total_amount,red_packet.warm_prompt,red_packet.packet_type,red_packet.receive_type,red_packet.begin_time,red_packet.command,red_packet.type,red_packet.time,red_packet.status,red_packet.order_num,red_packet.remain_count from red_packet where packet_type=? and remain_count>0 order by remain_count desc";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, Config.PacketType.PACKET_TYPE_HEALTH);
			rs = pstat.executeQuery();
			if (rs.next()) {
				RedPacketInfo redPacketInfo = new RedPacketInfo();
				redPacketInfo.red_packet_id = rs.getString("red_packet_id");
				redPacketInfo.user_id = rs.getString("user_id");
				redPacketInfo.packet_name = rs.getString("packet_name");
				redPacketInfo.company_img = rs.getString("company_img");
				redPacketInfo.company_name = rs.getString("company_name");
				redPacketInfo.public_num = rs.getString("public_num");
				redPacketInfo.command = rs.getString("command");
				redPacketInfo.warm_prompt = rs.getString("warm_prompt");
				redPacketInfo.order_num = rs.getString("order_num");
				redPacketInfo.count = rs.getInt("count");
				redPacketInfo.packet_type = rs.getInt("packet_type");
				redPacketInfo.receive_type = rs.getInt("receive_type");
				redPacketInfo.type = rs.getInt("type");
				redPacketInfo.status = rs.getInt("status");
				redPacketInfo.total_amount = rs.getFloat("total_amount");
				redPacketInfo.begin_time = rs.getLong("begin_time");
				redPacketInfo.remain_count = rs.getInt("remain_count");
				redPacketInfo.time = rs.getLong("time");
				return redPacketInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	/**
	 * 财务对红包的管理
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static FinancialSearchRedPacketsResEntity searchRedPackets(FinancialSearchRedPacketsReqEntity req) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		FinancialSearchRedPacketsResEntity res = new FinancialSearchRedPacketsResEntity();
		try {
			conn = DBUtils.getConnection();
			FinancialInfo finanInfo = FinancialDao.getFinancialByToken(conn, req.financial_token);
			if (finanInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sql = new StringBuffer(
					"select userinfo.nickname,red_packet.red_packet_id,red_packet.user_id,red_packet.packet_name,red_packet.company_img,red_packet.company_name,red_packet.public_num,red_packet.count,red_packet.total_amount,red_packet.warm_prompt,red_packet.packet_type,red_packet.receive_type,red_packet.begin_time,red_packet.command,red_packet.type,red_packet.time,red_packet.status,red_packet.remain_count from red_packet left join userinfo on userinfo.user_id=red_packet.user_id");
//			sql.append(" WHERE red_packet.packet_type!=").append(Config.PacketType.PACKET_TYPE_COMPANY);
			sql.append(" WHERE 1=1");
			if(req.begin_time !=0)
				sql.append(" and time>="+req.begin_time);
			if(req.end_time !=0)
				sql.append(" and time<="+req.end_time);
			long time=System.currentTimeMillis();
			switch (req.status) {
			case 2:
				sql.append(" and begin_time > "+time);
				break;
			case 3:
				sql.append(" and begin_time <= "+time).append(" and remain_count > 0");
				break;
			case 4:
				sql.append(" and begin_time <= "+time);
				break;
			default:
				break;
			}
			sql.append(" ORDER BY time desc LIMIT ?,?");
			pstat = conn.prepareStatement(sql.toString());
			req.begin = req.begin < 0 ? 0 : req.begin;
			pstat.setObject(1, req.begin);
			if (req.count > 0) {
				pstat.setObject(2, req.count);
			} else {
				pstat.setObject(2, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			List<RedPacket> redPackets = new ArrayList<>();
			RedPacket redPacket = null;
			while (rs.next()) {
				redPacket = setRedPacket(conn,null, rs, null);
				redPacket.time=rs.getLong("time");
				redPacket.username=rs.getString("nickname");
				redPacket.packet_type=rs.getInt("packet_type");
				redPackets.add(redPacket);
			}
			res.redPackets = redPackets;
			//获取总条数和总金额
			getSumAndCount(conn,res,req.begin_time,req.end_time,req.status);
			boolean has_more_data = true;
			if (req.count > 0) {
				has_more_data = res.redPackets.size() == req.count;
			} else {
				has_more_data = res.redPackets.size() == Config.ONCE_QUERY_COUNT;
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
	
	/**
	 * 获取发布红包总金额和总条数
	 * @param conn
	 * @param res
	 * @throws SQLException
	 */
	private static void getSumAndCount(DBUtils.ConnectionCache conn,FinancialSearchRedPacketsResEntity res,long begin_time,long end_time,int status) throws SQLException{
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			StringBuffer sql=new StringBuffer("select sum(red_packet.total_amount) sum  from red_packet where packet_type!=?");
			if(begin_time !=0)
				sql.append(" and time>="+begin_time);
			if(end_time !=0)
				sql.append(" and time<="+end_time);
			long time=System.currentTimeMillis();
			switch (status) {
			case 2:
				sql.append(" and begin_time>"+time);
				break;
			case 3:
				sql.append(" and begin_time<="+time).append(" and remain_count>0");
				break;
			case 4:
				sql.append(" and begin_time<="+time);
				break;
			default:
				break;
			}
			pstat = conn.prepareStatement(sql.toString());
			pstat.setObject(1, Config.PacketType.PACKET_TYPE_COMPANY);
			rs = pstat.executeQuery();
			if (rs.next()) {
				res.total_amount=rs.getFloat("sum");
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	/**
	 * 更新红包状态
	 * @param req
	 * @return
	 * @throws SQLException
	 */
	public static RedPacketsManageResEntity updateRedPacketStatus(RedPacketsManageReqEntity req) throws SQLException {
		RedPacketsManageResEntity res = new RedPacketsManageResEntity();
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			FinancialInfo finanInfo = FinancialDao.getFinancialByToken(conn, req.financial_token);
			if (finanInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			RedPacketInfo redPacketInfo=getRedPacketInfo(conn, req.red_id);
			if (redPacketInfo == null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			int status=RED_PACKET_STATUS_NORMAL;
			if(redPacketInfo.status==RED_PACKET_STATUS_NORMAL)
				status=RED_PACKET_STATUS_BARRED;
			StringBuffer sb=new StringBuffer("update red_packet set status=").append(status);
			sb.append(" where red_packet_id=?");
			pstat=conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.red_id);
			pstat.executeUpdate();
			res.status = Config.STATUS_OK;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
		return res;
	}
}

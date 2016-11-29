package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.entity.CarouselRedPacketsReqEntity;
import com.scchuangtou.entity.CarouselRedPacketsResEntity;
import com.scchuangtou.entity.FinishRedPacketsReqEntity;
import com.scchuangtou.entity.FinishRedPacketsResEntity;
import com.scchuangtou.entity.GrabHealthRedPacketReqEntity;
import com.scchuangtou.entity.GrabHealthRedPacketResEntity;
import com.scchuangtou.entity.GrabRedPacketInfo;
import com.scchuangtou.entity.GrabRedPacketReqEntity;
import com.scchuangtou.entity.GrabRedPacketResEntity;
import com.scchuangtou.entity.PublishedRedPacketsReqEntity;
import com.scchuangtou.entity.PublishedRedPacketsResEntity;
import com.scchuangtou.entity.RankListPacketsReqEntity;
import com.scchuangtou.entity.RankListPacketsResEntity;
import com.scchuangtou.entity.ReceiveRedPacketsReqEntity;
import com.scchuangtou.entity.ReceiveRedPacketsResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.helper.RedPacketHelper;
import com.scchuangtou.model.RedPacketDetailInfo;
import com.scchuangtou.model.RedPacketInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserValueInfo;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DESUtils;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.MD5Utils;
import com.scchuangtou.utils.MathUtils;
import com.scchuangtou.utils.StringUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.DateUtil;

public class RedPacketDetailDao {

	/**
	 * 批量插入红包表
	 * 
	 * @param conn
	 * @param list
	 * @param red_packet_id
	 * @return
	 * @throws Exception
	 */
	public static int addRedPacketDetail(DBUtils.ConnectionCache conn, List<RedPacketHelper.RedPacket> list,
			String red_packet_id) throws Exception {
		int row = 0;
		List<Map<String, Object>> dataLists = new ArrayList<>();
		long order = System.currentTimeMillis();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> datamap = new HashMap<String, Object>();
			String red_packet_detail_id = IdUtils.createId("red_packet_detail");
			order = order + 1;
			datamap.put("red_packet_detail_id", red_packet_detail_id);
			datamap.put("red_packet_id", red_packet_id);
			datamap.put("money", list.get(i).money);
			datamap.put("type", list.get(i).type);
			datamap.put("packet_order", order);
			dataLists.add(datamap);
		}
		row = DBUtils.insertBatch(conn, "INSERT INTO `red_packet_detail`", dataLists);
		return row;
	}

	/**
	 * 抢红包
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static GrabRedPacketResEntity grabRedPacket(GrabRedPacketReqEntity req) throws Exception {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				GrabRedPacketResEntity res = new GrabRedPacketResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			
			byte[] bytes = DESUtils.decryptStr(req.data, user.user_key);
			if (bytes == null || bytes.length == 0) {
				return null;
			}
			GrabRedPacketInfo data = JSON.parseObject(new String(bytes, Charset.forName(Config.CHARSET)), GrabRedPacketInfo.class);
			if (data == null || StringUtils.emptyString(data.red_packet_id)) {
				return null;
			}
			
			RedPacketInfo redPacketInfo = RedPacketDao.getRedPacketInfo(conn, data.red_packet_id);
			if (redPacketInfo == null) {
				GrabRedPacketResEntity res = new GrabRedPacketResEntity();
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			if (redPacketInfo.type == Config.PacketCommandType.PACKET_COMMAND_TYPE_COMMAND) {
				if (StringUtils.emptyString(data.command)) {
					return null;
				} else if (!data.command.equals(redPacketInfo.command)) {
					GrabRedPacketResEntity res = new GrabRedPacketResEntity();
					res.status = Config.STATUS_COMMAND_ERROR;
					return res;
				}
			}
			String get_id = "";
			if (redPacketInfo.receive_type == Config.PacketReciveType.PACKET_RECIVE_TYPE_ONCE) {
				get_id = createRedPacketDetailId(user.user_id, redPacketInfo.red_packet_id);
			} else if (redPacketInfo.receive_type == Config.PacketReciveType.PACKET_RECIVE_TYPE_DAY) {
				get_id = createDayRedPacketDetailId(user.user_id, redPacketInfo.red_packet_id,
						DateUtil.getDayTime(System.currentTimeMillis()));
			}
			RedPacketDetailInfo redPacketDetailInfo = getRedPacketDetailInfo(conn, get_id);
			if (redPacketDetailInfo != null) {
				GrabRedPacketResEntity res = new GrabRedPacketResEntity();
				res.gold=user.gold;
				res.status = Config.STATUS_RED_PACKET_UNABLE;
				return res;
			}
			int remainSize = redPacketInfo.remain_count;
			if (remainSize <= 0) {
				GrabRedPacketResEntity res = new GrabRedPacketResEntity();
				res.status = Config.STATUS_RED_PACKET_OVER;
				return res;
			}
			int row = 0;
			GrabRedPacketResEntity res = null;
			try {
				DBUtils.beginTransaction(conn);
				res = new GrabRedPacketResEntity();
				StringBuffer sql = new StringBuffer();
				sql.append(
						"update red_packet_detail set get_id=?,user_id=?,time=? where red_packet_id=? and (user_id is null or user_id ='') order by red_packet_detail.packet_order asc LIMIT 1");
				row = DBUtils.executeUpdate(conn, sql.toString(),
						new Object[] { get_id, user.user_id, System.currentTimeMillis(), redPacketInfo.red_packet_id });
				if (row > 0) {
					row = RedPacketDao.updateCount(conn, -1, data.red_packet_id);
				}
				if (row > 0) {
					redPacketDetailInfo = getRedPacketDetailInfo(conn, get_id);
					UserValueInfo userValueInfo = new UserValueInfo();
					userValueInfo.gold = redPacketDetailInfo.money;
					row = UserDao.updateUserValue(conn, user.user_id, userValueInfo, null, null,
							Config.GoldChangeType.GOLD_CHANGE_TYPE_GET_RED_PACKET, redPacketInfo.red_packet_id);
				}
				if (row > 0) {
					DBUtils.commitTransaction(conn);
				}
			} catch (Exception e) {
				LogUtils.log(e);
				row = 0;
				res.status = Config.STATUS_SERVER_ERROR;
			}
			if (row > 0) {
				res.status = Config.STATUS_OK;
				res.amount = redPacketDetailInfo.money;
				res.gold=MathUtils.sum(user.gold, redPacketDetailInfo.money);
				res.remain_count = redPacketInfo.remain_count - 1;
			} else {
				DBUtils.rollbackTransaction(conn);
				if (res.status == null)
					res.status = Config.STATUS_RED_PACKET_OVER;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}
	
	public static GrabHealthRedPacketResEntity grabHealthRedPacket(GrabHealthRedPacketReqEntity req) throws Exception {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				GrabHealthRedPacketResEntity res = new GrabHealthRedPacketResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if(user.red_packet_count==0){
				GrabHealthRedPacketResEntity res = new GrabHealthRedPacketResEntity();
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			String get_id = IdUtils.createId("health_red_packet_detail");
			RedPacketInfo redPacketInfo=RedPacketDao.getMaxHealthRedPacket(conn);
			if(redPacketInfo==null){
				GrabHealthRedPacketResEntity res = new GrabHealthRedPacketResEntity();
				res.status = Config.STATUS_RED_PACKET_OVER;
				return res;
			}
			int row = 0;
			GrabHealthRedPacketResEntity res = null;
			RedPacketDetailInfo redPacketDetailInfo=null;
			try {
				DBUtils.beginTransaction(conn);
				res = new GrabHealthRedPacketResEntity();
				StringBuffer sql = new StringBuffer();
				sql.append(
						"update red_packet_detail set get_id=?,user_id=?,time=? where red_packet_id=? and (user_id is null or user_id ='') order by red_packet_detail.packet_order asc LIMIT 1");
				row = DBUtils.executeUpdate(conn, sql.toString(),
						new Object[] { get_id, user.user_id, System.currentTimeMillis(), redPacketInfo.red_packet_id });
				if (row > 0) {
					row = RedPacketDao.updateCount(conn, -1, redPacketInfo.red_packet_id);
				}
				if (row > 0) {
					redPacketDetailInfo = getRedPacketDetailInfo(conn, get_id);
					UserValueInfo userValueInfo = new UserValueInfo();
					userValueInfo.gold = redPacketDetailInfo.money;
					row = UserDao.updateUserValue(conn, user.user_id, userValueInfo, null, null,
							Config.GoldChangeType.GOLD_CHANGE_TYPE_GET_RED_PACKET, redPacketInfo.red_packet_id);
				}
				if (row > 0) {
					row=UserDao.updateRedPacketCount(conn, user.user_id, -1);
				}
				if(row<=0)
					res.status=Config.STATUS_PERMISSION_ERROR;
				if (row > 0) {
					DBUtils.commitTransaction(conn);
				}
			} catch (Exception e) {
				LogUtils.log(e);
				row = 0;
				res.status = Config.STATUS_SERVER_ERROR;
			}
			if (row > 0) {
				res.status = Config.STATUS_OK;
				res.amount = redPacketDetailInfo.money;
				res.gold=MathUtils.sum(user.gold, redPacketDetailInfo.money);
			} else {
				DBUtils.rollbackTransaction(conn);
				if (res.status == null)
					res.status = Config.STATUS_RED_PACKET_OVER;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	/**
	 * 只能抢一次的get_id
	 * 
	 * @param user_id
	 * @param object_id
	 * @return
	 */
	public static String createRedPacketDetailId(String user_id, String object_id) {
		return MD5Utils.md5((user_id + object_id).getBytes(Charset.forName(Config.CHARSET)), MD5Utils.MD5Type.MD5_16);
	}

	/**
	 * 每日只能抢一次的get_id
	 * 
	 * @param user_id
	 * @param object_id
	 * @param day
	 * @return
	 */
	public static String createDayRedPacketDetailId(String user_id, String object_id, long day) {
		return MD5Utils.md5((user_id + object_id + day).getBytes(Charset.forName(Config.CHARSET)),
				MD5Utils.MD5Type.MD5_16);
	}

	public static String getGetId(String user_id, String red_packet_id, int receive_type) {
		String get_id = "";
		if (receive_type == Config.PacketReciveType.PACKET_RECIVE_TYPE_ONCE) {
			get_id = createRedPacketDetailId(user_id, red_packet_id);
		} else if (receive_type == Config.PacketReciveType.PACKET_RECIVE_TYPE_DAY) {
			get_id = createDayRedPacketDetailId(user_id, red_packet_id,
					DateUtil.getDayTime(System.currentTimeMillis()));
		}
		return get_id;
	}

	/**
	 * 抢红包明细
	 * 
	 * @param conn
	 * @param getID
	 * @return
	 * @throws SQLException
	 */
	public static RedPacketDetailInfo getRedPacketDetailInfo(DBUtils.ConnectionCache conn, String getID)
			throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select red_packet_detail.red_packet_detail_id,red_packet_detail.red_packet_id,red_packet_detail.money,red_packet_detail.user_id,red_packet_detail.get_id,red_packet_detail.time,red_packet_detail.type from red_packet_detail where get_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, getID);
			rs = pstat.executeQuery();
			if (rs.next()) {
				RedPacketDetailInfo redPacketInfo = new RedPacketDetailInfo();
				redPacketInfo.red_packet_id = rs.getString("red_packet_id");
				redPacketInfo.user_id = rs.getString("user_id");
				redPacketInfo.red_packet_detail_id = rs.getString("red_packet_detail_id");
				redPacketInfo.money = rs.getFloat("money");
				redPacketInfo.user_id = rs.getString("user_id");
				redPacketInfo.get_id = rs.getString("get_id");
				redPacketInfo.time = rs.getLong("time");
				redPacketInfo.type = rs.getInt("type");
				return redPacketInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	/**
	 * 排行榜
	 * 
	 * @param req
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static RankListPacketsResEntity rankRedPacketDetail(RankListPacketsReqEntity req, HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			RedPacketInfo redPacketInfo = RedPacketDao.getRedPacketInfo(conn, req.red_packet_id);
			if (redPacketInfo == null) {
				RankListPacketsResEntity res = new RankListPacketsResEntity();
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			if (redPacketInfo.remain_count != 0) {
				return null;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					"select  red_packet_detail.red_packet_detail_id,red_packet_detail.red_packet_id,red_packet_detail.money,red_packet_detail.user_id,red_packet_detail.get_id,red_packet_detail.packet_order,red_packet_detail.type,red_packet_detail.time,userinfo.user_id,userinfo.nickname,userinfo.head_pic,userinfo.growth from red_packet_detail ");
			sb.append(" LEFT JOIN userinfo ON red_packet_detail.user_id =userinfo.user_id ");
			sb.append(" WHERE red_packet_detail.red_packet_id=?");
			sb.append(" and red_packet_detail.type in (").append(RedPacketHelper.RedPacket.TYPE_FAST + ",")
					.append(RedPacketHelper.RedPacket.TYPE_GOOD + ",").append(RedPacketHelper.RedPacket.TYPE_SLOWEST)
					.append(")");
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.red_packet_id);
			rs = pstat.executeQuery();
			RankListPacketsResEntity res = new RankListPacketsResEntity();
			res.detail_list = new ArrayList<>();
			RankListPacketsResEntity.Detail detail;
			RankListPacketsResEntity.UserInfo userInfo;
			while (rs.next()) {
				detail = new RankListPacketsResEntity.Detail();
				detail.money = rs.getFloat("money");
				detail.time = rs.getLong("time");
				detail.type = rs.getInt("type");
				// 得到红包人信息
				userInfo = new RankListPacketsResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				detail.userInfo = userInfo;
				res.detail_list.add(detail);
			}
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	public static RedPacketDetailInfo getRedPacketDetailInfo(String getID) throws SQLException {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			String sql = "select red_packet_detail.red_packet_detail_id,red_packet_detail.red_packet_id,red_packet_detail.money,red_packet_detail.user_id,red_packet_detail.get_id,red_packet_detail.time from red_packet_detail where get_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, getID);
			rs = pstat.executeQuery();
			if (rs.next()) {
				RedPacketDetailInfo redPacketInfo = new RedPacketDetailInfo();
				redPacketInfo.red_packet_id = rs.getString("red_packet_id");
				redPacketInfo.user_id = rs.getString("user_id");
				redPacketInfo.red_packet_detail_id = rs.getString("red_packet_detail_id");
				redPacketInfo.money = rs.getFloat("money");
				redPacketInfo.user_id = rs.getString("user_id");
				redPacketInfo.get_id = rs.getString("get_id");
				redPacketInfo.time = rs.getLong("time");
				return redPacketInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	/**
	 * 获取所有抢红包的人的数据
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static FinishRedPacketsResEntity listRedPacketDetail(FinishRedPacketsReqEntity req,
			HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				FinishRedPacketsResEntity res = new FinishRedPacketsResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			RedPacketInfo redPacketInfo = RedPacketDao.getRedPacketInfo(conn, req.red_packet_id);
			if (redPacketInfo == null) {
				FinishRedPacketsResEntity res = new FinishRedPacketsResEntity();
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					"select red_packet_detail.red_packet_detail_id,red_packet_detail.red_packet_id,red_packet_detail.money,red_packet_detail.user_id,red_packet_detail.get_id,red_packet_detail.packet_order,red_packet_detail.type,red_packet_detail.time,userinfo.user_id,userinfo.nickname,userinfo.head_pic,userinfo.growth from red_packet_detail ");
			sb.append(" INNER JOIN userinfo ON red_packet_detail.user_id =userinfo.user_id ");
			sb.append(" WHERE red_packet_detail.red_packet_id=?");
			sb.append(" ORDER BY red_packet_detail.money desc LIMIT ").append(req.begin).append(",")
					.append(Config.ONCE_QUERY_COUNT);
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.red_packet_id);
			rs = pstat.executeQuery();
			FinishRedPacketsResEntity res = new FinishRedPacketsResEntity();
			res.detail_list = new ArrayList<>();
			FinishRedPacketsResEntity.Detail detail;
			FinishRedPacketsResEntity.UserInfo userInfo;
			while (rs.next()) {
				// 获取基本信息
				detail = new FinishRedPacketsResEntity.Detail();
				detail.money = rs.getFloat("money");
				detail.time = rs.getLong("time");
				// 得到红包人信息
				userInfo = new FinishRedPacketsResEntity.UserInfo();
				userInfo.user_id = rs.getString("user_id");
				userInfo.nickname = rs.getString("nickname");
				userInfo.level = Config.getLevel(rs.getInt("growth"));
				userInfo.head_pic = ImageHelper.getImageUrl(request, rs.getString("head_pic"));
				detail.user_info = userInfo;
				res.detail_list.add(detail);
			}
			if (redPacketInfo.remain_count == 0) {
				res.cost_time = packetsCostTime(conn, req.red_packet_id);
			} else {
				res.remainSize = redPacketInfo.remain_count;
			}
			res.status = Config.STATUS_OK;
			res.has_more_data = res.detail_list.size() >= Config.ONCE_QUERY_COUNT;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	/**
	 * 只有抢完的情况下获取消耗时间
	 * 
	 * @param red_packet_id
	 * @return
	 * @throws SQLException
	 */
	public static long packetsCostTime(DBUtils.ConnectionCache conn, String red_packet_id) throws SQLException {
		long max = 0;
		long min = 0;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			StringBuffer sql = new StringBuffer(
					"select time from red_packet_detail where red_packet_id=? and type in (?,?) order by type desc");
			pstat = conn.prepareStatement(sql.toString());
			pstat.setObject(1, red_packet_id);
			pstat.setObject(2, RedPacketHelper.RedPacket.TYPE_SLOWEST);
			pstat.setObject(3, RedPacketHelper.RedPacket.TYPE_FAST);
			rs = pstat.executeQuery();
			if (rs.next()) {
				max = rs.getLong("time");
			}
			if (rs.next()) {
				min = rs.getLong("time");
			}
			return max - min;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	/**
	 * 轮播信息
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static CarouselRedPacketsResEntity listCarouselRedPackets(CarouselRedPacketsReqEntity req) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			StringBuffer sb = new StringBuffer();
			sb.append(
					"select userinfo.nickname,red_packet_detail.money,red_packet_detail.time from red_packet_detail INNER JOIN userinfo on userinfo.user_id=red_packet_detail.user_id where red_packet_detail.get_id is not null ORDER BY time desc LIMIT 0,? ");
			pstat = conn.prepareStatement(sb.toString());
			if (req.grab_count > 0) {
				pstat.setObject(1, req.grab_count);
			} else {
				pstat.setObject(1, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			CarouselRedPacketsResEntity res = new CarouselRedPacketsResEntity();
			List<CarouselRedPacketsResEntity.Info> grabList = new ArrayList<>();
			List<CarouselRedPacketsResEntity.Info> publicshList = null;
			CarouselRedPacketsResEntity.Info info;
			while (rs.next()) {
				info = new CarouselRedPacketsResEntity.Info();
				info.name = rs.getString("nickname");
				info.money = rs.getFloat("money");
				info.time = rs.getLong("time");
				info.type = Config.CarouselRedPacketType.CAROUSEL_RED_PACKET_TYPE_GRAD;
				grabList.add(info);
			}
			boolean has_more_data = true;
			if (req.grab_count > 0) {
				has_more_data = grabList.size() == req.grab_count;
			} else {
				has_more_data = grabList.size() == Config.ONCE_QUERY_COUNT;
			}
			res.grab_has_more_data = has_more_data;
			publicshList = publicshList(conn, res, req);
			if (publicshList != null && publicshList.size() != 0)
				grabList.addAll(publicshList);
			Collections.shuffle(grabList);
			// 按时间排序
			// Collections.sort(grabList, new
			// Comparator<CarouselRedPacketsResEntity.Info>(){
			// @Override
			// public int compare(CarouselRedPacketsResEntity.Info info1,
			// CarouselRedPacketsResEntity.Info info2) {
			// return info2.time.compareTo(info1.time);
			// }
			// });
			res.infomation = grabList;
			res.status = Config.STATUS_OK;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	public static List<CarouselRedPacketsResEntity.Info> publicshList(DBUtils.ConnectionCache conn,
			CarouselRedPacketsResEntity res, CarouselRedPacketsReqEntity req) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		List<CarouselRedPacketsResEntity.Info> publicshList = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select company_name,total_amount,time from red_packet ORDER BY time desc LIMIT 0,? ");
			pstat = conn.prepareStatement(sb.toString());
			if (req.publish_count > 0) {
				pstat.setObject(1, req.publish_count);
			} else {
				pstat.setObject(1, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			publicshList = new ArrayList<>();
			CarouselRedPacketsResEntity.Info info;
			while (rs.next()) {
				info = new CarouselRedPacketsResEntity.Info();
				info.name = rs.getString("company_name");
				info.money = rs.getFloat("total_amount");
				info.time = rs.getLong("time");
				info.type = Config.CarouselRedPacketType.CAROUSEL_RED_PACKET_TYPE_PUBLISH;
				publicshList.add(info);
			}
			boolean has_more_data = true;
			if (req.publish_count > 0) {
				has_more_data = publicshList.size() == req.publish_count;
			} else {
				has_more_data = publicshList.size() == Config.ONCE_QUERY_COUNT;
			}
			res.grab_has_more_data = has_more_data;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
		return publicshList;
	}

	/**
	 * 我获取到的红包
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static ReceiveRedPacketsResEntity listReceiveRedPackets(ReceiveRedPacketsReqEntity req) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				ReceiveRedPacketsResEntity res = new ReceiveRedPacketsResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					"select red_packet_detail.type,red_packet.red_packet_id,red_packet_detail.time,red_packet_detail.money,red_packet.packet_type from red_packet_detail INNER JOIN red_packet on red_packet.red_packet_id=red_packet_detail.red_packet_id where red_packet_detail.user_id=? ");
			sb.append(" ORDER BY time desc LIMIT ?,?");
			req.begin = req.begin < 0 ? 0 : req.begin;
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, user.user_id);
			pstat.setObject(2, req.begin);
			if (req.count > 0) {
				pstat.setObject(3, req.count);
			} else {
				pstat.setObject(3, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			ReceiveRedPacketsResEntity res = new ReceiveRedPacketsResEntity();
			res.detail_list = new ArrayList<>();
			ReceiveRedPacketsResEntity.Detail detail;
			while (rs.next()) {
				// 获取基本信息
				detail = new ReceiveRedPacketsResEntity.Detail();
				detail.red_packet_id = rs.getString("red_packet_id");
				detail.isMax = rs.getInt("type")==RedPacketHelper.RedPacket.TYPE_GOOD?true:false;
				detail.money = rs.getFloat("money");
				detail.time = rs.getLong("time");
				detail.type = rs.getInt("packet_type");
				res.detail_list.add(detail);
			}
			getReceiveMaxAndCount(conn,res,user);
			res.status = Config.STATUS_OK;
			boolean has_more_data=true;
			if (req.count > 0) {
				has_more_data=res.detail_list.size()==req.count;
			} else {
				has_more_data=res.detail_list.size()==Config.ONCE_QUERY_COUNT;
			}
			res.has_more_data = has_more_data;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}
	
	/**
	 * 获取到红包的个数和最大值
	 * @param conn
	 * @param res
	 * @param user
	 * @throws SQLException
	 */
	private static void getReceiveMaxAndCount(DBUtils.ConnectionCache conn,ReceiveRedPacketsResEntity res,User user) throws SQLException{
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			StringBuffer sql = new StringBuffer(
					"select sum(red_packet_detail.money) sum,count(red_packet_detail.red_packet_detail_id) count from red_packet_detail  where red_packet_detail.user_id=?");
			pstat = conn.prepareStatement(sql.toString());
			pstat.setObject(1, user.user_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				res.count = rs.getInt("count");
				res.sum = rs.getFloat("sum");
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	/**
	 * 我发出的红包
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static PublishedRedPacketsResEntity listPublishedRedPackets(PublishedRedPacketsReqEntity req) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				PublishedRedPacketsResEntity res = new PublishedRedPacketsResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sb = new StringBuffer();
			sb.append(
					"select red_packet.count,red_packet.total_amount,red_packet.packet_type,red_packet.time from red_packet where red_packet.user_id=? ");
			sb.append(" ORDER BY time desc LIMIT ?,?");
			req.begin = req.begin < 0 ? 0 : req.begin;
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, user.user_id);
			pstat.setObject(2, req.begin);
			if (req.count > 0) {
				pstat.setObject(3, req.count);
			} else {
				pstat.setObject(3, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			PublishedRedPacketsResEntity res = new PublishedRedPacketsResEntity();
			res.detail_list = new ArrayList<>();
			PublishedRedPacketsResEntity.Detail detail;
			while (rs.next()) {
				// 获取基本信息
				detail = new PublishedRedPacketsResEntity.Detail();
				detail.money = rs.getFloat("total_amount");
				detail.time = rs.getLong("time");
				detail.count = rs.getInt("count");
				detail.type = rs.getInt("packet_type");
				res.detail_list.add(detail);
			}
			getPublishedRedSumAndCount(conn,res,user);
			res.status = Config.STATUS_OK;
			boolean has_more_data=true;
			if (req.count > 0) {
				has_more_data=res.detail_list.size()==req.count;
			} else {
				has_more_data=res.detail_list.size()==Config.ONCE_QUERY_COUNT;
			}
			res.has_more_data = has_more_data;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}
	
	/**
	 * 获取发布红包的个数和总钱数
	 * @param conn
	 * @param res
	 * @param user
	 * @throws SQLException
	 */
	private static void getPublishedRedSumAndCount(DBUtils.ConnectionCache conn,PublishedRedPacketsResEntity res,User user) throws SQLException{
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			StringBuffer sql = new StringBuffer(
					"select sum(red_packet.total_amount) sum,count(red_packet.red_packet_id) count from red_packet where red_packet.user_id=?");
			pstat = conn.prepareStatement(sql.toString());
			pstat.setObject(1, user.user_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				res.count = rs.getInt("count");
				res.sum = rs.getFloat("sum");
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
}

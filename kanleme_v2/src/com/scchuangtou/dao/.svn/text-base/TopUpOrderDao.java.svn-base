package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.scchuangtou.config.Config;
import com.scchuangtou.config.MessageConfig;
import com.scchuangtou.entity.CheckTopUpReqEntity;
import com.scchuangtou.entity.CheckTopUpResEntity;
import com.scchuangtou.entity.GetGraphDataResEntity;
import com.scchuangtou.entity.TopUpReqEntity;
import com.scchuangtou.entity.TopUpResEntity;
import com.scchuangtou.model.ArticleInfo;
import com.scchuangtou.model.HomepageGraphInfo;
import com.scchuangtou.model.TopUpOrderInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.model.UserMessageInfo;
import com.scchuangtou.model.UserValueInfo;
import com.scchuangtou.task.MessageTask;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.MathUtils;
import com.scchuangtou.utils.StringUtils;

/**
 * Created by SYT on 2016/3/14.
 */
public class TopUpOrderDao {

	public static TopUpResEntity pay(TopUpReqEntity req) throws SQLException {
		TopUpResEntity res = new TopUpResEntity();
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();

			HashMap<String, Object> datas = new HashMap<>();
			if (!StringUtils.emptyString(req.token)) {
				User user = UserDao.getUserByToken(conn, req.token);
				if (user == null) {
					res.status = Config.STATUS_TOKEN_ERROR;
					return res;
				}
				datas.put("user_id", user.user_id);
				datas.put("object_user_id", user.user_id);
			}
			datas.put("top_up_purpose", req.top_up_purpose);
			if (req.top_up_purpose == Config.TopUpPurpose.TOP_UP_PURPOSE_ARTICLE_REWARD) {
				ArticleInfo info = ArticleDao.getArticleInfo(conn, req.request_data);
				if (info == null) {
					res.status = Config.STATUS_NOT_EXITS;
					return res;
				}
				datas.put("object_user_id", info.user_id);
				datas.put("source_id", info.article_id);
				datas.put("source_name", info.article_title);
			}
			float reward_gold = 0;
			String orderNo = IdUtils.createId("top_up");

			datas.put("reward_gold", reward_gold);
			datas.put("order_no", orderNo);
			datas.put("topup_money", req.price);
			datas.put("status", Config.TopUpOrderStatusType.WAIT_BUYER_PAY);
			datas.put("start_time", System.currentTimeMillis());
			datas.put("type", req.type);

			int row = DBUtils.insert(conn, "INSERT IGNORE INTO topup_order", datas);
			if (row > 0) {
				res.status = Config.STATUS_OK;
				res.order_number = orderNo;
				res.reward_gold = MathUtils.sum(req.price, reward_gold);
			} else {
				res.status = Config.STATUS_SERVER_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	public static TopUpOrderInfo getOrderByOrderNo(String orderNo) throws SQLException {
		if (orderNo == null) {
			return null;
		}
		TopUpOrderInfo orderEntity = null;
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();

			String sql = "SELECT topup_order.order_no,topup_order.top_up_purpose,topup_order.topup_money,topup_order.status,topup_order.start_time,topup_order.type,topup_order.object_user_id,topup_order.user_id,topup_order.end_time,topup_order.trade_no,topup_order.sign,topup_order.reward_gold,topup_order.source_id,topup_order.source_name,userinfo.os FROM topup_order INNER JOIN userinfo ON topup_order.object_user_id = userinfo.user_id WHERE order_no=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, orderNo);
			rs = pstat.executeQuery();
			if (rs.next()) {
				orderEntity = new TopUpOrderInfo();

				orderEntity.order_no = rs.getString("order_no");
				orderEntity.top_up_purpose = rs.getInt("top_up_purpose");
				orderEntity.user_id = rs.getString("user_id");
				orderEntity.topup_money = rs.getFloat("topup_money");
				orderEntity.status = rs.getInt("status");
				orderEntity.start_time = rs.getLong("start_time");
				orderEntity.type = rs.getInt("type");
				orderEntity.end_time = rs.getLong("end_time");
				orderEntity.trade_no = rs.getString("trade_no");
				orderEntity.sign = rs.getString("sign");
				orderEntity.reward_gold = rs.getFloat("reward_gold");
				orderEntity.object_user_id = rs.getString("object_user_id");
				orderEntity.source_id = rs.getString("source_id");
				orderEntity.source_name = rs.getString("source_name");
				orderEntity.obj_user_os = rs.getInt("os");
			}
			return orderEntity;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

	public static TopUpOrderInfo getTopUpMoneyByOrderNo(DBUtils.ConnectionCache conn, String user_id, String orderNo)
			throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		TopUpOrderInfo orderEntity = null;
		try {
			String sql = "select topup_money,reward_gold,type from topup_order where order_no=? and user_id = ? and status = ?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, orderNo);
			pstat.setObject(2, user_id);
			pstat.setObject(3, Config.TopUpOrderStatusType.TRADE_FINISHED);
			rs = pstat.executeQuery();
			if (rs.next()) {
				orderEntity = new TopUpOrderInfo();
				orderEntity.topup_money=rs.getFloat("topup_money");
				orderEntity.type=rs.getInt("type");
			}
			return orderEntity;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	public static int payFaild(String orderNo) throws SQLException {
		String sql = "update `topup_order` set status=? where order_no=? and status<>?";

		DBUtils.ConnectionCache conn = null;
		try{
			conn = DBUtils.getConnection();
			return DBUtils.executeUpdate(conn,sql, new Object[] { Config.TopUpOrderStatusType.TRADE_CLOSED, orderNo,
					Config.TopUpOrderStatusType.TRADE_FINISHED });
		}finally{
			DBUtils.close(conn);
		}
	}

	private static MessageTask.MessageParam notify(DBUtils.ConnectionCache conn, TopUpOrderInfo info)
			throws SQLException {
		UserMessageInfo mUserMessageInfo = new UserMessageInfo();
		mUserMessageInfo.message_type = Config.MessageType.MESSAGE_TYPE_REWARD;
		mUserMessageInfo.message_user = info.object_user_id;
		mUserMessageInfo.action_user = info.user_id;
		mUserMessageInfo.message_content = String.valueOf(info.topup_money);

		if (info.top_up_purpose == Config.TopUpPurpose.TOP_UP_PURPOSE_ARTICLE_REWARD) {
			mUserMessageInfo.source_type = Config.MessageSourceType.MESSAGE_SOURCE_TYPE_ARTICLE;
		}
		mUserMessageInfo.source_id = info.source_id;
		mUserMessageInfo.source_content = info.source_name;

		String messageId = UserMessageDao.addUserMessage(conn, mUserMessageInfo);
		if (!StringUtils.emptyString(messageId)) {
			MessageTask.MessageOs os = MessageTask.getOs(info.obj_user_os);
			MessageTask.MessageParam mMessageParam = new MessageTask.MessageParam(os, mUserMessageInfo.message_type,
					messageId);
			mMessageParam.addAlias(mUserMessageInfo.message_user);
			mMessageParam.title = MessageConfig.TITLE;
			mMessageParam.description = MessageConfig.getMessageDescription(mUserMessageInfo, null);
			return mMessageParam;
		}
		return null;
	}

	public static boolean paySuccess(TopUpOrderInfo orderEntity, String trade_no) throws SQLException {
		trade_no = trade_no == null ? "" : trade_no;
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			DBUtils.beginTransaction(conn);
			int row = 0;
			try {
				row = DBUtils
						.executeUpdate(conn,
								"update `topup_order` set trade_no =?, end_time=?, status=? where order_no=? and status=?",
								new Object[] { trade_no, System.currentTimeMillis(),
										Config.TopUpOrderStatusType.TRADE_FINISHED, orderEntity.order_no,
										Config.TopUpOrderStatusType.WAIT_BUYER_PAY });

				if (row > 0) {
					int mGoldChangeType = Config.GoldChangeType.GOLD_CHANGE_TYPE_TOPUP;
					if (orderEntity.top_up_purpose == Config.TopUpPurpose.TOP_UP_PURPOSE_ARTICLE_REWARD) {
						mGoldChangeType = Config.GoldChangeType.GOLD_CHANGE_TYPE_REWARD;
					}
					UserValueInfo userValueInfo = new UserValueInfo();
					userValueInfo.gold = MathUtils.sum(orderEntity.topup_money, orderEntity.reward_gold);
					row = UserDao.updateUserValue(conn, orderEntity.object_user_id, userValueInfo, null, null,
							mGoldChangeType, orderEntity.order_no);
				}
				MessageTask.MessageParam mMessageParam = null;
				if (row > 0 && orderEntity.top_up_purpose == Config.TopUpPurpose.TOP_UP_PURPOSE_ARTICLE_REWARD) {
					mMessageParam = notify(conn, orderEntity);
					if (mMessageParam == null) {
						row = 0;
					}
				}
				if (row > 0) {
					DBUtils.commitTransaction(conn);
					if (mMessageParam != null)
						MessageTask.addMessage(mMessageParam);
				}
			} catch (Exception e) {
				LogUtils.log(e);
				row = 0;
			}
			if (row > 0) {
				return true;
			} else {
				DBUtils.rollbackTransaction(conn);
				return false;
			}
		} finally {
			DBUtils.close(conn);
		}
	}

	public static CheckTopUpResEntity checkTopUp(CheckTopUpReqEntity req) throws SQLException {
		CheckTopUpResEntity res = new CheckTopUpResEntity();

		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;

		try {

			conn = DBUtils.getConnection();

			String sql = "select `topup_order`.status,`topup_order`.topup_money, `topup_order`.reward_gold,userinfo.gold from `topup_order` inner join `userinfo` on `topup_order`.user_id = `userinfo`.user_id"
					+ " where `topup_order`.order_no=?  and `userinfo`.token = ?";

			pstat = conn.prepareStatement(sql);

			pstat.setObject(1, req.order_number);
			pstat.setObject(2, req.token);

			rs = pstat.executeQuery();

			if (rs.next()) {
				int status = rs.getInt("status");
				if (status == Config.TopUpOrderStatusType.TRADE_FINISHED) {
					res.gold = rs.getFloat("gold");
					res.top_up_gold = MathUtils.sum(rs.getFloat("topup_money"), rs.getFloat("reward_gold"));

					res.status = Config.STATUS_OK;
				} else if (status == Config.TopUpOrderStatusType.TRADE_CLOSED) {
					res.status = Config.STATUS_CLOSE_TOPUP;
				} else {
					res.status = Config.STATUS_TOPUP_NOT_SUCCESS;
				}
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
	 * 充值记录
	 * @param conn
	 * @param begin_time
	 * @param end_time
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	public static List<HomepageGraphInfo> getPayRecord(DBUtils.ConnectionCache conn,long begin_time,long end_time)
			throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		HomepageGraphInfo info=null;
		List<HomepageGraphInfo> infolist=null;
		try {
			StringBuffer sb=new StringBuffer("SELECT sum(topup_money) sum,FROM_UNIXTIME(start_time/1000, '%Y-%m-%d') time FROM topup_order");
			sb.append(" WHERE status=").append(Config.TopUpOrderStatusType.TRADE_FINISHED);
			if(begin_time!=0)
				sb.append(" and start_time >=").append(begin_time);
			if(end_time!=0)
				sb.append(" and start_time <=").append(end_time);
			sb.append(" GROUP BY FROM_UNIXTIME(start_time/1000, '%Y-%m-%d') order by time desc");
			pstat = conn.prepareStatement(sb.toString());
			rs = pstat.executeQuery();
			infolist=new ArrayList<HomepageGraphInfo>();
			while (rs.next()) {
				info=new HomepageGraphInfo();
				info.date_str=rs.getString("time");
				info.gold=rs.getFloat("sum");
				infolist.add(info);
			}
			return infolist;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
	
	/**
	 * 获取充值总金额
	 * @param conn
	 * @param res
	 * @throws SQLException
	 */
	public static void getPaySum(DBUtils.ConnectionCache conn,GetGraphDataResEntity res)
			throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			StringBuffer sb=new StringBuffer("select sum(topup_money) sum from topup_order");
			sb.append(" WHERE status=").append(Config.TopUpOrderStatusType.TRADE_FINISHED);
			pstat = conn.prepareStatement(sb.toString());
			rs = pstat.executeQuery();
			if (rs.next()) {
				res.pay_gold=rs.getFloat("sum");
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}
}

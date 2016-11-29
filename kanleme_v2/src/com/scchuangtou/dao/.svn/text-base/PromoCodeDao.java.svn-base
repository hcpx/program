package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.scchuangtou.config.Config;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.FinancialAddPromoCodesReqEntity;
import com.scchuangtou.entity.FinancialAddPromoCodesResEntity;
import com.scchuangtou.entity.FinancialSearchPromoCodesReqEntity;
import com.scchuangtou.entity.FinancialSearchPromoCodesResEntity;
import com.scchuangtou.entity.GetPromoCodeReqEntity;
import com.scchuangtou.entity.GetPromoCodeResEntity;
import com.scchuangtou.helper.RedPacketHelper;
import com.scchuangtou.model.FinancialInfo;
import com.scchuangtou.model.PromoCodeInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.MD5Utils;
import com.scchuangtou.utils.MathUtils;
import com.scchuangtou.utils.StringUtils;

/**
 * 优惠码
 * 
 * @author lgh
 *
 */
public class PromoCodeDao {

	public static String addConversionCode(DBUtils.ConnectionCache conn, int type, float money, long start_time,
			long end_time) throws SQLException {
		String code = String.valueOf(type) + IdUtils.createId("").toUpperCase();
		HashMap<String, Object> datas = new HashMap<String, Object>();
		datas.put("promo_code", code);
		datas.put("type", type);
		datas.put("money", money);
		datas.put("start_time", start_time);
		datas.put("end_time", end_time);
		datas.put("time", System.currentTimeMillis());
		int row = DBUtils.insert(conn, "INSERT IGNORE INTO promo_code", datas);
		if (row > 0) {
			return code;
		} else {
			return addConversionCode(conn, type, money, start_time, end_time);
		}
	}

	public static PromoCodeInfo getPromoCodeInfo(DBUtils.ConnectionCache conn, String promoCode) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT promo_code,type,money,start_time,end_time,user_id,time FROM promo_code WHERE promo_code=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, promoCode);
			rs = pstat.executeQuery();
			if (rs.next()) {
				PromoCodeInfo mPromoCodeInfo = new PromoCodeInfo();
				mPromoCodeInfo.promo_code = rs.getString("promo_code");
				mPromoCodeInfo.type = rs.getInt("type");
				mPromoCodeInfo.money = rs.getFloat("money");
				mPromoCodeInfo.start_time = rs.getLong("start_time");
				mPromoCodeInfo.end_time = rs.getLong("end_time");
				mPromoCodeInfo.user_id = rs.getString("user_id");
				mPromoCodeInfo.time = rs.getLong("time");
				return mPromoCodeInfo;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	public static BaseResEntity getPromoCode(GetPromoCodeReqEntity reqEntity) throws SQLException {
		GetPromoCodeResEntity res = new GetPromoCodeResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			User user = UserDao.getUserByToken(conn, reqEntity.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			PromoCodeInfo mPromoCodeInfo = getPromoCodeInfo(conn, reqEntity.promo_code);
			if (mPromoCodeInfo == null || !StringUtils.emptyString(mPromoCodeInfo.user_id)) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			long time = System.currentTimeMillis();
			if (mPromoCodeInfo.start_time > time || (mPromoCodeInfo.end_time != 0 && mPromoCodeInfo.end_time < time)) {
				res.status = Config.STATUS_OVERDUE;
				return res;
			}
			String id = null;
			int row = 0;
			DBUtils.beginTransaction(conn);
			try {
				String sql = "update promo_code set user_id=? where promo_code=? and (user_id is null or user_id='')";
				row = DBUtils.executeUpdate(conn, sql, new Object[] { user.user_id, reqEntity.promo_code });
				if (row > 0) {
					id = UserVouchersDao.addVoucher(conn, user.user_id, mPromoCodeInfo.money, mPromoCodeInfo.start_time,
							mPromoCodeInfo.end_time);
					if (StringUtils.emptyString(id)) {
						row = 0;
					}
				} else {
					res.status = Config.STATUS_NOT_EXITS;
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
				res.id = id;
				res.type = mPromoCodeInfo.type;
				res.money = mPromoCodeInfo.money;
				res.start_time = mPromoCodeInfo.start_time;
				res.end_time = mPromoCodeInfo.end_time;
			} else {
				DBUtils.rollbackTransaction(conn);
				if (StringUtils.emptyString(res.status)) {
					res.status = Config.STATUS_SERVER_ERROR;
				}
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}
	
	/**
	 * 财务增加兑换码
	 * @param req
	 * @return
	 * @throws SQLException
	 */
	public static FinancialAddPromoCodesResEntity addPromoCode(FinancialAddPromoCodesReqEntity req) throws SQLException {
		FinancialAddPromoCodesResEntity res = new FinancialAddPromoCodesResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			FinancialInfo finanInfo = FinancialDao.getFinancialByToken(conn, req.financial_token);
			if (finanInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			String password = MD5Utils.md5(req.password.getBytes(Charset.forName(Config.CHARSET)), MD5Utils.MD5Type.MD5_32);
			if(!finanInfo.financial_pass.equals(password)){
				res.status = Config.STATUS_PASSWORD_ERROR;
				return res;
			}
			List<RedPacketHelper.RedPacket> redPacketList=null;
			if(req.type==Config.PromoCodePartitionType.PROMO_CODE_PARTITION_TYPE_AVERAGE){
				float money=MathUtils.divide(req.total_amount, req.count);
				redPacketList=new ArrayList<RedPacketHelper.RedPacket>();
				for(int i=0;i<req.count;i++){
					redPacketList.add(new RedPacketHelper.RedPacket(money));
				}
			}else if(req.type==Config.PromoCodePartitionType.PROMO_CODE_PARTITION_TYPE_RANDOM){
				redPacketList = RedPacketHelper.createRedPacket(req.total_amount,req.count);
			}else{
				return null;
			}
			int row = 0;
			try {
				DBUtils.beginTransaction(conn);
				for(RedPacketHelper.RedPacket redPacket:redPacketList){
					if(req.begin_time==0)
						req.begin_time=System.currentTimeMillis();
					addConversionCode(conn,Config.PromoCodeType.PROMO_CODE_TYPE_VOLUME,redPacket.money,req.begin_time,req.end_time);
					row++;
				}
				if (row == req.count) {
					DBUtils.commitTransaction(conn);
				}
			} catch (Exception e) {
				LogUtils.log(e);
				row = 0;
			}
			if (row == req.count) {
				res.count=row;
				res.status = Config.STATUS_OK;
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
	
	public static FinancialSearchPromoCodesResEntity searchPromoCodes(FinancialSearchPromoCodesReqEntity req) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		FinancialSearchPromoCodesResEntity res = new FinancialSearchPromoCodesResEntity();
		try {
			conn = DBUtils.getConnection();
			FinancialInfo finanInfo = FinancialDao.getFinancialByToken(conn, req.financial_token);
			if (finanInfo == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			StringBuffer sql = new StringBuffer(
					"select promo_code,type,money,start_time,end_time,user_id,time from promo_code ");
			sql.append(" WHERE 1=1 ");
			//未被使用过的码
			if(req.type==0){
				sql.append(" and (user_id is null or user_id='' )");
			}//已被使用过的码
			else if(req.type==1){
				sql.append(" and user_id is not null");
			}else{
				return null;
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
			List<FinancialSearchPromoCodesResEntity.PromoCode> promoCodes = new ArrayList<>();
			FinancialSearchPromoCodesResEntity.PromoCode promoCode = null;
			while (rs.next()) {
				promoCode=new FinancialSearchPromoCodesResEntity.PromoCode();
				promoCode.money=rs.getFloat("money");
				promoCode.time=rs.getLong("time");
				promoCode.no=rs.getString("promo_code");
				promoCodes.add(promoCode);
			}
			res.promoCodes = promoCodes;
			boolean has_more_data = true;
			if (req.count > 0) {
				has_more_data = res.promoCodes.size() == req.count;
			} else {
				has_more_data = res.promoCodes.size() == Config.ONCE_QUERY_COUNT;
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
}

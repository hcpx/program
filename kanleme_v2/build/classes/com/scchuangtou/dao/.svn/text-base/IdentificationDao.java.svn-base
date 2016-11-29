package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.AdminConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.entity.AdminGetIdentificationsReqEntity;
import com.scchuangtou.entity.AdminGetIdentificationsResEntity;
import com.scchuangtou.entity.EditIdentificationReqEntity;
import com.scchuangtou.entity.EditIdentificationResEntity;
import com.scchuangtou.entity.GetIdentificationInfoReqEntity;
import com.scchuangtou.entity.GetIdentificationInfoResEntity;
import com.scchuangtou.entity.IdentificationReqEntity;
import com.scchuangtou.entity.IdentificationResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.model.AdminInfo;
import com.scchuangtou.model.Identification;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.StringUtils;

public class IdentificationDao {

	/**
	 * 认证
	 * 
	 * @param req
	 * @param articlePicPart
	 * @return
	 * @throws Exception
	 */
	public static IdentificationResEntity identification(IdentificationReqEntity req, List<MyMutiPart> newPicPart,
			HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			IdentificationResEntity identificationRes = new IdentificationResEntity();
			User user = UserDao.getUserByToken(conn, req.token);
			// 判断token过期user为空，设置TOKEN_ERROR直接返回
			if (user == null) {
				IdentificationResEntity res = new IdentificationResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}

			List<String> jsonList = null;
			String identificationPic = "";
			Map<String, MyMutiPart> parts = new HashMap<>();
			if (newPicPart != null && 0 != newPicPart.size()) {
				jsonList = new ArrayList<String>();
				for (int i = 0; i < newPicPart.size(); i++) {
					identificationPic = getImageName(user.user_id);
					jsonList.add(identificationPic);
					parts.put(identificationPic, newPicPart.get(i));
				}
			}
			
			DBUtils.beginTransaction(conn);
			int row = 0;
			try {
				// 验证码不正确
				if (!VerifyCodeDao.checkVerifyCode(conn, req.phone_num, req.verify_code)) {
					identificationRes.status = Config.STATUS_VERIFY_CODE_ERROR;
				} else {
					HashMap<String, Object> datas = new HashMap<String, Object>();
					datas.put("user_id", user.user_id);
					datas.put("name", req.name);
					datas.put("id_num", req.id_num);
					datas.put("address", req.address);
					datas.put("phone_num", req.phone_num);
					String photos = "";
					if (jsonList != null && jsonList.size() != 0)
						photos = JSON.toJSONString(jsonList);
					datas.put("photos", photos);
					datas.put("type", req.type);
					datas.put("time", System.currentTimeMillis());
					row = DBUtils.insert(conn, "INSERT ignore INTO identification", datas);
					if (row > 0) {
						if (parts.size() > 0 && ImageHelper.upload(parts) == null) {
							row = 0;
						}
					}
					if (row > 0) {
						String sql = "UPDATE userinfo SET certification=? WHERE user_id=? and certification=?";
						row = DBUtils
								.executeUpdate(conn, sql,
										new Object[] { Config.CertificationStauts.CERTIFICATION_STATUS_AUDIT,
												user.user_id,
												Config.CertificationStauts.CERTIFICATION_STATUS_NORMAL });
					} else {
						identificationRes.status = Config.STATUS_REPEAT_ERROR;
					}
					if (row > 0)
						DBUtils.commitTransaction(conn);
				}
			} catch (Exception e) {
				row = 0;
				LogUtils.log(e);
			}
			if (row > 0) {
				identificationRes.status = Config.STATUS_OK;
				if (jsonList != null && jsonList.size() != 0) {
					for (int i = 0; i < jsonList.size(); i++) {
						jsonList.set(i, ImageHelper.getImageUrl(request, jsonList.get(i)));
					}
				}
				identificationRes.photos = jsonList;
			} else {
				// 图片上传不完全或者失败的情况下，进行已成功图片的删除。
				DBUtils.rollbackTransaction(conn);
				if (StringUtils.emptyString(identificationRes.status))
					identificationRes.status = Config.STATUS_SERVER_ERROR;
			}
			return identificationRes;
		} finally {
			DBUtils.close(conn);
		}
	}

	/**
	 * 修改认证
	 * 
	 * @param req
	 * @param articlePicPart
	 * @return
	 * @throws Exception
	 */
	/**
	 * 修改认证
	 * 
	 * @param req
	 * @param articlePicPart
	 * @return
	 * @throws Exception
	 */
	public static EditIdentificationResEntity editIdentification(EditIdentificationReqEntity req,
			List<MyMutiPart> newPicParts, HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		try {
			conn = DBUtils.getConnection();
			EditIdentificationResEntity identificationRes = new EditIdentificationResEntity();
			User user = UserDao.getUserByToken(conn, req.token);
			// 判断token过期user为空，设置TOKEN_ERROR直接返回
			if (user == null) {
				identificationRes.status = Config.STATUS_TOKEN_ERROR;
				return identificationRes;
			}
			// 必须是审核未通过状态
			if (user.certification != Config.CertificationStauts.CERTIFICATION_STATUS_NO_PASS) {
				identificationRes.status = Config.STATUS_NOT_CORRECT;
				return identificationRes;
			}
			// 必须有记录
			Identification identification = getIdentification(conn, user.user_id);
			if (identification == null) {
				identificationRes.status = Config.STATUS_NOT_EXITS;
				return identificationRes;
			}
			List<String> oldphotos = JSON.parseArray(identification.photos, String.class);
			Map<String, MyMutiPart> parts = new HashMap<>();
			List<String> deletes = new ArrayList<>();
			List<String> newJsonList = new ArrayList<>();
			if (req.delete_photos != null) {
				int size = oldphotos.size();
				for (int i = 0; i < size; i++) {
					String oname = oldphotos.get(i);
					boolean isDelete = false;
					for (String dul : req.delete_photos) {
						if (dul.endsWith(oname)) {
							isDelete = true;
							break;
						}
					}
					if (isDelete) {
						deletes.add(oname);
					} else {
						newJsonList.add(oname);
					}
				}
			}
			int size = newPicParts.size();
			for (int i = 0; i < size; i++) {
				String imgName = getImageName(user.user_id);
				newJsonList.add(imgName);
				parts.put(imgName, newPicParts.get(i));
			}
			// 组织认证的时候删除图片加上图片以及原来图片总和如果大于8或者小于0，都是不正常的状态返回参数错误
			if (req.type == Config.IdentificationStatusType.IDENTIFICATION_ORGANIZATION
					&& (newJsonList.size() > 8 || newJsonList.size() == 0)) {
				return null;
			}
			// 个人认证有且只有三张图片
			if (req.type == Config.IdentificationStatusType.IDENTIFICATION_PERSON && newJsonList.size() != 3) {
				return null;
			}
			
			DBUtils.beginTransaction(conn);
			int row = 1;
			try {
				// 验证码不正确
				if (!VerifyCodeDao.checkVerifyCode(conn, req.phone_num, req.verify_code)) {
					identificationRes.status = Config.STATUS_VERIFY_CODE_ERROR;
				} else {
					StringBuffer sb = new StringBuffer();
					sb.append("UPDATE identification SET name =?,phone_num=?");
					if (!StringUtils.emptyString(req.id_num) && !req.id_num.equals(identification.id_num)) {
						sb.append(",id_num='").append(req.id_num).append("'");
					}
					if (!StringUtils.emptyString(req.address) && !req.address.equals(identification.address)) {
						sb.append(",address='").append(req.address).append("'");
					}
					sb.append(",photos='").append(JSON.toJSONString(newJsonList)).append("'");
					if (req.type != identification.type) {
						sb.append(",type=").append(req.type);
					}
					sb.append(",failure_causes=''");
					sb.append(" where user_id=?");
					row = DBUtils.executeUpdate(conn, sb.toString(),
							new Object[] { req.name, req.phone_num, user.user_id });
					if (row > 0 && parts.size() > 0) {
						if (ImageHelper.upload(parts) == null) {
							row = 0;
						}
					}
					if (row > 0) {
						String sql = "UPDATE userinfo SET certification=? WHERE user_id=? and certification=?";
						row = DBUtils
								.executeUpdate(conn, sql,
										new Object[] { Config.CertificationStauts.CERTIFICATION_STATUS_AUDIT,
												user.user_id,
												Config.CertificationStauts.CERTIFICATION_STATUS_NO_PASS });
					}
					if (row > 0)
						DBUtils.commitTransaction(conn);
				}
			} catch (Exception e) {
				row = 0;
				LogUtils.log(e);
			}
			if (row > 0) {
				ImageHelper.deleteByNames(deletes);
				for (int i = 0; i < newJsonList.size(); i++) {
					newJsonList.set(i, ImageHelper.getImageUrl(request, newJsonList.get(i)));
				}
				identificationRes.photos = newJsonList;
				identificationRes.status = Config.STATUS_OK;
			} else {
				// 图片上传不完全或者失败的情况下，进行已成功图片的删除。
				DBUtils.rollbackTransaction(conn);
				if (StringUtils.emptyString(identificationRes.status))
					identificationRes.status = Config.STATUS_SERVER_ERROR;
			}
			return identificationRes;
		} finally {
			DBUtils.close(conn);
		}
	}

	/**
	 * 获取认证详情
	 * 
	 * @param conn
	 * @param userID
	 * @return
	 * @throws SQLException
	 */
	public static Identification getIdentification(DBUtils.ConnectionCache conn, String userID) throws SQLException {
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			String sql = "select identification.user_id,identification.name,identification.id_num,identification.address,identification.phone_num,identification.photos,identification.type,identification.time,identification.failure_causes from identification where user_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, userID);
			rs = pstat.executeQuery();
			if (rs.next()) {
				Identification identification = new Identification();
				identification.user_id = rs.getString("user_id");
				identification.name = rs.getString("name");
				identification.id_num = rs.getString("id_num");
				identification.address = rs.getString("address");
				identification.phone_num = rs.getString("phone_num");
				identification.photos = rs.getString("photos");
				identification.type = rs.getInt("type");
				identification.time = rs.getLong("time");
				return identification;
			}
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
		}
	}

	public static GetIdentificationInfoResEntity getIdentificationInfo(GetIdentificationInfoReqEntity req,
			HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			User user = null;
			user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				GetIdentificationInfoResEntity res = new GetIdentificationInfoResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			GetIdentificationInfoResEntity res = new GetIdentificationInfoResEntity();
			String sql = "select identification.user_id,identification.name,identification.id_num,identification.address,identification.phone_num,identification.photos,identification.type,identification.time,identification.failure_causes from identification where user_id=?";
			pstat = conn.prepareStatement(sql);
			pstat.setObject(1, user.user_id);
			rs = pstat.executeQuery();
			if (rs.next()) {
				res.name = rs.getString("name");
				res.id_num = rs.getString("id_num");
				res.type = rs.getInt("type");
				res.address = rs.getString("address");
				res.phone_num = rs.getString("phone_num");
				res.failure_causes = rs.getString("failure_causes");
				List<String> photos = null;
				if (!StringUtils.emptyString(rs.getString("photos"))) {
					photos = JSON.parseArray(rs.getString("photos"), String.class);
					for (int i = 0; i < photos.size(); i++) {
						photos.set(i, ImageHelper.getImageUrl(request, res.photos.get(i)));
					}
				}
				res.time = rs.getLong("time");
				res.photos = photos;
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

	private static String getImageName(String id) {
		return new StringBuffer().append("identification/").append(id).append("/")
				.append(IdUtils.createId("identification")).toString();
	}

	public static AdminGetIdentificationsResEntity adminGetIdentifications(AdminGetIdentificationsReqEntity req,
			HttpServletRequest request) throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			AdminInfo mAdminInfo = AdminDao.getAdminByToken(conn, req.admin_token);
			if (mAdminInfo == null) {
				AdminGetIdentificationsResEntity res = new AdminGetIdentificationsResEntity();
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			if (!mAdminInfo.hasMode(AdminConfig.MODE_MANAGER_IDENTIFICATION)) {
				AdminGetIdentificationsResEntity res = new AdminGetIdentificationsResEntity();
				res.status = Config.STATUS_PERMISSION_ERROR;
				return res;
			}

			StringBuffer sb = new StringBuffer(
					"select identification.user_id,identification.name,identification.id_num,identification.address,identification.phone_num,identification.photos,identification.type,identification.time,identification.failure_causes,userinfo.certification from identification INNER JOIN userinfo ON identification.user_id=userinfo.user_id");
			sb.append(" where userinfo.certification=? order by identification.time asc limit ?,?");
			pstat = conn.prepareStatement(sb.toString());
			pstat.setObject(1, req.status);
			pstat.setObject(2, req.begin);
			pstat.setObject(3, Config.ONCE_QUERY_COUNT);
			rs = pstat.executeQuery();

			AdminGetIdentificationsResEntity res = new AdminGetIdentificationsResEntity();
			res.status = Config.STATUS_OK;
			res.datas = new ArrayList<>();

			AdminGetIdentificationsResEntity.Data data;

			while (rs.next()) {
				data = new AdminGetIdentificationsResEntity.Data();

				data.user_id = rs.getString("user_id");
				data.name = rs.getString("name");
				data.id_num = rs.getString("id_num");
				data.type = rs.getInt("type");
				data.address = rs.getString("address");
				data.phone_num = rs.getString("phone_num");
				data.failure_causes = rs.getString("failure_causes");
				List<String> photos = null;
				if (!StringUtils.emptyString(rs.getString("photos"))) {
					photos = JSON.parseArray(rs.getString("photos"), String.class);
					for (int i = 0; i < photos.size(); i++) {
						photos.set(i, ImageHelper.getImageUrl(request, photos.get(i)));
					}
				}
				data.certification=rs.getInt("certification");
				data.time = rs.getLong("time");
				data.photos = photos;

				res.datas.add(data);
			}
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}

}

package com.scchuangtou.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.entity.GetDynamicsReqEntity;
import com.scchuangtou.entity.GetDynamicsResEntity;
import com.scchuangtou.entity.UpdateDynamicsReqEntity;
import com.scchuangtou.entity.UpdateDynamicsResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.model.ProjectInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.StringUtils;
import com.scchuangtou.utils.DBUtils.PreparedStatementCache;

public class DynamicsDao {
	public static UpdateDynamicsResEntity dynamics(UpdateDynamicsReqEntity req, List<MyMutiPart> picParts,
			HttpServletRequest request) throws Exception {
		UpdateDynamicsResEntity res = new UpdateDynamicsResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
			User user = UserDao.getUserByToken(conn, req.token);
			if (user == null) {
				res.status = Config.STATUS_TOKEN_ERROR;
				return res;
			}
			ProjectInfo projectInfo = ProjectDao.getProjectInfo(conn, req.help_each_id);
			if (projectInfo == null) {
				res.status = Config.STATUS_NOT_EXITS;
				return res;
			}
			if (!projectInfo.user_id.equals(user.user_id)) {
				res.status = Config.STATUS_PARAMETER_ERROR;
				return res;
			}

			Map<String, MyMutiPart> parts = new HashMap<>();
			String id = IdUtils.createId("dynamics");
			List<String> jsonList = null;
			if (picParts != null) {
				jsonList = new ArrayList<String>();
				for (int i = 0; i < picParts.size(); i++) {
					String imageName = getImageName(id);
					jsonList.add(imageName);
					parts.put(imageName, picParts.get(i));
				}
			}
			int row = 0;
			try {
				HashMap<String, Object> datas = new HashMap<>();
				datas.put("dynamics_id", id);
				datas.put("help_each_id", req.help_each_id);
				if (parts.size() > 0) {
					datas.put("dynamics_img", JSON.toJSONString(parts.keySet()));
				}
				datas.put("dynamics_desc", req.description);
				datas.put("time", System.currentTimeMillis());
				row = DBUtils.insert(conn, "insert ignore into dynamics", datas);
				if (row > 0) {
					if (parts.size() > 0 && ImageHelper.upload(parts)==null) {
						row = 0;
					}
				} else {
					res.status = Config.STATUS_REPEAT_ERROR;
					return res;
				}
			} catch (Exception e) {
				row = 0;
				LogUtils.log(e);
			}
			if (row > 0) {
				res.dynamics_id = id;
				if (jsonList != null && jsonList.size() != 0) {
					for (int i = 0; i < jsonList.size(); i++) {
						jsonList.set(i, ImageHelper.getImageUrl(request, jsonList.get(i)));
					}
				}
				res.dynamics_img = jsonList;
				res.status = Config.STATUS_OK;
			} else {
				res.status = Config.STATUS_SERVER_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	private static String getImageName(String id) {
		return new StringBuffer().append("dynamics/").append(id).append("/").append(IdUtils.createId("dynamics"))
				.toString();
	}
	
	public static GetDynamicsResEntity listDynamics(GetDynamicsReqEntity req,HttpServletRequest request)
			throws Exception {
		DBUtils.ConnectionCache conn = null;
		PreparedStatementCache pstat = null;
		GetDynamicsResEntity res = new GetDynamicsResEntity();
		ResultSet rs = null;
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
			StringBuffer sb=new StringBuffer();
			sb.append(
					" select dynamics.dynamics_id,dynamics.help_each_id,dynamics.dynamics_img,dynamics.dynamics_desc,dynamics.time from dynamics");
			sb.append(" where help_each_id=? order by time desc LIMIT ?,?");
			pstat = conn.prepareStatement(sb.toString());
			req.begin = req.begin < 0 ? 0 : req.begin;
			pstat.setObject(1, req.help_each_id);
			pstat.setObject(2, req.begin);
			if (req.count > 0) {
				pstat.setObject(3, req.count);
			} else {
				pstat.setObject(3, Config.ONCE_QUERY_COUNT);
			}
			rs = pstat.executeQuery();
			res.dynamicses = new ArrayList<>();
			GetDynamicsResEntity.Info info;
			List<String> dynamics_img=null;
			while (rs.next()) {
				info=new GetDynamicsResEntity.Info();
				info.description=rs.getString("dynamics_desc");
				String img=rs.getString("dynamics_img");
				if(!StringUtils.emptyString(img))
					dynamics_img=JSON.parseArray(img, String.class);
				info.dynamics_img=dynamics_img;
				res.dynamicses.add(info);
			}
			res.status = Config.STATUS_OK;
			boolean has_more_data=true;
			if (req.count > 0) {
				has_more_data=res.dynamicses.size()==req.count;
			} else {
				has_more_data=res.dynamicses.size()==Config.ONCE_QUERY_COUNT;
			}
			res.has_more_data = has_more_data;
			return res;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstat);
			DBUtils.close(conn);
		}
	}
}

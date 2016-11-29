package com.scchuangtou.dao;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.config.Config;
import com.scchuangtou.entity.DoubtReqEntity;
import com.scchuangtou.entity.DoubtResEntity;
import com.scchuangtou.helper.ImageHelper;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.model.ProjectInfo;
import com.scchuangtou.model.User;
import com.scchuangtou.utils.DBUtils;
import com.scchuangtou.utils.IdUtils;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.MD5Utils;

public class DoubtDao {
	public static int DOUBT_REPLY = 0; // 需要回复
	public static int DOUBT_NOT_REPLY = 1; // 不需要回复

	public static DoubtResEntity doubt(DoubtReqEntity req, List<MyMutiPart> picParts, HttpServletRequest request)
			throws Exception {
		DoubtResEntity res = new DoubtResEntity();
		DBUtils.ConnectionCache conn = DBUtils.getConnection();
		try {
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
			Map<String, MyMutiPart> parts = new HashMap<>();
			String id = createDoubtId(user.user_id, req.help_each_id);
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
				datas.put("doubt_id", id);
				datas.put("help_each_id", req.help_each_id);
				datas.put("user_id", user.user_id);
				datas.put("reply", req.reply == DOUBT_REPLY ? DOUBT_REPLY : DOUBT_NOT_REPLY);
				datas.put("phone", req.phone);
				datas.put("detail", req.detail);
				datas.put("time", System.currentTimeMillis());
				if (parts.size() > 0) {
					datas.put("doubt_img", JSON.toJSONString(parts.keySet()));
				}
				row = DBUtils.insert(conn, "insert ignore into doubt", datas);
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
				res.doubt_id = id;
				if (jsonList != null && jsonList.size() != 0) {
					for (int i = 0; i < jsonList.size(); i++) {
						jsonList.set(i, ImageHelper.getImageUrl(request, jsonList.get(i)));
					}
				}
				res.doubt_imgs = jsonList;
				res.status = Config.STATUS_OK;
			} else {
				res.status = Config.STATUS_SERVER_ERROR;
			}
			return res;
		} finally {
			DBUtils.close(conn);
		}
	}

	private static String createDoubtId(String user_id, String object_id) {
		return MD5Utils.md5((user_id + object_id).getBytes(Charset.forName(Config.CHARSET)), MD5Utils.MD5Type.MD5_16);
	}
	
	
	private static String getImageName(String id) {
		return new StringBuffer().append("doubt/").append(id).append("/")
				.append(IdUtils.createId("doubt")).toString();
	}
}

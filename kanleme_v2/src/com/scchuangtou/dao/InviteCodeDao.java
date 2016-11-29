package com.scchuangtou.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

import com.scchuangtou.config.Config;
import com.scchuangtou.utils.DBUtils;

public class InviteCodeDao {
	private static final Random mRandom = new Random();
	private static final String S = "123456789ABCDEFGHJKLMNPQRSTUVWXYZ";

	private static String createCode() {
		StringBuffer sb = new StringBuffer();
		int len = S.length();
		for (int i = 0; i < Config.INVITE_CODE_LEN; i++) {
			sb.append(S.charAt(mRandom.nextInt(len)));
		}
		return sb.toString();
	}

	public static String getInviteCode(DBUtils.ConnectionCache conn) throws SQLException {
		String code = createCode();
		HashMap<String, Object> datas = new HashMap<String, Object>();
		datas.put("code", code);
		int row = DBUtils.insert(conn, "INSERT IGNORE INTO invite_code", datas);
		if (row > 0) {
			return code;
		} else {
			return getInviteCode(conn);
		}
	}
}

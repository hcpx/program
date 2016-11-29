package com.scchuangtou.utils;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

import com.scchuangtou.config.Config;

public class IdUtils {
	public static String createId(String tag) {
		StringBuffer sb = new StringBuffer();
		if (tag != null) {
			sb.append(tag);
			sb.append("_");
		}
		Random mRandom = new Random();
		sb.append(mRandom.nextInt());
		sb.append(mRandom.nextInt());
		sb.append("_");
		sb.append(UUID.randomUUID().toString());
		return getId(sb.toString());
	}

	public static String getId(String tag) {
		return MD5Utils.md5(tag.toString().getBytes(Charset.forName(Config.CHARSET)), MD5Utils.MD5Type.MD5_16);
	}
}

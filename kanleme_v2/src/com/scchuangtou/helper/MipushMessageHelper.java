package com.scchuangtou.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.scchuangtou.config.AccountConfig;
import com.scchuangtou.http.UrlHttpClint;
import com.scchuangtou.utils.LogUtils;

public class MipushMessageHelper {

	public static void main(String[] args) {
		MessageParam msg = new MessageParam();
		msg.alias.add("31464e5ad8ac6968");
		msg.description = "这是一个测试31464e5ad8ac6968";
		msg.title = "国立看了么";
		System.out.println(MipushMessageHelper.sendIosMessage(msg));
	}

	public static boolean sendAllMessage(MessageParam msg) {
		boolean res = sendAndroidMessage(msg);
		if (res) {
			res = sendIosMessage(msg);
		}
		return res;
	}

	public static boolean sendIosMessage(MessageParam msg) {
		if (emptyString(msg.description)) {
			throw new IllegalArgumentException("tile or description is null");
		}
		msg.description = actionMessage(msg.description);
		String url = getUrl(msg.alias.size() > 0, false);
		UrlHttpClint client = new UrlHttpClint(url);
		client.setCharSet(AccountConfig.MiPushConfig.CHARSET);
		client.addRequestProperty("Authorization",
				new StringBuilder().append("key=").append(AccountConfig.MiPushConfig.iOS_SECRET_KEY).toString());
		client.addPart("description", msg.description);
		if (msg.getAliasSize() > 0) {
			client.addPart("alias", msg.getAlias());
		}
		if (msg.extras != null) {
			Set<String> keys = msg.extras.keySet();
			for (String key : keys) {
				client.addPart("extra." + key, msg.extras.get(key));
			}
		}
		try {
			UrlHttpClint.Response res = client.post();
			return paseResult(res);
		} catch (Exception e) {
			LogUtils.log(e);
		}
		return false;
	}

	public static boolean sendAndroidMessage(MessageParam msg) {
		if (emptyString(msg.title) || emptyString(msg.description)) {
			throw new IllegalArgumentException("tile or description is null");
		}
		msg.description = actionMessage(msg.description);
		String url = getUrl(msg.alias.size() > 0, true);
		UrlHttpClint client = new UrlHttpClint(url);
		client.setCharSet(AccountConfig.MiPushConfig.CHARSET);
		client.addRequestProperty("Authorization",
				new StringBuilder().append("key=").append(AccountConfig.MiPushConfig.ANDROID_SECRET_KEY).toString());
		client.addPart("restricted_package_name", AccountConfig.MiPushConfig.ANDROID_PKG);
		client.addPart("title", msg.title);
		client.addPart("description", msg.description);
		client.addPart("payload", msg.description);
		client.addPart("pass_through", "1");
		if (msg.getAliasSize() > 0) {
			client.addPart("alias", msg.getAlias());
		}
		if (msg.extras != null) {
			Set<String> keys = msg.extras.keySet();
			for (String key : keys) {
				client.addPart("extra." + key, msg.extras.get(key));
			}
		}
		try {
			UrlHttpClint.Response res = client.post();
			return paseResult(res);
		} catch (Exception e) {
			LogUtils.log(e);
		}
		return false;
	}

	private static String actionMessage(String msg) {
		if (msg.length() > 128) {
			return msg.substring(0, 125) + "...";
		} else {
			return msg;
		}
	}

	private static String getUrl(boolean hasAlias, boolean isAndroid) {
		if (isAndroid || !AccountConfig.MiPushConfig.IS_TEST) {
			return hasAlias ? "https://api.xmpush.xiaomi.com/v3/message/alias"
					: "https://api.xmpush.xiaomi.com/v3/message/all";
		} else {
			return hasAlias ? "https://sandbox.xmpush.xiaomi.com/v2/message/alias"
					: "https://sandbox.xmpush.xiaomi.com/v2/message/all";
		}
	}

	private static boolean paseResult(UrlHttpClint.Response res) throws Exception {
		if (res != null && res.statusCode == 200 && res.data != null) {
			String responseBody = new String(res.data, AccountConfig.MiPushConfig.CHARSET);
			JSONObject jsonObject = (JSONObject) JSONObject.parse(responseBody);
			boolean isok = "ok".equals(jsonObject.get("result"));
			if (!isok) {
				LogUtils.log(responseBody);
			}
			return isok;
		} else {
			return false;
		}
	}

	private static boolean emptyString(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static class MessageParam {
		private List<String> alias = new ArrayList<String>();
		public String title;// android is not null,Up to 16 characters
		public String description;// is not null,Up to 128 characters
		public HashMap<String, String> extras = new HashMap<>();// max size 10

		public int getAliasSize() {
			return alias.size();
		}

		public String getAlias() {
			StringBuffer sb = new StringBuffer();
			boolean flag = true;
			for (String a : alias) {
				if (!flag) {
					sb.append(",");
				}
				flag = false;
				sb.append(a);
			}
			return sb.toString();
		}

		public void addAlias(String a) {
			if (alias.contains(a)) {
				return;
			}
			alias.add(a);
		}
	}
}

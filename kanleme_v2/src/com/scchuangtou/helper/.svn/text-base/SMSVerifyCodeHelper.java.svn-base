package com.scchuangtou.helper;

import java.util.Random;

import com.scchuangtou.config.Config;
import com.scchuangtou.config.AccountConfig;
import com.scchuangtou.http.UrlHttpClint;
import com.scchuangtou.utils.LogUtils;

public class SMSVerifyCodeHelper {
	private static final String CODES = "0123456789";

	public static String createVerifyCode() {
		Random mRandom = new Random();

		StringBuffer sb = new StringBuffer();
		int rn = 0;
		int count = 0;
		while (count < Config.SMS_VERIFY_CODE_LEN) {
			rn = mRandom.nextInt(CODES.length());
			if (rn < 0 || rn >= CODES.length()) {
				continue;
			}
			sb.append(CODES.charAt(rn));
			count++;
		}
		return sb.toString();
	}

	public static boolean sendSMS(String phoneNumber, String msg) {
		try {
			UrlHttpClint mUrlHttpClint = new UrlHttpClint(AccountConfig.SMSConfig.URL);
			mUrlHttpClint.addPart("userid", AccountConfig.SMSConfig.USERID);
			mUrlHttpClint.addPart("account", AccountConfig.SMSConfig.ACCOUNT);
			mUrlHttpClint.addPart("password", AccountConfig.SMSConfig.PASSWORD);
			mUrlHttpClint.addPart("mobile", phoneNumber);

			mUrlHttpClint.addPart("content", msg);
			mUrlHttpClint.addPart("action", "send");
			UrlHttpClint.Response mResponse = mUrlHttpClint.post();
			if (mResponse.data != null && new String(mResponse.data).toLowerCase().indexOf("<message>ok</message>") != -1) {
				return true;
			}
		} catch (Exception e) {
			LogUtils.log(e);
		}
		return false;
	}

	public static void main(String[] args) {
		String msg = "验证码:776520,请勿将此验证码发给任何号码及其他人，若非本人操作，请忽略本条短信【国立看了么】";
		sendSMS("7413409", msg);
	}
}

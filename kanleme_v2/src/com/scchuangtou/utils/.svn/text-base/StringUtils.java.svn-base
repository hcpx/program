package com.scchuangtou.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	public static boolean isPhoneNum(String cellPhoneNr) {
		if (emptyString(cellPhoneNr))
			return false;
//		String reg = "^[+]*[0-9]+$";
		String reg = "(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}";
		return startCheck(reg, cellPhoneNr);
	}

	public static boolean isEmail(String email) {
		if (emptyString(email)) {
			return false;
		}
		String emailMatch = "^[a-zA-Z0-9!#$%&\'*+\\/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&\'*+\\/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$";
		return startCheck(emailMatch, email);
	}

	public static boolean startCheck(String reg, String string) {
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(string);
		return matcher.matches();
	}

	public static boolean emptyString(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	/**
	 * 判断是否有颜文字
	 * @param str
	 * @return
	 */
	public static boolean isEmoji(String str){
		if(str.matches(".*\\p{So}.*"))
			return true;
		else
			return false;
	}

}

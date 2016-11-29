package com.scchuangtou.model;

import java.util.List;

public class AdminInfo {
	public String admin_user;
	public String admin_email;
	public String admin_phone;
	public String admin_pass;
	public int status;
	public List<String> modes;

	public boolean hasMode(String modename) {
		if (modes == null) {
			return false;
		}
		return modes.contains(modename);
	}
}

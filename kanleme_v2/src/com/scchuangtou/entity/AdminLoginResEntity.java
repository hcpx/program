package com.scchuangtou.entity;

import java.util.List;

import com.scchuangtou.model.AdminModeInfo;

public class AdminLoginResEntity extends BaseResEntity {
	public String admin_token;
	public String username;
	public String admin_email;
	public String admin_phone;
	public List<AdminModeInfo> modes;
}

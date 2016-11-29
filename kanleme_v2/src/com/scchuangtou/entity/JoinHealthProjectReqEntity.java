package com.scchuangtou.entity;

import java.util.List;

import com.scchuangtou.model.HealthProjectInfo;

public class JoinHealthProjectReqEntity {
	public String health_project_id;
	public String token;
	public String traders_password;
	public String order_num;
	public int type;
	public String invite_code;
	public List<String> vouchers_id;
	public List<PersonInfo> join_person_list;
	public static class PersonInfo{
		public String name;
		public String id_num;
		public String phone_num;
		public HealthProjectInfo healthProjectInfo;
		public String type;
	}
}

package com.scchuangtou.entity;

import java.util.List;

public class GetHealthProjectMembersResEntity extends BaseResEntity{
	public List<ProjectMembers> projectMembersList;
	public boolean has_more_data;
	public static class ProjectMembers{
		public String no;
		public String name;
		public String id_num;
		public String phone_num;
		public int status;
		public float balance;
		public long join_time;
		public long effect_time;
		public Detail project;
	}
	
	public static class Detail{
		public String health_project_id;
		public String health_project_name;
		public String index_img;
		public int all_member;
		public float share_amount;
		public float has_amount;
		public int type;
		public String detail_url;
		public float max_security;
		public String desc;
		public float join_money;
		public String scope;
		public int min_age;
		public int min_type;
		public int max_age;
		public int max_type;
	}
}

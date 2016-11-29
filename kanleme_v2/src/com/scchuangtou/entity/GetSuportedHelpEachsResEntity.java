package com.scchuangtou.entity;

import java.util.List;

public class GetSuportedHelpEachsResEntity extends BaseResEntity{
	public boolean has_more_data;
	public List<Info> projects;
	
	public static class Info
	{
		public String help_each_id;
		public String title;
		public long publish_time;
		public double capital_goal;
		public double gold_count;
		public long begin_time;
		public long end_time;
	    public long comment_num;
	    public long view_count;
		public int project_suport_num;
		public UserInfo user_info;
	   
	}
	
	public static class UserInfo{
		public String user_id;
		public String nickname;
		public String head_pic;
		public int level;
	}
}

package com.scchuangtou.entity;

import java.util.List;

public class GetParticipationActivitysResEntity extends BaseResEntity{
	public boolean has_more_data;
	public List<Info> activitys;
	
	public static class Info
	{
		public String activity_id;
		public String activity_name;
		public String poster_img;
		public long activity_comment_num;
		public long activity_browse_num;
	    public List<String> activity_type;
	    public List<String> activity_lable;
	    public long time;
	    public String focus;
	    public UserInfo user_info;
	    public int type; //0=评论过的活动，1=报名过的帖子
	}
	
	public static class UserInfo{
		public String user_id;
		public String nickname;
		public String head_pic;
		public int level;
	}
}

package com.scchuangtou.entity;

import java.util.List;

public class GetCircleMembersResEntity extends BaseResEntity{
	public List<UserInfo> users;
	public boolean has_more_data;
	public static class UserInfo{
		public String user_id;
		public String nickname;
		public String head_pic;
		public int level;
	}
}

package com.scchuangtou.entity;

import java.util.ArrayList;
import java.util.List;

import com.scchuangtou.model.UserInfo;

public class GetCommentResEntity extends BaseResEntity {
	public List<Comment> comments = new ArrayList<>();
	public boolean has_more_data;

	public static class Comment {
		public String comment_id;
		public long comment_time;
		public String comment_content;
		public UserInfo user_info;
		public ParentInfo parent_info;
		public int praise_count;
		public String praise_id;
	}

	public static class ParentInfo {
		public String comment_id;
		public long comment_time;
		public String comment_content;
		public UserInfo user_info;
	}
}

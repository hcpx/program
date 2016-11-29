package com.scchuangtou.entity;

import java.util.List;

public class GetArticleInfoResEntity extends BaseResEntity{
	public Info article;
	
	public static class Info
	{
		public String article_id;
		public String article_title;
		public String article_content;
		public String article_collecteid;
		public String article_praiseid;
		public Long article_time;
		public UserInfo user_info;
	    public long comment_num;
	    public long view_count;
	    public long article_praise_num;
	    public List<String> article_type;
	    public List<String> article_label;
	    public List<img> article_pic;
	}
	
	public static class img{
		public String article_image_url;
		public String article_image_description;
	}
	
	public static class UserInfo{
		public String user_id;
		public String nickname;
		public String head_pic;
		public String signature;
		public int level;
	}
}

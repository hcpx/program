package com.scchuangtou.entity;

import java.util.List;

public class AdminGetArticlesResEntity extends BaseResEntity{
	public boolean has_more_data;
	public List<Info> article;
	
	public static class Info
	{
		public String article_id;
		public String article_title;
		public String article_content;
		public Long article_time;
	    public long comment_num;
	    public long view_count;
	    public long article_praise_num;
	    public List<String> article_type;
	    public List<String> article_label;
	    public int article_status;
	}
}

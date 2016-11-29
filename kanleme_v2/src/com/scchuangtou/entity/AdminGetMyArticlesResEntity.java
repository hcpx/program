package com.scchuangtou.entity;

import java.util.List;

public class AdminGetMyArticlesResEntity extends BaseResEntity{
	public boolean has_more_data;
	public List<Info> article;
	
	public static class Info
	{
		public String article_id;
		public String article_title;
		public Long article_time;
	}

}

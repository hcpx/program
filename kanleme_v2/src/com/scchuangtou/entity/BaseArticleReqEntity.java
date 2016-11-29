package com.scchuangtou.entity;

public class BaseArticleReqEntity {
	public String circle_id;
	public String title;
	public String article_content;
	public String[] article_type;
	public String[] article_label;
	public Pic[] article_pic_desc;

	public static class Pic {
		public String article_pic_partname;
		public String article_pic_des;
	}
}

package com.scchuangtou.entity;

import java.util.List;

public class BaseArticleResEntity extends BaseResEntity {
	public String article_id;
	public long giving_growth;
	public List<img> article_pics;

	public static class img {
		public String article_image_url;
		public String article_image_description;
	}
}

package com.scchuangtou.entity;

import java.util.List;

public class GetCirclesResEntity extends BaseResEntity{
	public boolean has_more_data;
	public List<Circle> circleList;
	public static class Circle{
		public String circle_id;
		public String circle_name;
		public String circle_sign;
		public Long circle_created_time;
		public int circle_user_count;
		public int circle_articles;
		public String circle_pic;
		public int status;
		public String create_user;
		public String circle_back_img;
	}
}

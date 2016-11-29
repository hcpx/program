package com.scchuangtou.entity;

import java.util.List;

public class GetMyGoldInfoResEntity extends BaseResEntity {
	public List<Data> datas;
	public boolean has_more_data;
	public static class Data {
		public int change_type;
		public long change_time;
		public float change_value;
		public String change_desc;
		public String desc;
	}
}

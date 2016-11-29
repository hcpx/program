package com.scchuangtou.entity;

import java.util.List;

import com.scchuangtou.model.UserInfo;

public class GetGoldInfoResEntity extends BaseResEntity {
	public boolean has_more_data;
	public List<Data> datas;
	public static class Data {
		public int change_type;
		public long change_time;
		public float change_value;
		public String change_desc;
		public String desc;
		public UserInfo user_info;
	}
}

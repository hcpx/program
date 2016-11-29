package com.scchuangtou.entity;

import java.util.List;

public class AdminGetIdentificationsResEntity extends BaseResEntity {
	public List<Data> datas;

	public static class Data {
		public String user_id;
		public String name;
		public String id_num;
		public int type;
		public String address;
		public String phone_num;
		public long time;
		public String failure_causes;
		public List<String> photos;
		public int certification;
	}
}

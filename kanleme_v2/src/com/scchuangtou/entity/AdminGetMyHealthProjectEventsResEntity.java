package com.scchuangtou.entity;

import java.util.List;

public class AdminGetMyHealthProjectEventsResEntity extends BaseResEntity{
	public boolean has_more_data;
	public List<Info> infos;
	
	public static class Info
	{
		public String health_project_event_id;
		public String event_title;
		public Long publish_time;
	}
}

package com.scchuangtou.entity;

import java.util.List;

import com.scchuangtou.entity.JoinHealthProjectReqEntity.PersonInfo;

public class JoinHealthProjectResEntity extends BaseResEntity{
	public List<String> health_project_member_id;
	public List<PersonInfo> person_info_list;
	public float gold;
}

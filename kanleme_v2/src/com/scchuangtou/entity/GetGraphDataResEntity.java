package com.scchuangtou.entity;

import java.util.List;

import com.scchuangtou.model.HomepageGraphInfo;

public class GetGraphDataResEntity extends BaseResEntity{
	public List<String> payList;
	public List<String> withdrawList;
	public List<String> dates;
	public List<HomepageGraphInfo> healthList;
	public float pay_gold;
	public float withdraw_gold;
}

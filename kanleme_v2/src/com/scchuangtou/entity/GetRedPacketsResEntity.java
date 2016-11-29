package com.scchuangtou.entity;

import java.util.List;

import com.scchuangtou.model.RedPacket;

public class GetRedPacketsResEntity extends BaseResEntity{
	public List<RedPacket> all_redPacket;//企业红包
	public boolean all_has_more_data;
}

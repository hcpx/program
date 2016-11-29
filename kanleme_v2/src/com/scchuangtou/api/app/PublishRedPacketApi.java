package com.scchuangtou.api.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.RedPacketDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.PublishRedPacketReqEntity;
import com.scchuangtou.entity.PublishRedPacketResEntity;
import com.scchuangtou.http.MyMutiPart;
import com.scchuangtou.utils.StringUtils;

public class PublishRedPacketApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		Part companyimgPart = request.getPart("company_img");
		if (StringUtils.emptyString(json)) {
			return null;
		}
		PublishRedPacketReqEntity req = JSON.parseObject(json, PublishRedPacketReqEntity.class);
		if (req == null || StringUtils.emptyString(req.token) || StringUtils.emptyString(req.company_name)
				|| req.company_name.length() > 10 || StringUtils.emptyString(req.public_num)
				|| req.public_num.length() > 13 || (req.type == Config.PacketCommandType.PACKET_COMMAND_TYPE_COMMAND
						&& StringUtils.emptyString(req.command))) {
			return null;
		}
		MyMutiPart pic = null;
		if (companyimgPart != null) {
			pic = new MyMutiPart(companyimgPart);
		}
		PublishRedPacketResEntity res = RedPacketDao.addRedPacket(req, pic, request);
		return res;
	}

}

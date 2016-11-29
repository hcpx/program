package com.scchuangtou.api.app;

import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.VerifyCodeDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.SendVerifyCodeReqEntity;
import com.scchuangtou.utils.DESUtils;
import com.scchuangtou.utils.StringUtils;

public class SendVerifyCodeApi extends BaseApi {

	@Override
	protected BaseResEntity action(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (json == null) {
			return null;
		}
		
		byte[] bytes = DESUtils.decryptStr(json, Config.EncryptionKey.verifyCodeKey);
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		
		SendVerifyCodeReqEntity req = JSON.parseObject(new String(bytes, Charset.forName(Config.CHARSET)), SendVerifyCodeReqEntity.class);
		if (req == null || StringUtils.emptyString(req.account)) {
			return null;
		}
		String userIp = BaseApi.getClientIP(request);
		
		return VerifyCodeDao.sendVerifyCode(req,userIp);
	}

}

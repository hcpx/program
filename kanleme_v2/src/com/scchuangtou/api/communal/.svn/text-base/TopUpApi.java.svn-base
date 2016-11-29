package com.scchuangtou.api.communal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.scchuangtou.api.BaseApi;
import com.scchuangtou.config.AccountConfig.AlipayConfig;
import com.scchuangtou.config.AccountConfig.UnionPayConfig;
import com.scchuangtou.config.AccountConfig.WeChatPayConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.TopUpOrderDao;
import com.scchuangtou.entity.BaseResEntity;
import com.scchuangtou.entity.TopUpReqEntity;
import com.scchuangtou.entity.TopUpResEntity;
import com.scchuangtou.pay.AliPayHelper;
import com.scchuangtou.pay.UnionPayHelper;
import com.scchuangtou.pay.WeChatPayHelper;
import com.scchuangtou.utils.StringUtils;

/**
 * Created by SYT on 2016/3/29.
 */
public class TopUpApi extends BaseApi {
	@Override
	protected BaseResEntity action(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = request.getParameter(Config.REQUEST_PARAMETER_DATA);
		if (StringUtils.emptyString(json)) {
			return null;
		}

		TopUpReqEntity req = JSON.parseObject(json, TopUpReqEntity.class);

		if (req == null) {
			return null;
		}
		req.price = Config.parseGold(req.price);
		if (req.price == 0) {
			return null;
		}
		if (req.top_up_purpose == Config.TopUpPurpose.TOP_UP_PURPOSE_ARTICLE_REWARD
				&& StringUtils.emptyString(req.request_data)) {
			return null;
		}
		if (req.top_up_purpose == Config.TopUpPurpose.TOP_UP_PURPOSE_NORMAL && StringUtils.emptyString(req.token)) {
			return null;
		}
		return doPay(request, req);
	}

	private TopUpResEntity doPay(HttpServletRequest request, TopUpReqEntity req) throws Exception {
		TopUpResEntity res = TopUpOrderDao.pay(req);
		if (res == null || !res.isSuccess()) {
			return res;
		}
		String requestUrl = request.getRequestURL().toString();
		String notifyUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/api"));
		if (req.type == Config.PayType.AliPay) {
			notifyUrl = notifyUrl + AlipayConfig.ASYNC_NOTIFY_URL;

			AliPayHelper.AlipayReqParam reqPram = new AliPayHelper.AlipayReqParam();
			reqPram.private_key = AlipayConfig.private_key;
			reqPram.input_charset = Config.CHARSET;
			reqPram.partner = AlipayConfig.partner;
			reqPram.seller = AlipayConfig.SELLER;
			reqPram.orderNo = res.order_number;
			reqPram.subject = req.subject;
			reqPram.body = req.body;
			reqPram.price = req.price;
			reqPram.notifyUrl = notifyUrl;
			reqPram.time_out = AlipayConfig.TIME_OUT;

			res.sign = AliPayHelper.pay(reqPram);
		} else if (req.type == Config.PayType.WeChatPay) {
			notifyUrl = notifyUrl + WeChatPayConfig.ASYNC_NOTIFY_URL;

			WeChatPayHelper.WeChatParam reqPram = new WeChatPayHelper.WeChatParam();
			reqPram.url = WeChatPayConfig.URL;
			reqPram.app_id = WeChatPayConfig.APP_ID;
			reqPram.mch_id = WeChatPayConfig.MCH_ID;
			reqPram.api_key = WeChatPayConfig.API_KEY;
			reqPram.notifyUrl = notifyUrl;
			reqPram.charset = Config.CHARSET;

			reqPram.orderNo = res.order_number;
			reqPram.body = req.body;
			reqPram.price = req.price;
			reqPram.ip = BaseApi.getClientIP(request);

			WeChatPayHelper.WeChatPayRes mWeChatPayRes = WeChatPayHelper.pay(reqPram);
			if (mWeChatPayRes != null) {
				Map<String, Object> weChatMap = new HashMap<String, Object>();
				weChatMap.put("appid", mWeChatPayRes.appid);
				weChatMap.put("noncestr", mWeChatPayRes.noncestr);
				weChatMap.put("partnerid", mWeChatPayRes.partnerid);
				weChatMap.put("prepayid", mWeChatPayRes.prepayid);
				weChatMap.put("package", mWeChatPayRes._package);
				weChatMap.put("sign", mWeChatPayRes.sign);
				weChatMap.put("timestamp", mWeChatPayRes.timestamp);

				res.sign = JSON.toJSONString(weChatMap);
			}
		} else if (req.type == Config.PayType.UnionPay) {
			notifyUrl = notifyUrl + UnionPayConfig.notify_url;

			UnionPayHelper.UnionPayParam param = new UnionPayHelper.UnionPayParam();
			param.price = req.price;
			param.notify_url = notifyUrl;
			param.charset = Config.CHARSET;
			param.orderNo = res.order_number;

			param.merId = UnionPayConfig.merId;
			param.version = UnionPayConfig.version;
			res.sign = UnionPayHelper.pay(param);
		} else {
			return null;
		}
		if (StringUtils.emptyString(res.sign)) {
			res.status = Config.STATUS_SERVER_ERROR;
		}
		return res;
	}
}

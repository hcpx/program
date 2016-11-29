package com.scchuangtou.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.integralwall.sign.DianYueIntegralSign;
import com.integralwall.sign.YouMiIntegralSign;
import com.scchuangtou.config.AccountConfig;
import com.scchuangtou.config.Config;
import com.scchuangtou.dao.IntegralDetailDao;
import com.scchuangtou.utils.LogUtils;
import com.scchuangtou.utils.StringUtils;

public class IntegralWallServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final String RESPONSE_SUCCESS = "success";
	private final String RESPONSE_IP_ERR = "iperr";
	private final String RESPONSE_FAILD = "faild";
	private final String RESPONSE_REPEAT = "repeat";
	private final String RESPONSE_REDUCE = "reduce";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String res = null;
		StringBuffer logStr = new StringBuffer();
		int wallType = -1;
		try {
			logStr.append("\r\n").append("---------------------integralwall start------------------------------");
			String pkg="";
			//包名
			int os=0;
			// 唯一交易订单号
			boolean ipFlage=false;
			String orderid = request.getParameter("order");
			String userid = request.getParameter("snuid");
			String ip=getIpAddress(request);
			logStr.append("\r\n").append("ip:").append(ip);
			if (!StringUtils.emptyString(orderid)) {
				userid = getDecodeParam(request, "user");
				wallType = AccountConfig.IntegralWallConfig.INTEGRALWAll_TYPE_YOUMI;
				for(String trueip:AccountConfig.IntegralWallConfig.YOUMI_IPS){
					if(ip.equals(trueip)){
						ipFlage=true;
						break;
					}
				}
				if(!ipFlage){
					res = RESPONSE_IP_ERR;
					logStr.append("\r\n").append("非正确有米ip地址");
				}
				pkg= getDecodeParam(request, "pkg");
				if(StringUtils.emptyString(pkg))
					os=Config.Os.IOS;
				else
					os=Config.Os.ANDROID;
			} else if(!StringUtils.emptyString(userid)){
				userid = getDecodeParam(request, "snuid");
				String device_id = request.getParameter("device_id");
				String pack_name = request.getParameter("pack_name");
				String trade_type = request.getParameter("trade_type");
				String task_id = request.getParameter("task_id");
				orderid=DianYueIntegralSign.createDianYueOrderid(device_id, pack_name, trade_type, task_id);
				wallType = AccountConfig.IntegralWallConfig.INTEGRALWAll_TYPE_DIANLE;
				if(ip.contains(AccountConfig.IntegralWallConfig.DIANLE_IPS[0])){
					int index =ip.lastIndexOf(".")+1;
					for(int i=Integer.valueOf(AccountConfig.IntegralWallConfig.DIANLE_IPS[1]);i<=Integer.valueOf(AccountConfig.IntegralWallConfig.DIANLE_IPS[2]);i++){
						int ipend=Integer.valueOf(ip.substring(index,ip.length()));
						if(ipend==i){
							ipFlage=true;
							break;
						}
					}
				}
				if(!ipFlage){
					res = RESPONSE_IP_ERR;
					logStr.append("\r\n").append("非正确点乐ip地址");
				}
				if(request.getParameter("app_id").equals(AccountConfig.IntegralWallConfig.DIANLE_ANDROID_APP_ID))
					os=Config.Os.ANDROID;
				else
					os=Config.Os.IOS;
				
			}
			orderid = URLDecoder.decode(orderid, AccountConfig.IntegralWallConfig.CHARSET);
			if (wallType != -1 && ipFlage) {
				logStr.append("\r\n").append("orderid:").append(orderid);
				logStr.append("\r\n").append("walltype:").append(wallType);
				logStr.append("\r\n").append("url:").append(getFullUrl(request));
				HashMap<String, Object> datas = new HashMap<String, Object>();
				if (IntegralDetailDao.checkExist(orderid)) {
					res = RESPONSE_REPEAT;
					logStr.append("\r\n").append("已存在订单号:").append(orderid).append("，重复发送了。");
				} else {
					String points = "0";
					String price="0";
					//getParameterDecode(request, datas);
					if (wallType == AccountConfig.IntegralWallConfig.INTEGRALWAll_TYPE_YOUMI){
						points = getDecodeParam(request, "points");
						price = getDecodeParam(request, "price");
						datas.put("device", getDecodeParam(request, "device"));
					}else if(wallType == AccountConfig.IntegralWallConfig.INTEGRALWAll_TYPE_DIANLE){
						points = getDecodeParam(request, "currency");
						datas.put("device", getDecodeParam(request, "device_id"));
					}
					datas.put("price", price);
					datas.put("orderid", orderid);
					datas.put("points", points);
					datas.put("user_id", userid);
					datas.put("type", wallType);
					datas.put("os", os);
					//验证是否是正确的签名
					if (isTrueSig(wallType, request,os)) {
						//对数据库进行操作，但是不接受积分减少的情况
						if (Integer.valueOf(points) > 0) {
							if (IntegralDetailDao.addIntegralWallByCallBack(datas, wallType,logStr)) {
								logStr.append("\r\n").append("积分更改成功，成功更新数据库状态");
							} else {
								res = RESPONSE_FAILD;
								logStr.append("\r\n").append("积分更改失败，更新数据库失败状态");
							}
						} else {
							res = RESPONSE_REDUCE;
							logStr.append("\r\n").append("减去积分，不需要与第三方进行通讯");
						}
					} else {
						res = RESPONSE_FAILD;
						logStr.append("\r\n").append("链接签名不正确");
					}
				}
			} else {
				if(ipFlage){
					res = RESPONSE_FAILD;
					logStr.append("\r\n").append("不属于点乐或者有米");
				}
			}
			if (res == null)
				res = RESPONSE_SUCCESS;
			logStr.append("\r\n---------------------integralwall end------------------------------");
			LogUtils.log(logStr.toString());
		} catch (Exception e) {
			res = null;
			LogUtils.log(logStr.toString(), e);
		}
		if (!res.equals(RESPONSE_REPEAT)) {
			// 表示接受成功
			response.setStatus(200);
			OutputStream os = response.getOutputStream();
			if(wallType == AccountConfig.IntegralWallConfig.INTEGRALWAll_TYPE_DIANLE)
				os.write("200".getBytes("UTF-8"));
			else
				os.write(res.getBytes("UTF-8"));
			os.flush();
		} else {
			// 订单重复发送了去重
			response.setStatus(403);
			OutputStream os = response.getOutputStream();
			if(wallType == AccountConfig.IntegralWallConfig.INTEGRALWAll_TYPE_DIANLE)
				os.write("403".getBytes("UTF-8"));
			else
				os.write(res.getBytes("UTF-8"));
			os.flush();
		}
	}

	/**
	 * 对已加密参数进行解密
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
//	private static void getParameterDecode(HttpServletRequest req, HashMap<String, Object> datas)
//			throws UnsupportedEncodingException {
//		for (String dataKey : datas.keySet()) {
//			String param = req.getParameter(dataKey);
//			if (!StringUtils.emptyString(param)) {
//				param = URLDecoder.decode(param, AccountConfig.IntegralWallConfig.CHARSET);
//			}
//			datas.put(dataKey, param);
//		}
//	}

	/**
	 * 获取单个字段的decode
	 * 
	 * @param req
	 * @param paramName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String getDecodeParam(HttpServletRequest req, String paramName) throws UnsupportedEncodingException {
		String param = req.getParameter(paramName);
		if (!StringUtils.emptyString(param)) {
			param = URLDecoder.decode(param, AccountConfig.IntegralWallConfig.CHARSET);
		}
		return param;
	}

	/**
	 * 签名是否正确
	 * 
	 * @return
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	private static boolean isTrueSig(int type, HttpServletRequest req,int os) throws MalformedURLException, IOException {
		boolean flage = false;
		if (type == AccountConfig.IntegralWallConfig.INTEGRALWAll_TYPE_YOUMI && os==Config.Os.ANDROID) {
			flage = YouMiIntegralSign.checkYoumiUrlSig(req, AccountConfig.IntegralWallConfig.YOUMI_PRIVATE_KEY);
		} else if(type == AccountConfig.IntegralWallConfig.INTEGRALWAll_TYPE_DIANLE && os==Config.Os.ANDROID){
			flage = DianYueIntegralSign.checkDianYueUrlSig(req, AccountConfig.IntegralWallConfig.DIANLE_PRIVATE_KEY);
		}
		return flage;
	}

	/**
	 * 根据req获取全部的链接url
	 * @param req
	 * @return
	 */
	private static String getFullUrl(HttpServletRequest req) {
		StringBuffer url = new StringBuffer();
		url.append("http://").append(req.getServerName()).append(":").append(req.getServerPort())
				.append(req.getContextPath()).append(req.getServletPath()).append("?").append(req.getQueryString());
		return url.toString();
	}
	
	 /** 
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址; 
     *  
     * @param request 
     * @return 
     * @throws IOException 
     */  
    private final static String getIpAddress(HttpServletRequest request) throws IOException {  
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址 
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("Proxy-Client-IP");               
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_CLIENT_IP");     
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");       
            }  
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
                ip = request.getRemoteAddr();
            }
            ip=ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");  
            for (int index = 0; index < ips.length; index++) {  
                String strIp = (String) ips[index];  
                if (!("unknown".equalsIgnoreCase(strIp))) {  
                    ip = strIp;  
                    break;
                }  
            }  
        }  
        return ip;  
    }
}

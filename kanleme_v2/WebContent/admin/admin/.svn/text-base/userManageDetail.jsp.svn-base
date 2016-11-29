<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%
	pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
	long ctime = System.currentTimeMillis();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>看了么公益后台管理</title>
<link href="${ctx}/admin/css/adminv1.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/admin/css/perfect-scrollbar.css" type="text/css" rel="stylesheet" />
</head>

<body>
<div class="wrap">
    <div class="wrap_r fl">
        <div class="actin">
        	<!--头部-->
        	<div class="actin_top">
            	<h2>用户详情</h2>
                <p>首页/用户管理/用户详情</p>
            </div>
            <!--列表-->
            <div class="actin_list" style="background:#f1f2f7; border-top:none;">
            	<!--用户详情-->
            	<div class="ydetile clearfix">
                	<!--左-->
                	<div class="fl ydetilez">
                    	<!--头部-->
                		<div class="ydetilez_t"><a class="ydetilez_ta" href="javascript:history.go(-1);">返回用户管理</a><span class="ydetilez_ts">用户详情</span></div>
                        <ul class="ydetilez_ul1 clearfix">
                            <li class="fl" id="registration_time"><span>注册时间：</span></li>
                            <li class="fl" id="user_name"><span>用户名：</span></li>
                            <li class="fl" id="nickname"><span>昵称：</span></li>
                            <li class="fl" id="gold"><span>账户余额：</span></li>
                            <li class="fl" ><span>证件类型：</span>身份证</li>
                            <li class="fl" id="id_num"><span>证件号码：</span></li>
                        </ul>
                        <ul class="ydetilez_ul2 clearfix">
                        	<li class="fl" id="phone_number"><span>手机号：</span></li>
                            <li class="fl ydetilez_ul2img" id="imgs"><span>本人身份证照：</span>
                            
                            </li>
                        </ul>
                    </div>
                    <!--右-->
                    <div class="fl ydetiley">
            			<div class="index_bl fl contentHolder ydetileyul1"style="width:100%;">
            				<div class="ydetileyul1_li1">发布的互助项目</div>
                            <ul class="index_bl_ul" id="health_events">
                                
                            </ul>
                            <div class="index_bmli_more1" id="health_events_more" style="display:none">查看更多</div>
                        </div>
                        <div class="index_bl fl contentHolder ydetileyul1" style="width:100%;">
                        	<div class="ydetileyul1_li1">发布的帖子</div>
                            <ul class="index_bl_ul" id="articles">

                            </ul>
                            <div class="index_bmli_more1" id="article_more" style="display:none">查看更多</div>
                        </div>
                        
                    </div>
                </div>
            </div>
        </div> 
    </div>
</div>
<script type="text/javascript" src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="${ctx}/js/perfect-scrollbar.js"></script>
<script type="text/javascript" src="${ctx}/js/cycle.js"></script>
<script type="text/javascript" src="${ctx}/js/raphael.js"></script>
<script type="text/javascript" src="${ctx}/js/adminv1.js"></script>
<script src="${ctx}/js/getQueryString.js" type="text/javascript"></script>
<script type="text/javascript">
	var user_id = getQueryString("user_id");
	
	var event_begin=0;
	var event_count=20;
	var event_datas="";
	
	var article_begin=0;
	var article_count=20;
	var article_datas="";
	
	function loadUserInfo() {
        var requestParams = {
            "type": "admin_users_info",
            'data': '{' + '"user_id":"' + user_id + '"}'
        };
        $.ajax({
            url: "${ctx}/adminapi",
            type: "post",
            data: requestParams,
            dataType: "json",
            success: function (data) {
                if (data != null && data.status == "ok") {
                	$("#registration_time").append(localFormat(data.registration_time));
                    $("#user_name").append(data.user_name);
                    $("#nickname").append(data.nickname);
                    $("#gold").append(data.gold);
                    $("#id_num").append(data.id_num);
                    $("#phone_number").append(data.phone_number);
                    var photos=data.photos;
                    if(photos!=null && photos!='undefined'){
                    	var len = photos.length;
                    	var imgs="";
                    	for (var i = 0; i < len; ++i) {
                    		imgs+="<img name='photos' src='"+photos[i]+"'/>";
                    	}
                    	$("#imgs").append(imgs);
                    	$("img[name='photos']").attr("onerror","javascript:this.src='${ctx}/admin/images/09.png'");
                    }
                }else if (data != null && data.status == "server_error") {
					alert("服务器内部错误。");
				}else if (data != null && data.status == "parameter_error") {
					alert("发送参数错误。");
				}else if (data != null && data.status == "permission_error") {
					alert("没有权限。");
				}else if (data != null && data.status == "not_exits") {
					alert("用户不存在。");
				}else if (data != null && data.status == "token_error") {
					alert("登陆超时,请重新登陆。");
				}
            },
            error: function () {
            	alert("服务器内部错误。");
            }
        });
    }
	
	function loadEvents(user_id,count,begin) {
        var requestParams = {
            "type": "admin_get_user_health_events",
            'data': '{' + '"user_id":"' + user_id + '","count":'+count+',"begin":'+begin+'}'
        };
        $.ajax({
            url: "${ctx}/adminapi",
            type: "post",
            data: requestParams,
            dataType: "json",
            success: function (data) {
                if (data != null && data.status == "ok") {
                    var infos=data.infos;
                    var hasMoreData = data.has_more_data;
                    if(infos!=null && infos!='undefined'){
                    	var len = infos.length;
                    	event_begin = event_begin+len;
                    	for (var i = 0; i < len; ++i) {
                    		var title="暂无标题";
                    		if(infos[i].event_title!=null && infos[i].event_title!='undefined'){
                    			title=infos[i].event_title;
                    		}
                    		event_datas+="<li class='clearfix'><span class='ydetileyul1s1 fl'>"+title+"</span>";
                    		event_datas+="<span class='fr'>"+localFormat(infos[i].publish_time)+"</span></li>";
                    	}
                    	$("#health_events").append(event_datas);
                    	if (hasMoreData) {
             	            $("#health_events_more").css('display', 'block');
             	        } else {
             	            $("#health_events_more").css('display', 'none');
             	        }
                    }
                }else if (data != null && data.status == "server_error") {
					alert("服务器内部错误。");
				}else if (data != null && data.status == "parameter_error") {
					alert("发送参数错误。");
				}else if (data != null && data.status == "permission_error") {
					alert("没有权限。");
				}else if (data != null && data.status == "not_exits") {
					alert("用户不存在。");
				}else if (data != null && data.status == "token_error") {
					alert("登陆超时,请重新登陆。");
				}
            },
            error: function () {
            	alert("服务器内部错误。");
            }
        });
    }
	
	function loadArticles(user_id,count,begin) {
		var requestParams = {
	            "type": "admin_get_my_articles",
	            'data': '{' + '"user_id":"' + user_id + '","count":'+count+',"begin":'+begin+'}'
	        };
	        $.ajax({
	            url: "${ctx}/adminapi",
	            type: "post",
	            data: requestParams,
	            dataType: "json",
	            success: function (data) {
	                if (data != null && data.status == "ok") {
	                    var infos=data.article;
	                    var hasMoreData = data.has_more_data;
	                    if(infos!=null && infos!='undefined'){
	                    	var len = infos.length;
	                    	article_begin=article_begin+len;
	                    	for (var i = 0; i < len; i++) {
	                    		article_datas+="<li class='clearfix'><span class='ydetileyul1s1 fl'>"+infos[i].article_title+"</span>";
	                    		article_datas+="<span class='fr'>"+localFormat(infos[i].article_time)+"</span></li>";
	                    	}
	                    	$("#articles").append(article_datas);
	                    	if (hasMoreData) {
	             	            $("#article_more").css('display', 'block');
	             	        } else {
	             	            $("#article_more").css('display', 'none');
	             	        }
	                    }
	                }else if (data != null && data.status == "server_error") {
						alert("服务器内部错误。");
					}else if (data != null && data.status == "parameter_error") {
						alert("发送参数错误。");
					}else if (data != null && data.status == "permission_error") {
						alert("没有权限。");
					}else if (data != null && data.status == "not_exits") {
						alert("用户不存在。");
					}else if (data != null && data.status == "token_error") {
						alert("登陆超时,请重新登陆。");
					}
	            },
	            error: function () {
	            	alert("服务器内部错误。");
	            }
	      });
    }
	
	$("#health_events_more").click(function() {
        $("#health_events_more").css('display', 'none');
        loadEvents(user_id,event_count,event_begin);
    });
	
	$("#article_more").click(function() {
        $("#article_more").css('display', 'none');
        loadArticles(user_id,article_count,article_begin);
    });

	$(function () {
		loadUserInfo();
		loadEvents(user_id,event_count,event_begin);
		loadArticles(user_id,article_count,article_begin);
	});
	
    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
    function localFormat(time) {
        return new Date(time).Format("yyyy-MM-dd hh:mm:ss");
    }
</script>
</body>
</html>

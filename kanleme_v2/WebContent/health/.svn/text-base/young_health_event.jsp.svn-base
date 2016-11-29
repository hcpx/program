<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%
	pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>
<!DOCTYPE html>
<html lang="zh">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width" />
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
<title>健康互助事件</title>
<link href="css/health.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="yhe_wrap">
	<p class="shuju_jiazai">数据加载中......</p>
<!--加载更多-->
<button id="loadMoreBtn" class="yhe_more" style="display:none;" onClick="loadMore()">查看更多</button>
</div>
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script src="${ctx}/js/getQueryString.js" type="text/javascript"></script>
<script type="text/javascript" src="js/health.js"></script>
<script type="text/javascript">
	var begin = 0;
	var count = 20;
	var has_more_data = false;
	var data="";
	var health_project_id = getQueryString("health_project_id");
	
	$(document).ready(function() {
		loadData(begin, loadDataCallback);
	});
	
	function loadMore(){
		loadData(begin, loadDataCallback);
	}
	
	
	function loadDataCallback(json) {
		var datas = json.events;
		var length = datas.length;
		var loadmore=$("#loadMoreBtn");
		if (datas != null && datas != undefined && length > 0) {
			var dataHtml = "<ul class='yhelist'>";
			for (var i = 0; i < length; ++i) {
				data += "<li class='yhelistli'>";
				data += "<div class='yhelistli_d1 clearfix'>";
				data += "<img class='yhelistli_d1z fl' src='"+datas[i].event_img+"'/>";
				data += "<div class='yhelistli_d1y fr'>";
				data += "<h1 class='yhelistli_d1yh1'>"+datas[i].event_title+"</h1>";
				data += "<p class='yhelistli_d1yp1'>编号："+datas[i].health_project_event_id+"</p>";
				if(datas[i].event_infomation != null && datas[i].event_infomation != undefined && datas[i].event_infomation.length > 0)
					data += "<p class='yhelistli_d1yp2'>"+datas[i].event_infomation.substring(0,25)+"......</p></div></div>";
				else
					data += "<p class='yhelistli_d1yp2'></p></div></div>";
				data += "<div class='yhelistli_d2 clearfix'><span class='yhelistli_d2s fl'><img src='images/ic_time.png' />"+localFormat(datas[i].publish_time)+"</span>";
				data += "<a class='yhelistli_d2a fr' href='young_health_event_details.jsp?health_project_event_id="+datas[i].health_project_event_id+"'>详情<img src='images/jiantou.png' /></a>";
				data += "</div></li>";
			}
			dataHtml = dataHtml+data+"</ul>";
			$(".yhe_wrap").empty();
		    $(".yhe_wrap").html(dataHtml).append(loadmore);
		    begin=begin+count;
		}else if(begin==0){
			$(".shuju_jiazai").empty();
		    $(".shuju_jiazai").html("暂无数据......");
		}

		hasMoreData = json.has_more_data;
		if (hasMoreData) {
			$("#loadMoreBtn").css('display', 'block');
		} else {
			$("#loadMoreBtn").css('display', 'none');
		}
	}

	function loadData(begin, callback) {
			var requestParam = {
				"type" : "web_get_health_project_events",
				'data' : '{'
	                + '"health_project_id":"'+health_project_id+'",'
	                + '"count":'+count+','
	                + '"begin":' + begin
	                + '}'
			};
			$.ajax({
			url : "${ctx}/api",
			type : "post",
			data : requestParam,
			dataType : "json",
			success : function(data) {
				if (data != null && data.status == "ok") {
					callback(data);
				}
			},
			error : function() {
			}
		});
	}
	
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
        return new Date(time).Format("yyyy-MM-dd");
    }
</script>
</body>
</html>

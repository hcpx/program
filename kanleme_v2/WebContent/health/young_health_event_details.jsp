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
<title>健康互助事件详情</title>
<link href="css/health.css" rel="stylesheet" type="text/css" />
</head>

<body>
    <p id="has_no" class="shuju_jiazai" style="display:none;">该互助事件已不存在！</p>
    <p id="loading" class="shuju_jiazai">数据加载中！</p>
<div class="yhed_wrap" style="display: none">
<div class="yhed1">
	<img src="images/img_head_1.png" />
    <p>李小丽 女</p>
</div>


</div>
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/health.js"></script>
<script src="${ctx}/js/getQueryString.js" type="text/javascript"></script>
<script type="text/javascript">
	var health_project_event_id = getQueryString("health_project_event_id");
	function datamap(key,value)
	{
	this.key=key;
	this.value=value;
	}
	
	$(document).ready(function() {
		loadData(loadDataCallback);
	});
	
	function loadDataCallback(json) {
		if (json != null && json != undefined) {
			$(".yhed_wrap").css('display','block');
			var data = "";
			var sex=json.sex==0?'男':'女';
			data += "<div class='yhed1'><img src='images/img_head_1.png' /><p>"+json.name+" "+sex+"</p></div>";
			var datas=[];
			datas.push(new datamap('互助事件编号',json.health_project_event_id));
			datas.push(new datamap('互助计划',json.health_project_name));
			datas.push(new datamap('年龄',json.age));
			datas.push(new datamap('户籍',json.family_register));
			datas.push(new datamap('加入时间：',json.join_time));
			datas.push(new datamap('生效时间：',json.effect_time));
			datas.push(new datamap('参与互助人数：',json.participate_member));
			datas.push(new datamap('互助金额：',json.total_money));
			datas.push(new datamap('公示日期：',json.notice_time));
			datas.push(new datamap('扣款日期：',json.cut_payment_time));
			data += "<ul class='yhed2'>";
			for(var i=0;i<datas.length;i++){
				if (datas[i].key != null && datas[i].key != undefined) {
					data += "<li><b>"+datas[i].key+"</b>";
				}
				if (datas[i].value != null && datas[i].value != undefined) {
					if(datas[i].key.indexOf("时间")>=0 || datas[i].key.indexOf("日期")>=0)
						data += "<span>"+localFormat(datas[i].value)+"</span></li>";
					else
						data += "<span>"+datas[i].value+"</span></li>";
				}
			}
			data += "</ul><div class='yhed3'><div class='yhed3_d1 clearfix'><img class='fl' src='images/ic_time_ac.png' /><span class='fl'>时间情况</span></div>";
			var event_infomation="";
			if (json.event_infomation != null && json.event_infomation != undefined) {
				event_infomation=contentFormat(json.event_infomation);
			}
			data += "<div class='yhed3_d2'><p>"+event_infomation+"</p></div></div>";
			data += "</ul><div class='yhed3'><div class='yhed3_d1 clearfix'><img class='fl' src='images/ic_investigation.png' /><span class='fl'>调查过程</span></div>";
			var survey_infomation="";
			if (json.survey_infomation != null && json.survey_infomation != undefined) {
				survey_infomation=contentFormat(json.survey_infomation);
			}
			data += "<div class='yhed3_d2'><p>"+survey_infomation+"</p></div></div>";
			data += "</ul><div class='yhed3'><div class='yhed3_d1 clearfix'><img class='fl' src='images/ic_mutual.png' /><span class='fl'>互助详情</span></div>";
			var help_each_detail="";
			if (json.help_each_detail != null && json.help_each_detail != undefined) {
				help_each_detail=contentFormat(json.help_each_detail);
			}
			data += "<div class='yhed3_d2'><p>"+help_each_detail+"</p></div></div>";
			data += "<div class='yhed3'><div class='yhed3_d1 clearfix'><img class='fl' src='images/ic_material.png' /><span class='fl'>相关资料</span></div><div class='yhed3_d2'><ul class='yhed3_d2ul'>";
			data += "<li class='fl'><img src='images/d_image2.png' /></li><li class='fl yhed3_d2ullim'><img src='images/d_image2.png' /></li><li class='fl'><img src='images/d_image2.png' /></li></ul><ul class='yhed3_d2ul'>";
			data += "<li class='fl'><img src='images/d_image2.png' /></li><li class='fl yhed3_d2ullim'><img src='images/d_image2.png' /></li><li class='fl'><img src='images/d_image2.png' /></li></ul></div></div>";
			$(".yhed_wrap").empty();
		    $(".yhed_wrap").html(data);
		    $("#loading").css('display', 'none');
		}
	}

	function loadData(callback) {
		var requestParam = {
				"type" : "web_get_health_project_event_detail",
				'data' : '{'
	                + '"health_project_event_id":"'+health_project_event_id+'"'
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
				}else{
					$("#loading").css('display', 'none');
					$("#has_no").css('display', 'block');
				}
			},
			error : function() {
				$("#loading").css('display', 'none');
				$("#has_no").css('display', 'block');
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
    
    function contentFormat(content) {
    	content=content.replace(/\r\n/g,"</br>");
    	content=content.replace(/\n/g,"</br>");
    	content=content.replace(/\r/g,"</br>");
        return content;
    }
</script>
</body>
</html>

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
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<title></title>
<link href="css/subject.css" rel="stylesheet" type="text/css" />
</head>

<body>
<!--banner和正文-->
<div class="su_top">
<img id="icon" class="su_top_ba" src="" />
<ul class="su_topul">
    <li id="content">&nbsp;</li>
</ul>
</div>
<!--图文阐述-->
<ul class="su_con"></ul>
<script type="text/javascript" src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script src="${ctx}/js/getQueryString.js" type="text/javascript"></script>
<script type="text/javascript">
	var subject_id = getQueryString("subject_id");
	var token = getQueryString("token");
	$(document).ready(function() {
		loadData(loadDataCallback);
	});
	
	function loadDataCallback(json) {
		if (json.subject != null && json.subject != undefined) {
			if (json.subject.subject_icon != null && json.subject.subject_icon != undefined) {
				$("#icon").attr("src",json.subject.subject_icon);
			}else{
				$("#icon").attr("src","images/zhun01.png");
			}
			var content=json.subject.subject_content;
			content=content.replace(/\r\n/g,"</br>");
			content=content.replace(/\n/g,"</br>");
			content=content.replace(/\r/g,"</br>");
			$("#content").html(content);
		    
			var data = "";
			for(var i=0;i<json.subject.subject_pic.length;i++){
				 data += "<li><img src="+json.subject.subject_pic[i].subject_image_url+"/>";
				 data += "<p>"+json.subject.subject_pic[i].subject_image_description+"</p> </li>";
			}
		    $(".su_con").append(data);
		    
		    $("#loading").css('display', 'none');
		}
	}

	function loadData(callback) {
		var requestParam = {
				"type" : "web_get_subject_info",
				'data' : '{'
					+ '"token":"'+token+'",'
	                + '"subject_id":"'+subject_id+'"'
	                + '}'
			};
			$.ajax({
			url : "${ctx}/api",
			type : "post",
			data : requestParam,
			dataType : "json",
			async: false,
			success : function(data) {
				var title = '{"status": "'+data.status+'"';
				if(data.subject != null){
					title = title+',"subject":{';
					title = title+'"subject_id": "'+data.subject.subject_id+'",';
					if(data.subject.subject_collecteid != null && data.subject.subject_collecteid != undefined)
						title = title+'"subject_collecteid": "'+data.subject.subject_collecteid+'",';
					if(data.subject.article_praiseid != null && data.subject.article_praiseid != undefined)
						title = title+'"subject_praiseid": "'+data.subject.subject_praiseid+'",';
					title = title+'"subject_title": "'+data.subject.title+'"';
					title = title+'}';
				}
				title = title+'}';
				document.title = title;
				if (data != null && data.status == "ok") {
					callback(data);
				}
			}
		});
	}
</script>
</body>
</html>

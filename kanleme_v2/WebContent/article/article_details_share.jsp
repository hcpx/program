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
<title>帖子详情</title>
<link href="css/tiezi.css" rel="stylesheet" type="text/css" />
</head>

<body>
<div class="td_warp">
<!--上方帖子内容-->
<div class="td_top">
    <p id="has_no" class="shuju_jiazai" style="display:none;">该帖子已不存在！</p>
    <p id="loading" class="shuju_jiazai">数据加载中！</p>
    <h1 class="td_top1">&nbsp;</h1>
    <div class="td_top2 clearfix">
    	<div class="td_top2z fl clearfix">
    		<img id="head_pic"class="td_top2zimg1 fl" />
            <img class="td_top2zimg2 fl" src="images/touxiang.png" />
            <span id="name" class="fl"></span>
        </div>
        <div class="td_top2y fr">
        	<span class="td_top2ys1"><img src="images/ic_look.png" /></span>
            <span class="td_top2ys2"><img src="images/ic_say.png" /></span>
        </div>
    </div>
    <ul id="label" class="td_top3 clearfix">
    </ul>
    <div id="pics" class="td_top4">
    <p id="content" style="margin-bottom:22px;">&nbsp;</p>
    </div>
</div>
<div class="liukong"></div>
<a class="liji_join" href="http://kanleme.scchuangtou.com/">进入官网</a>
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/tiezi.js"></script>
<script src="${ctx}/js/getQueryString.js" type="text/javascript"></script>
</div>
<script type="text/javascript">
	var article_id = getQueryString("article_id");
	
	$(document).ready(function() {
		loadData(loadDataCallback);
	});
	
	function loadDataCallback(json) {
		var datas = json.article;
		if (datas != null && datas != undefined) {
			$("#loading").css('display', 'none');
		    $(".td_top1").html(datas.article_title);
		    
		    var head="images/img_touxiang.png";
		    $("#head_pic").attr("onerror","javascript:this.src='images/img_touxiang.png'");
		    if (datas.user_info.head_pic != null && datas.user_info.head_pic != undefined) { 
		    	head = datas.user_info.head_pic;
			}
		    $("#head_pic").attr("src",head);
		    
		    var nickname="";
		    if (datas.user_info.nickname != null && datas.user_info.nickname != undefined) {
		    	nickname=datas.user_info.nickname;
			}
		    $("#name").html(nickname);
		    
		    $(".td_top2ys1").append(datas.view_count);
		    $(".td_top2ys2").append(datas.comment_num);
		    
		    var label="";
		    if (datas.article_label != null && datas.article_label != undefined && datas.article_label.length > 0) {
				for (var i = 0; i < datas.article_label.length; ++i) {
					label += "<li class='fl'><img src='images/ic_label.png' /><span>"+datas.article_label[i]+"</span></li>";
				}
			}
		    $("#label").append(label);
		    var content=datas.article_content;
			content=content.replace(/\r\n/g,"</br>");
			content=content.replace(/\n/g,"</br>");
			content=content.replace(/\r/g,"</br>");
		    $("#content").html(content);
		    var pics="";
		    if (datas.article_pic != null && datas.article_pic != undefined && datas.article_pic.length > 0) {
				for (var i = 0; i < datas.article_pic.length; ++i) {
					pics += "<img class='content_img' src='"+datas.article_pic[i].article_image_url +"'/>";
					pics += "<p>"+datas.article_pic[i].article_image_description+"</p>";
				}
			}
		    $("#pics").append(pics);
		    $('#pics img').removeClass('content_img')
		    
		}
	}

	function loadData(callback) {
		var requestParam = {
				"type" : "web_get_article_info",
				'data' : '{'
	                + '"article_id":"'+article_id+'"'
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
</script>
</body>
</html>

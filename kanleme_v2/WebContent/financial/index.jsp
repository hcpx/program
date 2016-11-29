<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%
    pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8"/>
    <title>国立看了么财务后台</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <link href="${ctx}/financial/images/logo.ico" rel="shortcut icon">

    <link rel="stylesheet" href="${ctx}/financial/css/global.css" type="text/css" />
    <link rel="stylesheet" href="${ctx}/financial/css/framework.css" type="text/css" />
    <link rel="stylesheet" href="${ctx}/financial/css/base.css" type="text/css" />

    <script src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.js" type="text/javascript"></script>
    <script src="${ctx}/js/getQueryString.js" type="text/javascript"></script>
</head>
<body>

<div class="left">
		<div class="wrap_lt clearfix">
        	<div class="fl wrap_ltz">
            	<img src="images/01.png" />
            </div>
            <div class="fl wrap_lty">
                <span class="wrap_lty1">国立看了么</span>
            </div>
        </div>
	<ul id="side_nav"></ul>
</div>
<div class="right">
	<div class="top">
		<div>
			<div class="wrap_rt clearfix">
        	<h2 class="wrap_rtz fl">国立看了么财务后台</h2>
            <ul class="wrap_rty fr">
            	<li class="fl wrap_rty1">欢迎使用国立看了么财务后台</li>
                <li class="fl wrap_rty2"><a class="dian_messge" href="javascript:;">消息<span id="message_num">0</span></a></li>
                <li class="fl wrap_rty3"><a href="javascript:;">项目<span id="health_project_num">0</span></a></li>
                <li class="fl wrap_rty4"><a href="javascript:;" onclick="logout()">退出</a></li>
                <li class="CL"></li>
            </ul>
        </div>	
			<div class="cls"></div>
			<span id="children_amount" style="display:none">0</span><span id="youth_amount" style="display:none">0</span>
			<span id="all_amount" style="display:none">0</span><span id="all_user" style="display:none">0</span>
			<span id="health_amount" style="display:none">0</span>
		</div>
	</div>
	<iframe id="main" width="100%" height="100%" src="homepage.jsp" frameborder="0"></iframe>
</div>
<script>
	
	var ctx = "${ctx}";

	//region 窗口大小控制
	var width = $(window).width();
	var height = $(window).height();
	var height_main = $(window).height()-100;
	var $main = $("#main");
	

	$(".right").css("width", width - $(".left").width());
	$(".left, .right").css("height", height);
	$main.css("height", height_main);

	$(window).resize(function() {
		$(".right").css("width", $(window).width() - $(".left").width());
		$(".left, .right").css("height", $(window).height());
		$main.css("height", height_main);
	});
	//endregion
	
	//region 导航菜单加载
	var navMenuHtml = "";
	var jsonUrl = "${ctx}/financial/side_nav.json";

	$.getJSON(jsonUrl, function(json) {
		var menus = null;
		menus = json.menus;
		var $sideNav = $("#side_nav");

		if (menus) {
			loadData();
			loadNavMenu(menus);

			$sideNav.html(navMenuHtml);

			$("#side_nav > li").addClass("side_nav_directly_li");

			$("#side_nav > li[top]>div").click(function(event) {
				$(this).parent().toggleClass("hover");
			});
			$('#side_nav').find('li').eq(0).addClass("focus");
			$("#side_nav>li[target]").click(function() {
				$("#side_nav li.hover").removeClass("hover");
				$("#side_nav li[target]").removeClass("focus");
				$(this).addClass("focus")
			});

			$("#side_nav .nav_child_menu li[target]").click(function() {
				$("#side_nav li[target]").removeClass("focus");
				$(this).addClass("focus")
			});

			$("#side_nav li[target]").click(function() {
				var uri = $(this).attr("target");

				$main.prop("src", uri);
			});
		}
		//点击消息的时候
		$('.dian_messge').click(function(){
			$('#main').prop('src','showUserWithdrawals.jsp');
			$('#side_nav').find('li').removeClass("focus");
			$('#side_nav').find('li').removeClass("hover");
			$('#side_nav').find('li').eq(3).addClass("focus");
		})
	});

	function loadNavMenu(menus) {
		var length = menus.length;

		for (var i = 0; i < length; ++i) {
			var menu = menus[i];

			if (menu.child) {
				var name = menu.name;
				var uri = menu.uri;

				navMenuHtml += "<li top><div>" + name
						+ "</div><ul class='nav_child_menu'>";

				loadNavMenu(menu.child, navMenuHtml);

				navMenuHtml += "</ul></li>";
			} else {
				var name = menu.name;
				var uri = menu.uri;
				var level = menu.level;

				if (level) {
					navMenuHtml += "<li style='padding-left: " + level * 25 + "px;' target='" + ctx + uri + "'>"
							+ name + "</li>";
				} else {
					navMenuHtml += "<li target='" + ctx + uri + "'>" + name
							+ "</li>";
				}
			}
		}
	}
	//endregion
	function logout() {
		var uri = "${ctx}/logout";
		$.get(uri, function(data) {
			if (data != null && data == "ok") {
				window.location.href = "${ctx}/financial/login.jsp";
			} else {
				alert("服务器内部错误，登录失败");
			}
		});
	}
	
	function loadData() {
		var requestParam = {
			"type" : "get_common_data",
			'data' : '{' + '"financial_token":""'+'}'
		};
		$.ajax({
			url : "${ctx}/financialapi",
			type : "post",
			data : requestParam,
			dataType : "json",
			async:false,
			success : function(data) {
				$("#loading").empty();
				if (data != null && data.status == "ok") {
					$("#message_num").html(data.message_num);
					$("#health_project_num").html(data.health_project_num);
					$("#children_amount").html(data.children_amount);
					$("#youth_amount").html(data.youth_amount);
					$("#all_amount").html(data.all_amount);
					$("#health_amount").html(data.health_amount);
					$("#all_user").html(data.all_user);
					$("#loadMoreBtn").css('display', 'block');
					$("#loadMoreBtn").css('display', 'block');
				}else if (data != null && data.status == "server_error") {
					alert("服务器内部错误。");
				}else if (data != null && data.status == "parameter_error") {
					alert("发送参数错误。");
				}else if (data != null && data.status == "password_error") {
					alert("密码错误。");
				}else if (data != null && data.status == "token_error") {
					alert("登陆超时,请重新登陆。");
				}
			},
			error : function() {
				alert("服务器内部错误。");
			}
		});
	}
</script>

</body>
</html>
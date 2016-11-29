<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%
	pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="UTF-8" />
<title>看了么后台管理系统</title>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<link href="${ctx}/admin/images/logo.ico" rel="shortcut icon">

<link rel="stylesheet" href="${ctx}/admin/css/global.css"
	type="text/css">
<link rel="stylesheet" href="${ctx}/admin/css/framework.css"
	type="text/css">
<link rel="stylesheet" href="${ctx}/admin/css/adminv1.css" type="text/css" />

<script src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script src="${ctx}/js/getQueryString.js"></script>
<script src="${ctx}/js/jquery.cookie.js"></script>
<script src="${ctx}/admin/js/admin_info.js"></script>
</head>
<body>

	<div class="left">
		<h4>
			Hi, <span id="user_name"></span>
		</h4>
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
		<iframe id="main" width="100%" height="100%" src="" frameborder="0"></iframe>
	</div>

	<script>
		var userinfo = getUserInfo();
		if(userinfo){
			$("#user_name").html(userinfo.username);
		}
		var ctx = "${ctx}";
		//region 窗口大小控制
		var width = $(window).width();
		var height = $(window).height();
		var $main = $("#main");

		$(".right").css("width", width - $(".left").width());
		$(".left, .right").css("height", height);

		$(window).resize(function() {
			$(".right").css("width", $(window).width() - $(".left").width());
			$(".left, .right").css("height", $(window).height());
		});
		//endregion

		//region 导航菜单加载
		var navMenuHtml = "";

		var jsonUrl = "${ctx}/admin/side_nav.json";

		$.getJSON(jsonUrl, function(json) {
			var menus = null;
			menus = json.menus;
			var $sideNav = $("#side_nav");

			if (menus) {
				loadNavMenu(getUserModes(), menus);

				$sideNav.html(navMenuHtml);

				$("#side_nav > li").addClass("side_nav_directly_li");

				$("#side_nav > li[top]>div").click(function(event) {
					$(this).parent().toggleClass("hover");
				});

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
		});

		function loadNavMenu(usermodes, menus) {
			var length = menus.length;

			for (var i = 0; i < length; i++) {
				var menu = menus[i];
				if (menu.child) {
					var name = menu.name;
					var uri = menu.uri;
					if ('个人中心' != name) {
						var flag = false;
						if (usermodes) {
							A: for (var x = 0; x < menu.child.length; x++) {
								for (var j = 0; j < usermodes.length; j++) {
									if (menu.child[x].name == usermodes[j].name) {
										flag = true;
										break A;
									}
								}
							}
						}
						if (!flag) {
							continue;
						}
					}
					navMenuHtml += "<li top><div>" + name
							+ "</div><ul class='nav_child_menu'>";

					loadNavMenu(usermodes, menu.child);

					navMenuHtml += "</ul></li>";
				} else {
					var name = menu.name;
					var uri = menu.uri;
					if ('主页' != name && '资料修改' != name && '密码修改'!=name) {
						var flag = false;
						if (usermodes) {
							for (var x = 0; x< usermodes.length; x++) {
								if (name == usermodes[x].name) {
									flag = true;
									uri = usermodes[x].uri;
									break;
								}
							}
						}
						if (!flag) {
							continue;
						}
					}
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
					logout();
					window.location.href = "${ctx}/admin/login.jsp";
				} else {
					alert("服务器内部错误，登录失败");
				}
			});
		}
	</script>

</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%
	pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="UTF-8" />
<title>国立看了么财务后台</title>

<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<link href="${ctx}/financial/${ctx}/financial/images/logo.ico" rel="shortcut icon">
<link rel="stylesheet" href="${ctx}/financial/css/global.css" type="text/css">
<link rel="stylesheet" href="${ctx}/financial/css/finance_login.css" type="text/css">
</head>
<style>
.zhezhao{display:none;z-index:99; width:100%; height:100%; background-color:rgba(0,0,0,0.2); position:absolute; top:0; left:0;filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#19000000,endColorstr=#19000000);}
.loading{display:none;position:fixed;width:200px;height:200px; z-index:999; text-align:center;top:50%; margin-top:-100px;left:50%; margin-left:-100px;}
.loading img{width:100px; height:auto; margin:0 auto;}
.loading span{color:#000;display:block;width:100%;height:40px;line-height:40px;font-size:14px; text-align:center;}
</style>
<body>
<div class="zhezhao"></div>
<div class="loading"><img src="images/loading.gif" /><span>登录中...</span></div>
	<div class="container" style=" padding:24px;height:auto;">
		<div class="login-nav">
        	<img src="${ctx}/financial/images/finance_logo.png" />
			<!--<button id="adminLoginNav" class="nav-button-hover b-r-line"
				style="width: 100%;">国立看了么财务管理系统</button>!-->
		</div>
		<div class="login-box">
        	<div class="d_title" style="text-align:center;"><span>财务人员登录</span></div>
        	<div class="cls"><p id="tip" class="error-notic">用户名或者密码错误</p></div>
            <div class="d_con">
				<div class="input-group">
				<div class="input-field">
					<label class="d_con_phone"><img src="${ctx}/financial/images/invite_phone.png" /></label> <input id="userName" class="text-input"
						onblur="effectUserName(this)" onkeyup="effectUserName(this)" placeholder="请输入用户名">
				</div>
				<p id="userNameErrorNotic" class="error-notic">用户名不能为空!</p>
			</div>
				<div class="input-group">
				<div class="input-field">
					<label class="d_con_pwd"><img src="${ctx}/financial/images/invite_pwd.png" /></label> <input id="password"
						class="text-input" type="password" onblur="effectPassword(this)"
						onkeyup="effectPassword(this)" placeholder="请输入密码">
				</div>
				<p id="passwordErrorNotic" class="error-notic">密码不能为空!</p>
			</div>
				<div>
				<div class="id_yan">
					<label><img src="${ctx}/financial/images/finance_tuyan.png" /></label> <input placeholder="请输入图中验证码" id="imageCode" type="text" maxlength="20"
						onblur="effectVerifyCode(this)" onkeyup="effectVerifyCode(this)"> <img
						id="imageVerifyCode" onclick="loadImageCode()"
						style="cursor: pointer;vertical-align: middle;" src="${ctx}/imagecode">
				</div>
				<span id="imageVerifyCodeErrorNotic" class="error-notic"
					style="display: none;">图片验证码不能为空！</span>
			</div>
            </div>
		</div>
		<div class="from-submit" style="position:relative;margin-top:20px;">
			<button id="loginBtn">登&nbsp;&nbsp;&nbsp;录</button>
		</div>
	</div>
</body>
</html>
<script src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.js" type="text/javascript"></script>
<script src="${ctx}/js/md5.js"></script>
<script>
/*loding加载:*/
	var window_w=$(window).width();
	var window_h=$(window).height();
	$(function() {
		if(top.location!=self.location)
	    {
	        top.location=self.location;
	    }
	});
	
	$('.zhezhao').css('height',window_h)
	
	var ctx = "${ctx}";

	$(document).keyup(function(event) {
		if (event.keyCode == 13) {
			$("#loginBtn").click();
		}
	});

	$(".login-nav button").click(
			function() {
				var $button = $(this);

				$button.removeClass("nav-button-no-hover").addClass(
						"nav-button-hover");
				$button.siblings(".nav-button-hover").removeClass(
						"nav-button-hover").addClass("nav-button-no-hover");
			});
	function loadImageCode() {
		document.getElementById("imageVerifyCode").src = document
				.getElementById("imageVerifyCode").src
				+ "?nocache=" + new Date().getTime();
	};
	function effectUserName(arg) {
		$("#userNameErrorNotic").hide();
		$("#tip").hide();
	}
	function effectPassword(arg) {
		$("#passwordErrorNotic").hide();
		$("#tip").hide();
	}
	function effectVerifyCode(arg) {
		$("#imageVerifyCodeErrorNotic").hide();
	}
	$("#loginBtn").click(function() {
		var $userName = $("#userName");
		var $password = $("#password");
		var user_name = $userName.val();
		var password = $password.val();
		var image_verify_code = $("#imageCode").val();
		if (user_name == "") {
			$("#userNameErrorNotic").html("请输入用户名").show();
			return;
		}
		if (password == "") {
			$("#passwordErrorNotic").html("请输入密码").show();
			return;
		}
		if (image_verify_code == "") {
			$("#imageVerifyCodeErrorNotic").html("请输入验证码").show();
			return;
		}
		
		var uri = ctx + "/financialapi";
		var type = "financial_login";
		var param = {
			uri : uri,
			user_name : user_name,
			password : password,
			image_verify_code : image_verify_code,
			type : type
		};
		
		login(param);
	});

	function login(param) {
		$('.zhezhao').show();
		$('.loading').show();
		$.ajax({
			url : param.uri,
			type : "post",
			data : {
				"type" : param.type,
				'data' : '{"user_name":"' + param.user_name + '",'
						+ '"image_verify_code":"' + param.image_verify_code + '",'
						+ '"password":"' + hex_md5(param.password) + '"}'
			},
			dataType : "json",
			success : function(data) {
				$('.zhezhao').hide();
				$('.loading').hide();
				if (data != null && data.status == "ok") {
					window.location.href = "${ctx}/financial/index.jsp";
				}else if(data.status == "verify_code_error"){
					loadImageCode();
					$("#imageVerifyCodeErrorNotic").html("验证码错误").show();
				} else if (data.status == "password_error") {
					loadImageCode();
					$("#tip").html("用户名或密码错误").show();
				} else {
					alert("服务器内部错误，登录失败" + data.status);
				}
			},
            error: function () {
            	$('.zhezhao').hide();
				$('.loading').hide();
                alert("服务器内部错误，登录失败");
            }
		});
	}
</script>

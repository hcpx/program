<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%
	pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>
<html lang="zh-cn">
<head>
 <meta charset="UTF-8"/>
 <title>登录</title>

 <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
 <link href="${ctx}/admin/images/logo.ico" rel="shortcut icon">
 <link rel="stylesheet" href="${ctx}/admin/css/global.css" type="text/css">
 <link rel="stylesheet" href="${ctx}/admin/css/admin_now_login.css" type="text/css">
 <style type="text/css">
.zhezhao{display:none;z-index:99; width:100%; height:100%; background-color:rgba(0,0,0,0.2); position:absolute; top:0; left:0;filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=#19000000,endColorstr=#19000000);}
.loading{display:none;position:fixed;width:200px;height:200px; z-index:999; text-align:center;top:50%; margin-top:-100px;left:50%; margin-left:-100px;}
.loading img{width:100px; height:auto; margin:0 auto;}
.loading span{color:#000;display:block;width:100%;height:40px;line-height:40px;font-size:14px; text-align:center;}
</style>
</head>
<body>
<div class="zhezhao"></div>
<div class="loading"><img src="images/loading.gif" /><span>登录中...</span></div>
<div class="container" style=" padding:24px;height:auto;">
    <div class="login-nav">
    	<img src="${ctx}/admin/images/finance_logo.png" />
        <!--<button id="adminLoginNav" class="nav-button-hover b-r-line" style="width:100%;">国立看了么管理员登录</button>-->
        <!--<button id="saleLoginNav" class="nav-button-no-hover">销售人员登录</button>-->
    </div>
    <div class="login-box">
        	<div class="d_title" style="text-align:center;"><span>管理员登录</span></div>
        	<div class="cls"><p id="tip" class="error-notic">用户名或者密码错误</p></div>
            <div class="d_con">
                <div class="input-group">
                    <div class="input-field">
                        <label class="d_con_phone"><img src="${ctx}/admin/images/invite_phone.png" /></label>
                        <input id="userName" class="text-input" onblur="effectUserName(this)" onkeyup="effectUserName(this)" placeholder="请输入用户名">
                    </div>
                    <p id="userNameErrorNotic"  class="error-notic">用户名不能为空!</p>
        		</div>
        		<div class="input-group">
                    <div class="input-field">
                        <label class="d_con_pwd"><img src="${ctx}/admin/images/invite_pwd.png" /></label>
                        <input id="password" class="text-input" type="password" onblur="effectPassword(this)"
						onkeyup="effectPassword(this)" placeholder="请输入密码">
                    </div>
            		<p id="passwordErrorNotic" id="password_tip" class="error-notic">密码不能为空!</p>
        		</div>
        		<div>
				<div class="id_yan">
					<label><img src="${ctx}/admin/images/finance_tuyan.png" /></label>
					<input id="imageCode" type="text" maxlength="20"
					onblur="effectVerifyCode(this)" onkeyup="effectVerifyCode(this)"
					style="width: 150px; margin-right: 10px;" placeholder="请输入图中验证码"> <img
					id="imageVerifyCode" onclick="loadImageCode()"
					style="cursor: pointer;vertical-align: middle;" src="${ctx}/imagecode">
				</div>
				<span id="imageVerifyCodeErrorNotic" class="error-notic"
				style="display: none;">图片验证码不能为空！</span>
			</div>
        </div>   
    </div>
    <div class="from-submit" style="position:relative;margin-top:20px;">
        <button id="loginBtn">
            登&nbsp;&nbsp;&nbsp;录
        </button>
    </div>
</div>

</body>
</html>
<script src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script src="${ctx}/js/md5.js"></script>
<script src="${ctx}/js/jquery.cookie.js"></script>
<script src="${ctx}/js/json2.js"></script>
<script src="${ctx}/admin/js/admin_info.js"></script>
<script>
    var ctx = "${ctx}";
    $(function() {
		if(top.location!=self.location)
	    {
	        top.location=self.location;
	    }
	});

    $(document).keyup(function(event)
    {
        if (event.keyCode == 13)
        {
            $("#loginBtn").click();
        }
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
	
    $("#loginBtn").click(function () {
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
		if(image_verify_code ==""){
			$("#imageVerifyCodeErrorNotic").html("请输入验证码").show();
			return;
		}
        var param = {
            uri:  ctx + "/adminapi",
            user_name: user_name,
            password: password,
            image_verify_code:image_verify_code,
            type: "admin_login"
        };
        login(param);
    });

    function login(param) {
		$('.zhezhao').show();
		$('.loading').show();
        $.ajax({
            url: param.uri,
            type: "post",
            data: {
                "type": param.type,
                'data': '{'
                + '"user_name":"' + param.user_name + '",'
                + '"password":"' + hex_md5(param.password) + '",'
                + '"image_verify_code":"' + param.image_verify_code + '"'
                + '}'
            },
            dataType: "json",
            success: function (data) {
            	$('.zhezhao').hide();
				$('.loading').hide();
                if (data != null && data.status == "ok") {
                	admin_login(data);
                    window.location.href = "${ctx}/admin/index.jsp";
                }else if(data.status == "verify_code_error"){
					loadImageCode();
					$("#imageVerifyCodeErrorNotic").html("验证码错误").show();
                } else if (data.status == "password_error") {
                	loadImageCode();
                	$("#tip").html("用户名或密码错误").show();
                } else {
                    alert("服务器内部错误，登录失败");
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

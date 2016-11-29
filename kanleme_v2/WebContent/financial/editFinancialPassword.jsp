<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%
    pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>国立看了么财务后台</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${ctx}/financial/images/logo.ico" rel="shortcut icon">

    <!-- CSS -->
    <link rel="stylesheet" href="${ctx}/financial/css/global.css">
    <link rel="stylesheet" href="${ctx}/financial/css/from.css">
    <link href="css/redEnvelopeList.css" type="text/css" rel="stylesheet" />
</head>

<body>
<div class="actin_top">
	<h2>密码修改</h2>
    <p>首页/个人中心/密码修改</p>
</div>
<div id="contentbody" class="from-box" style="width:100%; margin-top:30px;">
	<div class="actin_list">
		<div class="yongjie_div2">
			<h1>修改个人资料</h1>
		</div>
	    <div class="input-group" style="height:auto;margin-bottom:20px;">
	        <div class="input-field" style="margin-top:20px;">
	            <label>原密码</label>
	            <input id="password" class="text-input" type="password" style="width:200px;background:#eeeeee;" >
	        </div>
	        <p class="error-notic">原密码不能为空!</p>
	    </div>
	    <div class="input-group" style="height:auto;margin-bottom:20px;">
	        <div class="input-field">
	            <label>新密码</label>
	            <input id="new_password" class="text-input" type="password" style="width:200px;background:#eeeeee;" >
	        </div>
	        <p class="error-notic">新密码不能为空!</p>
	    </div>
	    
	    <div class="input-group">
	        <div class="input-field">
	            <label>确认新密码</label>
	            <input id="sure_new_password" class="text-input" type="password" style="width:200px;background:#eeeeee;" >
	        </div>
	        <p class="error-notic">新密码不能为空!</p>
	    </div>
	    
	    
	    <div class="from-submit from-submit-tow" style="padding-bottom:34px;">
	        <button type="button" class="submit-btn b-l" id="editBtn" style="padding:5px 20px; margin-right:20px;">确&nbsp;&nbsp;定</button>
	        <button type="button" class="cancel-btn b-l" id="cancelBtn" style="padding:5px 20px;">取&nbsp;&nbsp;消</button>
	        <div class="cls"></div>
	    </div>
	 </div>
</div>
 <label  style="color:red" class="input-field" id="successed"></label>
</body>

</html>
<script src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.js" type="text/javascript"></script>
<script src="${ctx}/js/md5.js"></script>
<script src="${ctx}/js/marginAdaptive.js"></script>

<script type="text/javascript">
    var ctx = "${ctx}";

    /*marginAdaptive(".from-box");*/

    $("#editBtn").click(function () {
        var password = $("#password").val();
        var newPassword = $("#new_password").val();
        var SureNewPassword = $("#sure_new_password").val();

        if (password == "") {
            alert("原密码不能为空");

            return;
        }
        if (newPassword == "") {
            alert("新密码不能为空");

            return;
        }
        if (SureNewPassword == "") {
            alert("请确认新密码");

            return;
        }
        if(newPassword!=SureNewPassword){
        	alert("两次新密码输入值不一样");
        	return;
        }

        var requestParam = {
            "type": "update_financial_password",
            'data': '{'
            + '"password":"' + hex_md5(password) + '",'
            + '"new_password":"' + hex_md5(newPassword) + '"'
            + '}'
        };

        $.ajax({
            url: "${ctx}/financialapi",
            type: "post",
            data: requestParam,
            dataType: "json",
            success: function (data) {
                if (data != null && data.status == "ok") {
                	$("#contentbody").empty();
                	 document.getElementById("contentbody").innerHTML="<span style='font-size:20px;color:#3EA1E2;margin-left:30px; margin-top:20px;'>恭喜你,密码修改成功..";
                } else if (data.status == "parameter_error") {
                    alert("修改失败，参数错误!");
                } else if (data.status == "password_error") {
                    alert("修改失败，密码错误!");
                } else {
                    alert("服务器内部错误，修改失败"+data);
                }
            },
            error: function () {
                alert("请求错误");
            }
        });

    });
    
    $("#cancelBtn").click(function () {
        window.top.location.reload();
    });
</script>


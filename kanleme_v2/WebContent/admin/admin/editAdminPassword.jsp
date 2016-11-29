<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%
    pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${ctx}/admin/images/logo.ico" rel="shortcut icon">

    <!-- CSS -->
    <link rel="stylesheet" href="${ctx}/admin/css/global.css">
    <link rel="stylesheet" href="${ctx}/admin/css/from.css">
</head>

<body>
<div class="actin_top">
	<h2>密码修改</h2>
    <p>首页/个人中心/密码修改</p>
</div>
<div class="from-box">
    <div class="input-group">
        <div class="input-field">
            <label>原密码</label>
            <input id="password" class="text-input" type="password" >
        </div>
        <p class="error-notic">原密码不能为空!</p>
    </div>
    <div class="input-group">
        <div class="input-field">
            <label>新密码</label>
            <input id="new_password" class="text-input" type="password" >
        </div>
        <p class="error-notic">新密码不能为空!</p>
    </div>
    <div class="from-submit from-submit-tow">
        <button type="button" class="submit-btn b-l" id="editBtn">保&nbsp;&nbsp;&nbsp;存</button>
        <button type="button" class="cancel-btn b-r" id="cancelBtn">取&nbsp;&nbsp;&nbsp;消</button>
        <div class="cls"></div>
    </div>
</div>
</body>

</html>
<script src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.js" type="text/javascript"></script>
<script src="${ctx}/js/md5.js"></script>
<script src="${ctx}/js/marginAdaptive.js"></script>

<script type="text/javascript">
    var ctx = "${ctx}";

    marginAdaptive(".from-box");

    $("#editBtn").click(function () {
        var password = $("#password").val();
        var newPassword = $("#new_password").val();

        if (password == "") {
            alert("原密码不能为空");

            return;
        }
        if (newPassword == "") {
            alert("新密码不能为空");

            return;
        }

        var requestParam = {
            "type": "update_admin_password",
            'data': '{'
            + '"password":"' + hex_md5(password) + '",'
            + '"new_password":"' + hex_md5(newPassword) + '"'
            + '}'
        };

        $.ajax({
            url: "${ctx}/adminapi",
            type: "post",
            data: requestParam,
            dataType: "json",
            success: function (data) {
                if (data != null && data.status == "ok") {
                    alert("修改成功！");
                    window.top.location.href = "${ctx}/admin/index.jsp";
                } else if (data.status == "parameter_error") {
                    alert("修改失败，参数错误!");
                } else if (data.status == "password_error") {
                    alert("修改失败，密码错误!");
                } else {
                    alert("服务器内部错误，修改失败");
                }
            },
            error: function () {
                alert("服务器内部错误，修改失败");
            }
        });

    });

    $("#cancelBtn").click(function () {
        window.top.location.reload();
    });
</script>


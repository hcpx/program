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
	<h2>资料修改</h2>
    <p>首页/个人中心/资料修改</p>
</div>

<div class="from-box">
    <div class="input-group">
        <div class="input-field">
            <label>Email</label>
            <input id="admin_email" class="text-input">
        </div>
        <p class="error-notic">Email不能为空!</p>
    </div>
    <div class="input-group">
        <div class="input-field">
            <label>电&nbsp;&nbsp;&nbsp;话</label>
            <input id="admin_phone" class="text-input">
        </div>
        <p class="error-notic">电话号码不能为空!</p>
    </div>
    <div class="from-submit from-submit-tow">
        <button type="button" class="submit-btn b-l" id="editBtn">保&nbsp;&nbsp;&nbsp;存</button>
        <button type="button" class="cancel-btn b-r" id="cancelBtn">取&nbsp;&nbsp;&nbsp;消</button>
        <div class="cls"></div>
    </div>
</div>
</body>

</html>
<script src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script src="${ctx}/js/getQueryString.js"></script>
<script src="${ctx}/js/jquery.cookie.js"></script>
<script src="${ctx}/admin/js/admin_info.js"></script>
<script src="${ctx}/js/md5.js"></script>
<script src="${ctx}/js/marginAdaptive.js"></script>

<script type="text/javascript">
    var ctx = "${ctx}";
    marginAdaptive(".from-box");

    var userinfo = getUserInfo();
	if(userinfo){
		$("#admin_email").val(userinfo.admin_email);
		$("#admin_phone").val(userinfo.admin_phone);
	}
	
    $("#editBtn").click(function () {
        var admin_email = $("#admin_email").val();
        var admin_phone = $("#admin_phone").val();

        if (admin_email == "") {
            alert("管理员邮箱不能为空");

            return;
        }
        if (admin_phone == "") {
            alert("管理员电话号码不能为空");

            return;
        }

        var requestParam = {
            "type": "update_admin_info",
            'data': '{'
            + '"admin_email":"' + admin_email + '",'
            + '"admin_phone":"' + admin_phone + '"'
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

                    window.top.location.href = "${ctx}/admin/index.do";
                } else if (data.status == "parameter_error") {
                    alert("修改失败，参数错误!");
                } else if (data.status == "token_error") {
                    alert("修改失败，用户令牌不正确!");
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


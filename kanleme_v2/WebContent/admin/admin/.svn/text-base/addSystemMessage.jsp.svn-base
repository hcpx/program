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
    <link rel="stylesheet" href="${ctx}/admin/css/data-list.css">

    <style>
        input{
            color: #aaa;
            text-align: center;
        }

        textarea{
            color: #aaa;
        }
    </style>
</head>

<body>
<div class="from-box">
    <div class="input-group">
        <div class="input-field">
            <input id="message_title" class="text-input" value="标 题">
        </div>
    </div>
    <div class="input-area-group">
        <div class="input-field">
            <textarea id="message_content" class="text-area" cols="30" rows="10">在此输入系统信息内容...</textarea>
        </div>
        <p class="error-notic">系统信息内容不能为空!</p>
    </div>
    <div class="from-submit from-submit-tow">

        <div class="cls"></div>
    </div>
</div>

<div class="data-footer">
    <button type="button" class="submit-btn" id="addBtn">保&nbsp;&nbsp;&nbsp;存</button>
    <button type="button" class="cancel-btn" id="cancelBtn">取&nbsp;&nbsp;&nbsp;消</button>
    <div class="cls"></div>
</div>
</body>

</html>
<script src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.js" type="text/javascript"></script>
<script src="${ctx}/js/md5.js"></script>
<script src="${ctx}/js/marginAdaptive.js"></script>

<script type="text/javascript">
    var ctx = "${ctx}";

    String.prototype.trim = function ()
    {
        return this.replace(/(^\s*)|(\s*$)/g,"");
    }

    marginAdaptive(".from-box");

    $("#message_title, #message_content").focus(function ()
    {
        var value = $(this).val().trim();

        if (value == "标 题" || value == "在此输入系统信息内容...")
        {
            $(this).val("");
        }
    }).blur(function () {
        var value = $(this).val().trim();

        if (value == "")
        {
            var id = $(this).prop("id");

            if (id == "message_title")
            {
                $(this).val("标 题");
            }
            else
            {
                $(this).val("在此输入系统信息内容...");
            }
        }
    });

    $("#addBtn").click(function () {
        var message_title = $("#message_title").val().trim();
        var message_content = $("#message_content").val().trim();

        if (message_title == "") {
            alert("系统信息标题不能为空！");

            return;
        }
        if (message_content == "") {
            alert("系统信息内容不能为空");

            return;
        }

        $.ajax({
            url: "${ctx}/adminapi",
            type: "post",
            data: {
                "type": "add_system_message",
                'data': '{'
                + '"message_title":"' + message_title + '",'
                + '"message_content":"' + message_content + '"'
                + '}'
            },
            dataType: "json",
            success: function (data) {
                if (data != null && data.status == "ok") {
                    alert("添加成功！");

                    window.location.href = "${ctx}/admin/admin/showSystemMessage.jsp";
                } else if (data.status == "parameter_error") {
                    alert("添加失败，参数错误!");
                } else if (data.status == "password_error") {
                    alert("添加失败，密码错误!");
                } else if (data.status == "service_code_exits") {
                    alert("添加失败，service code 已存在!");
                } else {
                    alert("服务器内部错误，添加失败");
                }
            },
            error: function () {
                alert("服务器内部错误，添加失败");
            }
        });

    });

    $("#cancelBtn").click(function () {
        window.location.href = "${ctx}/admin/admin/showSystemMessage.jsp";
    });
</script>


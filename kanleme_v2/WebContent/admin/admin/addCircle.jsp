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
    <link href="${ctx}/admin/images/logo.ico" rel="shortcut icon" />

    <!-- CSS -->
    <link rel="stylesheet" href="${ctx}/admin/css/global.css">
    <link rel="stylesheet" href="${ctx}/admin/css/from.css">

    <style>
        .login-box {
            width: 450px;
        }

        .input-field label {
            font-size: 16px;
            width: 16%;
            text-align: right;
            display: inline-block;
            color: #aaa;
        }

        .input-field .text-input {
            padding: 8px 4px;
            border: 1px solid #000;
            width: 65%;
            margin-left: 22px;
            border-radius: 5px;
        }

        .from-submit {
            padding-left: 0;
        }

        .cancel-btn {
            margin-right: 40px;
        }
    </style>
</head>

<body>


<div class="from-box" style="margin-top: 20px;">
    <form id="addForm" action="${ctx}/api" enctype="multipart/form-data" method="post">
        <div class="input-group">
            <div class="input-field">
                <label for="title">标题</label>
                <input id="title" type="text" class="text-input">
            </div>
            <p class="error-notic">标题不能为空!</p>
        </div>
         <div class="input-group" style="height:auto; margin-bottom:30px;">
            <div class="input-field" style="height:auto;">
                <label for="content">简介</label>
                <textarea id="content" type="text" class="text-input" rows="10" cols="40"></textarea>
            </div>
            <p class="error-notic">简介不能为空!</p>
        </div>
        <div class="input-group">
            <div class="input-field">
                <label for="circle_pic">图标</label>
                <input id="circle_pic" name="circle_pic" type="file" class="text-input">
            </div>
            <p class="error-notic">图标不能为空!</p>
        </div>
        <div class="input-group">
            <div class="input-field">
                <label for="circle_back_img">背景图片</label>
                <input id="circle_back_img" name="circle_back_img" type="file" class="text-input">
            </div>
            <p class="error-notic">图标不能为空!</p>
        </div>
    </form>
    <div class="from-submit from-submit-tow">
        <button type="button" class="submit-btn b-l" id="addBtn">保&nbsp;&nbsp;&nbsp;存</button>
        <button type="button" class="cancel-btn b-r" id="cancelBtn">取&nbsp;&nbsp;&nbsp;消</button>
        <div class="cls"></div>
    </div>
</div>
</body>

</html>
<script src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.js" type="text/javascript"></script>
<script src="${ctx}/js/md5.js"></script>
<script src="${ctx}/js/marginAdaptive.js"></script>
<script src="http://libs.useso.com/js/jquery.form/3.50/jquery.form.min.js"></script>
<script type="text/javascript">
    var ctx = "${ctx}";

    $("#addBtn").click(function () {
        var title = $("#title").val();
        var content = $("#content").val();
        var bannerImg = $("input[name=circle_pic]").val();
        var circle_back_img = $("input[name=circle_back_img]").val();


        if (!title) {
            alert("资源不能为空");

            return;
        }

        if (!content) {
            alert("广告开始时间不能为空！");

            return;
        }
        if (!bannerImg) {
            bannerImg = null;

            alert("广告图片不能为空！");

            return;
        }
        else {
            var suffix = /.+\.(.+)/gi.exec($("[name=circle_pic]").val());

            if (suffix == null) {
                alert("上传的图片类型非法！");

                return;
            }
            else {
                suffix = suffix[1];

                if (suffix != "jpg" && suffix != "png" && suffix != "gif" && suffix != "jpeg" && suffix != "bmp") {
                    alert("上传的图片类型非法！");

                    return;
                }
            }
        }
        
        if (!circle_back_img) {
        	circle_back_img = null;

            alert("背景图片不能为空！");

            return;
        }
        else {
            var suffix = /.+\.(.+)/gi.exec($("[name=circle_back_img]").val());

            if (suffix == null) {
                alert("上传的图片类型非法！");

                return;
            }
            else {
                suffix = suffix[1];

                if (suffix != "jpg" && suffix != "png" && suffix != "gif" && suffix != "jpeg" && suffix != "bmp") {
                    alert("上传的图片类型非法！");

                    return;
                }
            }
        }

        $("#addForm").ajaxSubmit({
            url: "${ctx}/adminapi",
            type: "post",
            data: {
                "type": "admin_add_circle",
                'data': '{'
                + '"circle_name":"' + title + '",'
                + '"circle_sign":"' + content + '"'
                + '}'
            },
            dataType: "json",
            success: function (data) {
                if (data != null && data.status == "ok") {
                    alert("添加成功！");

                    window.location.href = "${ctx}/admin/admin/showCircles.jsp";
                } else if (data.status == "parameter_error") {
                    alert("添加失败，参数错误!");
                }
                else if (data.status == "password_error") {
                    alert("添加失败，密码错误!");
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
        window.location.href = "${ctx}/admin/admin/showBanners.do";
    });
</script>
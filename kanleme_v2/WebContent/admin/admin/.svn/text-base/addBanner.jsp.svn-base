<%@page import="com.sun.org.apache.xalan.internal.xsltc.compiler.sym"%>
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
            border: 1px solid #F2F2F2;
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
    <form id="addForm" action="${ctx}/adminapi" enctype="multipart/form-data" method="post">
        <div class="input-group">
            <div class="input-field">
                <label for="bannerType">广告类型</label>
                <select id="bannerType" class="text-select">
                    <option selected="selected">选择广告类型</option>
                    <option value="1">主界面</option>
                </select>
            </div>
        </div>
        <div class="input-group">
            <div class="input-field">
                <label for="sourceType">资源类型</label>
                <select id="sourceType" class="text-select">
                    <option selected="selected">选择资源类型</option>
                    <option value="1">网页</option>
                    <option value="2">帖子</option>
                    <option value="3">专题</option>
                    <option value="4">公益项目</option>
                </select>
            </div>
        </div>
        <div class="input-group">
            <div class="input-field">
                <label for="source">资&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;源</label>
                <input id="source" type="text" class="text-input">
            </div>
            <p class="error-notic">资源不能为空!</p>
        </div>
        <div class="input-group">
            <div class="input-field">
                <label for="start_time">开始时间</label>
                <input id="start_time" readOnly="readOnly"  onClick="WdatePicker({doubleCalendar:false,skin:'twoer',dateFmt:'yyyy-MM-dd HH:mm:ss'})"
                       class="text-input">
            </div>
            <p class="error-notic">开始时间不能为空!</p>
        </div>
        <div class="input-group">
            <div class="input-field">
                <label for="end_time">结束时间</label>
                <input id="end_time"  readOnly="readOnly" onClick="WdatePicker({doubleCalendar:false,skin:'twoer',dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="text-input">
            </div>
            <p class="error-notic">结束时间不能为空!</p>
        </div>
        <div class="input-group">
            <div class="input-field">
                <label for="bannerImg">广告图片(1080x864)</label>
                <input id="bannerImg" name="bannerImg" type="file" class="text-input">
            </div>
            <p class="error-notic">广告图片不能为空!</p>
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
<script type="text/javascript" src="${ctx}/js/jquery-1.8.3.min.js"></script>
<script src="${ctx}/js/md5.js"></script>
<script src="${ctx}/js/marginAdaptive.js"></script>
<script src="${ctx}/js/datePicker/WdatePicker.js"></script>
<script src="http://libs.useso.com/js/jquery.form/3.50/jquery.form.min.js"></script>
<script type="text/javascript">
    var ctx = "${ctx}";

    $("#addBtn").click(function () {
        var sourceType = $("#sourceType").val();
        var bannerType = $("#bannerType").val();
        var source = $("#source").val();
        var start_time = $("#start_time").val().replace(/-/gm, "/");
        var end_time = $("#end_time").val().replace(/-/gm, "/");
        var bannerImg = $("input[name=bannerImg]").val();

        if (bannerType == "选择广告类型") {
            alert("广告类型不能为空!");

            return;
        }

        if (sourceType == "选择资源类型") {
            alert("资源类型不能为空!");

            return;
        }

        if (!source) {
            alert("资源不能为空");

            return;
        }

        if (!start_time) {
            alert("广告开始时间不能为空！");

            return;
        }
        else {
            start_time = new Date(start_time).getTime();
        }

        if (!end_time) {
            alert("广告结束时间不能为空！");

            return;
        }
        else {
            end_time = new Date(end_time).getTime();
        }

        var curDate = new Date();
        var curDateStr = curDate.getFullYear() + "/" + (curDate.getMonth() + 1) + "/" + curDate.getDate();
        var curDateTime = new Date(curDateStr).getTime();

        if (start_time < curDateTime) {
            alert("开始日期不能为过去的日期！");

            return;
        }

        if (end_time < curDateTime) {
            alert("结束日期不能为过去的日期！");

            return;
        }

        if (start_time > end_time) {
            alert("开始时间大于结束时间,请重新选择..");
            return;
        }
        if (!bannerImg) {
            bannerImg = null;

            alert("广告图片不能为空！");

            return;
        }
        else {
            var suffix = /.+\.(.+)/gi.exec($("[name=bannerImg]").val());

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
                "type": "admin_add_banner",
                'data': '{'
                + '"banner_type":"' + bannerType + '",'
                + '"source_type":"' + sourceType + '",'
                + '"source":"' + source + '",'
                + '"start_time":"' + start_time + '",'
                + '"end_time":"' + end_time + '"'
                + '}'
            },
            dataType: "json",
            success: function (data) {
                if (data != null && data.status == "ok") {
                    alert("添加成功！");
                    window.location.href = "${ctx}/admin/admin/showBanners.jsp";
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
        window.location.href = "${ctx}/admin/admin/showBanners.jsp";
    });
</script>
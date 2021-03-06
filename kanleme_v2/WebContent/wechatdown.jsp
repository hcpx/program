<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
    pageContext.setAttribute("ctx", pageContext.getServletContext().getContextPath());
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport"
          content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black"/>
    <title>看了么</title>
    <style>
        * {
            padding: 0;
            margin: 0;
        }

        .dxinz {
            width: 100%;
            height: 100%;
            background: url(${ctx}/images/wechat/background.png) no-repeat 0 0;
            background-size: 100% 100%;
            position: relative;
        }

        .dxinzp1 {
            position: absolute;
            top: 10px;
            right: 10px;
        }

        .dxinzp1 img {
            width: 40px;
            height: auto;
        }

        .dxinzd {
            position: absolute;
            width: 100%;
            text-align: center;
        }

        .dxinzd p {
            width: 100%;
            text-align: center;
            margin-bottom: 5px;
            color: #fff;
        }

        .dxinzd p span {
            color: #caffa0;
        }
    </style>
</head>

<body>
<div class="dxinz">
    <p class="dxinzp1"><img src="${ctx}/images/wechat/finger.png"/></p>
    <div class="dxinzd">
        <p class="dxinzd1">因微信暂时不支持下载</p>
        <p class="dxinzd2">请点击右上角<span>在浏览器中打开</span></p>
    </div>
</div>
</body>
</html>
<script src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.js" type="text/javascript"></script>
<script type="text/javascript">


    <%
        String url = request.getRequestURL().toString();

        String tempContextUrl = url.substring(0, url.lastIndexOf("/"));

        pageContext.setAttribute("apkUrl", tempContextUrl+"/android/kanleme.apk");
    %>

    if (!isWechat())
    {
        $("body").html("");

        window.location.href = "${apkUrl}";
    }

    var dxinzw = $(window).width() + 'px';
    var dxinzy = $(window).height() + 'px';
    var dxinzdl = ($(window).width() - $('.dxinzd').width()) / 2 + 'px';
    var dxinzd2 = ($(window).height() - $('.dxinzd').height()) / 2 - 30 + 'px';
    $('.dxinz').css('width', dxinzw);
    $('.dxinz').css('height', dxinzy);
    $('.dxinzd').css('left', dxinzdl);
    $('.dxinzd').css('top', dxinzd2);


    function isWechat(){
        var ua = navigator.userAgent.toLowerCase();
        if(ua.match(/MicroMessenger/i)=="micromessenger") {
            return true;
        } else {
            return false;
        }
    }
</script>

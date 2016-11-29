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
    <link rel="stylesheet" href="${ctx}/admin/css/data-list.css">
    <link rel="stylesheet" href="${ctx}/js/jqueryui/jquery-ui.min.css">

    <style>
    	a{color:#000;}
        .data-body th a {
            color: #008000;
        }

        #dialog-content {
            padding: 5px;
            font-size: 14px;
        }
        input,button{border:none;background:none;}
		.yhe_more{ cursor:pointer;display:block; width:300px; height:30px; line-height:30px; text-align:center; font-size:14px; color:#fff; background:#1ab394;margin:0 auto;margin-top:20px; border-radius:5px;}
    </style>
</head>
<body>

<div class="container" style="height:auto">
	<div class="yongjie_div2">
			<h1>系统公告</h1>
		</div>
    <table class="data-table" style="margin-bottom:0;">
        <thead class="data-header">
        <tr>
        	<th style="width: 10%">ID</th>
        	<th style="width: 20%">标题</th>
        	<th style="width: 50%">内容</th>
       	    <th style="width: 20%">时间</th>
        </tr>
        </thead>
        <tbody class="data-body">
        </tbody>
        <tbody id="loading"/>
    </table>
    <button id="loadMoreBtn" class="yhe_more" style="display:none">查看更多</button>
    <div class="data-footer">
        <div class="data-operator">
            <a onclick="addSystemMessage();" class="add">
                <img src="${ctx}/admin/images/sale_add_logo.png"/>
                <span>添加系统信息</span>
            </a>
        </div>
        <div class="cls"></div>
    </div>
</div>
<div id="dialog" title="系统公告详情">
    <p id="dialog-content"></p>
</div>
</body>
</html>
<script src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.js" type="text/javascript"></script>
<script src="${ctx}/js/jqueryui/jquery-ui.min.js"></script>

<script type="text/javascript">
    function addSystemMessage() {
        window.location.href = "${ctx}/admin/admin/addSystemMessage.jsp";
    };

    $(function () {
        var limit = 0;
        var hasMoreData = false;
        var $window = $(window);
        var $document = $(document);
        var lock = false;
        loadData(limit, loadDataCallback);

        function loadData(requestLimit, callback) {
            lock = true;

            var requestParam = {
                "type": "list_system_message",
                'data': '{'
                + '"begin":"' + requestLimit + '"'
                + '}'
            };
            $("#loading").html("<center><td colspan='4' style='text-align:center'>加载数据中，请稍后......</td></center>");
            $.ajax({
                url: "${ctx}/adminapi",
                type: "post",
                data: requestParam,
                dataType: "json",
                success: function (data) {
                    $("#loading").empty();
                    if (data != null && data.status == "ok") {
                        callback(data);
                    } else if (data.status == "parameter_error") {
                        alert("数据加载失败，参数错误!");
                    } else if (data.status == "token_error") {
                        alert("数据加载失败，用户令牌不正确!");
                    } else {
                        alert("服务器内部错误，数据加载失败");
                    }
                    lock = false;
                },
                error: function () {
                    $("#loading").empty();
                    lock = false;
                }
            });
        };

        function loadDataCallback(json) {
            var datas = json.messages;
            var length = datas.length;

            if (datas != null && datas != undefined && length > 0) {
                var message = null;
                for (var i = 0; i < length; ++i) {
                    message = datas[i];
                    var dataHtml = "<tr>";
                    dataHtml += "<td name='message_id' style='text-align:center'>" + message.message_id + "</td>";
                    dataHtml += "<td style='text-align:center'>" + message.message_title + "</td>";
                    var content = message.message_content;
                    content=content.replace(/\r\n/g,"</br>");
                    content=content.replace(/\n/g,"</br>");
                    content=content.replace(/\r/g,"</br>");
                    dataHtml += "<td style='text-align:center;display:none;' name='content'>" + content + "</td>";
                    dataHtml += "<td style='text-align:center'><a onclick='showModal(this);' href='javascript:void(0)'>" + "查看详细" + "</a></td>";
                    dataHtml += "<td style='text-align:center'>" + localFormat(message.message_time) + "</td>";
                    dataHtml += "</tr>";
                    $(".data-body").append(dataHtml);
                }
            } else {
                if ($(".data-body").find("tr th[colspan='4']:contains('无数据')").length == 0) {
                    $(".data-body").append("<tr><th colspan='4'>无数据</th></tr>");
                }
            }

            hasMoreData = json.has_more_data;
            if (hasMoreData) {
                $("#loadMoreBtn").css('display', 'block');
            } else {
                $("#loadMoreBtn").css('display', 'none');
            }
            var container_h=$('.data-table').height()+200;
            $('.container').css('height',container_h)
            $(window).resize(function() {
                var container_h=$('.data-table').height()+200;
                $('.container').css('height',container_h)
            });
        };

        function showModal(e) {
            var content =$(e).parent().parent().find("[name=content]").text();
            $("#dialog").dialog("open");
            $("#dialog-content").text(content);
        }
        $("#dialog").dialog({
            autoOpen: false,
            width: 600,
            height: 400,
            buttons: [
                {
                    text: "关闭",
                    click: function () {
                        $(this).dialog("close");
                    }
                }
            ]
        });
        $("#loadMoreBtn").click(function() {
            $("#loadMoreBtn").css('display', 'none');
            loadData(limit, loadDataCallback);
        });

        Date.prototype.Format = function (fmt) { //author: meizz
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds() //毫秒
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
                if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
        }
        function localFormat(time) {
            return new Date(time).Format("yyyy-MM-dd");
        }
    });
</script>
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
    <link rel="stylesheet" href="${ctx}/financial/css/data-list.css">
</head>
<body>
<div style="margin-top:30px; margin-left:40px;margin-bottom:20px;">
	<a style=" display:inline-block; width:141px; height:30px; background:url(images/19.png) no-repeat 0 0; background-size:100% 100%;" href="showUserWithdrawals.jsp">
	</a>
</div>
<div class="container">
	
    <table class="data-table">
        <thead class="data-header">
        <tr>
        <th style="display:none;">ID</th>
        <th style="display:none;">用户名</th>
         <th>金币变动时间</th>
        <th>金币变动类型</th>
        <th>金币变动值</th>
        <th style="display:none;">变动描述</th>
        </tr>
        </thead>
        <tbody class="data-body">
        </tbody>
        <tbody id="loading"></tbody>
    </table>
</div>
</body>
</html>
<script src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.js" type="text/javascript"></script>
<script src="${ctx}/js/getQueryString.js"></script>
<script type="text/javascript">
    var limit = 0;
    var hasMoreData = false;
    var $window = $(window);
    var $document = $(document);
    var lock = false;
	var userid = getQueryString("user_id");
	var time = getQueryString("time");
    
    
    loadData(limit, loadDataCallback);

    $window.scroll(function () {
        var winHeight = window.innerHeight ? window.innerHeight : $window.height();
        var closeToBottom = ($window.scrollTop() + winHeight > $document.height() - 100);

        if (closeToBottom && hasMoreData) {
            limit = $(".data-body tr").size();
            if (!lock) {
                loadData(limit, loadDataCallback);
            }
        }
    });
    
    function getChangeType(type,money){
    	var types = ["充值","提现","提现失败","积分兑换","项目捐助","项目捐助提款","发宋红","抢红包","打赏","健康互助充值","签到"];
    	return types[type];
    }

    function loadDataCallback(json) {
        var sales = json.datas;
        var length = sales.length;

        // 数据回显
        if (sales != null && sales != undefined && length > 0) {
            for (var i = 0; i < length; ++i) {
                var dataHtml = "<tr>";
                var changeValue = sales[i].change_value;
                dataHtml += "<th>" + localFormat(sales[i].change_time) + "</th>";
                dataHtml += "<th>" + getChangeType(sales[i].change_type,changeValue) + "</th>";
                dataHtml += "<th>" + changeValue + "</th>";
                if (sales[i].change_desc == undefined) {
                	dataHtml += "<th></th>";
                }else{
                	dataHtml += "<th style='display:none;'>" + sales[i].change_desc + "</th>";                	
                }
                dataHtml += "</tr>";
                $(".data-body").append(dataHtml);
            }
        }
        hasMoreData = json.has_more_data;
    }

    function loadData(requestLimit, callback) {
    	if(lock){
    		return;
    	}
        lock = true;
        var requestParam = {
            "type": "financial_get_gold_info",
            'data': '{'
            	  + '"user_id":"' + userid + '",'
            	  + '"gold_type":0,'
            	  + '"time":"' + time + '"'
            + '}'
        };
        $("#loading").html("<center><td colspan='8' style='text-align:center'>加载数据中，请稍后......</td></center>");
        $.ajax({
            url: "${ctx}/financialapi",
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
                    alert("服务器内部错误，数据加载失败"+data);
                }

                lock = false;
            },
            error: function () {
            	$("#loading").empty();
                alert("请求错误");
                lock = false;
            }
        });
    }
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
        return new Date(time).Format("yyyy-MM-dd hh:mm:ss");
    }
</script>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
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

<style>
.data-body th a {
	color: #008000;
}
.red_status{height: 40px; padding-top: 10px;background: #fff; width: 89%; margin: 0 auto;  margin-top: 20px;border-top: 4px solid #e7eaec;}
.red_status h2{ margin-left:2%;}
.yongjie_div2 { width: 94%;height: 50px;line-height: 50px;margin: 0 auto;margin-top: 20px;border-bottom: 1px solid #eaeaea;text-indent: 2%;background: #fff;
border-top: 4px solid #e7eaec;}
.yongjie_div2 h1{font-size: 16px;font-weight: 400;}
</style>


</head>
<body>
<div class="actin_top">
    	<h2>充值信息</h2>
        <p>首页/充值信息</p>
    </div>
	<div class="container">
		<div class="red_status" style="width: 94%; margin: 20px auto;">
			<h2>
				<span style="font-size:16px;font-weight:400;">日期：</span> <input id="time" onClick="WdatePicker({doubleCalendar:false,skin:'twoer',dateFmt:'yyyy-MM-dd'})" readonly>
				<select id="type" style="font-size: 15px;height: 31px;border-radius: 5px;">
					<option value="-1">全部</option>
					<option value="0">支付宝</option>
					<option value="1">微信</option>
				</select>
				<button id="queryBtn" onclick="queryTopUp.call(this)" style="width: 80px;font-size: 16px;height: 30px;border-radius: 5px;border: none;background: #1ab394;color: #fff;">查询</button>
			</h2>
		</div>
		<div class="yongjie_div2">
			<h1>充值信息</h1>
		</div>
		<table class="data-table">
			<thead class="data-header">
				<tr>
					<th style="width: 25%">充值类型</th>
					<th style="width: 25%">充值时间</th>
					<th style="width: 25%">充值金额</th>
					<th style="width: 25%">订单号</th>
				</tr>
			</thead>
			<tbody class="data-body"></tbody>
			<tbody id="loading"></tbody>
		    <tbody></tbody>
	</table>
	<div class="yhe_morew">
         <button id="loadMoreData" class="yhe_more" style="display:none;">查看更多</button>
     </div>
    <div class="data-footer">
        <div class="cls"></div>
    </div>
</div>
</body>
</html>
<script src="http://apps.bdimg.com/libs/jquery/1.9.1/jquery.js" type="text/javascript"></script>
<script src="${ctx}/js/dataFormat.js"></script>
<script src="${ctx}/js/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
	var limit = 0;
	var hasMoreData = false;
	var lock = false;
	var $window = $(window);
	var $document = $(document);
	var date = new Date();
	var today = date.getFullYear() + "/" + (date.getMonth() + 1) + "/"
			+ date.getDate();
	var todayTime = new Date(today).getTime();
	var queryParam = null;

	function queryTopUp() {
		var targetDate = $("#time").val();
		var ps = targetDate.split(" ");
		var pd = ps[0].split("-");
		var pt = ps.length>1 ? ps[1].split(":"): [0,0,0];
		var d = new Date(pd[0],pd[1]-1,pd[2],pt[0],pt[1],pt[2]);
		queryParam = {
			"time" : targetDate ? new Date(d).getTime() : -1,
			"type" : $("#type").val()
		};

		limit = 0;
		$(".data-body").html("");
		loadData(limit, loadDataCallback);
	}
	
	loadData(limit, loadDataCallback);

	$("#loadMoreData").click(function() {
		$("#loadMoreData").css('display', 'none');
		loadData(limit, loadDataCallback);
	});

	function getTopUpType(type) {
		var types = [ "支付宝", "微信"];

		return types[type];
	}

	function loadDataCallback(json) {
		var datas = json.datas;
		// 数据回显
		if (datas != null && datas != undefined) {
			var dlen = datas.length;
			limit = limit+dlen;
			for (var i = 0; i < dlen; ++i) {
				var data = datas[i];
				var dataHtml = "<tr>";

				dataHtml += "<th>" + getTopUpType(data.type) + "</th>";
				dataHtml += "<th>"
						+ new Date(data.top_up_time).format("yyyy-MM-dd")
						+ "</th>";
				dataHtml += "<th>" + data.price + "</th>";
				dataHtml += "<th>" + data.order_no + "</th>";

				dataHtml += "</tr>";

				$(".data-body").append(dataHtml);
				var container_h=$('.data-table').height()+300;
				$('.container').css('height',container_h)
				$(window).resize(function() {
					var container_h=$('.data-table').height()+300;
					$('.container').css('height',container_h)
				});
			}
		}
		hasMoreData = json.has_more_data;
		if (hasMoreData) {
            $("#loadMoreData").css('display', 'block');
        } else {
            $("#loadMoreData").css('display', 'none');
        }
	}

	function loadData(requestLimit, callback) {
		if (lock) {
			return;
		}
		lock = true;

		var data = '{' + '"begin":"' + requestLimit + '"' + '}';

		if (queryParam != null) {
			data = '{' + '"begin":"' + requestLimit + '",' + '"time":"'
					+ queryParam.time + '",' + '"type":"' + queryParam.type
					+ '"' + '}';
		}
		var requestParam = {
			"type" : "get_topup_info",
			'data' : data
		};
		$("#loading").html("<center><td colspan='4' style='text-align:center'>加载数据中，请稍后......</td></center>");
		$.ajax({
			url : "${ctx}/financialapi",
			type : "post",
			data : requestParam,
			dataType : "json",
			success : function(data) {
				$("#loading").empty();
				if (data != null && data.status == "ok") {
					callback(data);
				}
				lock = false;
			},
			error : function() {
				$("#loading").empty();
				lock = false;
			}
		});
	}
</script>